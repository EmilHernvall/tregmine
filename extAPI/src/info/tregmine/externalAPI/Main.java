package info.tregmine.externalAPI;

import java.io.IOException;
import java.util.logging.Logger;


import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Main extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	java.net.ServerSocket ServerSocket;
    boolean listening = true;
	
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
		
		
		try {
			ServerSocket = new java.net.ServerSocket(4444);
	        while (listening)
	    	    new Server(ServerSocket.accept()).start();
		} catch (IOException e) {
			this.log.warning("Could not listen on port: 4444");
			this.getServer().getPluginManager().disablePlugin(this);
			e.printStackTrace();
		}
		


	}

	public void onDisable(){
		try {
			ServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onLoad() {
	}

}