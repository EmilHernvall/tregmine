package info.tregmine.invis;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


import info.tregmine.Tregmine; 


public class Invis extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;

	@Override
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
		getServer().getPluginManager().registerEvents(new InvisPlayer(this), this);
	}

	@Override
	public void onDisable(){
	}

	@Override
	public void onLoad() {
	}


	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();

		if(!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		info.tregmine.api.TregminePlayer tregPlayer = this.tregmine.tregminePlayer.get(player.getName());


		if (commandName.matches("cansee") && args.length > 0  && tregPlayer.isAdmin()) {
			Player target = this.getServer().matchPlayer(args[0]).get(0);
			if (target != null) {
				tregPlayer.sendMessage(ChatColor.AQUA + target.getName() + " can now see you");
				target.showPlayer(player);
			} else {
				tregPlayer.sendMessage(ChatColor.RED + "player not found");				
			}
			return true;
		}
			return false;
	}	
}
