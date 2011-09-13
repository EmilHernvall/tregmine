package info.tregmine.who;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 
//import com.tregdev.tregmine.TregmineBlock;
//import info.tregmine.currency.Wallet;


public class Who extends JavaPlugin {

	Who plugin = this;

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;

	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
	}

	public void onDisable(){
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player p = null;
		Player from = null;


		if(!(sender instanceof Player)) {
			ConsoleCommandSender con = (ConsoleCommandSender) sender;
			con.sendMessage("test");
			return false;
		} else {
			from = (Player) sender;
		}
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(from.getName());

		if(commandName.equals("who") || commandName.equals("playerlist") || commandName.equals("list")){

			int i = 0;
			int x = 0;
			int c = 0;
			String[] buff = new String[100];
			try {
				p = findPlayer(args[0]);
			}
			catch (Exception e) {
				p = null;
			}

			if (p == null) {
				from.sendMessage(ChatColor.DARK_AQUA + "********** PLAYERLIST **********");
				for (Player player : plugin.getServer().getOnlinePlayers()) {
					if(i>=4) {
						i=0;
						x++;
					}
					if (buff[x] == null ) {
						buff[x] = "";
					}
					info.tregmine.api.TregminePlayer locTregminePlayer = this.tregmine.tregminePlayer.get(player.getName());
					if (!locTregminePlayer.getMetaBoolean("invis")) {
						buff[x] = buff[x] + locTregminePlayer.getChatName() + ChatColor.WHITE + ", ";
						i++;
						c++;
					}
				}
				for (String line : buff) {
					if (line != null) {
						from.sendMessage(line);
					}
				}
				from.sendMessage(ChatColor.DARK_AQUA + "********** "+ ChatColor.DARK_PURPLE + c + ChatColor.DARK_AQUA +" players online **********");
				return true;
			} else {
				if (tregminePlayer.isAdmin()) {
					info.tregmine.currency.Wallet wallet = new info.tregmine.currency.Wallet(p);
					info.tregmine.api.TregminePlayer pPlayer = this.tregmine.tregminePlayer.get(p.getName());

					from.sendMessage(ChatColor.DARK_AQUA + "********** PLAYERLINFO **********");
					String countryName = pPlayer.getMetaString("countryName");
					String city = pPlayer.getMetaString("city");
					String ip = pPlayer.getMetaString("ip");
					String postalCode = pPlayer.getMetaString("postalCode");
					String region = pPlayer.getMetaString("region");
					String hostname = pPlayer.getMetaString("hostname");

					String uid = pPlayer.getMetaString("uid");

					from.sendMessage("username: " + p.getDisplayName() + " UID: " + uid );
					from.sendMessage("ip: " + ip);
					from.sendMessage("hostname: " + hostname);
					from.sendMessage("countryName: " + countryName);
					from.sendMessage("city: " + city);
					from.sendMessage("postalCode: " + postalCode);
					from.sendMessage("region: " + region);
					from.sendMessage("wallet: " + wallet.formatBalance());

					from.sendMessage(ChatColor.DARK_AQUA + "********** PLAYERLINFO **********");
					return true;
				}
			}



			return false;
		}
		return true;
	}


	public Player findPlayer(String name) {
		Player[] players =  plugin.getServer().getOnlinePlayers();

		for (Player player : players) {
			String onlineName = player.getName();
			if ( onlineName.toLowerCase().indexOf(name.toLowerCase()) != -1 ) {
				return plugin.getServer().getPlayer(onlineName);
			}
		}
		return null;
	}
	public void onLoad() {
	}

}