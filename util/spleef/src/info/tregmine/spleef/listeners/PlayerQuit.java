package info.tregmine.spleef.listeners;

import info.tregmine.spleef.Arena;
import info.tregmine.spleef.ArenaManager;
import info.tregmine.spleef.Spleef;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final Spleef plugin;

    public PlayerQuit(Spleef instance){
        this.plugin = instance;
    }
    
	@EventHandler
	public void onPlayerExitGame(PlayerQuitEvent e) {
		Arena a = ArenaManager.getInstance().getArena(e.getPlayer());
		if (a == null) return;
		a.removePlayer(e.getPlayer(), false);
	}
}
