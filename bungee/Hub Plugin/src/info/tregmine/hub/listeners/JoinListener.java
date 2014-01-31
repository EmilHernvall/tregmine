package info.tregmine.hub.listeners;

import info.tregmine.hub.Hub;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinListener implements Listener
{
	private Hub plugin;

	public JoinListener(Hub instance)
	{
		this.plugin = instance;
	}

	ItemStack book = new ItemStack(Material.BOOK);
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Location spawn = p.getWorld().getSpawnLocation();
		p.teleport(spawn.add(0.5,0,0.5));
		p.getInventory().clear();
		p.getInventory().addItem(book);
	}
}