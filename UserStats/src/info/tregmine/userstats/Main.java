package info.tregmine.userstats;


//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.TimeZone;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 



public class Main extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public boolean enable = true;

	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
			}
		}

		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (Player p : getServer().getOnlinePlayers()) {
					p.sendMessage("SPAWM");
				}
			}
		}, 20L, 20L);

	}

	public void onDisable(){
//		String info = this.getDescription().getName() + " " + this.getDescription().getVersion() + " disabled"; 
//		log.info(info);
		//		this.getServer().broadcastMessage(info);
		this.enable = false;
	}
	public void onLoad() {
	}

}
