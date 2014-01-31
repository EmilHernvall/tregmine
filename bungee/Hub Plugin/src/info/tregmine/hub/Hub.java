package info.tregmine.hub;

import info.tregmine.hub.commands.HeadCommand;
import info.tregmine.hub.commands.SpawnCommand;
import info.tregmine.hub.listeners.BlockListener;
import info.tregmine.hub.listeners.BoostListener;
import info.tregmine.hub.listeners.DropListener;
import info.tregmine.hub.listeners.JoinListener;
import info.tregmine.hub.listeners.PickupListener;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Hub extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
        PluginManager pluginMgm = Bukkit.getServer().getPluginManager();
        pluginMgm.registerEvents(new BoostListener(this), this);
        pluginMgm.registerEvents(new BlockListener(this), this);
        pluginMgm.registerEvents(new JoinListener(this), this);
        pluginMgm.registerEvents(new DropListener(this), this);
        pluginMgm.registerEvents(new PickupListener(this), this);
        
        getCommand("setspawn").setExecutor(new SpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("head").setExecutor(new HeadCommand(this));

        setDay(0);
	}
	
	public void setDay(int task) {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				for(World world : Bukkit.getWorlds()){
					world.setTime(8000L);
					world.setStorm(false);
					world.setThundering(false);
				}
			}
		}, 0L, 10L);
	}
}