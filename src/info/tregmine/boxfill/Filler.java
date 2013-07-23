package info.tregmine.boxfill;

import info.tregmine.Tregmine;

import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import info.tregmine.api.TregminePlayer;

public class Filler extends AbstractFiller
{
    private History history;
    private TregminePlayer player;
    private MaterialData item;
    private SavedBlocks currentJob;

    public Filler(Tregmine plugin, History history, TregminePlayer player,
            Block block1, Block block2, MaterialData item, int workSize)
    {
        super(plugin, block1, block2, workSize);

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
