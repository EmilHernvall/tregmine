package info.tregmine.teleport;

import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
//import org.bukkit.entity.Player;
import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerChatEvent;
//import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.Location;

//import com.tregdev.tregmine.Tregmine;

public class Warp {
	private final Teleport plugin;
	//	private PlayerChatEvent event;
	Player from;
	private String[] args;

	public Warp(Teleport instance, Player player, String[] args) {
		plugin = instance;
		//		this.event = event;
		this.args = args;
		plugin.getServer();
		this.from = player;

	}

	private World getWorld(String name) {

		for (World world : plugin.getServer().getWorlds())

			if (world.getName().matches(name))
			{
				return world;
			}

		return null;
	}

	public void run() {
		Location warppoint = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM warps WHERE name=?");
			stmt.setString(1, args[0]);
			stmt.execute();
			
			rs = stmt.getResultSet();
			if (!rs.next()) {
				from.sendMessage("Warp not found!");
				plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  " -- not found");
				return;
			}

			double x = rs.getDouble("x");
			double y = rs.getDouble("y");
			double z = rs.getDouble("z");
			World world = getWorld(rs.getString("world"));

			if (from.getLocation().getWorld().getName().matches("ChildsPlay")) {
				warppoint = new Location(from.getLocation().getWorld(),x,y,z);
				from.sendMessage(ChatColor.AQUA + "You teleported to " + ChatColor.DARK_GREEN + args[0] + ChatColor.AQUA + " in " + ChatColor.BLUE +  "Alpha." );
				warppoint.getWorld().loadChunk(warppoint.getWorld().getChunkAt(warppoint));

				if (warppoint.getWorld().isChunkLoaded(warppoint.getWorld().getChunkAt(warppoint))){
					from.teleport(warppoint);
					plugin.log.info("[warp] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
				} else {
					from.sendMessage(ChatColor.RED + "Chunk failed to load. Please try to warp again.");
				}
				return;
			}


			if(world.getName().matches(from.getWorld().getName())) {					
				warppoint = new Location(world,x,y,z);
				from.sendMessage(ChatColor.AQUA + "You teleported to " + ChatColor.DARK_GREEN + args[0] + ChatColor.AQUA + " in " + ChatColor.BLUE +  world.getName() + "." );
				plugin.log.info("[warp] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
			} else {
				from.sendMessage(ChatColor.DARK_RED + "Sorry, that warp is in another world!");
				warppoint = from.getLocation();		        	
				plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) {} 
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}

		if (warppoint != null) {
			from.setNoDamageTicks(200);
			
			warppoint.getWorld().loadChunk(warppoint.getWorld().getChunkAt(warppoint));

			if (warppoint.getWorld().isChunkLoaded(warppoint.getWorld().getChunkAt(warppoint))){
				from.teleport(warppoint);
			} else {
				from.sendMessage(ChatColor.RED + "Chunk failed to load. Please try to warp again");
			}

			
		}
	}

	public void wrun() {
		Location warppoint = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM warps WHERE name=?");
			stmt.setString(1, args[0]);
			stmt.execute();
			
			rs = stmt.getResultSet();
			if (!rs.next()) {
				from.sendMessage("Warp not found!");
				plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  " -- not found");
				return;
			}

			double x = rs.getDouble("x");
			double y = rs.getDouble("y");
			double z = rs.getDouble("z");
			World world = getWorld(rs.getString("world"));

			if(world.getName().matches(from.getWorld().getName())) {					
				warppoint = new Location(world,x,y,z);
				from.sendMessage(ChatColor.AQUA + "You teleported to " + ChatColor.DARK_GREEN + args[0] + ChatColor.AQUA + " in " + ChatColor.BLUE +  world.getName() + "." );
				plugin.log.info("[warp] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
			} else {
				from.sendMessage(ChatColor.DARK_RED + "Sorry, that warp is in another world!");
				warppoint = from.getLocation();		        	
				plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) {} 
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}

		if (warppoint != null) {
			//			Location spawn = new Location(this.getWorld("alpha"), -116217, 84, -234971);
			
			warppoint.getWorld().loadChunk(warppoint.getWorld().getChunkAt(warppoint));

			if (warppoint.getWorld().isChunkLoaded(warppoint.getWorld().getChunkAt(warppoint))){
				from.getWorld().setSpawnLocation(warppoint.getBlockX(), warppoint.getBlockY(), warppoint.getBlockZ());
				from.teleport(warppoint);
				from.teleport(warppoint);
				this.getWorld("alpha").setSpawnLocation(-116217, 84, -234971);
			} else {
				from.sendMessage(ChatColor.RED + "Chunk failed to load, please try to warp again");
			}

			
		}
	}

}
