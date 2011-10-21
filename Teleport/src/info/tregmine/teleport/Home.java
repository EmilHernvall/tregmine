package info.tregmine.teleport;

import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.Server;
//import org.bukkit.entity.Player;

public class Home {
	String player;
	Server server;
	
	public Home(String _player, Server _server) {
		this.player = _player;
		this.server = _server;
	}

	public void save(Location loc) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();
			
	    	stmt = conn.prepareStatement("insert into home (name, x, y, z, yaw, pitch, world, time) values (?, ?, ?, ?, ?, ?, ?, ?)");
	    	stmt.setString(1, player);
	    	stmt.setDouble(2, loc.getX());
	    	stmt.setDouble(3, loc.getY());
	    	stmt.setDouble(4, loc.getZ());
	    	stmt.setFloat(5, loc.getYaw());
	    	stmt.setFloat(6, loc.getPitch());
	    	stmt.setString(7, loc.getWorld().getName());
	    	stmt.setDouble(8, System.currentTimeMillis());
	    	stmt.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}
	}

	public Location get() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();
			
			stmt = conn.prepareStatement("SELECT * FROM home WHERE name = ? ORDER BY time DESC");
			stmt.setString(1, player);
			stmt.execute();
			
			rs = stmt.getResultSet();
			if (rs.next()) {
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                float pitch = rs.getFloat("pitch");
                float yaw = rs.getFloat("yaw");
                String world = rs.getString("world");
                Location loc =  new Location(server.getWorld(world), x,y,z, yaw, pitch);
                return loc;
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
		return null;
	}
}
