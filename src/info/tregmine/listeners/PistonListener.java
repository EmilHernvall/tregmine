package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Lot;
import info.tregmine.zones.ZoneWorld;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener implements Listener{
    private Tregmine plugin;

    public PistonListener(Tregmine instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent event){
        ZoneWorld world = plugin.getWorld(event.getBlock().getWorld());
        Location loc = event.getBlock().getLocation();
        Lot lot = world.findLot(new Point(loc.getBlockX(), loc.getBlockZ()));
        if (lot == null) {
            return;
        }
        Set<Integer> owner = lot.getOwners();
        
        for(Block b : event.getBlocks()){
            for(Integer i : owner){
                TregminePlayer p = plugin.getPlayerOffline(i);
                if(!p.hasBlockPermission(b.getLocation(), false)){
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void pistonRetract(BlockPistonRetractEvent event){
        ZoneWorld world = plugin.getWorld(event.getBlock().getWorld());
        Location loc = event.getBlock().getLocation();
        Lot lot = world.findLot(new Point(loc.getBlockX(), loc.getBlockZ()));
        if (lot == null) {
            return;
        }
        Set<Integer> owner = lot.getOwners();
        
        for(Integer i : owner){
            TregminePlayer p = plugin.getPlayerOffline(i);
            if(!p.hasBlockPermission(event.getRetractLocation(), false)){
                event.setCancelled(true);
            }
        }
    }
}
