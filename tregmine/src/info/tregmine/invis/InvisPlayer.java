package info.tregmine.invis;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class InvisPlayer implements  Listener  {
	private final Tregmine plugin;

	public InvisPlayer(Tregmine instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		if (event.getPlayer().getName() != null) {
			
			TregminePlayer player = plugin.getPlayer(event.getPlayer());

			if (player.getMetaBoolean("invis")) {
				Player[] players = plugin.getServer().getOnlinePlayers();
				for (Player allplayer : players) {
					allplayer.hidePlayer(event.getPlayer());
				}				
			} else {
				Player[] players = plugin.getServer().getOnlinePlayers();
				for (Player allplayer : players) {
					allplayer.showPlayer(event.getPlayer());
				}
			}


			Player[] players = plugin.getServer().getOnlinePlayers();

			for (Player allplayer : players) {
				TregminePlayer aplayer = this.plugin.getPlayer(allplayer);
				if (aplayer.getMetaBoolean("invis")) {
					player.hidePlayer(allplayer);
				} else {
					player.showPlayer(allplayer);
				}

			}				
		}


	}
}