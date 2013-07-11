package info.tregmine.boxfill;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public class Filler extends AbstractFiller
{
    private History history;
    private Player player;
    private MaterialData item;
    private SavedBlocks currentJob;

    public Filler(History history, Player player, Block block1, Block block2,
            MaterialData item, int workSize)
    {
        super(block1, block2, workSize);

        this.history = history;
        this.player = player;
        this.item = item;
        this.currentJob = new SavedBlocks();
    }

    @Override
    public void changeBlock(Block block)
    {
        currentJob.addBlock(block.getState());

        block.setType(item.getItemType());
        block.setData(item.getData());
    }

    @Override
    public void finished()
    {
        history.set(player, currentJob);
    }
}
