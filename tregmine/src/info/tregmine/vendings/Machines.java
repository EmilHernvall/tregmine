package info.tregmine.vendings;

import info.tregmine.Tregmine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Machines implements Listener {

	private final Tregmine plugin;

	public Machines(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void buttons(PlayerInteractEvent event) {
		
	}
	
	
}