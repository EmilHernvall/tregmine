package info.tregmine.boxfill;

import info.tregmine.Tregmine;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class AbstractFiller implements Runnable
{
    private final Logger log = Logger.getLogger("Minecraft");
    
    protected Tregmine plugin;

    private int workSize;

    private BukkitScheduler scheduler;
    private int taskId;

    private Block b1;
    private Block b2;

    protected int x, xMin, xMax;
    protected int y, yMin, yMax;
    protected int z, zMin, zMax;
    protected int totalVolume;

    public AbstractFiller(Tregmine instance, Block block1, Block block2, int workSize)
    {
    	this.plugin = instance;
        this.b1 = block1;
        this.b2 = block2;
        this.workSize = workSize;

        xMax =
                Math.max(b1.getLocation().getBlockX(), b2.getLocation()
                        .getBlockX());
        xMin =
                Math.min(b1.getLocation().getBlockX(), b2.getLocation()
                        .getBlockX());
        x = xMin;

        yMax =
                Math.max(b1.getLocation().getBlockY(), b2.getLocation()
                        .getBlockY());
        yMin =
                Math.min(b1.getLocation().getBlockY(), b2.getLocation()
                        .getBlockY());
        y = yMin;

        zMax =
                Math.max(b1.getLocation().getBlockZ(), b2.getLocation()
                        .getBlockZ());
        zMin =
                Math.min(b1.getLocation().getBlockZ(), b2.getLocation()
                        .getBlockZ());
        z = zMin;

        totalVolume = ((xMax - x + 1) * (yMax - y + 1) * (zMax - z + 1));

        log.info(String.format("Fill task starting: (%d,%d,%d)-(%d,%d,%d)", x,
                y, z, xMax, yMax, zMax));
        log.info(totalVolume + " blocks in total.");
    }

    public int getTotalVolume()
    {
        return totalVolume;
    }

    public void setScheduleState(BukkitScheduler scheduler, int taskId)
    {
        this.scheduler = scheduler;
        this.taskId = taskId;
    }

    @Override
    public void run()
    {
        if (b1 == null || b2 == null) {
            return;
        }

        World world = b1.getWorld();

        log.info(String.format("Fill task resuming: (%d,%d,%d)", x, y, z));

        int c = 0;
        boolean partialWork = false;
        for (; x <= xMax; x++) {
            for (; y <= yMax; y++) {
                for (; z <= zMax; z++) {
                    Block block = world.getBlockAt(x, y, z);

                    if(!plugin.getBlessedBlocks().containsKey(block.getLocation())){
                    	changeBlock(block);//in theory, should prevent blessed chests ect. from being filled away accidentally
                    }

                    if (++c % workSize == 0) {
                        partialWork = true;
                        break;
                    }
                }

                if (partialWork) {
                    break;
                }
                else {
                    z = zMin;
                }
            }

            if (partialWork) {
                break;
            }
            else {
                y = yMin;
            }
        }

        // log.info("Finished unit of work.");

        if (!partialWork) {
            log.info("Fill task ended.");
            finished();
            scheduler.cancelTask(taskId);
        }
    }

    public abstract void changeBlock(Block block);

    public void finished()
    {
    }
}
