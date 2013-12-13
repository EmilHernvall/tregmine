package info.tregmine.christmas.listeners;

import info.tregmine.christmas.ChristmasMain;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TargetingListener implements Listener {

	private ChristmasMain plugin;

	public TargetingListener(ChristmasMain instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		if(e.getEntityType() == EntityType.GIANT){
			return;
		}
		if (e.getEntity().getLocation().getWorld().getName() == "Christmas"
				&& e.getEntity() != null
				&& e.getEntity().getLocation().getWorld() != null
				){
			if ((e.getTarget() instanceof Player) || e.getTarget() instanceof Villager) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerMount(PlayerInteractEntityEvent e) {
		if(!(e.getRightClicked() instanceof Player)){
			return;
		}
		
		//Just in case Chrille tries to mount me and crash my server again
		if(!(e.getPlayer().getPassenger() != null)){
			return;
		}
		
		e.getRightClicked().setPassenger(e.getPlayer());
	}
}