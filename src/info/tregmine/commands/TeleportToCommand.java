package info.tregmine.commands;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Chunk;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

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

        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);

        Location loc = new Location(player.getWorld(), x, y, z);

        World world = loc.getWorld();
        Chunk chunk = world.getChunkAt(loc);
        world.loadChunk(chunk);

        if (world.isChunkLoaded(chunk)) {
            player.teleport(loc);
        }

        return true;
    }
}
