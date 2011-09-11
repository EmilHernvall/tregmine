package info.tregmine;


import info.tregmine.api.TregminePlayer;
import info.tregmine.listeners.TregmineBlockListener;
import info.tregmine.listeners.TregmineEntityListener;
import info.tregmine.listeners.TregminePlayerListener;
import info.tregmine.listeners.TregmineWeatherListener;
import info.tregmine.stats.BlockStats;

import java.util.logging.Logger;

//import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
//import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Ein Andersson - www.tregmine.info
 * @version 0.8
 */
public class Tregmine extends JavaPlugin 
{
	public final Logger log = Logger.getLogger("Minecraft");
	
	public final TregminePlayerListener listener = new TregminePlayerListener(this);
	private final TregmineBlockListener tregmineblock = new TregmineBlockListener(this);
	private final TregmineEntityListener tregmineentity = new TregmineEntityListener(this);
	private final TregmineWeatherListener weather = new TregmineWeatherListener(this);
	public final BlockStats blockStats = new BlockStats(this);

	public Map<String, TregminePlayer> tregminePlayer = new TreeMap<String, TregminePlayer>(new Comparator<String>() {
			public int compare(String a, String b) {
				return a.compareToIgnoreCase(b);
			}
		});
	
	public int version = 0;
	public int amount = 0;

	public void onEnable() 
	{
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN,listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_LOGIN, listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_PRELOGIN, listener, Priority.Highest, this);
		
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_BUCKET_FILL, listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_PICKUP_ITEM, listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_DROP_ITEM, listener, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_KICK, listener, Priority.Highest, this);

		// BLOCK
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_IGNITE, tregmineblock, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BURN, tregmineblock, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, tregmineblock, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, tregmineblock, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.SIGN_CHANGE, tregmineblock, Priority.Highest, this);
		getServer().getPluginManager().registerEvent(Event.Type.LEAVES_DECAY, tregmineblock, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.BLOCK_CANBUILD,	tregmineblock, Priority.Highest, this);

		// Weather
		getServer().getPluginManager().registerEvent(Event.Type.WEATHER_CHANGE,	weather, Priority.Highest, this);
		
		getServer().getPluginManager().registerEvent(Event.Type.ENTITY_EXPLODE,	tregmineentity, Priority.Highest, this); // ENTITY EXPLODE
	}

	public void onDisable() { //run when plugin is disabled
	}
	
	public void onLoad() 
	{
		Player[] players = this.getServer().getOnlinePlayers();

		for (Player player : players) {
			String onlineName = player.getName();
			TregminePlayer tregPlayer = new TregminePlayer(player, onlineName);
			tregPlayer.load();
			this.tregminePlayer.put(onlineName, tregPlayer);
		}
	}
	
	public TregminePlayer matchPlayer(String name)
	{
		List<Player> players = getServer().matchPlayer(name);
		if (players.size() != 1) {
			return null;
		}
		
		Player player = players.get(0);
		return getPlayer(player);
	}
	
	public TregminePlayer getPlayer(String name)
	{
		return tregminePlayer.get(name);
	}
	
	public TregminePlayer getPlayer(Player player)
	{
		return tregminePlayer.get(player.getName());
	}
}
