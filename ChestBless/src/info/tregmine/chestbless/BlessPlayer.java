package info.tregmine.chestbless;

import info.tregmine.currency.Wallet;

import java.util.Set;
import java.util.HashSet;
//import org.bukkit.ChatColor;
//import org.bukkit.Location;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class BlessPlayer extends PlayerListener {
	private final Bless plugin;
	private Set<Material> allowedMaterials;

	public BlessPlayer(Bless instance) {
		this.plugin = instance;
		plugin.getServer();
		allowedMaterials = new HashSet<Material>();
		allowedMaterials.add(Material.CHEST);
		allowedMaterials.add(Material.FURNACE);
		allowedMaterials.add(Material.BURNING_FURNACE);
		allowedMaterials.add(Material.WOOD_DOOR);
		allowedMaterials.add(Material.WOODEN_DOOR);
		allowedMaterials.add(Material.LEVER);
		allowedMaterials.add(Material.STONE_BUTTON);
		allowedMaterials.add(Material.STONE_PLATE);
		allowedMaterials.add(Material.WOOD_PLATE);
		allowedMaterials.add(Material.WORKBENCH);
		allowedMaterials.add(Material.SIGN_POST);
		allowedMaterials.add(Material.DIODE);
		allowedMaterials.add(Material.DIODE_BLOCK_OFF);
		allowedMaterials.add(Material.DIODE_BLOCK_ON);
		allowedMaterials.add(Material.JUKEBOX);
		allowedMaterials.add(Material.SIGN);
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(player.getName());

		/*if ((event.getAction() == Action.RIGHT_CLICK_BLOCK  || event.getAction() == Action.LEFT_CLICK_BLOCK) && 
				allowedMaterials.contains(block.getType())) {

			Location loc = block.getLocation();
 			int checksum = (loc.getBlockX() + "," + loc.getBlockZ() +"," + loc.getWorld().getName()).hashCode();
			int newchecksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();

			if (this.plugin.chests.containsKey(checksum)) {

				String name = this.plugin.chests.get(checksum);
				player.sendMessage(ChatColor.AQUA + "You reblessed a block for " + name + ".");
				this.plugin.chests.put(newchecksum, name);
				this.plugin.chests.remove(checksum);
				info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();
				mysql.connect();

				info.tregmine.chestbless.Store.savebless(newchecksum, name, player.getWorld().getName(), mysql);
				info.tregmine.chestbless.Store.deletebless(checksum, name, player.getWorld().getName(), mysql);
				mysql.close();

			}

		}*/

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && 
				player.getItemInHand().getType() == Material.BONE && 
				(tregminePlayer.isAdmin() || tregminePlayer.getMetaBoolean("mentor")) &&
				allowedMaterials.contains(block.getType())) {

			if (!tregminePlayer.isAdmin()) {
				int amount = 5000;
				if (block.getType().equals(Material.CHEST)) {
					amount = 30000;
				}

				Wallet wallet = new Wallet(tregminePlayer.getName());
				long newbalance = wallet.balance()-amount;
				if (newbalance >= 0) {
					wallet.take(amount);
					event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + (amount + " tregs was taken from you"));
				} else {
					event.getPlayer().sendMessage(ChatColor.RED + "You need " + amount + " tregs");
					return;
				}
			}

			Location loc = block.getLocation();
			int checksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();

			String name = tregminePlayer.getMetaString("chestbless");
			this.plugin.getServer().getPlayer(name).sendMessage(ChatColor.AQUA + "Your god blessed it in your name!");
			player.sendMessage(ChatColor.AQUA + "You blessed for " + name + ".");
			this.plugin.chests.put(checksum, name);
			info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();
			mysql.connect();
			info.tregmine.chestbless.Store.savebless(checksum, name, player.getWorld().getName(), mysql);
			this.plugin.log.info(player.getName() + " Blessed a block " + checksum + " to " + name + ".");
			mysql.close();

			event.setCancelled(true);
			return;
		}


		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK  || event.getAction() == Action.LEFT_CLICK_BLOCK) && 
				allowedMaterials.contains(block.getType())) {

			Location loc = block.getLocation();
			int checksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();

			if (this.plugin.chests.containsKey(checksum)) {
				String name = this.plugin.chests.get(checksum);
				if(!player.getName().matches(name)) {
					if (tregminePlayer.isAdmin()) {
						player.sendMessage(ChatColor.YELLOW + "Blessed to: "+ name + "." );
					} else {
						player.sendMessage(ChatColor.RED + "Blessed to: "+ name + ".");
						event.setCancelled(true);
					}
				} else {
					player.sendMessage(ChatColor.AQUA + "Blessed to you.");
					event.setCancelled(false);
				}
			}
			return;
		}
	}
}