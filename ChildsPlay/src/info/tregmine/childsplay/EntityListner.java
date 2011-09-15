package info.tregmine.childsplay;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;


public class EntityListner extends EntityListener {
	private final Main plugin;

	public EntityListner(Main instance) {
		this.plugin = instance;
		plugin.getServer();
	}


	public void onCreatureSpawn (CreatureSpawnEvent event) {
//			event.setCancelled(true);			
	}

}
