package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;

import info.tregmine.api.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import info.tregmine.Tregmine;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IWarpDAO;
import info.tregmine.database.ILogDAO;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Warp;

public class WarpCommand extends AbstractCommand
{
	private Tregmine plugin;
    private static class WarpTask implements Runnable
    {
        private TregminePlayer player;
        private Location loc;

        public WarpTask(TregminePlayer player, Location loc)
        {
            this.player = player;
            this.loc = loc;
        }

        @Override
        public void run()
        {
            if (player.getRank().canTeleportBetweenWorlds()) {
                player.teleportWithHorse(loc);
                return;
            }

            World playerWorld = player.getWorld();
            String playerWorldName = playerWorld.getName();
            World locWorld = loc.getWorld();
            String locWorldName = locWorld.getName();

            if (playerWorldName.equalsIgnoreCase(locWorldName)) {
                player.teleportWithHorse(loc);

                PotionEffect ef =
                        new PotionEffect(PotionEffectType.BLINDNESS, 60, 100);
                player.addPotionEffect(ef);
            } else {
                player.sendMessage(RED + "You can't teleport between worlds.");
            }
        }
    }

    public WarpCommand(Tregmine tregmine)
    {
        super(tregmine, "warp");
        plugin = tregmine;
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length == 0) {
            return false;
        }

        Server server = tregmine.getServer();
        String name = args[0];
        if(name.equalsIgnoreCase("irl")){
        	player.kickPlayer(plugin, "Welcome to IRL.");
        	Bukkit.broadcastMessage(ChatColor.GOLD + player.getChatName() + " found the IRL warp!");
        	return true;
        }

        Warp warp = null;
        try (IContext ctx = tregmine.createContext()) {
            IWarpDAO warpDAO = ctx.getWarpDAO();
            warp = warpDAO.getWarp(name, server);
            if (warp == null) {
                player.sendMessage("Warp not found!");
                LOGGER.info("[warp failed] + <" + player.getName() + "> "
                        + name + " -- not found");
                return true;
            }

            ILogDAO logDAO = ctx.getLogDAO();
            logDAO.insertWarpLog(player, warp.getId());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        Location warpPoint = warp.getLocation();

        World world = warpPoint.getWorld();

        player.sendMessage(AQUA + "You started teleport to " + DARK_GREEN
                + name + AQUA + " in " + BLUE + world.getName() + ".");
        LOGGER.info("[warp] + <" + player.getName() + "> " + name + ":"
                + world.getName());

        player.setNoDamageTicks(200);

        Chunk chunk = world.getChunkAt(warpPoint);
        world.loadChunk(chunk);

        if (world.isChunkLoaded(chunk)) {
            long delay = player.getRank().getTeleportTimeout();

            player.sendMessage(AQUA + "You must now stand still and wait "
                    + (delay/20) + " seconds for the stars to align, "
                    + "allowing you to warp");

            BukkitScheduler scheduler = server.getScheduler();
            scheduler.scheduleSyncDelayedTask(tregmine, new WarpTask(player,
                    warpPoint), delay);

        }
        else {
            player.sendMessage(RED
                    + "Chunk failed to load. Please try to warp again");
        }

        return true;
    }
}
