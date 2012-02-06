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

		if (event.getMessage().equals(this.plugin.lastline.get(sender.getName())) ) {
			sender.getPlayer().sendMessage("No need to repeat yourself!"); 
			event.setCancelled(true);
			return;
		}
		
		this.plugin.lastline.put(sender.getName(), event.getMessage());
		
		
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

			if (this.plugin.channel.get(sender.getName()).toUpperCase().equals(this.plugin.channel.get(player.getName()).toUpperCase())) {
				String channel = this.plugin.channel.get(sender.getName()) + " ";
				if (this.plugin.channel.get(sender.getName()).matches("GLOBAL")) {
					channel = "";
				}
				if (event.getMessage().length() > 53) {
					int nameLenght = event.getPlayer().getName().length()+5 + channel.length();
					String firstMessage = event.getMessage().substring(0, 54 - nameLenght);
					String spaces = "";
					int firstInt = firstMessage.lastIndexOf(" ");
					firstMessage = event.getMessage().substring(0, firstInt);
					String lastMessage = event.getMessage().substring (firstInt+1);

					for (int i=0; i<nameLenght; i++) {
						spaces = spaces + " ";
					}

					firstMessage = ChatColor.stripColor(firstMessage);
					lastMessage = ChatColor.stripColor(lastMessage);
					
					player.sendMessage(channel+"<" + tregminePlayer.getChatName() + ChatColor.WHITE + "> "+ txtColor + firstMessage );
					player.sendMessage(txtColor + spaces + lastMessage);
				} else {
					player.sendMessage(channel+"<" + tregminePlayer.getChatName() + ChatColor.WHITE + "> " + txtColor + event.getMessage().replace("  ", ""));
				}
			}
		}

		plugin.log.info("["+ sender.getWorld().getName() +  "]["+ this.plugin.channel.get(sender.getName())  +"]<" + sender.getName() +  "> " + event.getMessage() );
		event.setCancelled(true);
	}
}
