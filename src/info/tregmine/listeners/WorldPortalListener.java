package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import info.tregmine.events.TregminePortalEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class WorldPortalListener implements Listener{
    private Tregmine plugin;
    public WorldPortalListener(Tregmine instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void portalHandler(PlayerMoveEvent event)
    {
        final TregminePlayer player = plugin.getPlayer(event.getPlayer());
        Block under = player.getLocation().subtract(0, 1, 0).getBlock();
        Block in = event.getTo().getBlock();
        
        // Simply add another line changing frame, under, world and name to add a new portal! (Similar to end portal)
        handlePortal(player, Material.OBSIDIAN, Material.EMERALD_BLOCK, plugin.getRulelessWorld(), "anarchy", in, under);
        handlePortal(player, Material.OBSIDIAN, Material.NETHERRACK, plugin.getRulelessNether(), "anarchy nether", in, under);
        handlePortal(player, Material.OBSIDIAN, Material.ENDER_STONE, plugin.getRulelessEnd(), "anarchy end", in, under);
    }

    public void handlePortal(TregminePlayer player, Material underType, Material frame, World newWorld, String worldName, Block in, Block under)
    {
        if (under.getType() != underType || !in.isLiquid()) {
            return;
        }

        if (  !(frameCheck(player, -1, 3, -1, 3, frame) ||
                frameCheck(player, -1, 3, -2, 2, frame) ||
                frameCheck(player, -1, 3, -3, 1, frame) ||
                frameCheck(player, -2, 2, -1, 3, frame) ||
                frameCheck(player, -2, 2, -2, 2, frame) ||
                frameCheck(player, -2, 2, -3, 1, frame) ||
                frameCheck(player, -3, 1, -1, 3, frame) ||
                frameCheck(player, -3, 1, -2, 2, frame) ||
                frameCheck(player, -3, 1, -3, 1, frame))) {
            return;
        }

        if (player.getWorld().getName().equalsIgnoreCase(newWorld.getName())) {
			plugin.getServer().getPluginManager().callEvent(new TregminePortalEvent(player.getWorld(), plugin.getServer().getWorld("world"), player));
            player.teleportWithHorse(plugin.getServer().getWorld("world").getSpawnLocation());
            player.sendMessage(ChatColor.GOLD + "[PORTAL] " + ChatColor.GREEN + "Teleporting to main world!");
        } else {
			plugin.getServer().getPluginManager().callEvent(new TregminePortalEvent(player.getWorld(), newWorld, player));
            player.teleportWithHorse(newWorld.getSpawnLocation());
            player.sendMessage(ChatColor.GOLD + "[PORTAL] " + ChatColor.GREEN + "Teleporting to " + worldName + " world!");
        }
        player.setFireTicks(0);
    }
    
    public boolean frameCheck(TregminePlayer p, int x1, int x2, int z1, int z2, Material portal)
    {
        if(     p.getLocation().add(x1, 0, 0).getBlock().getType().equals(portal) && 
                p.getLocation().add(0, 0, z1).getBlock().getType().equals(portal) && 
                p.getLocation().add(x2, 0, 0).getBlock().getType().equals(portal) && 
                p.getLocation().add(0, 0, z2).getBlock().getType().equals(portal)) {
            return true;
        } else {
            return false;
        }
    }
}
