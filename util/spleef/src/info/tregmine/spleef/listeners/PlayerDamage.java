package info.tregmine.spleef.listeners;

import info.tregmine.spleef.ArenaManager;
import info.tregmine.spleef.Spleef;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerDamage implements Listener {

    private final Spleef plugin;

    public PlayerDamage(Spleef instance){
        this.plugin = instance;
    }
    
	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (ArenaManager.getInstance().getArena(p) != null) e.setCancelled(true);
		}
	}
	
    @EventHandler
    public void onPlayerFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getCause() == DamageCause.FALL && ArenaManager.getInstance().getArena(p) != null) {
                e.setDamage(0.0);
            }
        }
    }
}
