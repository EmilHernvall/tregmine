package info.tregmine.chat;

//import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatPlayer implements Listener {
	private final Chat plugin;

	public ChatPlayer(Chat instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player sender = event.getPlayer();
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(sender.getName());
		Player[] players = plugin.getServer().getOnlinePlayers();



		if ( !this.plugin.lasttime.containsKey(tregminePlayer.getId())) {
			this.plugin.lasttime.put(tregminePlayer.getId(), 0L);
		}

		if (this.plugin.lasttime.get(tregminePlayer.getId()) >= System.currentTimeMillis()) {
			if (tregminePlayer.isDonator()) {
				tregminePlayer.sendMessage("Please avoid spamming!");
			} else {
				tregminePlayer.kickPlayer("Please do no spam!");
				this.plugin.getServer().broadcastMessage("Spammer kicked : " + tregminePlayer.getChatName());
			}
		}

		this.plugin.lastline.put(sender.getName(), event.getMessage());

		
		if ("".matches(tregminePlayer.getMetaString("ChatChannel"))) {
			tregminePlayer.setMetaString("ChatChannel", "GLOBAL");
		}

//		if (!this.plugin.channel.containsKey(sender.getName())) {
//			this.plugin.channel.put(sender.getName(), "global".toUpperCase());
//		}

		for (Player player : players) {
			ChatColor txtColor = ChatColor.WHITE;
			info.tregmine.api.TregminePlayer to = this.plugin.tregmine.tregminePlayer.get(player);
			
			
			if(sender == player) {
				txtColor = ChatColor.GRAY;
			}
			if ("".matches(tregminePlayer.getMetaString("ChatChannel"))) {
				tregminePlayer.setMetaString("ChatChannel", "GLOBAL");
			}
			
			if (tregminePlayer.getMetaString("ChatChannel").equals(to.getMetaString("ChatChannel").toUpperCase())) {
				String channel = tregminePlayer.getMetaString("ChatChannel") + " ";
				if (tregminePlayer.getMetaString("ChatChannel").matches("GLOBAL")) {
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


		plugin.log.info("["+ sender.getWorld().getName() +  "]["+ tregminePlayer.getMetaString("ChatChannel")  +"]<" + sender.getName() +  "> " + event.getMessage() );
		event.setCancelled(true);
		this.plugin.lasttime.put(tregminePlayer.getId(), System.currentTimeMillis() + 1200);
	}
}
