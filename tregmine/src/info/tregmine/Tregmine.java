package info.tregmine;


import info.tregmine.api.TregminePlayer;
import info.tregmine.commands.Invis;
import info.tregmine.listeners.TregmineBlockListener;
import info.tregmine.listeners.TregmineEntityListener;
import info.tregmine.listeners.TregminePlayerListener;
import info.tregmine.listeners.TregmineWeatherListener;
import info.tregmine.stats.BlockStats;

import java.util.logging.Logger;

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
	public void onEnable() 
	{

		getServer().getPluginManager().registerEvents(new TregminePlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineBlockListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineEntityListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineWeatherListener(this), this);

	}

	@Override
	public void onDisable() { //run when plugin is disabled
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

		info.tregmine.api.TregminePlayer tregminePlayer = this.tregminePlayer.get(from.getName());

//		TregminePlayer _tregPlayer, String _command, String _flaggs,  Tregmine _tregmine
		
		if(commandName.equals("invis")) {
			Invis invis = new Invis(tregminePlayer, commandName, args, this);
			invis.execute();
		}
		
		
		return false;
	}
}
