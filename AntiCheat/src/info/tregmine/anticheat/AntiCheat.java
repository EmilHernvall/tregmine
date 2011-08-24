package info.tregmine.anticheat;

import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;




public class AntiCheat extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
//	public AntiCheatPlayer player = new AntiCheatPlayer(this);
	public AntiCheatBlock block = new AntiCheatBlock(this);

	public void onEnable(){
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, block, Priority.Monitor, this);
	}

	public void onDisable(){
	}

	public void onLoad() {
	}

}
