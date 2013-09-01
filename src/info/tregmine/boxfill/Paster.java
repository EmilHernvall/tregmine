package info.tregmine.boxfill;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
//import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import info.tregmine.api.TregminePlayer;

public class Paster implements Runnable
{
    private History undoHistory;
    private TregminePlayer player;
    private World world;
    private Block base;
    private SavedBlocks blocks;
    private double theta;
    private int workSize;
    private SavedBlocks undo;
    private Set<Location> modifiedSet;

    private BukkitScheduler scheduler;
    private int taskId;

    private int i;

    public Paster(History undoHistory, TregminePlayer player, World world, Block base,
            SavedBlocks blocks, double theta, int workSize)
    {
        this.undoHistory = undoHistory;
        this.player = player;
        this.world = world;
        this.base = base;
        this.blocks = blocks;
        this.theta = theta;

        this.workSize = workSize;
        this.i = 0;
        this.undo = new SavedBlocks();
        this.modifiedSet = new HashSet<Location>();
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
        boolean partialWork = false;
        for (; i < list.size(); i++) {
            BlockState state = list.get(i);

            int x = state.getX() - blocks.getX();
            int y = state.getY() - blocks.getY();
            int z = state.getZ() - blocks.getZ();

            if (theta != 0.0) {

                int xp2 = (int)Math.round(x * Math.cos(theta) - z * Math.sin(theta));
                int zp2 = (int)Math.round(x * Math.sin(theta) + z * Math.cos(theta));

                x = xp2;
                z = zp2;
            }

            Block block = world.getBlockAt(base.getX() + x,
                                           base.getY() + y,
                                           base.getZ() + z);

            // use a set to make sure that we don't add the same block
            // more than once. this might happen because we apply
            // the rotation matrix but have to truncate the values
            // to integers.
            if (!modifiedSet.contains(block.getLocation())) {
                undo.addBlock(block.getState());
                modifiedSet.add(block.getLocation());
            }

            block.setType(state.getType());
            block.setData(state.getData().getData());

            if (i != 0 && i % workSize == 0) {
                partialWork = true;
                break;
            }
        }

        if (!partialWork) {
            undoHistory.set(player, undo);
            player.sendMessage(ChatColor.DARK_AQUA + "Paste is finished.");
            scheduler.cancelTask(taskId);
        }
    }
}
