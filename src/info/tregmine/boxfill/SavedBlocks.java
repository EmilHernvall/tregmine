package info.tregmine.boxfill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockState;

public class SavedBlocks
{
    private List<BlockState> blocks;
    private int x, y, z;

    public SavedBlocks()
    {
        this(0, 0, 0);
    }

    public SavedBlocks(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        blocks = new ArrayList<BlockState>();
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public void addBlock(BlockState block)
    {
        blocks.add(block);
    }

    public List<BlockState> getBlocks()
    {
        return blocks;
    }
}
