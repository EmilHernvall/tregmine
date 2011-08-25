package info.tregmine.zones;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.api.Zone.Permission;
import info.tregmine.database.Mysql;
import info.tregmine.listeners.ZoneBlockListener;
import info.tregmine.listeners.ZoneEntityListener;
import info.tregmine.listeners.ZonePlayerListener;
import info.tregmine.quadtree.IntersectionException;
import info.tregmine.quadtree.QuadTree;
import info.tregmine.quadtree.Rectangle;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
import info.tregmine.ZonesDAO;

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
		pluginMgm.registerEvent(Event.Type.BLOCK_PLACE, new ZoneBlockListener(this), Priority.High, this);
		pluginMgm.registerEvent(Event.Type.PLAYER_INTERACT, new ZonePlayerListener(this), Priority.High, this);
		pluginMgm.registerEvent(Event.Type.PLAYER_MOVE, new ZonePlayerListener(this), Priority.High, this);
		pluginMgm.registerEvent(Event.Type.PLAYER_TELEPORT, new ZonePlayerListener(this), Priority.High, this);
		pluginMgm.registerEvent(Event.Type.CREATURE_SPAWN, new ZoneEntityListener(this), Priority.High, this);
		
		zonesLookup = new QuadTree<Zone>(0);
		zoneNameLookup = new HashMap<String, Zone>();
		
		Mysql mysql = null;
		try {
			mysql = new Mysql();
			mysql.connect();
			ZonesDAO dao = new ZonesDAO(mysql.connect);
			List<Zone> zones = dao.getZones();
			for (Zone zone : zones) {
				for (Rectangle rect : zone.getRects()) {
					try {
						zonesLookup.insert(rect, zone);
					} catch (IntersectionException e) {
						throw new RuntimeException(e);
					}
					zoneNameLookup.put(zone.getName(), zone);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			mysql.close();
		}
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
			else if ("adduser".equals(args[0])) {
				addUser(player, args);
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
		
		Rectangle rect = new Rectangle(b1.getX(), b1.getZ(), b2.getX(), b2.getZ());
		
		Zone zone = new Zone();
		zone.setName(name);
		zone.addRect(rect);
		zone.setTextEnter("Welcome to " + name + "!");
		zone.setTextExit("Now leaving " + name + ".");
		zone.addUser(player.getName(), Zone.Permission.Owner);
		
		player.sendMessage("Creating zone at " + rect);
		
		Mysql mysql = null;
		try {
			mysql = new Mysql();
			mysql.connect();
			ZonesDAO dao = new ZonesDAO(mysql.connect);
			int userId = dao.getUserId(player.getName());
			if (userId == -1) {
				player.sendMessage("Player " + player.getName() + " was not found.");
				return;
			}
			
			dao.createZone(zone);
			dao.addRectangle(zone.getId(), rect);
			dao.addUser(zone.getId(), userId, Zone.Permission.Owner);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			mysql.close();
		}
		
		try {
			zonesLookup.insert(rect, zone);
			zoneNameLookup.put(zone.getName(), zone);
			player.sendMessage("Zone created successfully.");
		} catch (IntersectionException e) {
			player.sendMessage("The zone you tried to create overlaps an existing zone.");
		}
	}
	
	public void addUser(TregminePlayer player, String[] args)
	{
		if (args.length < 4) {
			player.sendMessage("syntax: /zone adduser [zone] [player] [perm]");
			return;
		}
		
		String zoneName = args[1];
		String userName = args[2];
		Zone.Permission perm = Zone.Permission.fromString(args[3]);
		
		Zone zone = zoneNameLookup.get(zoneName);
		if (zone == null) {
			player.sendMessage("Specified zone does not exist.");
			return;
		}
		
		if (zone.getUser(player.getName()) != Permission.Owner) {
			player.sendMessage("You do not have permission to add new builders.");
			return;
		}
		
		if (perm == null) {
			player.sendMessage("Unknown permission " + args[3] + ".");
			return;
		}
		
		// store permission change in db
		Mysql mysql = null;
		try {
			mysql = new Mysql();
			mysql.connect();
			ZonesDAO dao = new ZonesDAO(mysql.connect);
			int userId = dao.getUserId(userName);
			if (userId == -1) {
				player.sendMessage("Player " + userName + " was not found.");
				return;
			}
			
			dao.deleteUser(zone.getId(), userId);
			dao.addUser(zone.getId(), userId, perm);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			mysql.close();
		}
		
		zone.addUser(userName, perm);
		player.sendMessage(userName + " can now build in " + zoneName + ".");
		
		TregminePlayer player2 = tregmine.getPlayer(userName);
		if (player2 != null) {
			player2.sendMessage("You can now build in " + zoneName + ".");
		}
	}
}
