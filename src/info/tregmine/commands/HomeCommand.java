package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Chunk;
import org.bukkit.entity.Horse;

import info.tregmine.Tregmine;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IHomeDAO;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.Distance;

public class HomeCommand extends AbstractCommand
{
    public HomeCommand(Tregmine tregmine)
    {
        super(tregmine, "home");
    }

    private boolean teleport(TregminePlayer player)
    {
        Location loc = null;
        try (IContext ctx = tregmine.createContext()) {
            IHomeDAO homeDAO = ctx.getHomeDAO();
            loc = homeDAO.getHome(player);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (loc == null) {
            player.sendMessage(RED + "Telogric lift malfunctioned. "
                    + "Teleportation failed.");
            return true;
        }

        World world = loc.getWorld();
        Chunk chunk = world.getChunkAt(loc);
        world.loadChunk(chunk);

        if (world.isChunkLoaded(chunk)) {
            if (!world.getName().equalsIgnoreCase(player.getWorld().getName())) {
                player.sendMessage(RED
                        + "You can't use a home thats in another world!");
                return true;
            }

            player.teleportWithHorse(loc);

            player.sendMessage(AQUA + "Hoci poci, little gnome. Magic worked, "
                    + "you're in your home!");
        }
        else {
            player.sendMessage(RED
                    + "Loading your home chunk failed, try /home again.");
        }

        return true;
    }

    private boolean save(TregminePlayer player)
    {
        if (!player.getRank().canSaveHome()) {
            return true;
        }

        Location playerLoc = player.getLocation();
        World playerWorld = playerLoc.getWorld();
        if ("world_the_end".equalsIgnoreCase(playerWorld.getName())) {
            player.sendMessage(RED + "You can't set your home in The End");
            return true;
        }

        Server server = tregmine.getServer();
        World mainWorld = server.getWorld("world");
        if (Distance.calc2d(mainWorld.getSpawnLocation(), player.getLocation()) < 700) {

            player.sendMessage(RED
                    + "Telogric lift malfunctioned. Teleportation "
                    + "failed, to close to spawn.");
            return true;
        }

        try (IContext ctx = tregmine.createContext()) {
            IHomeDAO homeDAO = ctx.getHomeDAO();
            homeDAO.insertHome(player, playerLoc);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(AQUA + "Home saved!");

        return true;
    }

    private boolean teleportTo(TregminePlayer player, String playerName)
    {
        if (!player.getRank().canVisitHomes()) {
            return true;
        }

        TregminePlayer target = tregmine.getPlayerOffline(playerName);
        if (target == null) {
            player.sendMessage(RED + playerName + " was not found in database.");
            return true;
        }

        Location loc = null;
        try (IContext ctx = tregmine.createContext()) {
            IHomeDAO homeDAO = ctx.getHomeDAO();
            loc = homeDAO.getHome(target.getId(), tregmine.getServer());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (loc == null) {
            player.sendMessage(RED
                    + "Telogric lift malfunctioned. Teleportation failed.");
            return true;
        }

        World world = loc.getWorld();
        Chunk chunk = world.getChunkAt(loc);
        world.loadChunk(chunk);

        if (world.isChunkLoaded(chunk)) {
            player.teleportWithHorse(loc);

            player.sendMessage(AQUA
                    + "Like a drunken gnome, you fly across the world to "
                    + playerName + "'s home. Try not to hit any birds.");
        } else {
            player.sendMessage(RED
                    + "Loading of home chunk failed, try /home again");
        }

        return true;
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length == 0) {
            return teleport(player);
        }
        else if ("save".equalsIgnoreCase(args[0])) {
            return save(player);
        }
        else if ("to".equalsIgnoreCase(args[0])) {
            if (args.length < 2) {
                player.sendMessage(RED + "Usage: /home to <player>.");
                return true;
            }

            return teleportTo(player, args[1]);
        }

        return true;
    }
}
