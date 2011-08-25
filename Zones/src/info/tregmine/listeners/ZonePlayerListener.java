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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

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
				Zone.Permission perm = currentZone.getUser(player.getName());
		    	if (perm != null && perm == Zone.Permission.Banned) {
					player.sendMessage("You are banned from " + currentZone.getName() + ".");
					
					Vector a = new Vector(movingFrom.getX(), movingFrom.getY(), movingFrom.getZ());
					Vector b = new Vector(movingTo.getX(), movingTo.getY(), movingTo.getZ());
					
					Vector diff = b.subtract(a);
					diff = diff.multiply(-5);
					
					Vector newPosVector = a.add(diff);
					Location newPos = new Location(player.getWorld(), newPosVector.getX(), newPosVector.getY(), newPosVector.getZ());
					player.teleport(newPos);
					
					return;
				}
				
				player.sendMessage(currentZone.getTextEnter());
			}
			player.setCurrentZone(currentZone);
		}
	}
	
	@Override
	public void onPlayerTeleport(PlayerTeleportEvent event)
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
				Zone.Permission perm = currentZone.getUser(player.getName());
		    	if (perm != null && perm == Zone.Permission.Banned) {
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
