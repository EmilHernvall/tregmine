package info.tregmine.christmas.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class TargetingListener implements Listener {

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		if (e.getTarget().getLocation().getWorld().getName() != "Christmas"){
			return;
		}
		
		if ((e.getTarget() instanceof Player)) {
			e.setCancelled(true);
		}
	}
}