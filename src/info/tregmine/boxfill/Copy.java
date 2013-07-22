package info.tregmine.boxfill;

import info.tregmine.Tregmine;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Copy extends AbstractFiller
{
    private History history;
    private Player player;
    private SavedBlocks currentJob;

    public Copy(Tregmine plugin, History history, Player player, Block block1, Block block2,
            int workSize)
    {
        super(plugin, block1, block2, workSize);

        this.history = history;
        this.player = player;
        this.currentJob =
                new SavedBlocks(block1.getX(), block1.getY(), block1.getZ());
    }

    @Override
    public void changeBlock(Block block)
    {
        currentJob.addBlock(block.getState());
    }

    @Override
    public void finished()
    {
        history.set(player, currentJob);
    }
}
