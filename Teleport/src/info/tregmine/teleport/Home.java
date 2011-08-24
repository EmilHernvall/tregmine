package info.tregmine.teleport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Home {
	public final info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();
	Player player;
	Server server;
	
	public Home(Player _player, Server _server) {
		this.player = _player;
		this.server = _server;
	}

	public void save(Location loc) {
       	try {
       		this.mysql.connect();
	    	PreparedStatement ps = this.mysql.connect.prepareStatement("insert into home (name, x, y, z, yaw, pitch, world, time) values (?, ?, ?, ?, ?, ?, ?, ?)");
	    	ps.setString(1, player.getName());
	    	ps.setDouble(2, loc.getX());
	    	ps.setDouble(3, loc.getY());
	    	ps.setDouble(4, loc.getZ());
	    	ps.setFloat(5, loc.getYaw());
	    	ps.setFloat(6, loc.getPitch());
	    	ps.setString(7, loc.getWorld().getName());
	    	ps.setDouble(8, System.currentTimeMillis());
	    	
	    	ps.execute();
	    	ps.close();
	    	this.mysql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Location get() {
		try {
			this.mysql.connect();
			this.mysql.statement.executeQuery("SELECT * FROM home WHERE name='" +  player.getName() + "' ORDER BY time DESC");
			ResultSet rs = this.mysql.statement.getResultSet();
			rs.first();
			double x = rs.getDouble("x");
			double y = rs.getDouble("y");
			double z = rs.getDouble("z");
			float pitch = rs.getFloat("pitch");
			float yaw = rs.getFloat("yaw");
			String world = rs.getString("world");
			Location loc =  new Location(server.getWorld(world), x,y,z, yaw, pitch);
			this.mysql.close();
			return loc;
		} catch (Exception e) {
			e.printStackTrace();
			return player.getWorld().getSpawnLocation();
		}

	}

}
