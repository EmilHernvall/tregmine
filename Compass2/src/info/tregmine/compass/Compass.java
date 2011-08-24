package info.tregmine.compass;


import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Compass extends JavaPlugin {
	
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public final CompassPlayer player = new CompassPlayer(this);
	
	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
		    if(test != null) {
//				String info = this.getDescription().getName() + " " + this.getDescription().getVersion() + " enabled"; 
//				log.info(info);
//				this.getServer().broadcastMessage(info);
			this.tregmine = ((Tregmine)test);
		    } else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
		    }
		}
		  getServer().getPluginManager().registerEvent(Event.Type.PLAYER_ANIMATION, player, Priority.Highest, this);
	}
	
	public void onDisable(){
//		String info = this.getDescription().getName() + " " + this.getDescription().getVersion() + " disabled"; 
//		log.info(info);
//		this.getServer().broadcastMessage(info);
	}
	
	public void onLoad() {
	}

}
