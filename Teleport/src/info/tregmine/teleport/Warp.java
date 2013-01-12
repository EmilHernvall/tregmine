package info.tregmine.teleport;

import info.tregmine.api.TregminePlayer;
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
            float pitch = rs.getFloat("pitch");
            float yaw = rs.getFloat("yaw");
			
			World world = getWorld(rs.getString("world"));

			if (world == null) {
				from.sendMessage("Warp not found!");
				plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  " -- not found");
				return;				
			}
			
//			if(world.getName().matches(from.getWorld().getName())) {					
				warppoint =  new Location(world, x,y,z, yaw, pitch);
				from.sendMessage(ChatColor.AQUA + "You started teleport to " + ChatColor.DARK_GREEN + args[0] + ChatColor.AQUA + " in " + ChatColor.BLUE +  world.getName() + "." );
				plugin.log.info("[warp] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
//			} else {
//				from.sendMessage(ChatColor.DARK_RED + "Sorry, that warp is in another world!");
//				warppoint = from.getLocation();		        	
//				plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
//			}

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
				long delay = 0;
				
//				if (tregminePlayer.isAdmin()) {
//					delay = 0;
//				}

				from.sendMessage(ChatColor.AQUA + "You must now stand still and wait " + delay + " seconds for the stars to align, allowing you to warp");
				final TregminePlayer tempfrom = this.plugin.tregmine.getPlayer(from);
				
				final Location temppoint = warppoint;
				
				this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin,new Runnable() {
					@Override
					public void run() {
						if (tempfrom.isAdmin()) {
							tempfrom.teleport(temppoint);
							return;
						}
						
						if (tempfrom.getWorld().getName().matches(temppoint.getWorld().getName())) {
							tempfrom.teleport(temppoint);
							return;
						} else {
							tempfrom.sendMessage(ChatColor.RED + "You can't teleport between worlds");
							return;
						} 
						
					}},20*delay);

			
			} else {
				from.sendMessage(ChatColor.RED + "Chunk failed to load. Please try to warp again");
			}

			
		}
	}
}
