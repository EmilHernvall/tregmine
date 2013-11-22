package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import org.bukkit.Location;
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

        Location location = event.getRightClicked().getLocation();
        
        if (!player.hasBlockPermission(location, true)) {
            event.setCancelled(true);
        }
    }
    
}
