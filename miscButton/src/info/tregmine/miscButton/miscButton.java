package info.tregmine.miscButton;


import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class miscButton extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public miscButtonPlayer player = new miscButtonPlayer(this);
	public HashMap<String,Long> time = new HashMap<String,Long>();

	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				String info = this.getDescription().getName() + " " + this.getDescription().getVersion() + " enable"; 
				log.info(info);
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, player, Priority.Monitor, this);
	}

	public void onDisable(){
	}

	public void onLoad() {
	}

}
