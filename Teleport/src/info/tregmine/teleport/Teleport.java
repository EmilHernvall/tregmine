package info.tregmine.teleport;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 
//import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;


public class Teleport extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public Player from = null;

	@Override
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
	}

	@Override
	public void onDisable(){
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		if(!(sender instanceof Player)) {
			return false;
		} else {
			from = (Player) sender;
		}
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(from.getName());

		if(commandName.equals("tpto") && tregminePlayer.isAdmin()) {
			Location loc = new Location(from.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]) );

			loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));

			if (loc.getWorld().isChunkLoaded(loc.getWorld().getChunkAt(loc))){
				from.teleport(loc);
			}
			return true;
		}

		if(commandName.equals("home") && tregminePlayer.isDonator()) {
			Home home = new Home(from.getName(), getServer());

			if (args.length == 0) {
				Location loc = home.get();
				
				if (loc == null) {
					from.sendMessage(ChatColor.RED + "Telogric lift malfunctioned. Teleportation failed.");
					return true;
				}

				loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));

				if (loc.getWorld().isChunkLoaded(loc.getWorld().getChunkAt(loc))){
					
						if (!loc.getWorld().getName().matches(from.getWorld().getName())) {
							from.sendMessage(ChatColor.RED + "You can't use a home thats in another world!");
							return true;
						}

					
					from.teleport(loc);
					from.sendMessage(ChatColor.AQUA + "Hoci poci, little gnome. Magic worked, you're in your home!");
				} else {
					from.sendMessage(ChatColor.RED + "Loading your home chunk failed, try /home again.");
				}
				
			} else if ("save".matches(args[0])) {
				
				if (from.getLocation().getWorld().getName().matches("world_the_end")) {
					from.sendMessage(ChatColor.RED + "You can't set your home in The End");
					return true;
				}
				
				if (info.tregmine.api.math.Distance.calc2d(this.getServer().getWorld("world").getSpawnLocation(), tregminePlayer.getLocation()) < 700) {
					from.sendMessage(ChatColor.RED + "Telogric lift malfunctioned. Teleportation failed, to close to spawn.");
					return true;
				}
				
				home.save(from.getLocation());
				from.sendMessage(ChatColor.AQUA + "Home saved!");
			} else if ("to".equals(args[0]) && (tregminePlayer.getMetaBoolean("mentor") || tregminePlayer.isAdmin())) {
				if (args.length < 2) {
					from.sendMessage(ChatColor.RED + "Usage: /home to <player>.");
					return true;
				}

				String playerName = args[1];
				Home playersHome = new Home(playerName, getServer());

				Location loc = playersHome.get();
				if (loc == null) {
					from.sendMessage(ChatColor.RED + "Telogric lift malfunctioned. Teleportation failed.");
					return true;
				}

				loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));

				if (loc.getWorld().isChunkLoaded(loc.getWorld().getChunkAt(loc))){
					from.teleport(loc);
					from.sendMessage(ChatColor.AQUA + "Like a drunken gnome, you fly across the world to " + playerName 
							+ "'s home. Try not to hit any birds.");
					from.sendMessage(ChatColor.RED + "Loading of home chunk failed, try /home again");
				}
			}
			return true;
		}


		if(commandName.equals("makewarp") && tregminePlayer.isAdmin()) {
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				conn = ConnectionPool.getConnection();

				stmt = conn.prepareStatement("insert into warps (name, x, y, z, yaw, pitch, world) values (?, ?, ?, ?, ?, ?, ?)");
				Location loc = tregminePlayer.getLocation();

				stmt.setString(1, args[0]);
				stmt.setDouble(2, loc.getX());
				stmt.setDouble(3, loc.getY());
				stmt.setDouble(4, loc.getZ());
				stmt.setFloat(5, loc.getYaw());
				stmt.setFloat(6, loc.getPitch());
				stmt.setString(7, loc.getWorld().getName());
				stmt.execute();

				tregminePlayer.sendMessage("Warp "+ args[0] +" created");
				this.log.info("WARPCREATE: " + args[0] + " by " + tregminePlayer.getName());
			} catch (SQLException e) {
				tregminePlayer.sendMessage("Warp creation error");
				throw new RuntimeException(e);
			} finally {
				if (stmt != null) {
					try { stmt.close(); } catch (SQLException e) {}
				}
				if (conn != null) {
					try { conn.close(); } catch (SQLException e) {}
				}
			}



			return true;
		}



		if(commandName.equals("tp") && tregminePlayer.isTrusted()) {
			try {
				List<Player> to = this.getServer().matchPlayer(args[0]);
				Tp tp = new Tp(from, to.get(0), this);
				tp.hashCode();
				return true;			
			} catch (Exception e) {
				return false;
			}
		}

		if(commandName.equals("s") && tregminePlayer.isAdmin()) {
			Player victim = this.getServer().matchPlayer(args[0]).get(0);
			if (victim != null ){

				if (victim.getName().matches("einand")) {
					from.sendMessage(ChatColor.RED + "Forbidden command.");
					victim.sendMessage(from.getName() + " tried to summon you.");
					return true;
				}
				info.tregmine.api.TregminePlayer victimPlayer = this.tregmine.tregminePlayer.get(victim.getName());

				victim.setNoDamageTicks(200);
				victim.teleport(from.getLocation());
				victim.sendMessage(tregminePlayer.getChatName() + ChatColor.AQUA + " summoned you.");
				from.sendMessage(ChatColor.AQUA + "You summoned " +  victimPlayer.getChatName() + ChatColor.AQUA + " to yourself.");
			} else {
				from.sendMessage(ChatColor.RED + "Can't find user.");
			}
			return true;
		}


		if(commandName.equals("warp")) {
			Warp warp = new Warp(this, from, args);
			warp.run();
			return true;
		}


		if (commandName.matches("spawn")) {
					from.teleport(from.getWorld().getSpawnLocation());
			return true;
		}

		return false;
	}

	@Override
	public void onLoad() {
	}
}
