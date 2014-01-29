package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.block.Block;

import info.tregmine.quadtree.Rectangle;
import info.tregmine.quadtree.IntersectionException;

import info.tregmine.Tregmine;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IZonesDAO;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Rank;
import info.tregmine.zones.Zone;
import static info.tregmine.zones.Zone.Permission;
import info.tregmine.zones.ZoneWorld;

public class ZoneCommand extends AbstractCommand
{
    public ZoneCommand(Tregmine tregmine, String commandName)
    {
        super(tregmine, commandName);
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if ("town".equals(command)) {
            if (args.length == 0) {
                return false;
            }

            Zone zone = player.getCurrentZone();
            if (zone == null) {
                player.sendMessage(RED + "You are not currently in a zone.");
                return true;
            }

            String[] args2 = new String[args.length + 1];
            args2[0] = args[0];
            args2[1] = zone.getName();
            for (int i = 1; i < args.length; i++) {
                args2[i + 1] = args[i];
            }
            args = args2;
        }

        if (args.length == 0) {
            return true;
        }

        if ("create".equals(args[0]) && player.getRank().canModifyZones()) {
            createZone(player, args);
            return true;
        }
        else if ("delete".equals(args[0]) && player.getRank().canModifyZones()) {
            deleteZone(player, args);
            return true;
        }
        else if ("adduser".equals(args[0])) {
            addUser(player, args);
            return true;
        }
        else if ("deluser".equals(args[0])) {
            delUser(player, args);
            return true;
        }
        else if ("entermsg".equals(args[0])) {
            changeValue(player, args);
            return true;
        }
        else if ("exitmsg".equals(args[0])) {
            changeValue(player, args);
            return true;
        }
        else if ("pvp".equals(args[0]) && player.getRank().canModifyZones()) {
            changeValue(player, args);
            return true;
        }
        else if ("hostiles".equals(args[0])
                && player.getRank().canModifyZones()) {
            changeValue(player, args);
            return true;
        }
        else if ("communism".equals(args[0])
                && player.getRank().canModifyZones()) {
            changeValue(player, args);
            return true;
        }
        else if ("publicprofile".equals(args[0])) {
            changeValue(player, args);
            return true;
        }
        else if ("enter".equals(args[0])) {
            changeValue(player, args);
            return true;
        }
        else if ("place".equals(args[0])) {
            changeValue(player, args);
            return true;
        }
        else if ("destroy".equals(args[0])) {
            changeValue(player, args);
            return true;
        }
        else if ("info".equals(args[0])) {
            zoneInfo(player, args);
            return true;
        }
        else if ("flag".equals(args[0])) {
            flagZone(player, args);
            return true;
        }

        return false;
    }

    public void flagZone(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 4) {
            player.sendMessage("syntax: /zone flag [name] [flag] [true/false]");
            return;
        }

        String name = args[1];

        Zone zone = world.getZone(name);
        if (zone == null) {
            player.sendMessage(RED + "No zone named " + name + " found.");
            return;
        }
        
        if (zone.getUser(player) != Zone.Permission.Owner) {
            player.sendMessage(RED + "You must be the zone owner to flag!");
            return;
        }

        Zone.Flags flag = null;
        for (Zone.Flags i : Zone.Flags.values()) {
            if (args[2].equalsIgnoreCase(i.name())) {
                flag = i;
                break;
            }
        }

        if (flag == null) {
            player.sendMessage(RED + "Flag not found! Try the following:");

            for (Zone.Flags i : Zone.Flags.values()) {
                player.sendMessage(AQUA + i.name());
            }
            return;
        }

        if (flag == Zone.Flags.ADMIN_ONLY &&
            (player.getRank() != Rank.JUNIOR_ADMIN &&
            player.getRank() != Rank.SENIOR_ADMIN)) {

            player.sendMessage(RED + "This flag is only for administrators!");
            return;
        }

        boolean value = Boolean.valueOf(args[3]);

        if (value) {
            zone.setFlag(flag);
            player.sendMessage(GREEN + "Added flag: " + flag.name());
        } else {
            zone.removeFlag(flag);
            player.sendMessage(GREEN + "Removed flag: " + flag.name());
        }

        try (IContext ctx = tregmine.createContext()) {
            IZonesDAO dao = ctx.getZonesDAO();
            dao.updateZone(zone);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createZone(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 3) {
            player.sendMessage("syntax: /zone create [zone-name] [owner]");
            return;
        }

        String name = args[1];
        if (world.zoneExists(name)) {
            player.sendMessage(RED + "A zone named " + name
                    + " does already exist.");
            return;
        }

        Block b1 = player.getZoneBlock1();
        Block b2 = player.getZoneBlock2();
        if (b1 == null || b2 == null) {
            player.sendMessage("Please select two corners");
            return;
        }

        Rectangle rect =
                new Rectangle(b1.getX(), b1.getZ(), b2.getX(), b2.getZ());

        Zone zone = new Zone();
        zone.setWorld(world.getName());
        zone.setName(name);
        zone.addRect(rect);

        zone.setTextEnter("Welcome to " + name + "!");
        zone.setTextExit("Now leaving " + name + ".");
        zone.addUser(player, Zone.Permission.Owner);

        zone.setMainOwner(args[2]);

        player.sendMessage(RED + "Creating zone at " + rect);

        try {
            world.addZone(zone);
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "Zone created successfully.");
        } catch (IntersectionException e) {
            player.sendMessage(RED
                    + "The zone you tried to create overlaps an existing zone.");
            return;
        }

        try (IContext ctx = tregmine.createContext()) {
            IZonesDAO dao = ctx.getZonesDAO();
            int zoneId = dao.createZone(zone);
            zone.setId(zoneId);
            dao.addRectangle(zone.getId(), rect);
            dao.addUser(zone.getId(), player.getId(), Zone.Permission.Owner);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteZone(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 2) {
            player.sendMessage("syntax: /zone delete [name]");
            return;
        }

        String name = args[1];

        Zone zone = world.getZone(name);
        if (zone == null) {
            player.sendMessage(RED + "A zone named " + name
                    + " does not exist.");
            return;
        }

        world.deleteZone(name);
        player.sendMessage(RED + "[" + name + "] " + "Zone deleted.");

        try (IContext ctx = tregmine.createContext()) {
            IZonesDAO dao = ctx.getZonesDAO();
            dao.deleteZone(zone.getId());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 4) {
            player.sendMessage(RED
                    + "syntax: /zone adduser [zone] [player] [perm]");
            return;
        }

        String zoneName = args[1];
        String userName = args[2];
        Zone.Permission perm = Zone.Permission.fromString(args[3]);

        Zone zone = world.getZone(zoneName);
        if (zone == null) {
            player.sendMessage(RED + "Specified zone does not exist.");
            return;
        }

        if (zone.getMainOwner() != null) {
            if (!zone.getMainOwner().equalsIgnoreCase(player.getName())
                    && !player.getRank().canModifyZones()) {

                if (Permission.Owner.equals(perm)) {
                    player.sendMessage(RED
                            + "Only the main owner may add new owners");
                    return;
                }
            }
        }

        if (zone.getUser(player) != Permission.Owner &&
            !player.getRank().canModifyZones()) {

            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "You do not have permission to add users to this zone.");
            return;
        }

        if (perm == null) {
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "Unknown permission " + args[3] + ".");
            return;
        }

        TregminePlayer victim = tregmine.getPlayerOffline(userName);
        if (victim == null) {
            player.sendMessage(RED + "[" + zone.getName() + "] " + "Player "
                    + userName + " was not found.");
            return;
        }

        // store permission change in db
        try (IContext ctx = tregmine.createContext()) {
            IZonesDAO dao = ctx.getZonesDAO();

            dao.deleteUser(zone.getId(), victim.getId());
            dao.addUser(zone.getId(), victim.getId(), perm);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        zone.addUser(victim, perm);
        String addedConfirmation = perm.getAddedConfirmation();
        player.sendMessage(RED + "[" + zone.getName() + "] "
                + String.format(addedConfirmation, userName, zoneName));

        TregminePlayer player2 = tregmine.getPlayer(userName);
        if (player2 != null) {
            String addedNotification = perm.getAddedNotification();
            player2.sendMessage(RED + "[" + zone.getName() + "] "
                    + String.format(addedNotification, zoneName));
        }
    }

    public void delUser(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 3) {
            player.sendMessage(RED + "syntax: /zone deluser [zone] [player]");
            return;
        }

        String zoneName = args[1];
        String userName = args[2];

        Zone zone = world.getZone(zoneName);
        if (zone == null) {
            player.sendMessage(RED + "Specified zone does not exist.");
            return;
        }

        if (zone.getUser(player) != Permission.Owner) {
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "You do not have permission to add users to this zone.");
            return;
        }

        TregminePlayer victim = tregmine.getPlayerOffline(userName);
        if (victim == null) {
            player.sendMessage(RED + "[" + zone.getName() + "] " + "Player "
                    + userName + " was not found.");
            return;
        }

        Zone.Permission oldPerm = zone.getUser(victim);
        if (oldPerm == null) {
            player.sendMessage(RED + "[" + zone.getName() + "] " + userName
                    + " doesn't have any permissions.");
            return;
        }

        // store permission change in db
        try (IContext ctx = tregmine.createContext()) {
            IZonesDAO dao = ctx.getZonesDAO();
            dao.deleteUser(zone.getId(), victim.getId());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        zone.deleteUser(victim);
        String delConfirmation = oldPerm.getDeletedConfirmation();
        player.sendMessage(RED + "[" + zone.getName() + "] "
                + String.format(delConfirmation, userName, zoneName));

        TregminePlayer player2 = tregmine.getPlayer(userName);
        if (player2 != null) {
            String delNotification = oldPerm.getDeletedNotification();
            player2.sendMessage(RED + "[" + zone.getName() + "] "
                    + String.format(delNotification, zoneName));
        }
    }

    public void changeValue(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        // entermsg [zone] [message]
        if (args.length < 3) {
            player.sendMessage(RED + "unknown command");
            return;
        }

        String zoneName = args[1];

        Zone zone = world.getZone(zoneName);
        if (zone == null) {
            player.sendMessage(RED + "Specified zone does not exist.");
            return;
        }

        if (zone.getUser(player) != Permission.Owner) {
            player.sendMessage(RED + "[" + zone.getName() + "] " +
                "You do not have permission to change settings for this zone.");
            return;
        }

        if ("entermsg".equals(args[0]) || "exitmsg".equals(args[0])) {
            StringBuilder buf = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                buf.append(args[i]);
                buf.append(" ");
            }

            String message = buf.toString().trim();
            if ("entermsg".equals(args[0])) {
                zone.setTextEnter(message);
                player.sendMessage(RED + "[" + zone.getName() + "] "
                        + "Welcome message changed to \"" + message + "\".");
            }
            else if ("exitmsg".equals(args[0])) {
                zone.setTextExit(message);
                player.sendMessage(RED + "[" + zone.getName() + "] "
                        + "Exit message changed to \"" + message + "\".");
            }
        }
        else if ("pvp".equals(args[0])) {
            boolean status = Boolean.parseBoolean(args[2]);
            zone.setPvp(status);
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "PVP changed to \"" + (status ? "allowed" : "disallowed")
                    + "\".");
        }
        else if ("communism".equals(args[0])) {
            boolean status = Boolean.parseBoolean(args[2]);
            zone.setCommunist(status);
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "Communism changed to \"" + (status ? "yes" : "no")
                    + "\".");
        }
        else if ("publicprofile".equals(args[0])) {
            boolean status = Boolean.parseBoolean(args[2]);
            zone.setPublicProfile(status);
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "Public profile changed to \"" + (status ? "yes" : "no")
                    + "\".");
        }
        else if ("enter".equals(args[0])) {
            boolean status = Boolean.parseBoolean(args[2]);
            zone.setEnterDefault(status);
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "Enter default changed to \""
                    + (status ? "everyone" : "whitelisted") + "\".");
        }
        else if ("place".equals(args[0])) {
            boolean status = Boolean.parseBoolean(args[2]);
            zone.setPlaceDefault(status);
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "Place default changed to \""
                    + (status ? "everyone" : "whitelisted") + "\".");
        }
        else if ("destroy".equals(args[0])) {
            boolean status = Boolean.parseBoolean(args[2]);
            zone.setDestroyDefault(status);
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "Destroy default changed to \""
                    + (status ? "everyone" : "whitelisted") + "\".");
        }
        else if ("hostiles".equals(args[0])) {
            boolean status = Boolean.parseBoolean(args[2]);
            zone.setHostiles(status);
            player.sendMessage(RED + "[" + zone.getName() + "] "
                    + "Hostiles changed to \""
                    + (status ? "allowed" : "disallowed") + "\".");
        }

        try (IContext ctx = tregmine.createContext()) {
            IZonesDAO dao = ctx.getZonesDAO();
            dao.updateZone(zone);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public void zoneInfo(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 2) {
            player.sendMessage(RED + "unknown command");
            return;
        }

        String zoneName = args[1];
        int show;
        if (args.length > 2 && args[2].equals("perm")) {
            show = 2;
        }
        else {
            show = 1;
        }

        Zone zone = world.getZone(zoneName);
        if (zone == null) {
            player.sendMessage(RED + "Specified zone does not exist.");
            return;
        }

        player.sendMessage(YELLOW + "Info about " + zone.getName());

        if (show == 1) {
            player.sendMessage(YELLOW + "ID: " + zone.getId());
            player.sendMessage(YELLOW + "World: " + zone.getWorld());
            for (Rectangle rect : zone.getRects()) {
                player.sendMessage(YELLOW + "Rect: " + rect);
            }
            player.sendMessage(YELLOW
                    + "Enter: "
                    + (zone.getEnterDefault() ? "Everyone (true)"
                            : "Only allowed (false)"));
            player.sendMessage(YELLOW
                    + "Place: "
                    + (zone.getPlaceDefault() ? "Everyone (true)"
                            : "Only makers (false)"));
            player.sendMessage(YELLOW
                    + "Destroy: "
                    + (zone.getDestroyDefault() ? "Everyone (true)"
                            : "Only makers (false)"));
            player.sendMessage(YELLOW + "PVP: " + zone.isPvp());
            player.sendMessage(YELLOW + "Communism: " + zone.isCommunist());
            player.sendMessage(YELLOW + "Public Profile: "
                    + zone.hasPublicProfile());
            if (zone.hasPublicProfile()) {
                player.sendMessage(YELLOW + "Public Link: "
                        + "http://treg.co/index.php/zone/profile?id="
                        + zone.getId());
            }
            player.sendMessage(YELLOW + "Hostiles: " + zone.hasHostiles());
            player.sendMessage(YELLOW + "Enter message: " + zone.getTextEnter());
            player.sendMessage(YELLOW + "Exit message: " + zone.getTextExit());
        }
        else if (show == 2) {
            for (Integer id : zone.getUsers()) {
                TregminePlayer user = tregmine.getPlayerOffline(id);
                Zone.Permission perm = zone.getUser(user);
                player.sendMessage(YELLOW + user.getChatName() +
                        YELLOW + " - " + perm);
            }
        }
    }
}
