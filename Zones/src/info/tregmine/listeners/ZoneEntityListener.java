package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.ZonesPlugin;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class ZoneEntityListener extends EntityListener 
{
	private final ZonesPlugin plugin;
	private final Tregmine tregmine;

	public ZoneEntityListener(ZonesPlugin instance) 
	{
		this.plugin = instance;
		this.tregmine = instance.tregmine;
	}

	/*public void onCreatureSpawn(CreatureSpawnEvent event) 
	{
		event.setCancelled(true);			
	}*/
	
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
    	TregminePlayer player = tregmine.getPlayer((Player)event.getEntity());
    	Location location = player.getLocation();
    	Point pos = new Point(location.getBlockX(), location.getBlockZ());
    	
    	Zone currentZone = player.getCurrentZone();
    	if (currentZone == null || !currentZone.contains(pos)) {
    		currentZone = plugin.zonesLookup.find(pos);
    		player.setCurrentZone(currentZone);
    	}
    	
    	if (currentZone == null || !currentZone.isPvp()) {
    		event.setCancelled(true);
    		return;
    	}
	}
}
