package info.tregmine.zones;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.listeners.ZoneBlockListener;
import info.tregmine.listeners.ZoneEntityListener;
import info.tregmine.listeners.ZonePlayerListener;
import info.tregmine.quadtree.IntersectionException;
import info.tregmine.quadtree.QuadTree;
import info.tregmine.quadtree.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 

public class ZonesPlugin extends JavaPlugin 
{
	public final Logger log = Logger.getLogger("Minecraft");
	
	public Tregmine tregmine = null;
	public QuadTree<Zone> zonesLookup;
	public Map<String, Zone> zoneNameLookup;
	
	public void onLoad() 
	{
	}
	
	public void onEnable()
	{
		Server server = getServer();
		PluginManager pluginMgm = server.getPluginManager();
		
		if (this.tregmine == null) {
			Plugin plugin = pluginMgm.getPlugin("Tregmine");
			if (plugin != null) {
				this.tregmine = (Tregmine)plugin;
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				pluginMgm.disablePlugin(this);
			}
		}
		
		pluginMgm.registerEvent(Event.Type.BLOCK_BREAK, new ZoneBlockListener(this), Priority.High, this);
		pluginMgm.registerEvent(Event.Type.PLAYER_INTERACT, new ZonePlayerListener(this), Priority.High, this);
		pluginMgm.registerEvent(Event.Type.PLAYER_MOVE, new ZonePlayerListener(this), Priority.High, this);
		pluginMgm.registerEvent(Event.Type.CREATURE_SPAWN, new ZoneEntityListener(this), Priority.High, this);
		
		zonesLookup = new QuadTree<Zone>(0);
		zoneNameLookup = new HashMap<String, Zone>();
	}

	public void onDisable()
	{
	}


	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
	{
		String commandName = command.getName().toLowerCase();
		TregminePlayer player = null;

		if (sender instanceof Player) {
			player = tregmine.getPlayer((Player)sender);
		} else {
			return false;
		}
		
		if (commandName.equals("zone") && player.isAdmin()) {
			if (args.length == 0) {
				return true;
			}
			
			if ("create".equals(args[0])) {
				createZone(player, args);
				return true;
			}
			else if ("addbuilder".equals(args[0])) {
				addBuilder(player, args);
				return true;
			}
			else if ("addban".equals(args[0])) {
				addBan(player, args);
				return true;
			}
		}
		
		return false;
	}
	
	public void createZone(TregminePlayer player, String[] args)
	{
		if (args.length < 2) {
			player.sendMessage("syntax: /zone create [name]");
			return;
		}
		
		String name = args[1];
		if (zoneNameLookup.containsKey(name)) {
			player.sendMessage("A zone named " + name + " does already exist.");
			return;
		}
		
		Block b1 = player.getBlock("b1");
		Block b2 = player.getBlock("b2");
		
		player.sendMessage("" + b1.getLocation());
		player.sendMessage("" + b2.getLocation());
		
		Zone zone = new Zone();
		zone.setName(name);
		zone.setOwner(player.getName());
		zone.setRect(new Rectangle(b1.getX(), b1.getZ(), b2.getX(), b2.getZ()));
		zone.setTextEnter("Welcome to " + name + "!");
		zone.setTextExit("Now leaving " + name + ".");
		zone.addBuilder(player.getName());
		
		player.sendMessage("Creating zone at " + zone.getRect());
		
		try {
			zonesLookup.insert(zone.getRect(), zone);
			zoneNameLookup.put(zone.getName(), zone);
			player.sendMessage("Zone created successfully.");
		} catch (IntersectionException e) {
			player.sendMessage("The zone you tried to create overlaps an existing zone.");
		}
	}
	
	public void addBuilder(TregminePlayer player, String[] args)
	{
		if (args.length < 3) {
			player.sendMessage("syntax: /zone addbuilder [zone] [player]");
			return;
		}
		
		String zoneName = args[1];
		String builder = args[2];
		
		Zone zone = zoneNameLookup.get(zoneName);
		if (zone == null) {
			player.sendMessage("Specified zone does not exist.");
			return;
		}
		
		if (!player.getName().equals(zone.getOwner())) {
			player.sendMessage("You do not have permission to add new builders.");
			return;
		}
		
		zone.addBuilder(builder);
		player.sendMessage(builder + " can now build in " + zoneName + ".");
		Player builderPlayer = tregmine.getPlayer(builder);
		if (builderPlayer != null) {
			builderPlayer.sendMessage("You can now build in " + zoneName + ".");
		}
	}
	
	public void addBan(TregminePlayer player, String[] args)
	{
		if (args.length < 3) {
			player.sendMessage("syntax: /zone addban [zone] [player]");
			return;
		}
		
		String zoneName = args[1];
		String bannedPlayer = args[2];
		
		Zone zone = zoneNameLookup.get(zoneName);
		if (zone == null) {
			player.sendMessage("Specified zone does not exist.");
			return;
		}
		
		if (!player.getName().equals(zone.getOwner())) {
			player.sendMessage("You do not have permission to add new builders.");
			return;
		}
		
		zone.addBan(bannedPlayer);
		player.sendMessage(bannedPlayer + " is now banned from " + zoneName + ".");
		Player builderPlayer = tregmine.getPlayer(bannedPlayer);
		if (builderPlayer != null) {
			builderPlayer.sendMessage("You have been banned from " + zoneName + ".");
		}
	}
}
