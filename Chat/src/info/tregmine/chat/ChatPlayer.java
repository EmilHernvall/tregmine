package info.tregmine.chat;

//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.TimeZone;

//import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;



public class ChatPlayer extends PlayerListener {
	private final Chat plugin;

	public ChatPlayer(Chat instance) {
		plugin = instance;
		plugin.getServer();
	}

	public void onPlayerChat(PlayerChatEvent event) {
		Player sender = event.getPlayer();
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(sender.getName());
		Player[] players = plugin.getServer().getOnlinePlayers();

		if (!this.plugin.channel.containsKey(sender.getName())) {
			this.plugin.channel.put(sender.getName(), "global".toUpperCase());
		}

		for (Player player : players) {
				ChatColor txtColor = ChatColor.WHITE;

				if(sender == player) {
					txtColor = ChatColor.GRAY;
				}
					if (!this.plugin.channel.containsKey(player.getName())) {
						this.plugin.channel.put(player.getName(), "global".toUpperCase());
					}
					
					if (this.plugin.channel.get(sender.getName()).toUpperCase().matches(this.plugin.channel.get(player.getName()).toUpperCase())) {
						String channel = this.plugin.channel.get(sender.getName()) + " ";
						if (this.plugin.channel.get(sender.getName()).matches("GLOBAL")) {
							channel = "";
						}
						player.sendMessage(channel+"<" + tregminePlayer.getChatName() + ChatColor.WHITE + "> " + txtColor + event.getMessage());
					}
		}

		plugin.log.info("["+ sender.getWorld().getName() +  "]["+ this.plugin.channel.get(sender.getName())  +"]<" + sender.getName() +  "> " + event.getMessage() );
		event.setCancelled(true);
	}
}
