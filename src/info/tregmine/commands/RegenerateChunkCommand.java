package info.tregmine.commands;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Chunk;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class RegenerateChunkCommand extends AbstractCommand
{
    public RegenerateChunkCommand(Tregmine tregmine)
    {
        super(tregmine, "regeneratechunk");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.isOp()) {
            return false;
        }

        World world = player.getWorld();
        Block b1 = player.getFillBlock1();
        Chunk chunk = b1.getChunk();
        world.regenerateChunk(chunk.getX(), chunk.getZ());

        return true;
    }
}
