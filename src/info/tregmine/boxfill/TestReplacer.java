package info.tregmine.boxfill;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public class TestReplacer extends AbstractFiller
{
    private Player player;
    private MaterialData item;
    private MaterialData match;

    public TestReplacer(Player player, Block block1, Block block2,
            MaterialData match, MaterialData item, int workSize)
    {
        super(block1, block2, workSize);
        this.player = player;
        this.item = item;
        this.match = match;
    }

    @Override
    public void changeBlock(Block block)
    {
        if (block.getTypeId() == match.getItemTypeId()
                && (match.getData() == 0 || block.getData() == match.getData())) {

            player.sendBlockChange(block.getLocation(), item.getItemType(),
                    item.getData());
        }
    }
}
