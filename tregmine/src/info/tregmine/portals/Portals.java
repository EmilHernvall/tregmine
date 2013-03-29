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

	private void portalButton(int button, Block block, TregminePlayer player, Location loc) {
		Inventory inventory = player.getInventory();

		if(button == info.tregmine.api.math.Checksum.block(block)){

			for (int i = 0; i < inventory.getSize(); i++) {
				if (inventory.getItem(i) != null) {
					player.sendMessage(ChatColor.RED + "You are carrying too much for the portal's magic to work.");
					return;
				}
			}

			player.teleport(loc);
			player.sendMessage(ChatColor.YELLOW + "Thanks for traveling with TregPort!");
		}
	}


	@EventHandler
	public void buttons(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;

		Block block = event.getClickedBlock();
		TregminePlayer player = plugin.getPlayer(event.getPlayer());

		// Portal in tower of einhome
		portalButton(-1488547832, block, player, 	this.plugin.getServer().getWorld("world").getSpawnLocation());

		
		// Portal in elva
		portalButton(-1559526734, block, player,	this.plugin.getServer().getWorld("world").getSpawnLocation());
		portalButton(-1349166371, block, player,	this.plugin.getServer().getWorld("treton").getSpawnLocation());
		portalButton(1371197620, block, player,		this.plugin.getServer().getWorld("citadel").getSpawnLocation());
		
		// portals in world
		portalButton(-973919203, block, player,		this.plugin.getServer().getWorld("treton").getSpawnLocation());
		portalButton(-1559526734, block, player,	this.plugin.getServer().getWorld("treton").getSpawnLocation());
		portalButton(1259780606, block, player,		this.plugin.getServer().getWorld("citadel").getSpawnLocation());
		portalButton(690186900, block, player,		this.plugin.getServer().getWorld("elva").getSpawnLocation());
		portalButton(209068875, block, player,		this.plugin.getServer().getWorld("einhome").getSpawnLocation());
		
		// portals in TRETON
		portalButton(45939467, block, player,		this.plugin.getServer().getWorld("world").getSpawnLocation());
		portalButton(-1408237330, block, player,	this.plugin.getServer().getWorld("citadel").getSpawnLocation());
		portalButton(559131756, block, player,		this.plugin.getServer().getWorld("elva").getSpawnLocation());

		// portals in CITADEL
		portalButton(1609346891, block, player,		this.plugin.getServer().getWorld("world").getSpawnLocation());
		portalButton(-449465967, block, player,		this.plugin.getServer().getWorld("treton").getSpawnLocation());
		portalButton(1112623336, block, player,		this.plugin.getServer().getWorld("elva").getSpawnLocation());
	}
	

	//-1488547832
}
