package info.tregmine;


import info.tregmine.api.TregminePlayer;
import info.tregmine.listeners.TregmineBlockListener;
import info.tregmine.listeners.TregmineEntityListener;
import info.tregmine.listeners.TregminePlayerListener;
import info.tregmine.listeners.TregmineWeatherListener;
import info.tregmine.stats.BlockStats;
//import info.tregmine.world.citadel.CitadelLimit;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
//import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
//import org.bukkit.event.Event.Priority;
//import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Ein Andersson - www.tregmine.info
 * @version 0.8
 */
public class Tregmine extends JavaPlugin 
{
	public final Logger log = Logger.getLogger("Minecraft");

	public final BlockStats blockStats = new BlockStats(this);

	public Map<String, TregminePlayer> tregminePlayer = new HashMap<String, TregminePlayer>();

	public int version = 0;
	public int amount = 0;

	@Override
	public void onEnable() 	{
		
		WorldCreator citadelCreator = new WorldCreator("citadel"); 
		citadelCreator.environment(Environment.NORMAL);
		citadelCreator.createWorld();

		WorldCreator world = new WorldCreator("world"); 
		world.environment(Environment.NORMAL);
		world.createWorld();

		WorldCreator NETHER = new WorldCreator("world_nether"); 
		NETHER.environment(Environment.NETHER);
		NETHER.createWorld();

		getServer().getPluginManager().registerEvents(new info.tregmine.lookup.LookupPlayer(this), this);
		
		
		getServer().getPluginManager().registerEvents(new TregminePlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineBlockListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineEntityListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineWeatherListener(this), this);
		
		getServer().getPluginManager().registerEvents(new info.tregmine.invis.InvisPlayer(this), this);

		
		getServer().getPluginManager().registerEvents(new info.tregmine.death.DeathEntity(this), this);
		getServer().getPluginManager().registerEvents(new info.tregmine.death.DeathPlayer(this), this);

		
		
		getServer().getPluginManager().registerEvents(new info.tregmine.world.citadel.CitadelLimit(this), this);		
	 	getServer().getPluginManager().registerEvents(new info.tregmine.sign.Color(), this);
	}

	@Override
	public void onDisable() { //run when plugin is disabled
		this.getServer().getScheduler().cancelTasks(this);
	}

	@Override
	public void onLoad() 
	{
		Player[] players = this.getServer().getOnlinePlayers();

		for (Player player : players) {
			String onlineName = player.getName();
			TregminePlayer tregPlayer = new TregminePlayer(player, onlineName);
			tregPlayer.load();
			this.tregminePlayer.put(onlineName, tregPlayer);
		}
		
		getServer().getScheduler().scheduleAsyncRepeatingTask(this,new Runnable() {
			@Override
			public void run() {
			for (Player p : getServer().getOnlinePlayers()) {
				if (p.getAddress().getAddress().getHostAddress().matches("127.0.0.1")) {
					p.sendMessage(ChatColor.RED + "* * * YOU ARE connected to the WRONG adress, please use");
					p.sendMessage(ChatColor.AQUA + "---> mc.tregmine.info <----");
					p.sendMessage(ChatColor.RED + "You are black until you change adress, until then you can't build");
					info.tregmine.api.TregminePlayer tregPlayer = tregminePlayer.get(p.getName());
					tregPlayer.setTemporaryChatName(ChatColor.BLACK + tregPlayer.getName());
					tregPlayer.setTempMetaString("trusted", "false");
				}
			}
		}},400L, 400L);
		
	}

	public TregminePlayer getPlayer(String name)	{
		return tregminePlayer.get(name);
	}

	public TregminePlayer getPlayer(Player player)	{
		return tregminePlayer.get(player.getName());
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player from = null;
		if(!(sender instanceof Player)) {
			return false;
		} else {
			from = (Player) sender;
		}

//		info.tregmine.api.TregminePlayer tregminePlayer = this.tregminePlayer.get(from.getName());

		
		from.sendMessage(commandName);
		
		
		return false;
	}
}
