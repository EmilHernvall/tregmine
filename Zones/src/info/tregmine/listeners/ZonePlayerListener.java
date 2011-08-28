package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.ZonesPlugin;

import org.bukkit.ChatColor;
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
	
	private void movePlayerBack(TregminePlayer player, Location movingFrom, Location movingTo)
	{
		Vector a = new Vector(movingFrom.getX(), movingFrom.getY(), movingFrom.getZ());
		Vector b = new Vector(movingTo.getX(), movingTo.getY(), movingTo.getZ());
		
		Vector diff = b.subtract(a);
		diff = diff.multiply(-5);
		
		Vector newPosVector = a.add(diff);
		Location newPos = new Location(player.getWorld(), newPosVector.getX(), newPosVector.getY(), newPosVector.getZ());
		player.teleport(newPos);
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
				player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + currentZone.getTextExit());
			}
			
			currentZone = plugin.zonesLookup.find(currentPos);
			if (currentZone != null) {
				Zone.Permission perm = currentZone.getUser(player.getName());
				
				// if anyone is allowed to enter by default...
				if (currentZone.getEnterDefault()) {
					// ...we only need to reject banned players
			    	if (perm != null && perm == Zone.Permission.Banned) {
			    		bannedMessage(currentZone, player);
						movePlayerBack(player, movingFrom, movingTo);
						return;
					}
				}
				// if this is a whitelist zone...
				else {
					// ...reject people not in the user list, as well as banned people
			    	if (perm == null) {
			    		disallowedMessage(currentZone, player);
						movePlayerBack(player, movingFrom, movingTo);
						return;
					}
			    	else if (perm == Zone.Permission.Banned) {
			    		bannedMessage(currentZone, player);
						movePlayerBack(player, movingFrom, movingTo);
						return;			    		
			    	}
				}
				
				welcomeMessage(currentZone, player, perm);
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
				
				if (currentZone.getEnterDefault()) {
			    	if (perm != null && perm == Zone.Permission.Banned) {
			    		bannedMessage(currentZone, player);
						event.setCancelled(true);
						return;
					}
				} else {
			    	if (perm == null) {
			    		disallowedMessage(currentZone, player);
						event.setCancelled(true);
						return;
					}
			    	else if (perm == Zone.Permission.Banned) {
						bannedMessage(currentZone, player);
						event.setCancelled(true);
						return;			    		
			    	}
				}
				
				welcomeMessage(currentZone, player, perm);
			}
			player.setCurrentZone(currentZone);
		}
	}
	
	private void disallowedMessage(Zone currentZone, TregminePlayer player)
	{
		player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
				"You are not allowed in this zone. Contact the zone owner.");
	}
	
	private void bannedMessage(Zone currentZone, TregminePlayer player)
	{
		player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
				"You are banned from " + currentZone.getName() + ".");
	}
	
	private void welcomeMessage(Zone currentZone, TregminePlayer player, Zone.Permission perm)
	{
		player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
				currentZone.getTextEnter());
		if (currentZone.isPvp()) {
			player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
					"Warning! This is a PVP zone! Other players can damage or kill you here.");
		}
		if (perm != null) {
			String permNotification = perm.getPermissionNotification();
			player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
					permNotification);
		}
	}
}
