package info.tregmine.tools;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import java.io.IOException;
import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class PortalListener implements Listener
{
    public static class GravityTask implements Runnable
    {
        private TregminePlayer player;
        private Location srcLocation;
        private Location dstLocation;
        private FallingBlock currentBlock = null;
        private Material material;
        private byte data;

        private BukkitTask task = null;

        public GravityTask(TregminePlayer player)
        {
            this.player = player;
        }

        public Location getSrcLocation() { return srcLocation; }
        public void setSrcLocation(Location v) { this.srcLocation = v; }

        public Location getDstLocation() { return dstLocation; }
        public void setDstLocation(Location v) { this.dstLocation = v; }

        public Material getMaterial() { return material; }
        public void setMaterial(Material v) { this.material = v; }

        public byte getData() { return data; }
        public void setData(byte v) { this.data = v; }

        @Override
        public void run()
        {
            int distance = 5;
            Block block = null;
            while (distance > 0) {
                block = player.getDelegate().getTargetBlock((Set<Material>) null, distance);
                if (block.getType().equals(Material.AIR)) {
                    break;
                }

                distance--;
            }

            if (currentBlock != null) {
                currentBlock.remove();
                currentBlock = null;
            }

            FallingBlock gravityBlock =
                player.getWorld().spawnFallingBlock(block.getLocation(),
                                                    material,
                                                    data);
            gravityBlock.setDropItem(false);
            gravityBlock.setFallDistance(0f);
            gravityBlock.setVelocity(new Vector(0, 0.1, 0));

            dstLocation = gravityBlock.getLocation();
            currentBlock = gravityBlock;
        }

        public void start()
        {
            BukkitScheduler scheduler = player.getServer().getScheduler();
            task = scheduler.runTaskTimer(player.getPlugin(), this, 0L, 1L);
        }

        public void cancel()
        {
            task.cancel();
        }
    }

    public final static Set<Material> disallowedBlocks =
        EnumSet.of(
            Material.BED,
            Material.WOOD_DOOR,
            Material.WOODEN_DOOR,
            Material.IRON_DOOR,
            Material.IRON_DOOR_BLOCK,
            Material.DOUBLE_PLANT,
            Material.PISTON_EXTENSION,
            Material.BEDROCK,
            Material.ENDER_PORTAL_FRAME,
            Material.CHEST,
            Material.HOPPER,
            Material.MOB_SPAWNER,
            Material.DROPPER,
            Material.DISPENSER,
            Material.FURNACE,
            Material.BREWING_STAND
        );

    private Tregmine plugin;
    private Map<TregminePlayer, GravityTask> gravityTasks = null;

    public PortalListener(Tregmine instance)
    {
        this.plugin = instance;
        this.gravityTasks = new HashMap<TregminePlayer, GravityTask>();
    }

    @EventHandler
    public void dropBlock(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK) {

            return;
        }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (!gravityTasks.containsKey(player)) {
            return;
        }

        GravityTask task = gravityTasks.get(player);

        if (!player.hasBlockPermission(task.getDstLocation(), false)) {
            player.sendMessage(ChatColor.RED + "Can not place it here!");
            return;
        }

        gravityTasks.remove(player);
        task.cancel();
        player.sendMessage(ChatColor.GREEN + "Successfully dropped block!");
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void pickupBlock(PlayerInteractEvent event)
    throws IOException
    {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (event.getItem() == null ||
            !Material.DIAMOND_HOE.equals(event.getItem().getType())) {
            return;
        }

        TregminePlayer p = plugin.getPlayer(event.getPlayer());

        if (!p.getRank().canUseTools()) return;

        List<String> lore = p.getItemInHand().getItemMeta().getLore();

        if (lore.isEmpty()) return;
        if (!lore.get(0).equals(ToolsRegistry.GravityGunLoreTag)) return;

        Block block = event.getClickedBlock();
        if (!block.getType().isSolid() ||
            disallowedBlocks.contains(block.getType())) {

            p.sendMessage(ChatColor.RED + "Denied use of Gravity Gun!");
            p.sendMessage(ChatColor.AQUA + "Try using blocks!");

            return;
        }

        if (plugin.getBlessedBlocks().containsKey(block.getLocation())) {
            p.sendMessage(ChatColor.RED + "This block is blessed!");
            return;
        }

        if (plugin.getFishyBlocks().containsKey(block.getLocation())) {
            p.sendMessage(ChatColor.RED + "Can not move fishyblocks!");
            return;
        }

        if (!p.hasBlockPermission(block.getLocation(), false)) {
            p.sendMessage(ChatColor.RED + "Can not pick up from here!");
            return;
        }

        if (gravityTasks.containsKey(p)) {
            p.sendMessage(ChatColor.RED + "You are already carrying a block, You're not that strong!");
            return;
        }

        String[] durability = lore.get(1).split("/");
        if (Integer.parseInt(durability[0]) == 0) {
            p.sendMessage(ChatColor.RED + "You are out of durability, Try repairing!");
            return;
        }

        lore.remove(1);
        lore.add(Integer.parseInt(durability[0]) - 1 + "/1000");

        GravityTask task = new GravityTask(p);
        task.setMaterial(block.getType());
        task.setData(block.getData());
        task.setDstLocation(block.getLocation());
        task.setSrcLocation(block.getLocation());
        gravityTasks.put(p, task);

        task.start();

        block.setType(Material.AIR);
    }

    public void clearUp(PlayerQuitEvent event) {
        if (gravityTasks.containsKey(event.getPlayer())) {
            GravityTask task = gravityTasks.get(event.getPlayer());
            task.currentBlock.remove();
            event.getPlayer().getWorld().getBlockAt(task.srcLocation).setType(task.material);
            event.getPlayer().getWorld().getBlockAt(task.srcLocation).setData(task.data);
            gravityTasks.remove(event.getPlayer());
        }
    }
}
