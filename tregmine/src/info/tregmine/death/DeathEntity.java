package info.tregmine.death;

import java.util.Random;

import info.tregmine.Tregmine;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEntity implements Listener  {
	private final Tregmine plugin;

	public DeathEntity(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	
	@EventHandler
	public void onEntityDeath (EntityDeathEvent event) {
		 if (event instanceof PlayerDeathEvent) {
             PlayerDeathEvent e = (PlayerDeathEvent) event;
             e.setDeathMessage(Insult.random());
         }
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