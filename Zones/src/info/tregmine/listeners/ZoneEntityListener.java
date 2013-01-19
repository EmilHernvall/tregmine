package info.tregmine.listeners;

import java.util.EnumSet;
import java.util.Set;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.ZoneWorld;
import info.tregmine.zones.ZonesPlugin;

//import org.bukkit.ChatColor;
import org.bukkit.Location;
//import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
//import org.bukkit.event.entity.EntityShootBowEvent;

public class ZoneEntityListener implements Listener
{
	private static final Set<EntityType> allowedMobs = 
			EnumSet.of(
					EntityType.CHICKEN,
					EntityType.COW, 
					EntityType.PIG,
					EntityType.SHEEP, 
					EntityType.SQUID,
					EntityType.WOLF,
					EntityType.IRON_GOLEM,
					EntityType.BAT,
					EntityType.VILLAGER,
					EntityType.MUSHROOM_COW,
					EntityType.OCELOT
					);

	private final ZonesPlugin plugin;
	private final Tregmine tregmine;

	public ZoneEntityListener(ZonesPlugin instance) 
	{
		this.plugin = instance;
		this.tregmine = instance.tregmine;
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) 
	{
		Entity entity = event.getEntity();

		Location location = event.getLocation();
		Point pos = new Point(location.getBlockX(), location.getBlockZ());

		ZoneWorld world = plugin.getWorld(entity.getWorld());
		Zone zone = world.findZone(pos);
		
		if (zone == null || zone.hasHostiles()) {
			return;
		}

		if (!allowedMobs.contains(event.getEntityType())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {	

		if(event.getEntity().getWorld().getName().matches("world_the_end")) {
			return;
		}

	    if(event.getCause() == DamageCause.PROJECTILE)	    {

	    	
	    	
	    	Projectile proj = (Projectile)event.getDamager();
	        LivingEntity shooter = proj.getShooter();
	        
	        if(shooter instanceof Player) {
	        	Player p = (Player) shooter;
	        	
	    		ZoneWorld world = plugin.getWorld(p.getWorld());

	    		TregminePlayer player = tregmine.getPlayer(p);

	    		Location location = player.getLocation();
	    		Point pos = new Point(location.getBlockX(), location.getBlockZ());

	    		Zone currentZone = player.getCurrentZone();
	    		if (currentZone == null || !currentZone.contains(pos)) {
	    			currentZone = world.findZone(pos);
	    			player.setCurrentZone(currentZone);
	    		}

	    				if (currentZone == null || !currentZone.isPvp()) {
	    					event.setCancelled(true);
	    				} else {
	    					event.setCancelled(false);
	    				}

	        	
	        	
	        	
	        }

	    }
		
		
		
		//	public void onEntityDamage(EntityDamageEvent event)	{
		if (event.getEntity() instanceof Player && event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)event).getDamager() instanceof Player) {
		Entity entity = event.getEntity();
		
		ZoneWorld world = plugin.getWorld(entity.getWorld());

		TregminePlayer player = tregmine.getPlayer((Player)event.getEntity());

		Location location = player.getLocation();
		Point pos = new Point(location.getBlockX(), location.getBlockZ());

		Zone currentZone = player.getCurrentZone();
		if (currentZone == null || !currentZone.contains(pos)) {
			currentZone = world.findZone(pos);
			player.setCurrentZone(currentZone);
		}

				if (currentZone == null || !currentZone.isPvp()) {
					event.setCancelled(true);
				} else {
					event.setCancelled(false);
				}
				
		}
		return;
	}
}

