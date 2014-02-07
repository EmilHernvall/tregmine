package info.tregmine.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.*;
import org.bukkit.util.Vector;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.events.PlayerMoveBlockEvent;

public class WorldPortalListener implements Listener{
    private Tregmine plugin;

    public WorldPortalListener(Tregmine instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void anarchyPortal(PlayerMoveBlockEvent event)
    {
        // Simply add another line changing frame, under, world and name to add a new portal! (Similar to end portal)
        handlePortal(event.getPlayer(), Material.OBSIDIAN, Material.EMERALD_BLOCK, plugin.getRulelessWorld(), "anarchy");
    }
    
    @EventHandler
    public void handlePortal(TregminePlayer player, Material underType, Material frame, World newWorld, String worldName)
    {
        Block under = player.getLocation().subtract(new Vector(0, 1, 0)).getBlock();
        Block in = player.getLocation().getBlock();
        
        if (under.getType() != underType || !in.isLiquid()) {
            return;
        }
        
        if (    !portalCheck(player, -1, 3, -1, 3, frame) &&
                !portalCheck(player, -1, 3, -2, 2, frame) &&
                !portalCheck(player, -1, 3, -3, 1, frame) &&
                !portalCheck(player, -2, 2, -1, 3, frame) &&
                !portalCheck(player, -2, 2, -2, 2, frame) &&
                !portalCheck(player, -2, 2, -3, 1, frame) &&
                !portalCheck(player, -3, 1, -1, 3, frame) &&
                !portalCheck(player, -3, 1, -2, 2, frame) &&
                !portalCheck(player, -3, 1, -3, 1, frame)) {
            return;
        }
        
        if (player.getWorld().getName().equalsIgnoreCase(newWorld.getName())) {
            player.teleportWithHorse(plugin.getServer().getWorld("world").getSpawnLocation());
            player.sendMessage(ChatColor.GOLD + "[PORTAL] " + ChatColor.GREEN + "Teleporting to main world!");
        } else {
            player.teleportWithHorse(newWorld.getSpawnLocation());
            player.sendMessage(ChatColor.GOLD + "[PORTAL] " + ChatColor.GREEN + "Teleporting to " + worldName + " world!");
        }
    }
    
    private boolean portalCheck(TregminePlayer player, int x1, int x2, int z1, int z2, Material portal)
    {
        Location location = player.getLocation();
        if (    location.add(x1, 0, 0).getBlock().getType() == portal && 
                location.add(0, 0, z1).getBlock().getType() == portal &&
                location.add(x2, 0, 0).getBlock().getType() == portal && 
                location.add(0, 0, z2).getBlock().getType() == portal) { 
            return true;
        } else {
            return false;
        }
    }
}
