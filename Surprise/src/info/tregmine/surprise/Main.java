package info.tregmine.surprise;


//import java.text.NumberFormat;
import java.util.HashMap;
import java.util.logging.Logger;


import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Main extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
//	NumberFormat nf = NumberFormat.getNumberInstance();
	public SuprisePlayerListener inventory = new SuprisePlayerListener(this);
	public HashMap<Integer, String> whoDropedItem = new HashMap<Integer, String>();

	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		
		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
			
//			  getServer().getPluginManager().registerEvent(Event.Type.INVENTORY_OPEN, inventory, Priority.High, this);
			  getServer().getPluginManager().registerEvent(Event.Type.PLAYER_DROP_ITEM, inventory, Priority.Normal, this);
//			  getServer().getPluginManager().registerEvent(Event.Type.PLAYER_PICKUP_ITEM, inventory, Priority.Normal, this);

		}
	}

	public void onDisable(){
	}
	
	public void onLoad() {
	}

}