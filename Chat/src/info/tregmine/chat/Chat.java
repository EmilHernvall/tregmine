package info.tregmine.chat;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Chat extends JavaPlugin {

	public final ChatPlayer chatplayer = new ChatPlayer(this);
	public Tregmine tregmine = null;
	public final Logger log = Logger.getLogger("Minecraft");
	public HashMap<String, String> channel = new HashMap<String, String>();


	public void onDisable() {
	}

	public void onEnable() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}

		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, chatplayer, Priority.Highest, this);
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player from = null;

		if(!(sender instanceof Player)) {
			if(commandName.equals("say")) {
				StringBuffer buf = new StringBuffer();
				buf.append(args[0]);
				for (int i = 1; i < args.length; ++i) {
					buf.append(" " + args[i]);
				}
				String buffMsg = buf.toString();
				this.getServer().broadcastMessage("<" + ChatColor.BLUE + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
				this.log.info("CONSOLE: <GOD> " + buffMsg);
				return true;
			}
			return false;
		} else {
			from = (Player) sender;
		}

		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(from.getName());

		boolean isAdmin = tregminePlayer.isAdmin();

		if(commandName.equals("say") && isAdmin) {
			StringBuffer buf = new StringBuffer();
			buf.append(args[0]);
			for (int i = 1; i < args.length; ++i) {
				buf.append(" " + args[i]);
			}
			String buffMsg = buf.toString();
			this.getServer().broadcastMessage("<" + ChatColor.RED + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			this.log.info(from.getName() + ": <GOD> " + buffMsg);
			
			Player[] players =  this.getServer().getOnlinePlayers();

			for (Player player : players) {
				info.tregmine.api.TregminePlayer locTregminePlayer = this.tregmine.tregminePlayer.get(player.getName());
				if (locTregminePlayer.isAdmin()) {
					player.sendMessage(ChatColor.DARK_AQUA + "God used by: " + tregminePlayer.getChatName());
				}
			}
			return true;
		}

		if(commandName.equals("force")  && args.length == 2 ){
			this.channel.put(from.getName(), args[1].toUpperCase());
			Player to = getServer().matchPlayer(args[0]).get(0);
			this.channel.put(to.getName(), args[1].toUpperCase());
			to.sendMessage(ChatColor.RED + from.getName() + " forced you into chatchannel " + args[1].toUpperCase() );
			to.sendMessage(ChatColor.RED + "say /channel global to switch back to the global chat " );
			from.sendMessage(ChatColor.RED + "You are now in a forced chat " + args[1].toUpperCase()+ " with " + to.getName() );
			this.log.info(from.getName() + " FORCED CHAT WITH " + to.getName() + " IN CHANNEL " + args[1].toUpperCase());
			return true;
		}

		if(commandName.equals("channel")  && args.length == 1 ){
			this.channel.put(from.getName(), args[0].toUpperCase());
			from.sendMessage(ChatColor.RED + "You are now talking in channel " + args[0] );
			from.sendMessage(ChatColor.RED + "say /channel global to switch to the global chat " );
		}


		if(commandName.equals("me")  && args.length > 0 ){
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < args.length; ++i) {
				buf.append(" " + args[i]);
			}
			getServer().broadcastMessage("* " + tregminePlayer.getChatName() + ChatColor.WHITE + buf.toString());
			this.log.info("* " + from.getName() + buf.toString());
			return true;
		}

		if(commandName.equals("facepalm")) {
			from.sendMessage(tregminePlayer.getChatName() + ChatColor.YELLOW + " * facepalm *");
			return true;
		}


		if(commandName.equals("msg") || commandName.equals("m") || commandName.equals("tell"))
		{			
			Player to = getServer().getPlayer(args[0]);

			if (to != null) {
				info.tregmine.api.TregminePlayer toPlayer = this.tregmine.tregminePlayer.get(to.getName());

				StringBuffer buf = new StringBuffer();
				for (int i = 1; i < args.length; ++i) {
					buf.append(" " + args[i]);
				}
				String buffMsg = buf.toString();
				if (!toPlayer.getMetaBoolean("invis")) {
					from.sendMessage(ChatColor.GREEN + "(to) " + toPlayer.getChatName() + ChatColor.GREEN + ": "  + buffMsg);
				}
				to.sendMessage(ChatColor.GREEN + "(msg) " + tregminePlayer.getChatName() + ChatColor.GREEN + ": " + buffMsg);
				log.info(from.getName() + " => " + to.getName() + buffMsg);
				return true;
			}

		}
		return false;
	}
	public void onLoad() {
	}

}
