package info.tregmine.invis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class InvisPlayer implements  Listener  {
	private final Invis plugin;

	public InvisPlayer(Invis instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		if (event.getPlayer().getName() != null) {
			if ("einand".contentEquals(event.getPlayer().getName())) {
				Player[] players = plugin.getServer().getOnlinePlayers();
				for (Player allplayer : players) {
					allplayer.hidePlayer(event.getPlayer());
				} 
			} else {
				if (this.plugin.getServer().getPlayer("einand") != null) {
					if (this.plugin.getServer().getPlayer("einand").isOnline()) {
						event.getPlayer().hidePlayer(this.plugin.getServer().getPlayer("einand"));
					}
				}
			}
		}

		
	}

	
}
