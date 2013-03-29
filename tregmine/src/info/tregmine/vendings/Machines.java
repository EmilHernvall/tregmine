package info.tregmine.vendings;

import java.util.ArrayList;
import java.util.List;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.currency.Wallet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Machines implements Listener {

	private final Tregmine plugin;

	public Machines(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void buttons(PlayerInteractEvent event) {

		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			int hash = info.tregmine.api.math.Checksum.block(block);
			TregminePlayer tregminePlayer = plugin.getPlayer(event.getPlayer());
				if(Material.STONE_BUTTON.equals(block.getType())){
				if(hash == -1189228543){
					Wallet wallet = new Wallet(tregminePlayer.getPlayer().getName());
					if(wallet.take(25000)){
						ItemStack item = new ItemStack(Material.PAPER, 1);
						PlayerInventory inventory = tregminePlayer.getInventory();
						ItemMeta meta = item.getItemMeta();
						List<String> lore = new ArrayList<String>();
						lore.add(info.tregmine.api.lore.Created.PURCHASED.toColorString());
						lore.add(ChatColor.WHITE + "By: " + tregminePlayer.getName());
						lore.add(ChatColor.WHITE + "Value: 25,000 Tregs");
						meta.setLore(lore);
						meta.setDisplayName(ChatColor.GREEN + "DIRT -> SPONG Coupon");
						item.setItemMeta(meta);
						inventory.addItem(item);
						tregminePlayer.updateInventory();
						tregminePlayer.sendMessage(ChatColor.AQUA + "You received 1 coupon for 25,000 Tregs.");
						plugin.log.info(tregminePlayer.getName() + " :COUPONBUTTON");
					}else{
						tregminePlayer.sendMessage(ChatColor.RED + "You need at least 25,000 tregs for this button.");
					}
				}
			}	
		}
	}
}