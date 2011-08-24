package info.tregmine.signcolor;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;

public class SignColours extends JavaPlugin {
	
	SignListener listener;

	public void onDisable() {
	}

	public void onEnable() {
		listener = new SignListener();
		getServer().getPluginManager().registerEvent(Event.Type.SIGN_CHANGE, this.listener, Event.Priority.High, this);
	}

	public void onLoad() {
	}

}