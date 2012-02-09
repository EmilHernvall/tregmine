package info.tregmine.signcolor;

import org.bukkit.plugin.java.JavaPlugin;

public class SignColours extends JavaPlugin {
	

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
	 	  getServer().getPluginManager().registerEvents(new SignListener(), this);
	}

	@Override
	public void onLoad() {
	}

}