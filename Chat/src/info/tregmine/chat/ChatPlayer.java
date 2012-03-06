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


//		if ("einand".matches(tregminePlayer.getName())){

			if ( !this.plugin.lasttime.containsKey(tregminePlayer.getId())) {
				this.plugin.lasttime.put(tregminePlayer.getId(), 0L);
			}

//			tregminePlayer.sendMessage(":-" + (this.plugin.lasttime.get(tregminePlayer.getId()) - System.currentTimeMillis()));

			if (this.plugin.lasttime.get(tregminePlayer.getId()) -System.currentTimeMillis() <= 600 ) {
//				tregminePlayer.sendMessage("SPAMBLOCK TEST *IGNORE IT*, its not yet tuned");
				tregminePlayer.kickPlayer("Please do no spam!");
			}

//		}

		//		if (event.getMessage().equals(this.plugin.lastline.get(sender.getName())) ) {

		//		}


		//		if (event.getMessage().equals(this.plugin.lastline.get(sender.getName())) ) {
		//			sender.getPlayer().sendMessage("No need to repeat yourself!"); 
		//			sender.getPlayer().kickPlayer("Don't spam");
		//			event.setCancelled(true);
		//			return;
		//		}

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
		this.plugin.lasttime.put(tregminePlayer.getId(), System.currentTimeMillis());
	}
}
