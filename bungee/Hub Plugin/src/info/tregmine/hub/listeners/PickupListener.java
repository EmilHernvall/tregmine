package info.tregmine.hub.listeners;

import info.tregmine.hub.Hub;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupListener implements Listener
{
	private Hub plugin;

	public PickupListener(Hub instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}
}