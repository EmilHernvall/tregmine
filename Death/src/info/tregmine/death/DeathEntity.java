package info.tregmine.death;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class DeathEntity implements Listener  {
	private final Death plugin;

	public DeathEntity(Death instance) {
		plugin = instance;
		plugin.getServer();
	}

	
	@EventHandler
	public void onEntityDeath (EntityDeathEvent event) {
	}

	@EventHandler
	public void onEntityExplode (EntityExplodeEvent event) {
		if(event.getEntity() instanceof Player) {
		}
	}

	@EventHandler
	public void onEntityDamage (EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
		}
	}
}