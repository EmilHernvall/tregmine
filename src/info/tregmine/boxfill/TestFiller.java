package info.tregmine.boxfill;

import info.tregmine.Tregmine;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import info.tregmine.api.TregminePlayer;

public class TestFiller extends AbstractFiller
{
    private TregminePlayer player;
    private MaterialData item;

    public TestFiller(Tregmine plugin, TregminePlayer player, Block block1,
            Block block2, MaterialData item, int workSize)
    {
        super(plugin, block1, block2, workSize);
        this.player = player;
        this.item = item;
    }

    @Override
    public void changeBlock(Block block)
    {
        player.getDelegate().sendBlockChange(block.getLocation(),
                                             item.getItemType(),
                                             item.getData());
    }
}
