package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.Server;
//import org.bukkit.entity.Player;

import info.tregmine.api.TregminePlayer;

public class DBHomeDAO
{
    private Connection conn;

    public DBHomeDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insertHome(TregminePlayer player, Location loc)
    {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("insert into home (name, x, y, z, yaw, pitch, world, time) values (?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, player.getName());
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
        }
    }

    public Location getHome(TregminePlayer player)
    {
        return getHome(player.getName(), player.getServer());
    }

    public Location getHome(String name, Server server)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM home WHERE name = ? ORDER BY time DESC");
            stmt.setString(1, name);
            stmt.execute();

            rs = stmt.getResultSet();
            if (rs.next()) {
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                float pitch = rs.getFloat("pitch");
                float yaw = rs.getFloat("yaw");
                String world = rs.getString("world");

                return new Location(server.getWorld(world), x,y,z, yaw, pitch);
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
        }

        return null;
    }
}
