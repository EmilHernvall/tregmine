package info.tregmine.signcolor;


import javax.swing.event.DocumentEvent.EventType;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Priority;

public class SignColours extends JavaPlugin {
	
	SignListener listener;

	public void onDisable() {
	}

	public void onEnable() {
		listener = new SignListener();
//		getServer().getPluginManager().registerEvent(Event.Type.SIGN_CHANGE, this.listener, EventPriority.HIGH, this);

	}

	public void onLoad() {
	}

}