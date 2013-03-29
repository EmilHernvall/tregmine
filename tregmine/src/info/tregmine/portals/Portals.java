package info.tregmine.portals;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class Portals implements Listener {

	private final Tregmine plugin;

	public Portals(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	private void portalButton(int button, Block block, TregminePlayer player, String world, Location loc) {
		//		Location loc =  new Location(player.getServer().getWorld(world), x,y,z, yaw, pitch);
		Inventory inventory = player.getInventory();

		if(button == info.tregmine.api.math.Checksum.block(block)){

			if (inventory.getSize() > 0) {
				player.sendMessage(ChatColor.RED + "You carry to much for the portal magic to work");
				return;
			}

			player.teleport(loc);
			player.sendMessage(ChatColor.YELLOW + "Thanks for using TregPort's services");
		}
	}


	@EventHandler
	public void buttons(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;

		Block block = event.getClickedBlock();
		TregminePlayer player = plugin.getPlayer(event.getPlayer());

		portalButton(-1488547832, block, player, "world", player.getServer().getWorld("world").getSpawnLocation());



	}
	//-1488547832
}
