package info.tregmine.vendings;

import java.util.ArrayList;
import java.util.List;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.currency.Wallet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
//import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Machines implements Listener {

	public Tregmine tregmine = null;
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

			TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(event.getPlayer());
			if(block.getType() == Material.STONE_BUTTON){
				Location location = block.getLocation();
				int x = location.getBlockX();
				int y = location.getBlockY();
				int z = location.getBlockZ();
				if(x == -7573 && y == 70 && z == 560){
					Wallet wallet = new Wallet(tregminePlayer);
					if(wallet.take(25000)){
						ItemStack item = new ItemStack(Material.PAPER, 1);
						PlayerInventory inventory = tregminePlayer.getInventory();
						ItemMeta meta = item.getItemMeta();
						List<String> lore = new ArrayList<String>();
						lore.add(info.tregmine.api.lore.Created.PURCHASED.toColorString());
						lore.add(ChatColor.WHITE + "By: " + tregminePlayer.getName());
						lore.add(ChatColor.WHITE + " Value: 25,000 Tregs");
						meta.setLore(lore);
						meta.setDisplayName(ChatColor.GREEN + "Sponge Coupon");
						//						wallet.take(25000);

						item.setItemMeta(meta);
						inventory.addItem(item);
						tregminePlayer.updateInventory();
						plugin.log.info(tregminePlayer.getName() + " :COUPONBUTTON");

					}else{
						tregminePlayer.sendMessage(ChatColor.RED + "You need at least 25,000 tregs for this button.");
					}
				}
			}
		}
	}
}