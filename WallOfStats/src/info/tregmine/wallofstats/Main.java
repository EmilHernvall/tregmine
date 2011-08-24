package info.tregmine.wallofstats;


import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import info.tregmine.Tregmine; 


public class Main extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public boolean enable = true;
	private int task;

	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
			}
		}
		task = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new WallOfSignLoop(getServer().getWorld("world")), 1200L, 1200L);
	}

	public void onDisable(){
		this.enable = false;
		getServer().getScheduler().cancelTask(task);
	}

	public void onLoad() {
	}

}
