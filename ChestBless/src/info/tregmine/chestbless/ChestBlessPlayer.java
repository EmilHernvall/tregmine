package info.tregmine.chestbless;


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

public class ChestBlessPlayer extends PlayerListener {
	private final ChestBless plugin;

	public ChestBlessPlayer(ChestBless instance) {
		this.plugin = instance;
		plugin.getServer();
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(player.getName());

		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK  || event.getAction() == Action.LEFT_CLICK_BLOCK) && 
				(		block.getType() == Material.CHEST || 
						block.getType() == Material.FURNACE ||
						block.getType() == Material.BURNING_FURNACE ||
						block.getType() == Material.WORKBENCH)	
				) {
			Location loc = block.getLocation();
 			int checksum = (loc.getBlockX() + "," + loc.getBlockZ() +"," + loc.getWorld().getName()).hashCode();
			int newchecksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
			
			if (this.plugin.chests.containsKey(checksum)) {
				
				String name = this.plugin.chests.get(checksum);
				player.sendMessage(ChatColor.AQUA + "You reblessed a chest for " + name);
				this.plugin.chests.put(newchecksum, name);
				this.plugin.chests.remove(checksum);
				info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();
				mysql.connect();

				info.tregmine.chestbless.Store.savebless(newchecksum, name, player.getWorld().getName(), mysql);
				info.tregmine.chestbless.Store.deletebless(checksum, name, player.getWorld().getName(), mysql);
				mysql.close();

			}

		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.getItemInHand().getType() == Material.BONE && (tregminePlayer.isAdmin() || tregminePlayer.getMetaBoolean("bless") &&
				(		block.getType() == Material.CHEST || 
						block.getType() == Material.FURNACE ||
						block.getType() == Material.BURNING_FURNACE ||
						block.getType() == Material.WORKBENCH)	
				)) {
			Location loc = block.getLocation();
			int checksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
			
			String name = tregminePlayer.getMetaString("chestbless");
			this.plugin.getServer().getPlayer(name).sendMessage(ChatColor.AQUA + "Your god blessed a chest in your name!");
			player.sendMessage(ChatColor.AQUA + "You blessed a chest for " + name);
			this.plugin.chests.put(checksum, name);
			info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();
			mysql.connect();
			info.tregmine.chestbless.Store.savebless(checksum, name, player.getWorld().getName(), mysql);
			this.plugin.log.info(player.getName() + " Blessed a chest " + checksum + " to " + name);
			mysql.close();

			event.setCancelled(true);
			return;
		}


		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK  || event.getAction() == Action.LEFT_CLICK_BLOCK) && 
				(		block.getType() == Material.CHEST || 
						block.getType() == Material.FURNACE ||
						block.getType() == Material.BURNING_FURNACE ||
						block.getType() == Material.WORKBENCH)	
				) {
			Location loc = block.getLocation();
			int checksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
			
			if (this.plugin.chests.containsKey(checksum)) {
				String name = this.plugin.chests.get(checksum);
				if(!player.getName().matches(name)) {
					if (tregminePlayer.isAdmin()) {
						player.sendMessage(ChatColor.YELLOW + "Blessed to: "+ name );
					} else {
						player.sendMessage(ChatColor.RED + "Blessed to: "+ name );
						event.setCancelled(true);
					}
				} else {
					player.sendMessage(ChatColor.AQUA + "Blessed to you");
					event.setCancelled(false);
				}
			}
			return;
		}
	}
}