package info.tregmine.childsplay;


import info.tregmine.world.citadel.CitadelLimit;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public HashMap<String,Boolean> skylandBlock = new HashMap<String,Boolean>();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new CitadelLimit(this), this);
	}

	@Override
	public void onDisable(){
	}

	@Override
	public void onLoad() {
	}

}