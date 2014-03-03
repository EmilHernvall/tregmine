package info.tregmine.commands;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class TeleportToCommand extends AbstractCommand
{
    public TeleportToCommand(Tregmine tregmine)
    {
        super(tregmine, "tpto");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 3) {
            return false;
        }
        if (!player.getRank().canTeleportToPlayers()) {
            return true;
        }

        int x = 0;
        int y = 0;
        int z = 0;
        try{

            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);

        } catch (NumberFormatException e) {
            if ("~".equals(args[0])) {
                x = player.getLocation().getBlockX();
            }
            if ("~".equals(args[1])) {
                y = player.getLocation().getBlockY();
            }
            if ("~".equals(args[2])) {
                z = player.getLocation().getBlockZ();
            }

            if (x == 0 || y == 0 || z == 0) {
                player.sendMessage(ChatColor.RED + "Incorrect parameters passed!");
                return true;
            }
        }

        Location loc = new Location(player.getWorld(), x, y, z);

        World world = loc.getWorld();
        Chunk chunk = world.getChunkAt(loc);
        world.loadChunk(chunk);

        if (world.isChunkLoaded(chunk)) {
            player.teleportWithHorse(loc);
        }

        return true;
    }
}
