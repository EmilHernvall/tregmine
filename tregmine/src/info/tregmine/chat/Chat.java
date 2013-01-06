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

		String channel = sender.getChatChannel();
		ChatColor txtColor = ChatColor.WHITE;

		for (Player player : players) {
			info.tregmine.api.TregminePlayer to = this.plugin.getPlayer(player);


			if(sender.equals(to)) {
				txtColor = ChatColor.GRAY;
			} else {
				txtColor = ChatColor.WHITE;
			}

			
			if (sender.getChatChannel().equals(to.getChatChannel())) {
				
				
				if (sender.getChatChannel().matches("GLOBAL")) {
					channel = "";
				}
				
				player.sendMessage(channel+"<" + sender.getChatName() + ChatColor.WHITE + "> " + txtColor + event.getMessage());
			}
		}
		this.plugin.log.info(channel+"<" + sender.getName() + "> " + event.getMessage());
		event.setCancelled(true);
	}
}