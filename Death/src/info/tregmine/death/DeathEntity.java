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
		
		
//		if(event.getEntity() instanceof Player) {
//			Player player = (Player) event.getEntity();
//			String dieTXT = info.tregmine.death.Insult.random();
//			this.plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + player.getName() + " died - " + dieTXT);
			
//		}
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