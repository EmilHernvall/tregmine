package info.tregmine.death;

import org.bukkit.ChatColor;
//import org.bukkit.entity.Creature;
//import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
//import org.bukkit.entity.Slime;
//import org.bukkit.entity.Wolf;
//import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
//import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

public class DeathEntity extends EntityListener  {
	private final Death plugin;

	public DeathEntity(Death instance) {
		plugin = instance;
		plugin.getServer();
	}


	public void onEntityDeath (EntityDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			String dieTXT = info.tregmine.death.Insult.random();
			this.plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + player.getName() + " died - " + dieTXT);
		}
	}

	public void onEntityExplode (EntityExplodeEvent event) {
		if(event.getEntity() instanceof Player) {
//			Player player = (Player) event.getEntity();
//			info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(player.getName());

//			boolean isAdmin = tregminePlayer.isAdmin();
//			boolean isImmortal = tregminePlayer.isImmortal();
//			if (isAdmin || isImmortal) {				
//				event.setCancelled(true);
//				return;
//			}
		}
	}

	public void onEntityDamage (EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
//			Player player = (Player) event.getEntity();
//			info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(player.getName());

//			boolean isAdmin = tregminePlayer.isAdmin();
//			boolean isImmortal = tregminePlayer.isImmortal();
			
//			if (isAdmin || isImmortal) {
//				event.setCancelled(true);
//				return;
//			}
		}
	}
}