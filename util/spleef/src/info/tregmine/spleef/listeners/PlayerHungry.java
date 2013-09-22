package info.tregmine.spleef.listeners;

import info.tregmine.spleef.ArenaManager;
import info.tregmine.spleef.Spleef;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungry implements Listener {

    private final Spleef plugin;

    public PlayerHungry(Spleef instance){
        this.plugin = instance;
    }
    
	@EventHandler
	public void onPlayerHungry(FoodLevelChangeEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		if (ArenaManager.getInstance().getArena(((Player) e.getEntity())) != null) e.setCancelled(true);
	}
}