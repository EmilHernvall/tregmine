package info.tregmine.listeners;

//import org.bukkit.entity.Entity;
//import org.bukkit.entity.Monster;
//import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
//import org.bukkit.entity.Monster;
//import org.bukkit.ChatColor;
import info.tregmine.Tregmine;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
//import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
//import org.bukkit.event.entity.EntityDamageEvent.DamageCause;


public class TregmineEntityListener extends EntityListener  {
	private final Tregmine plugin;

	public TregmineEntityListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	public void onEndermanPickup(EndermanPickupEvent event) {
		event.setCancelled(true);
	}
	
	
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
	}

	public void onEntityDamageByBlock (EntityDamageByBlockEvent event){
		if(event.getEntity() instanceof Player) {
			event.setCancelled(true);
		}    
	}

	public void onEntityDamageByEntity (EntityDamageByEntityEvent 	event ) {
		
		Player player = null;
		Player attaker = null;

		if(event.getEntity() instanceof Player) {
			player = (Player) event.getEntity();
			info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(player.getName());
			if (tregminePlayer.isAdmin() || tregminePlayer.isImmortal()) {
				if(event.getDamager() instanceof Monster) {
					Monster mob = (Monster) event.getDamager();
					mob.setHealth(0);
				}
				
			} else {
			int newHealth = player.getHealth() - event.getDamage();
			
			if(event.getDamager() instanceof Player) {
				attaker = (Player) event.getDamager();
				attaker.sendMessage(ChatColor.RED + "Don't hurt your friend");
			} else {			

				if(newHealth > 0) {
					player.setHealth(newHealth);
				} else {
					player.setHealth(20);
					player.teleport(player.getWorld().getSpawnLocation());				
				}
			}
			}
			event.setCancelled(true);
			}
}

	public void onEntityDamage (EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
		if (event.getCause() !=	DamageCause.FIRE_TICK || event.getCause() != DamageCause.FIRE) {
			Player player = (Player) event.getEntity();
			info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(player.getName());
			if (tregminePlayer.isAdmin() || tregminePlayer.isImmortal()) {
				event.setCancelled(true);
			} else {
				int newHealth = player.getHealth() - event.getDamage();
				if (newHealth <= 0) {
					player.setHealth(20);
					player.teleport(player.getWorld().getSpawnLocation());
				} else {
					player.setHealth(newHealth);
				}
			}
			event.setCancelled(true);
		}    
		event.setCancelled(true);
		}
	}
}