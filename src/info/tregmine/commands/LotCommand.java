package info.tregmine.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.bukkit.ChatColor.*;
import org.bukkit.block.Block;
import info.tregmine.quadtree.Rectangle;
import info.tregmine.quadtree.Point;
import info.tregmine.quadtree.IntersectionException;

import info.tregmine.Tregmine;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBZonesDAO;
import info.tregmine.api.TregminePlayer;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import info.tregmine.zones.Lot;

public class LotCommand extends AbstractCommand
{
    public LotCommand(Tregmine tregmine)
    {
        super(tregmine, "lot");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if ("create".equals(args[0])) {
            createLot(player, args);
            return true;
        }
        else if ("addowner".equals(args[0])) {
            setLotOwner(player, args);
            return true;
        }
        else if ("delowner".equals(args[0])) {
            setLotOwner(player, args);
            return true;
        }
        else if ("delete".equals(args[0])) {
            deleteLot(player, args);
            return true;
        }

        return false;
    }

    public void createLot(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 3) {
            player.sendMessage("syntax: /lot create [name] [owner]");
            return;
        }

        Block tb1 = player.getZoneBlock1();

        Zone tzone = world.findZone(new Point(tb1.getX(), tb1.getZ()));

        String name = args[1] + "." + tzone.getName();

        if (world.lotExists(name)) {
            player.sendMessage(RED + "A lot named " + name
                    + " does already exist.");
            return;
        }

        String playerName = args[2];

        int userId;
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            DBZonesDAO dao = new DBZonesDAO(conn);
            userId = dao.getUserId(playerName);

            if (userId == -1) {
                player.sendMessage(RED + "Player " + args[2]
                        + " was not found.");
                return;
            }

            Block b1 = player.getZoneBlock1();
            Block b2 = player.getZoneBlock2();

            Zone zone = world.findZone(new Point(b1.getX(), b1.getZ()));

            Zone.Permission perm = zone.getUser(player.getName());
            if (perm != Zone.Permission.Owner) {
                player.sendMessage(RED
                        + "You are not allowed to create lots in zone "
                        + zone.getName() + " (" + perm + ").");
                return;
            }

            Zone checkZone = world.findZone(new Point(b2.getX(), b2.getZ()));

            // identity check. both lookups should return exactly the same
            // object
            if (zone != checkZone) {
                return;
            }

            Rectangle rect =
                    new Rectangle(b1.getX(), b1.getZ(), b2.getX(), b2.getZ());

            Lot lot = new Lot();
            lot.setZoneId(zone.getId());
            lot.setRect(rect);
            lot.setName(args[1] + "." + zone.getName());
            lot.addOwner(playerName);

            try {
                world.addLot(lot);
            } catch (IntersectionException e) {
                player.sendMessage(RED
                        + "The specified rectangle intersects an existing lot.");
                return;
            }

            dao.addLot(lot);
            dao.addLotUser(lot.getId(), userId);

            player.sendMessage(YELLOW + "[" + zone.getName() + "] Lot "
                    + args[1] + "." + zone.getName() + " created for player "
                    + playerName + ".");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void setLotOwner(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 3) {
            player.sendMessage("syntax: /lot addowner [name] [player]");
            return;
        }

        String name = args[1];

        Lot lot = world.getLot(name);
        if (lot == null) {
            player.sendMessage(RED + "No lot named " + name + " found.");
            return;
        }

        Zone zone = tregmine.getZone(lot.getZoneId());
        Zone.Permission perm = zone.getUser(player.getName());
        if (perm == Zone.Permission.Owner && !zone.isCommunist()) {
            // Zone owners can do this in communist zones
        }
        else if (lot.isOwner(player.getName())) {
            // Lot owners can always do it
        }
        else if (player.getRank().canModifyZones()) {
            // Admins etc.
        }
        else {
            player.sendMessage(RED
                    + "You are not an owner of lot " + lot.getName() + ".");
            return;
        }

        List<TregminePlayer> candidates = tregmine.matchPlayer(args[2]);
        if (candidates.size() != 1) {
            player.sendMessage(RED + "Player " + args[2]
                    + " was not found.");
            return;
        }

        TregminePlayer candidate = candidates.get(0);

        if (lot.isOwner(candidate.getName())) {
            player.sendMessage(RED + candidate.getChatName() + RED + " is " +
                    "already an owner of lot " + name + ".");
            return;
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            DBZonesDAO dao = new DBZonesDAO(conn);

            if ("addowner".equals(args[0])) {

                if (lot.isOwner(candidate.getName())) {
                    player.sendMessage(RED + candidate.getChatName() + RED +
                            " is already an owner of lot " + name + ".");
                    return;
                }
                else {
                    lot.addOwner(candidate.getName());
                    dao.addLotUser(lot.getId(), candidate.getId());
                    player.sendMessage(YELLOW + candidate.getChatName() + YELLOW +
                            " has been added as owner of " + lot.getName() + ".");
                }
            }
            else if ("delowner".equals(args[0])) {
                lot.deleteOwner(candidate.getName());
                dao.deleteLotUser(lot.getId(), candidate.getId());

                player.sendMessage(YELLOW + candidate.getChatName() + RED +
                        " is no longer an owner of " + lot.getName() + ".");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteLot(TregminePlayer player, String[] args)
    {
        ZoneWorld world = tregmine.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        if (args.length < 2) {
            player.sendMessage("syntax: /lot delete [name]");
            return;
        }

        String name = args[1];

        Lot lot = world.getLot(name);
        if (lot == null) {
            player.sendMessage(RED + "No lot named " + name + " found.");
            return;
        }

        Zone zone = tregmine.getZone(lot.getZoneId());
        Zone.Permission perm = zone.getUser(player.getName());
        if (perm == Zone.Permission.Owner && !zone.isCommunist()) {
            // Zone owners can do this in communist zones
        }
        else if (lot.isOwner(player.getName())) {
            // Lot owners can always do it
        }
        else if (player.getRank().canModifyZones()) {
            // Admins etc.
        }
        else {
            player.sendMessage(RED
                    + "You are not an owner of lot " + lot.getName() + ".");
            return;
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            DBZonesDAO dao = new DBZonesDAO(conn);

            dao.deleteLot(lot.getId());
            dao.deleteLotUsers(lot.getId());

            world.deleteLot(lot.getName());

            player.sendMessage(YELLOW + lot.getName() + " has been deleted.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
