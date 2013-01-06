package info.tregmine.chat;

import info.tregmine.Tregmine;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

	private final Tregmine plugin;

	public Chat(Tregmine instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		info.tregmine.api.TregminePlayer sender = this.plugin.getPlayer(event.getPlayer());
		Player[] players = plugin.getServer().getOnlinePlayers();		


		for (Player player : players) {
			ChatColor txtColor = ChatColor.WHITE;
			info.tregmine.api.TregminePlayer to = this.plugin.getPlayer(player);


			if(sender.equals(to)) {
				txtColor = ChatColor.GRAY;
			}

			String channel = sender.getChatChannel();
			
/*
			if (sender.getChatChannel().equals(to.getChatChannel())) {
				
				String channel = sender.getChatChannel();
				
				if (sender.getChatChannel().matches("GLOBAL")) {
					channel = "";
				}
*/
				player.sendMessage(channel+"<" + sender.getChatName() + ChatColor.WHITE + "> " + txtColor + event.getMessage());
//			}


		}
		event.setCancelled(true);
	}
}