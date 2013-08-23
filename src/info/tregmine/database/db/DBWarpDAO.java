package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Location;

import info.tregmine.api.Warp;
import info.tregmine.database.IWarpDAO;
import info.tregmine.database.DAOException;

public class DBWarpDAO implements IWarpDAO
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

    @Override
    public void insertWarp(String name, Location loc)
    throws DAOException
    {
        String sql = "INSERT INTO warp (warp_name, warp_x, warp_y, warp_z, " +
            "warp_yaw, warp_pitch, warp_world) ";
        sql += "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setDouble(2, loc.getX());
            stmt.setDouble(3, loc.getY());
            stmt.setDouble(4, loc.getZ());
            stmt.setFloat(5, loc.getYaw());
            stmt.setFloat(6, loc.getPitch());
            stmt.setString(7, loc.getWorld().getName());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public Warp getWarp(String name, Server server)
    throws DAOException
    {
        String sql = "SELECT * FROM warp WHERE warp_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("warp_id");
                String warpName = rs.getString("warp_name");
                double x = rs.getDouble("warp_x");
                double y = rs.getDouble("warp_y");
                double z = rs.getDouble("warp_z");
                float pitch = rs.getFloat("warp_pitch");
                float yaw = rs.getFloat("warp_yaw");

                World world = getWorld(server, rs.getString("warp_world"));

                if (world == null) {
                    return null;
                }

                Location location = new Location(world, x, y, z, yaw, pitch);

                Warp warp = new Warp();
                warp.setId(id);
                warp.setName(warpName);
                warp.setLocation(location);

                return warp;
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
