package info.tregmine.lookup;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;


import info.tregmine.Tregmine; 
 

public class Lookup extends JavaPlugin {
	
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	
//	public final LookupPlayer lookupplayer = new LookupPlayer(this);
	
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
		  getServer().getPluginManager().registerEvents(new LookupPlayer(this), this);
	}
	 
	public void onDisable(){
		this.getServer().getScheduler().cancelTasks(this);
	}
	public void onLoad() {

		getServer().getScheduler().scheduleAsyncRepeatingTask(this,new Runnable() {
			public void run() {
				for (Player p : getServer().getOnlinePlayers()) {
					if (p.getAddress().getAddress().getHostAddress().matches("127.0.0.1")) {
						p.sendMessage(ChatColor.RED + "* * * YOU ARE connected to the WRONG adress, please use");
						p.sendMessage(ChatColor.AQUA + "---> mc.tregmine.info <----");
						p.sendMessage(ChatColor.RED + "You are black until you change adress, until then you can't build");
						info.tregmine.api.TregminePlayer tregPlayer = tregmine.tregminePlayer.get(p.getName());
						tregPlayer.setTemporaryChatName(ChatColor.BLACK + tregPlayer.getName());
						tregPlayer.setTempMetaString("trusted", "false");
					}
				}
			}},400L, 400L);

		
	}

}
