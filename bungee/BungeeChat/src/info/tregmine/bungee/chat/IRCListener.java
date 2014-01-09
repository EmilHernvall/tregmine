package info.tregmine.bungee.chat;

import java.util.Collection;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class IRCListener extends ListenerAdapter implements Listener {
	private TregBungee plugin;

	IRCListener(TregBungee aThis) {
		this.plugin = aThis;
	}

	@Override
	public void onMessage(MessageEvent event) throws Exception {
		if (plugin.getConfig().ircChat){
			if (event.getMessage().startsWith("hello?") || event.getMessage().startsWith("Hello?")) {
				event.respond("Hey!");
			}else if (event.getMessage().equalsIgnoreCase("ping")) {
				event.respond("Pong!");
			}

			if (!event.getMessage().startsWith(".")) {
				for (ProxiedPlayer pl : this.plugin.getProxy().getPlayers()){
					if (pl instanceof ProxiedPlayer){
						pl.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "IRC" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + event.getUser().getNick() + ": " + ChatColor.WHITE + event.getMessage());
					}
				}
			}
			else if (event.getMessage().equalsIgnoreCase(".online") || event.getMessage().equalsIgnoreCase(".players")) {
				event.respond("There are " + plugin.getProxy().getOnlineCount() + " players online.");
				event.respond("They are: " + plugin.getProxy().getPlayers().toString());

			}
			else if (event.getMessage().equalsIgnoreCase(".version")){
				event.respond("Tregmine is currently running minecraft " + plugin.getProxy().getGameVersion());
			}
			else if (event.getMessage().equalsIgnoreCase(".info")){
				event.respond("Tregmine is currently running minecraft " + plugin.getProxy().getGameVersion());
				event.respond("There are " + plugin.getProxy().getOnlineCount() + " players online.");
				event.respond("They are: " + plugin.getProxy().getPlayers().toString());
			}
		}
	}
}