package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.ZonesPlugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ZoneBlockListener extends BlockListener 
{
    private final ZonesPlugin plugin;
    private final Tregmine tregmine;
	
    public ZoneBlockListener(ZonesPlugin instance) 
    {
        this.plugin = instance;
        this.tregmine = instance.tregmine;
    }

    public void onBlockBreak (BlockBreakEvent event) 
    {
    	TregminePlayer player = tregmine.getPlayer(event.getPlayer());
    	Block block = event.getBlock();
    	Location location = block.getLocation();
    	Point pos = new Point(location.getBlockX(), location.getBlockZ());
    	
    	Zone currentZone = player.getCurrentZone();
    	if (currentZone != null && !currentZone.contains(pos)) {
    		currentZone = plugin.zonesLookup.find(pos);
    		player.setCurrentZone(currentZone);
    	}
    	
    	if (currentZone != null && !currentZone.isBuilder(player.getName())) {
    		player.setFireTicks(50);
    		event.setCancelled(true);
    		player.sendMessage("You are not allowed to break blocks in " + currentZone.getName() + ".");
    	}
    }
    
	public void onBlockPlace (BlockPlaceEvent event)
	{
    	TregminePlayer player = tregmine.getPlayer(event.getPlayer());
    	Block block = event.getBlock();
    	Location location = block.getLocation();
    	Point pos = new Point(location.getBlockX(), location.getBlockZ());
    	
    	Zone currentZone = player.getCurrentZone();
    	if (currentZone != null && !currentZone.contains(pos)) {
    		currentZone = plugin.zonesLookup.find(pos);
    		player.setCurrentZone(currentZone);
    	}
    	
    	if (currentZone != null && !currentZone.isBuilder(player.getName())) {
    		player.setFireTicks(50);
    		block.setType(Material.AIR);
    		event.setCancelled(true);
    		player.sendMessage("You are not allowed to place blocks in " + currentZone.getName() + ".");
    	}
	}
}
