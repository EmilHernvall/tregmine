package info.tregmine.hub.listeners;

import info.tregmine.hub.Hub;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener
{
	private Hub plugin;

	public DropListener(Hub instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
}