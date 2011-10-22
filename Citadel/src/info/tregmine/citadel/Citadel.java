package info.tregmine.citadel;

import info.tregmine.Tregmine;

import java.util.logging.Logger;

//import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
//import org.bukkit.ChatColor;
//import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;



public class Citadel extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;

	public void onEnable() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");
		
		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
				
				WorldCreator citadelCreator = new WorldCreator("citadel"); 
				citadelCreator.environment(Environment.NORMAL);
				citadelCreator.createWorld();
				
				WorldCreator alpha = new WorldCreator("alpha"); 
				alpha.environment(Environment.NORMAL);
				alpha.createWorld();
				
				WorldCreator skyland = new WorldCreator("skyland"); 
				skyland.environment(Environment.SKYLANDS);
				skyland.createWorld();

			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
		
	}

	public void onDisable(){
	}

	public void onLoad() {
	}

	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player from = null;

		if(!(sender instanceof Player)) {
			return false;
		} else {
			from = (Player) sender;
		}
		info.tregmine.api.TregminePlayer tregPlayer = this.tregmine.tregminePlayer.get(from.getName());

		if(commandName.equals("citadel-") && (tregPlayer.getMetaBoolean("mentor") || tregPlayer.isAdmin() )) {
				from.teleport(this.getServer().getWorld("citadel").getSpawnLocation());
		}
		
		return false;
	}
	
}