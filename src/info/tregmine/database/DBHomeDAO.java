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
            stmt =
                    conn.prepareStatement("INSERT INTO player_home (player_id, "
                            + "home_x, home_y, home_z, home_yaw, home_pitch, home_world, "
                            + "home_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, player.getId());
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
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public Location getHome(TregminePlayer player)
    {
        return getHome(player.getId(), player.getServer());
    }

    public Location getHome(int playerId, Server server)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt =
                    conn.prepareStatement("SELECT * FROM player_home "
                            + "WHERE player_id = ? ORDER BY home_time DESC");
            stmt.setInt(1, playerId);
            stmt.execute();

            rs = stmt.getResultSet();
            if (rs.next()) {
                double x = rs.getDouble("home_x");
                double y = rs.getDouble("home_y");
                double z = rs.getDouble("home_z");
                float pitch = rs.getFloat("home_pitch");
                float yaw = rs.getFloat("home_yaw");
                String world = rs.getString("home_world");

                return new Location(server.getWorld(world), x, y, z, yaw, pitch);
            }
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

        return null;
    }
}
