package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof ItemFrame)) {
            return;
        }
        
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        TregminePlayer player = plugin.getPlayer((Player) event.getDamager());
        
        if (!player.hasBlockPermission(event.getEntity().getLocation(), true)) {
            event.setCancelled(true);
        }
    }
    
}
