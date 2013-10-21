package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ItemFrameListener implements Listener
{
    private Tregmine plugin;
    
    public ItemFrameListener(Tregmine instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void onRotate(PlayerInteractEntityEvent event)
    {
        if (!(event.getRightClicked().getType().equals(EntityType.ITEM_FRAME))) { // Before we do anything lets check were interacting with ItemFrames
            return;
        }
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if(player.getRank().canModifyZones()){ // Lets people with "canModifyZones"
            return;
        }
        
        ZoneWorld world = plugin.getWorld(player.getWorld());
        
        Entity entity = event.getRightClicked();
        Location location = entity.getLocation();
        Point pos = new Point(location.getBlockX(), location.getBlockZ());
        
        Zone zone = world.findZone(pos);
        Zone.Permission perm = zone.getUser(player);
        Lot lot = world.findLot(pos);
        
        if (perm != null && perm == Zone.Permission.Banned) { // Eliminate banned from zone
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "["
                    + zone.getName() + "] "
                    + "You are banned from " + zone.getName()
                    + ".");
        }
        
        if(zone != null && lot == null){ // If Zone but not Lot
            if(perm == Zone.Permission.Allowed ||
               perm == Zone.Permission.Maker ||
               perm == Zone.Permission.Owner){ // Checks if player is Allowed/Maker/Owner
                return;
            }
            if(zone.getDestroyDefault()){ // If people can destroy, Then we will let them rotate.
                return;
            }
            // If still at this point then they are not allowed to rotate item frames so BURN THEM ALIVE!!
            player.setFireTicks(50);
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "["
                    + zone.getName() + "] "
                    + "You are not allowed to rotate item frames in "
                    + zone.getName() + ".");
            return;
        }else if(zone != null && lot != null){ // If lot in a zone
            if (perm == Zone.Permission.Owner && zone.isCommunist()) { // Zone owners can modify lots in communist zones
                return;
            }
            if (lot.isOwner(player)) { // Lot owners can always modify lots
                return;
            }
            // If still at this point then they are not allowed to rotate item frames so BURN THEM ALIVE!!
            player.setFireTicks(50);
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "["
                    + zone.getName() + "] "
                    + "You are not allowed to rotate item frames in "
                    + zone.getName() + ".");
            return;
        }else{ // No Zone (obviously No Lot) so just end.
            return;
        }
    }
    
}
