package info.tregmine.chestbless;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlessBlock implements Listener {
	private final Bless plugin;


	public  BlessBlock(Bless instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void onBlockBreak (BlockBreakEvent event) {
		Location loc = event.getBlock().getLocation();
		int checksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
		Player player = event.getPlayer();
		if (this.plugin.chests.containsKey(checksum)) {
			player.sendMessage(ChatColor.RED + "You can't destroy a blessed item.");
			event.setCancelled(true);
			return;
		}

	}

	@EventHandler
	public void onBlockPlace (BlockPlaceEvent event)	{
		Block block = event.getBlockPlaced();

		if (block.getType() == Material.CHEST) {
			Player player = event.getPlayer();

			Location loc = block.getLocation();
			int checksum1 = ((loc.getBlockX()+1) + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
			int checksum2 = ((loc.getBlockX()-1) + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
			int checksum3 = (loc.getBlockX() + "," + (loc.getBlockZ()+1) + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
			int checksum4 = (loc.getBlockX() + "," + (loc.getBlockZ()-1) + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();

			if (this.plugin.chests.containsKey(checksum1) || this.plugin.chests.containsKey(checksum2) || this.plugin.chests.containsKey(checksum3) || this.plugin.chests.containsKey(checksum4)) {
				player.sendMessage(ChatColor.RED + "You can't place a chest next to one that is already blessed.");
				event.setCancelled(true);
				return;
			}
		}

		if (block.getType() == Material.HOPPER) {
			Player player = event.getPlayer();
			info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(player.getName());
			Location loc = block.getLocation();
			int checksum1 = (loc.getBlockX() + "," + loc.getBlockZ() + "," + (loc.getBlockY() - 1) + "," + loc.getWorld().getName()) .hashCode();
			int checksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
			if (this.plugin.chests.containsKey(checksum1)) {
				if (this.plugin.chests.containsKey(checksum)) {
					String name = this.plugin.chests.get(checksum);
					if (!player.getName().matches(name)) {
						if (!tregminePlayer.isAdmin()) {
							player.sendMessage(ChatColor.RED + "You can't place a hopper under a blessed chest.");
							event.setCancelled(true);
						}
					} else {
						player.sendMessage(ChatColor.AQUA + "You placed a hopper under a blessed chest");
						event.setCancelled(false);
					}
				}
			}
		}
	}
}
