package info.tregmine.compass;


import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Compass extends JavaPlugin {
	
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
		
		
		getServer().getPluginManager().registerEvents(new CompassPlayer(this), this);
	}
	
	@Override
	public void onDisable(){
	}
	
	@Override
	public void onLoad() {
	}

}
