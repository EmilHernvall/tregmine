package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Location;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class PositionCommand extends AbstractCommand
{
    public PositionCommand(Tregmine tregmine)
    {
        super(tregmine, "pos");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        Location loc = player.getLocation();
        Location spawn = player.getWorld().getSpawnLocation();
        double distance = info.tregmine.api.math.MathUtil.calcDistance2d(spawn, loc);

        player.sendMessage(DARK_AQUA + "World: " + WHITE
                + player.getWorld().getName());
        player.sendMessage(DARK_AQUA + "X: " + WHITE + loc.getX() + RED + " ("
                + loc.getBlockX() + ")");
        player.sendMessage(DARK_AQUA + "Y: " + WHITE + loc.getY() + RED + " ("
                + loc.getBlockY() + ")");
        player.sendMessage(DARK_AQUA + "Z: " + WHITE + loc.getZ() + RED + " ("
                + loc.getBlockZ() + ")");
        player.sendMessage(DARK_AQUA + "Yaw: " + WHITE + loc.getYaw());
        player.sendMessage(DARK_AQUA + "Pitch: " + WHITE + loc.getPitch());
        player.sendMessage(DARK_AQUA + "Blocks from spawn: " + WHITE + distance);

        LOGGER.info("World: " + player.getWorld().getName());
        LOGGER.info("X: " + loc.getX() + " (" + loc.getBlockX() + ")");
        LOGGER.info("Y: " + loc.getY() + " (" + loc.getBlockY() + ")");
        LOGGER.info("Z: " + loc.getZ() + " (" + loc.getBlockZ() + ")");
        LOGGER.info("Yaw: " + loc.getYaw());
        LOGGER.info("Pitch: " + loc.getPitch());

        return true;
    }
}
