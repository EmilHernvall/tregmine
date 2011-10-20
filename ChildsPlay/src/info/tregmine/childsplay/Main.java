package info.tregmine.childsplay;

import java.util.HashMap;
import java.util.logging.Logger;

//import org.bukkit.World;
//import org.bukkit.World.Environment;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public final SkylandPlayer listener = new SkylandPlayer(this);
	public HashMap<String,Boolean> skylandBlock = new HashMap<String,Boolean>();
	public final EntityListner el = new EntityListner(this);

	public void onEnable() {
		//		this.getServer().createWorld("ChildsPlay", Environment.NORMAL);
		//		this.getServer().createWorld("skyland", Environment.SKYLANDS);
		//		World matrix = this.getServer().createWorld("matrix", Environment.NORMAL);
		//		matrix.setPVP(true);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_TELEPORT, listener, Priority.Highest, this);
		//		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, listener, Priority.Highest, this);
		//		getServer().getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, el, Priority.Lowest, this);
	}

	public void onDisable(){
	}

	public void onLoad() {
	}

}