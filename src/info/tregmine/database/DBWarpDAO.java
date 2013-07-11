package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Location;

public class DBWarpDAO
{
    private Connection conn;

    public DBWarpDAO(Connection conn)
    {
        this.conn = conn;
    }

    private World getWorld(Server server, String name)
    {
        for (World world : server.getWorlds()) {
            if (name.matches(world.getName())) {
                return world;
            }
        }

        return null;
    }

    public void insertWarp(String name, Location loc)
    {
        PreparedStatement stmt = null;
        try {
            stmt =
                    conn.prepareStatement("insert into warps (name, x, y, z, yaw, pitch, world) values (?, ?, ?, ?, ?, ?, ?)");

            stmt.setString(1, name);
            stmt.setDouble(2, loc.getX());
            stmt.setDouble(3, loc.getY());
            stmt.setDouble(4, loc.getZ());
            stmt.setFloat(5, loc.getYaw());
            stmt.setFloat(6, loc.getPitch());
            stmt.setString(7, loc.getWorld().getName());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public Location getWarp(String name, Server server)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM warps WHERE name=?");
            stmt.setString(1, name);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return null;
            }

            double x = rs.getDouble("x");
            double y = rs.getDouble("y");
            double z = rs.getDouble("z");
            float pitch = rs.getFloat("pitch");
            float yaw = rs.getFloat("yaw");

            World world = getWorld(server, rs.getString("world"));

            if (world == null) {
                return null;
            }

            return new Location(world, x, y, z, yaw, pitch);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
