package info.tregmine.chestbless;

import java.util.HashMap;
import java.util.logging.Logger;

//import org.bukkit.ChatColor;
//import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
//import org.bukkit.entity.CreatureType;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
//import org.bukkit.entity.Slime;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Bless extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public final BlessPlayer player = new BlessPlayer(this);
	public final BlessBlock block = new BlessBlock(this);
	
	public HashMap<Integer, String> chests = new HashMap<Integer, String>();
	
	
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
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, player, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, block, Priority.Highest, this);
	}

	public void onDisable(){
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();

		if(!(sender instanceof Player)) {
			return false;
		}

		Player player = (Player) sender;
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(player.getName());

		boolean isAdmin = tregminePlayer.isAdmin();
		boolean bless = tregminePlayer.getMetaBoolean("bless");
		
		if (commandName.matches("bless") && ( isAdmin || bless)) {
			try {
				String name = getServer().matchPlayer(args[0]).get(0).getName();
				info.tregmine.api.TregminePlayer toPlayer = this.tregmine.tregminePlayer.get(getServer().matchPlayer(args[0]).get(0).getName());
				player.sendMessage(ChatColor.AQUA + "You will bless following chests to " + toPlayer.getChatName());
				tregminePlayer.setMetaString("chestbless", name);
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Something went wrong, player not online?");
				e.printStackTrace();
			}
			return true;
		}

		return false;
	}

	public void onLoad() {
		info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql(); 
		mysql.connect();
		this.chests = info.tregmine.chestbless.Store.loadbless(mysql);
		mysql.close();
	}
}
