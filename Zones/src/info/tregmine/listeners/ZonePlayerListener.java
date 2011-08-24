package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.ZonesPlugin;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ZonePlayerListener extends PlayerListener 
{
	private final ZonesPlugin plugin;
	private final Tregmine tregmine;

	public ZonePlayerListener(ZonesPlugin instance) 
	{
		this.plugin = instance;
		this.tregmine = instance.tregmine;
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) 
	{
		//TregminePlayer player = tregmine.getPlayer(event.getPlayer());
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event)
	{
		TregminePlayer player = tregmine.getPlayer(event.getPlayer());
		
		Location movingFrom = event.getFrom();
		Point oldPos = new Point(movingFrom.getBlockX(), movingFrom.getBlockZ());
		
		Location movingTo = event.getTo();
		Point currentPos = new Point(movingTo.getBlockX(), movingTo.getBlockZ());
		
		Zone currentZone = player.getCurrentZone();
		if (currentZone == null || !currentZone.contains(currentPos)) {
			
			if (currentZone != null && currentZone.contains(oldPos)) {
				player.sendMessage(currentZone.getTextExit());
			}
			
			currentZone = plugin.zonesLookup.find(currentPos);
			if (currentZone != null) {
				if (currentZone.isBanned(player.getName())) {
					player.sendMessage("You are banned from " + currentZone.getName() + ".");
					event.setCancelled(true);
					return;
				}
				
				player.sendMessage(currentZone.getTextEnter());
			}
			player.setCurrentZone(currentZone);
		}
	}
}
