package info.tregmine.zones;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.api.Zone.Permission;
import info.tregmine.database.Mysql;
import info.tregmine.listeners.ZoneBlockListener;
import info.tregmine.listeners.ZoneEntityListener;
import info.tregmine.listeners.ZonePlayerListener;
import info.tregmine.quadtree.IntersectionException;
import info.tregmine.quadtree.Point;
import info.tregmine.quadtree.Rectangle;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
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
	public Map<String, ZoneWorld> worlds;
	
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
		//pluginMgm.registerEvent(Event.Type.CREATURE_SPAWN, new ZoneEntityListener(this), Priority.High, this);
		pluginMgm.registerEvent(Event.Type.ENTITY_DAMAGE, new ZoneEntityListener(this), Priority.High, this);
		
		worlds = new TreeMap<String, ZoneWorld>(new Comparator<String>() {
			public int compare(String a, String b) {
				return a.compareToIgnoreCase(b);
			}
		});
	}

	public void onDisable()
	{
	}

	public ZoneWorld getWorld(World world)
	{
		ZoneWorld zoneWorld = worlds.get(world.getName());
		
		// lazy load zone worlds as required
		if (zoneWorld == null) {
			Mysql mysql = null;
			try {
				mysql = new Mysql();
				mysql.connect();
				ZonesDAO dao = new ZonesDAO(mysql.connect);
				
				zoneWorld = new ZoneWorld(world);
				List<Zone> zones = dao.getZones(world.getName());
				for (Zone zone : zones) {
					zoneWorld.addZone(zone);
				}
				
				worlds.put(world.getName(), zoneWorld);
			} catch (IntersectionException e) {
				throw new RuntimeException(e);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				mysql.close();
			}
		}
		
		return zoneWorld;
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
		
		if ("zone".equals(commandName) || "town".equals(commandName)) {
			if (args.length == 0) {
				return true;
			}
			
			if ("create".equals(args[0]) && player.getMetaBoolean("zones")) {
				createZone(player, args);
				return true;
			}
			else if ("delete".equals(args[0]) && player.getMetaBoolean("zones")) {
				deleteZone(player, args);
				return true;
			}
			else if ("adduser".equals(args[0])) {
				addUser(player, args);
				return true;
			}
			else if ("deluser".equals(args[0])) {
				delUser(player, args);
				return true;
			}
			else if ("entermsg".equals(args[0])) {
				changeValue(player, args);
				return true;
			}
			else if ("exitmsg".equals(args[0])) {
				changeValue(player, args);
				return true;
			}
			else if ("pvp".equals(args[0]) && player.getMetaBoolean("zones")) {
				changeValue(player, args);
				return true;
			}
			else if ("enter".equals(args[0])) {
				changeValue(player, args);
				return true;				
			}
			else if ("place".equals(args[0])) {
				changeValue(player, args);
				return true;				
			}
			else if ("destroy".equals(args[0])) {
				changeValue(player, args);
				return true;				
			}
			else if ("info".equals(args[0])) {
				zoneInfo(player, args);
				return true;
			}
		}
		else if ("lot".equals(commandName)) {
			if ("create".equals(args[0])) {
				createLot(player, args);
				return true;
			}
			else if ("delete".equals(args[0])) {
				return true;
			}
		}
		
		return false;
	}
	
	public void createZone(TregminePlayer player, String[] args)
	{
		ZoneWorld world = getWorld(player.getWorld());
		if (world == null) {
			return;
		}
		
		if (args.length < 2) {
			player.sendMessage("syntax: /zone create [name]");
			return;
		}
		
		String name = args[1];
		if (world.zoneExists(name)) {
			player.sendMessage(ChatColor.RED + "A zone named " + name + " does already exist.");
			return;
		}
		
		Block b1 = player.getBlock("zb1");
		Block b2 = player.getBlock("zb2");
		
		Rectangle rect = new Rectangle(b1.getX(), b1.getZ(), b2.getX(), b2.getZ());
		
		Zone zone = new Zone();
		zone.setWorld(world.getName());
		zone.setName(name);
		zone.addRect(rect);
		zone.setTextEnter("Welcome to " + name + "!");
		zone.setTextExit("Now leaving " + name + ".");
		zone.addUser(player.getName(), Zone.Permission.Owner);
		
		player.sendMessage(ChatColor.RED + "Creating zone at " + rect);
		
		try {
			world.addZone(zone);
			player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + "Zone created successfully.");
		} catch (IntersectionException e) {
			player.sendMessage(ChatColor.YELLOW + "The zone you tried to create overlaps an existing zone.");
			return;
		}
		
		Mysql mysql = null;
		try {
			mysql = new Mysql();
			mysql.connect();
			ZonesDAO dao = new ZonesDAO(mysql.connect);
			dao.createZone(zone);
			dao.addRectangle(zone.getId(), rect);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			mysql.close();
		}
	}
	
	public void deleteZone(TregminePlayer player, String[] args)
	{
		ZoneWorld world = getWorld(player.getWorld());
		if (world == null) {
			return;
		}
		
		if (args.length < 2) {
			player.sendMessage("syntax: /zone delete [name]");
			return;
		}
		
		String name = args[1];
		
		Zone zone = world.getZone(name);
		if (zone == null) {
			player.sendMessage(ChatColor.RED + "A zone named " + name + " does not exist.");
			return;
		}
		
		world.deleteZone(name);
		player.sendMessage(ChatColor.YELLOW + "[" + name + "] " + "Zone deleted.");
		
		Mysql mysql = null;
		try {
			mysql = new Mysql();
			mysql.connect();
			ZonesDAO dao = new ZonesDAO(mysql.connect);
			dao.deleteZone(zone.getId());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			mysql.close();
		}
	}
	
	public void addUser(TregminePlayer player, String[] args)
	{
		ZoneWorld world = getWorld(player.getWorld());
		if (world == null) {
			return;
		}
		
		if (args.length < 4) {
			player.sendMessage(ChatColor.RED + "syntax: /zone adduser [zone] [player] [perm]");
			return;
		}
		
		String zoneName = args[1];
		String userName = args[2];
		Zone.Permission perm = Zone.Permission.fromString(args[3]);
		
		Zone zone = world.getZone(zoneName);
		if (zone == null) {
			player.sendMessage(ChatColor.RED + "Specified zone does not exist.");
			return;
		}
		
		if (zone.getUser(player.getName()) != Permission.Owner && !player.isAdmin()) {
			player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + "You do not have permission to add users to this zone.");
			return;
		}
		
		if (perm == null) {
			player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + "Unknown permission " + args[3] + ".");
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
				player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + "Player " + userName + " was not found.");
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
		String addedConfirmation = perm.getAddedConfirmation();
		player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + String.format(addedConfirmation, userName, zoneName));
		
		TregminePlayer player2 = tregmine.getPlayer(userName);
		if (player2 != null) {
			String addedNotification = perm.getAddedNotification();
			player2.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + String.format(addedNotification, zoneName));
		}
	}
	
	public void delUser(TregminePlayer player, String[] args)
	{
		ZoneWorld world = getWorld(player.getWorld());
		if (world == null) {
			return;
		}
		
		if (args.length < 3) {
			player.sendMessage(ChatColor.RED + "syntax: /zone deluser [zone] [player]");
			return;
		}
		
		String zoneName = args[1];
		String userName = args[2];
		
		Zone zone = world.getZone(zoneName);
		if (zone == null) {
			player.sendMessage(ChatColor.RED + "Specified zone does not exist.");
			return;
		}
		
		if (zone.getUser(player.getName()) != Permission.Owner) {
			player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + "You do not have permission to add users to this zone.");
			return;
		}
		
		Zone.Permission oldPerm = zone.getUser(userName);
		if (oldPerm == null) {
			player.sendMessage(ChatColor.RED + "[" + zone.getName() + "]" +
					userName + " doesn't have any permissions.");
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
				player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + "Player " + userName + " was not found.");
				return;
			}
			
			dao.deleteUser(zone.getId(), userId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			mysql.close();
		}
		
		zone.deleteUser(userName);
		String delConfirmation = oldPerm.getDeletedConfirmation();
		player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + String.format(delConfirmation, userName, zoneName));
		
		TregminePlayer player2 = tregmine.getPlayer(userName);
		if (player2 != null) {
			String delNotification = oldPerm.getDeletedNotification();
			player2.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + String.format(delNotification, zoneName));
		}
	}
	
	public void changeValue(TregminePlayer player, String[] args)
	{
		ZoneWorld world = getWorld(player.getWorld());
		if (world == null) {
			return;
		}
		
		// entermsg [zone] [message]
		if (args.length < 2) {
			player.sendMessage(ChatColor.RED + "unknown command");
			return;
		}
		
		String zoneName = args[1];
		
		Zone zone = world.getZone(zoneName);
		if (zone == null) {
			player.sendMessage(ChatColor.RED + "Specified zone does not exist.");
			return;
		}
		
		if (zone.getUser(player.getName()) != Permission.Owner) {
			player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + "You do not have permission to change settings for this zone.");
			return;
		}
		
		if ("entermsg".equals(args[0]) || "exitmsg".equals(args[0])) {
			StringBuilder buf = new StringBuilder();
			for (int i = 2; i < args.length; i++) {
				buf.append(args[i]);
				buf.append(" ");
			}
			
			String message = buf.toString().trim();
			if ("entermsg".equals(args[0])) {
				zone.setTextEnter(message);
				player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + "Welcome message changed to \"" + message + "\".");
			} else if ("exitmsg".equals(args[0])) {
				zone.setTextExit(message);
				player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + "Exit message changed to \"" + message + "\".");
			}
		}
		else if ("pvp".equals(args[0])) {
			boolean status = args.length >= 3 ? Boolean.parseBoolean(args[2]) : false;
			zone.setPvp(status);
			player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + "PVP changed to \"" + (status ? "allowed" : "disallowed") + "\".");
		}
		else if ("enter".equals(args[0])) {
			boolean status = args.length >= 3 ? Boolean.parseBoolean(args[2]) : false;
			zone.setEnterDefault(status);
			player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + "Enter default changed to \"" + (status ? "everyone" : "whitelisted") + "\".");			
		}
		else if ("place".equals(args[0])) {
			boolean status = args.length >= 3 ? Boolean.parseBoolean(args[2]) : false;
			zone.setPlaceDefault(status);
			player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + "Place default changed to \"" + (status ? "everyone" : "whitelisted") + "\".");			
		}
		else if ("destroy".equals(args[0])) {
			boolean status = args.length >= 3 ? Boolean.parseBoolean(args[2]) : false;
			zone.setDestroyDefault(status);
			player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] " + "Destroy default changed to \"" + (status ? "everyone" : "whitelisted") + "\".");			
		}
		
		Mysql mysql = null;
		try {
			mysql = new Mysql();
			mysql.connect();
			ZonesDAO dao = new ZonesDAO(mysql.connect);
			dao.updateZone(zone);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			mysql.close();
		}
	}
	
	public void zoneInfo(TregminePlayer player, String[] args)
	{
		ZoneWorld world = getWorld(player.getWorld());
		if (world == null) {
			return;
		}
		
		if (args.length < 2) {
			player.sendMessage(ChatColor.RED + "unknown command");
			return;
		}
		
		String zoneName = args[1];
		
		Zone zone = world.getZone(zoneName);
		if (zone == null) {
			player.sendMessage(ChatColor.RED + "Specified zone does not exist.");
			return;
		}
		
		if (zone.getUser(player.getName()) != Permission.Owner && !player.isAdmin()) {
			player.sendMessage(ChatColor.RED + "[" + zone.getName() + "] " + "You do not have permission to change settings for this zone.");
			return;
		}

		player.sendMessage(ChatColor.YELLOW + "Info about " + zone.getName());
		player.sendMessage(ChatColor.YELLOW + "ID: " + zone.getId());
		player.sendMessage(ChatColor.YELLOW + "World: " + zone.getWorld());
		for (Rectangle rect : zone.getRects()) {
			player.sendMessage(ChatColor.RED + "Rect: " + rect);
		}
		player.sendMessage(ChatColor.YELLOW + "Enter: " + (zone.getEnterDefault() ? "Everyone (true)" : "Only allowed (false)"));
		player.sendMessage(ChatColor.YELLOW + "Place: " + (zone.getPlaceDefault() ? "Everyone (true)" : "Only makers (false)"));
		player.sendMessage(ChatColor.YELLOW + "Destroy: " + (zone.getDestroyDefault() ? "Everyone (true)" : "Only makers (false)"));
		player.sendMessage(ChatColor.YELLOW + "PVP: " + zone.isPvp());
		player.sendMessage(ChatColor.YELLOW + "Enter message: " + zone.getTextEnter());
		player.sendMessage(ChatColor.YELLOW + "Exit message: " + zone.getTextExit());

		for (String user : zone.getUsers()) {
			Zone.Permission perm = zone.getUser(user);
			player.sendMessage(ChatColor.YELLOW + user + " - " + perm);
		}
	}
	
	public void createLot(TregminePlayer player, String[] args)
	{
		ZoneWorld world = getWorld(player.getWorld());
		if (world == null) {
			return;
		}
		
		if (args.length < 3) {
			player.sendMessage("syntax: /lot create [name] [owner]");
			return;
		}
		
		String name = args[1];
		if (world.lotExists(name)) {
			player.sendMessage(ChatColor.RED + "A lot named " + name + " does already exist.");
			return;
		}
		
		Mysql mysql = null;
		int userId;
		try {
			mysql = new Mysql();
			mysql.connect();
			ZonesDAO dao = new ZonesDAO(mysql.connect);
			userId = dao.getUserId(args[2]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			mysql.close();
		}
		
		if (userId == -1) {
			player.sendMessage(ChatColor.RED + "Player " + args[2] + " was not found.");
			return;			
		}
		
		Block b1 = player.getBlock("zb1");
		Block b2 = player.getBlock("zb2");
		
		Zone zone = world.findZone(new Point(b1.getX(), b1.getZ()));
		
		Zone.Permission perm = zone.getUser(player.getName());
		if (perm != Zone.Permission.Owner) {
			player.sendMessage(ChatColor.RED + "You are not allowed to create lots in zone " + zone.getName() + ".");
			return;
		}
		
		Zone checkZone = world.findZone(new Point(b2.getX(), b2.getZ()));
		
		// identity check. both lookups should return exactly the same object
		if (zone != checkZone) {
			return;
		}
		
		Rectangle rect = new Rectangle(b1.getX(), b1.getZ(), b2.getX(), b2.getZ());
		
		Lot lot = new Lot();
		lot.setZoneId(zone.getId());
		lot.setRect(rect);
		lot.setUserId(userId);
		lot.setName(args[1]);
		
		try {
			world.addLot(lot);
		} catch (IntersectionException e) {
			player.sendMessage(ChatColor.RED + "The specified rectangle intersects an existing lot.");
			return;
		}
		
		player.sendMessage(ChatColor.YELLOW + "[" + zone.getName() + "] Lot " + args[1] + " created for player " + args[2] + ".");
	}
}
