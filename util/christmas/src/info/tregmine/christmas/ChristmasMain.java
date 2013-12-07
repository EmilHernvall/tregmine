
package info.tregmine.christmas;

import info.tregmine.christmas.listeners.PortalListener;
import info.tregmine.christmas.listeners.SparklerListener;
import info.tregmine.christmas.listeners.SpawnListener;
import info.tregmine.christmas.listeners.TargetingListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class ChristmasMain extends JavaPlugin implements Listener {
	private final static String WORLD_NAME = "Christmas";
	private static World map = null;

	public void onDisable() {
		Bukkit.broadcastMessage(ChatColor.RED + "Christmas has been, Unloaded!");
	}

	public void onEnable() {

		Bukkit.getPluginManager().registerEvents(this,this);
		Bukkit.getPluginManager().registerEvents(new SpawnListener(), this);
		Bukkit.getPluginManager().registerEvents(new SparklerListener(), this);
		Bukkit.getPluginManager().registerEvents(new TargetingListener(), this);
		Bukkit.getPluginManager().registerEvents(new PortalListener(this), this);
		
		Bukkit.broadcastMessage(ChatColor.GREEN + "Christmas has been, Loaded!");
	}

	public boolean isAnon(CommandSender sender) {
		if (!(sender instanceof Player)) {
			return true;
		} else {
			return false;
		}
	}

	public static World getMap() {
		WorldCreator wc = new WorldCreator(WORLD_NAME);
		wc.environment(Environment.NORMAL);
		wc.generator(new ChristmasChunkGenerator());
		wc.createWorld();
		World world =  Bukkit.getWorld(WORLD_NAME);
		if (map == null) {
			map = Bukkit.getServer().createWorld(wc);
		}
		return world;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new ChristmasChunkGenerator();
	}
}
