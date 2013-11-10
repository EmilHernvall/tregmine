package info.tregmine.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class ExpListener implements Listener
{
    public final static String ITEM_NAME = "Tregmine XP Bottle";

    private Tregmine plugin;

    public ExpListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void xpFirstFill(PlayerInteractEvent event)
    {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        if (item.getType() != Material.GLASS_BOTTLE) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR ||
            event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getLevel() < 2) {
            return;
        }

        if (!player.getRank().canBottleXP()) {
            return;
        }

        // Creating the XPBottle
        List<String> lores = new ArrayList<String>();
        lores.add("XP Value: 1 Level");
        ItemStack xpBottle = new ItemStack(Material.EXP_BOTTLE, 1);
        ItemMeta xpMeta = xpBottle.getItemMeta();
        xpMeta.setLore(lores);
        xpMeta.setDisplayName(ITEM_NAME);
        xpBottle.setItemMeta(xpMeta);

        Inventory inv = player.getInventory();
        if (item != null && item.getAmount() == 1) {
            player.setItemInHand(xpBottle);
        } else{
            HashMap<Integer, ItemStack> map = inv.addItem(xpBottle);
            if (map.containsValue(xpBottle)) {
                return;
            }

            item.setAmount(item.getAmount() - 1);
        }

        player.setLevel(player.getLevel() - 2);
    }

    @EventHandler
    public void xpHigherFill(PlayerInteractEvent event)
    {
        ItemStack item = event.getItem();
        if (item != null &&
            item.getType() != Material.EXP_BOTTLE) {
           return;
        }
        if (item != null && !ITEM_NAME.equals(item.getItemMeta().getDisplayName())) {
            return;
        }
        if (event.getAction() != Action.LEFT_CLICK_AIR &&
            event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        TregminePlayer player = this.plugin.getPlayer(event.getPlayer());
        if (!player.getRank().canBottleXP()) {
            return;
        }

        if (player.getLevel() < 2) {
            return;
        }

        String[] splitLore = null;
        try{
            splitLore = item.getItemMeta().getLore().toString().split(" ");
        } catch(NullPointerException e) {
            return;
        }

        int level = Integer.parseInt(splitLore[2])+1;

        if (level > 30) {
            player.sendMessage(ChatColor.AQUA +
                    "XP in a bottle is capped at 30 levels per bottle!");
            return;
        }

        // Creating the XPBottle
        List<String> lores = new ArrayList<String>();
        lores.add("XP Value: "+ level +" Levels");
        ItemStack xpBottle = new ItemStack(Material.EXP_BOTTLE, 1);
        ItemMeta xpMeta = xpBottle.getItemMeta();
        xpMeta.setLore(lores);
        xpMeta.setDisplayName(ITEM_NAME);
        xpBottle.setItemMeta(xpMeta);

        Inventory inv = player.getInventory();
        if (item.getAmount() == 1) {
            player.setItemInHand(xpBottle);
        } else {
            HashMap<Integer, ItemStack> map = inv.addItem(xpBottle);
            if (map.containsValue(xpBottle)) {
                return;
            }
        }

        if (event.getItem().getAmount() == 1) {
            inv.remove(event.getItem());
        } else {
            item.setAmount(item.getAmount() - 1);
        }
        player.setLevel(player.getLevel() - 2);
    }

    @EventHandler
    public void xpEmpty(PlayerInteractEvent event)
    {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.EXP_BOTTLE) {
            return;
        }
        if (item != null && !ITEM_NAME.equals(item.getItemMeta().getDisplayName())) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        String[] splitLore = null;
        try {
            splitLore = item.getItemMeta().getLore().toString().split(" ");
        } catch (NullPointerException e) {
            return;
        }

        event.setCancelled(true);

        int experience = Integer.parseInt(splitLore[2]);

        TregminePlayer player = this.plugin.getPlayer(event.getPlayer());
        player.setLevel(player.getLevel()+experience);

        Inventory inv = player.getInventory();
        if (event.getItem().getAmount() == 1) {
            inv.remove(event.getItem());
        } else {
            item.setAmount(item.getAmount() - 1);
        }
    }
}
