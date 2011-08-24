package info.tregmine.death;


//import java.io.File;
import java.util.logging.Logger;

//import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
//import org.bukkit.plugin.PluginDescriptionFile;
//import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Death extends JavaPlugin {
	
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public final DeathEntity entity = new DeathEntity(this);
	public final DeathPlayer player = new DeathPlayer(this);
	
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
		  getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, entity, Priority.Normal, this);
		  getServer().getPluginManager().registerEvent(Event.Type.ENTITY_EXPLODE, entity, Priority.Normal, this);
		  getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DEATH, entity, Priority.Normal, this);
		  getServer().getPluginManager().registerEvent(Event.Type.PLAYER_RESPAWN, player, Priority.Normal, this);
	}
	
	public void onDisable(){
	}

	public void onLoad() {
	}

}
