package info.tregmine.listeners;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Arrow;
//import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.event.entity.EntityShootBowEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Zone;
import info.tregmine.zones.Lot;
import info.tregmine.zones.ZoneWorld;

public class ZoneEntityListener implements Listener
{
    private static final Set<EntityType> allowedMobs = EnumSet.of(
            EntityType.CHICKEN, EntityType.COW, EntityType.PIG,
            EntityType.SHEEP, EntityType.SQUID, EntityType.WOLF,
            EntityType.IRON_GOLEM, EntityType.SNOWMAN, EntityType.BAT,
            EntityType.VILLAGER, EntityType.MUSHROOM_COW, EntityType.OCELOT,
            EntityType.HORSE);

    private Tregmine plugin;

    public ZoneEntityListener(Tregmine instance)
    {
        this.plugin = instance;
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

        if (!allowedMobs.contains(event.getEntityType()) &&
            event.getSpawnReason() == SpawnReason.NATURAL) {

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getEntity().getWorld().getName().matches("world_the_end")) {
            return;
        }
        
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent)event;
        if (!(edbeEvent.getDamager() instanceof Player)) {
            return;
        }

        Entity entity = event.getEntity();

        ZoneWorld world = plugin.getWorld(entity.getWorld());

        TregminePlayer player =
                plugin.getPlayer((Player) event.getEntity());

        Location location = player.getLocation();
        Point pos = new Point(location.getBlockX(), location.getBlockZ());

        Zone currentZone = player.updateCurrentZone();
        if (currentZone == null) {
			if (	event.getEntity().getWorld().getName().equalsIgnoreCase(plugin.getRulelessWorld().getName()) ||
					event.getEntity().getWorld().getName().equalsIgnoreCase(plugin.getRulelessEnd().getName()) ||
					event.getEntity().getWorld().getName().equalsIgnoreCase(plugin.getRulelessNether().getName())) {
				return;
			}
            
            event.setCancelled(true);
            return;
        }

        if (currentZone == null || !currentZone.isPvp()) {
            event.setCancelled(true);
        }
        else {
            event.setCancelled(false);
            return;
        }
        
        Lot currentLot = world.findLot(pos);
        if (currentLot == null) {
            event.setCancelled(true);
            return;
        }

        if (currentLot.hasFlag(Lot.Flags.PVP)) {
            event.setCancelled(false);
            return;
        }
        else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e)
    {
        Entity e1 = e.getEntity();
        Entity d1 = e.getDamager();

        if (e1 instanceof Player && d1 instanceof Arrow) {
            if (((Arrow)d1).getShooter() instanceof Player) {

                ZoneWorld world = plugin.getWorld(e1.getWorld());

                TregminePlayer player =
                        plugin.getPlayer((Player) e1);

				player.setCombatLog(10);
				player.sendMessage(ChatColor.RED + "You have been combat logged! Do NOT log out! 10 seconds remaining...");

                Location location = player.getLocation();
                Point pos = new Point(location.getBlockX(), location.getBlockZ());

                Zone currentZone = player.updateCurrentZone();
                if (currentZone == null) {
					if (	e.getEntity().getWorld().getName().equalsIgnoreCase(plugin.getRulelessWorld().getName()) ||
							e.getEntity().getWorld().getName().equalsIgnoreCase(plugin.getRulelessEnd().getName()) ||
							e.getEntity().getWorld().getName().equalsIgnoreCase(plugin.getRulelessNether().getName())) {
						return;
					}
                    e.setCancelled(true);
                    return;
                }

                Lot currentLot = world.findLot(pos);
                if (currentLot == null) {
                    e.setCancelled(true);
                    return;
                }

                if (currentLot.hasFlag(Lot.Flags.PVP)) {
                    e.setCancelled(false);
                }
                else {
                    e.setCancelled(true);
                }
            }
        }
    }
}

