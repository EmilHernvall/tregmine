package info.tregmine.listeners;

import info.tregmine.zones.ZonesPlugin;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

public class ZoneEntityListener extends EntityListener 
{
	private final ZonesPlugin plugin;

	public ZoneEntityListener(ZonesPlugin instance) 
	{
		this.plugin = instance;
		plugin.getServer();
	}

	public void onCreatureSpawn(CreatureSpawnEvent event) 
	{
		event.setCancelled(true);			
	}
}
