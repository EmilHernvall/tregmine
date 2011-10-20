package info.tregmine.chestbless;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlessBlock extends BlockListener {
	private final Bless plugin;


	public  BlessBlock(Bless instance) {
		plugin = instance;
		plugin.getServer();
	}

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
				player.sendMessage(ChatColor.RED + "You can't place a " +
						" next to a already blessed one.");
				event.setCancelled(true);
				return;
			}
		}


	}
}
