package info.tregmine.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import static org.bukkit.ChatColor.*;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class BrushCommand extends AbstractCommand implements Listener
{

    public BrushCommand(Tregmine tregmine)
    {
        super(tregmine, "brush");

        PluginManager pm = tregmine.getServer().getPluginManager();
        pm.registerEvents(this, tregmine);
    }


    public List<Block> makeSphere(Location center, int radius)
    {
        List<Block> sphere = new ArrayList<Block>();
        World world = center.getWorld();
        for (int X = -radius; X < radius; X++) {
            for (int Y = -radius; Y < radius; Y++) {
                for (int Z = -radius; Z < radius; Z++) {
                    if (Math.sqrt((X*X) + (Y*Y) + (Z*Z)) <= radius &&
                        Y + center.getBlockY() >= 0) {
                        Block block = world.getBlockAt(X + center.getBlockX(),
                                                       Y + center.getBlockY(),
                                                       Z + center.getBlockZ());
                        sphere.add(block);
                    }
                }
            }
        }

        return sphere;
    }

    @Override
    public boolean handlePlayer(TregminePlayer p, String[] args)
    {
        if (!p.getRank().canBrush()) {
            return true;
        }

        // Check how many arguments on the command
        if (args.length != 2) {
            return false;
        }

        // Check if an item in hand.
        if (p.getItemInHand().getAmount() == 0) {
            p.sendMessage(RED + "[Sphere] No item to bind to in hand!");
            return true;
        }

        Material m = Material.matchMaterial(args[0]);
        if (m == null) {
            int blockId;
            try {
                blockId = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                p.sendMessage(RED + "[Sphere] We detected an issue with your block choice!");
                return true;
            }

            m = Material.getMaterial(blockId);
        }

        if (m == null) {
            p.sendMessage(RED + "[Sphere] We detected an issue with your block choice!");
            return true;
        }

        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            p.sendMessage(RED + "[Sphere] Please specify an integer radius.");
            return true;
        }

        if (radius > 10) {
            p.sendMessage(RED + "[Sphere] Radius can't be larger than 10.");
            return true;
        }

        // No liquids
        if (m == Material.WATER ||
            m == Material.STATIONARY_WATER ||
            m == Material.LAVA ||
            m == Material.STATIONARY_LAVA) {

            p.sendMessage(RED + "[Sphere] You cant brush liquids!");
            return true;
        }

        // Ensure it's a block
        if (!m.isBlock()) {
            p.sendMessage(RED + "[Sphere] You must brush a block, not items!");
            return true;
        }

        List<String> lores = new ArrayList<String>();
        lores.add("Sphere");
        lores.add(m.name());
        lores.add(Integer.toString(radius));
        ItemMeta itemMeta = p.getItemInHand().getItemMeta();
        itemMeta.setLore(lores);

        p.getItemInHand().setItemMeta(itemMeta);

        p.sendMessage(GREEN + "Binded a " + m.name() + " brush with a " +
                      radius + " block radius to this item!");

        return true;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e)
    {
        TregminePlayer p = tregmine.getPlayer(e.getPlayer());
        if (!p.getRank().canBrush()) {
            return;
        }

        Action a = e.getAction();

        // Check right clicking
        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack heldItem = p.getItemInHand();
        if (heldItem == null ||
            heldItem.getItemMeta() == null ||
            heldItem.getItemMeta().getLore() == null) {
            return;
        }

        // Check it's a sphere tool
        List<String> i = heldItem.getItemMeta().getLore();
        if (i == null) {
            return;
        }

        if (!i.contains("Sphere")) {
            return;
        }

        // Check for valid radius
        int radius;
        try {
            radius = Integer.parseInt(i.get(2));
        } catch (NumberFormatException nfe) {
            p.sendMessage(RED + "[Sphere] We detected an issue with your radius!");
            return;
        }

        Location l = p.getDelegate().getTargetBlock(null, 0).getLocation();
        Tregmine.LOGGER.info("Brush " + i.get(1) + " at " + l);
        List<Block> sphere = makeSphere(l, radius);

        Material m = Material.getMaterial(i.get(1));
        for (Block b : sphere) {
            b.setType(m);
        }
    }
}
