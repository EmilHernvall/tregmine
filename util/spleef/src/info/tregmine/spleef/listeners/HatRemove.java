package info.tregmine.spleef.listeners;

import info.tregmine.spleef.ArenaManager;
import info.tregmine.spleef.Spleef;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HatRemove implements Listener {

    private final Spleef plugin;

    public HatRemove(Spleef instance){
        this.plugin = instance;
    }
    
	@EventHandler
	public void onPlayerClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		
		if (ArenaManager.getInstance().getArena(p) != null) e.setCancelled(true);
	}
}