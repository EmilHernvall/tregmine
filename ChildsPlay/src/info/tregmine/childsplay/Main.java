package info.tregmine.childsplay;


import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public final SkylandPlayer listener = new SkylandPlayer(this);
	public HashMap<String,Boolean> skylandBlock = new HashMap<String,Boolean>();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new SkylandPlayer(this), this);
	}

	@Override
	public void onDisable(){
	}

	@Override
	public void onLoad() {
	}

}