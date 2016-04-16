package info.tregmine.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class AfkListener  implements Listener{
	private Tregmine t;
    private Map<Item, TregminePlayer> droppedItems;

    public AfkListener(Tregmine instance)
    {
        this.t = instance;

        droppedItems = new HashMap<Item, TregminePlayer>();
    }
    
    void updateActivity(TregminePlayer player){
    	final long currentTime = System.currentTimeMillis();
    	player.setLastOnlineActivity(currentTime);
    }
    
    TregminePlayer matchPlayer(Player player){
    	return t.getPlayer(player);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
    	t.getPlayer(e.getPlayer()).setLastOnlineActivity(System.currentTimeMillis());
    }
    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onPlayerItemChange(PlayerItemHeldEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onPlayerGameMode(PlayerGameModeChangeEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
    	if ((e.getDamager() instanceof Player)){
            TregminePlayer player = t.getPlayer((Player) e.getDamager());
            updateActivity(player);
    	}
    	if ((e.getEntity() instanceof Player)){
        TregminePlayer player = t.getPlayer((Player) e.getEntity());
        if(player.isAfk()){
        	e.setCancelled(true);
        }
    	}
    }
    @EventHandler
    public void onPlayerBook(PlayerEditBookEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e){
    	TregminePlayer a = matchPlayer(e.getPlayer());
    	if(a.isAfk()){
    		e.setCancelled(true);
    	}
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
    @EventHandler
    public void playerToggleSneak(PlayerToggleSneakEvent e){
    	updateActivity(matchPlayer(e.getPlayer()));
    }
}
