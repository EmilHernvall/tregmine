package info.tregmine.listeners;

import info.tregmine.Tregmine;

//import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExplodeEvent;


public class TregmineEntityListener  implements Listener  {
	private final Tregmine plugin;

	public TregmineEntityListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) 	{
		if (event.getSpawnReason() == SpawnReason.SPAWNER_EGG) {
			event.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onEntityExplode (EntityExplodeEvent event) {

		if(event.getLocation().getWorld().getName().matches("world")) {
			event.setCancelled(true);
		}

		if(event.getLocation().getWorld().getName().matches("citadel")) {
			event.setCancelled(true);
		}

		if(event.getLocation().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}
		
		if(event.getLocation().getWorld().getName().matches("creative")) {
			event.setCancelled(true);
		}
	}
}