package info.tregmine.listeners;

import java.util.EnumSet;
import java.util.Set;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.ZoneWorld;
import info.tregmine.zones.ZonesPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
//import org.bukkit.entity.CreatureType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class ZoneEntityListener implements Listener
{
	private static final Set<EntityType> allowedMobs = 
			EnumSet.of(EntityType.CHICKEN, EntityType.COW, 
					EntityType.PIG, EntityType.SHEEP, 
					EntityType.SQUID, EntityType.WOLF,
					EntityType.VILLAGER, EntityType.MUSHROOM_COW);
	
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

//	@EventHandler
//	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
//		this.plugin.tregmine.log.info("onEntityDamageByEntity");
		
//		Entity entity2 = event.getEntity();
//		if (entity2 instanceof Player) {
//			TregminePlayer player = tregmine.getPlayer((Player)entity2);
//			player.sendMessage("V:" + event.getCause().toString());
//		}

//		Entity entDamager = event.getDamager();
//		if (entDamager instanceof Player) {
//			TregminePlayer player = tregmine.getPlayer((Player)entDamager);
//			player.sendMessage("A:" + event.getCause().toString());
//		}
//	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
//	public void onEntityDamage(EntityDamageEvent event)	{
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}

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
			if (event.getCause() == DamageCause.PROJECTILE) {
				EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
				EntityDamageByEntityEvent eEvent = (EntityDamageByEntityEvent) damageEvent; 

				
				if(eEvent.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) eEvent.getDamager();

					if(arrow.getShooter() instanceof Player) {
						Player offender = (Player) arrow.getShooter();
						offender.sendMessage("Shooting");
						event.setCancelled(true);
					}
				}
			}
			
			if(event.getCause() == DamageCause.ENTITY_ATTACK) {
				
				EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
				EntityDamageByEntityEvent eEvent = (EntityDamageByEntityEvent) damageEvent; 

				if(eEvent.getDamager() instanceof Player) {
					Player offender = (Player) eEvent.getDamager();
					offender.sendMessage(ChatColor.YELLOW + "This is a not a pvp zone!");
					event.setCancelled(true);
				}

			}
			return;
		}
	}
}
