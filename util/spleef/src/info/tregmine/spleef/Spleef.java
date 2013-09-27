package info.tregmine.spleef;

import info.tregmine.Tregmine;
import info.tregmine.api.Rank;
import info.tregmine.spleef.listeners.BlockBreak;
import info.tregmine.spleef.listeners.FallFromArena;
import info.tregmine.spleef.listeners.HatRemove;
import info.tregmine.spleef.listeners.PlayerHungry;
import info.tregmine.spleef.listeners.PlayerQuit;
import info.tregmine.spleef.listeners.SpleefSign;
import info.tregmine.spleef.listeners.PlayerDamage;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Spleef extends JavaPlugin implements Listener{

    public Tregmine tregmine = null;
    
	public void onEnable() {
		SettingsManager.getInstance().setup(this);
		
		ArenaManager.getInstance().setup();
		
		CommandManager cm = new CommandManager();
		cm.setup();
		getCommand("spleef").setExecutor(cm);
		
        PluginManager pluginMgm = getServer().getPluginManager();

        // Check for tregmine plugin
        if (tregmine == null) {
            Plugin mainPlugin = pluginMgm.getPlugin("tregmine");
            if (mainPlugin != null) {
                tregmine = (Tregmine)mainPlugin;
            } else {
                Tregmine.LOGGER.info(getDescription().getName() + " " +
                         getDescription().getVersion() +
                         " - could not find Tregmine");
                pluginMgm.disablePlugin(this);
                return;
            }
        }
        
        pluginMgm.registerEvents(this, this);
        pluginMgm.registerEvents(new HatRemove(this), this);
		pluginMgm.registerEvents(new BlockBreak(this), this);
		pluginMgm.registerEvents(new SpleefSign(this), this);
		pluginMgm.registerEvents(new PlayerDamage(this), this);
		pluginMgm.registerEvents(new FallFromArena(this), this);
		pluginMgm.registerEvents(new PlayerQuit(this), this);
		pluginMgm.registerEvents(new PlayerHungry(this), this);
		
	}
	
	public void onDisable() {
		for (Arena a : ArenaManager.getInstance().getArenas()) {
			a.stop(null);
		}
    }
}
