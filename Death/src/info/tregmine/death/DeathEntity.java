package info.tregmine.death;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class DeathEntity extends EntityListener  {
	private final Death plugin;

	public DeathEntity(Death instance) {
		plugin = instance;
		plugin.getServer();
	}

	
    @Override
	public void onEntityDeath (EntityDeathEvent event) {
	}

	public void onEntityExplode (EntityExplodeEvent event) {
		if(event.getEntity() instanceof Player) {
		}
	}

	public void onEntityDamage (EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
		}
	}
}