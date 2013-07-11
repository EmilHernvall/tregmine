package info.tregmine.boxfill;

import java.util.List;
import java.util.logging.Logger;

//import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitScheduler;

public class Undo implements Runnable
{
    private final static Logger logger = Logger.getLogger("Minecraft");

    private World world;
    private SavedBlocks blocks;
    private int workSize;

    private BukkitScheduler scheduler;
    private int taskId;

    private int i;

    public Undo(World world, SavedBlocks blocks, int workSize)
    {
        this.world = world;
        this.blocks = blocks;

        this.workSize = workSize;
        this.i = 0;
    }

    public void setScheduleState(BukkitScheduler scheduler, int taskId)
    {
        this.scheduler = scheduler;
        this.taskId = taskId;
    }

    @Override
    public void run()
    {
        List<BlockState> list = blocks.getBlocks();
        logger.info("Undo in process. " + i + " of " + list.size() + " done.");

        boolean partialWork = false;
        for (; i < list.size(); i++) {
            BlockState state = list.get(i);

            Block block =
                    world.getBlockAt(state.getX(), state.getY(), state.getZ());

            block.setType(state.getType());
            block.setData(state.getData().getData());

            if (i != 0 && i % workSize == 0) {
                partialWork = true;
                // increment an additional time to make sure we start off at the
                // right
                // position next time.
                i++;
                break;
            }
        }

        if (!partialWork) {
            scheduler.cancelTask(taskId);
        }
    }
}
