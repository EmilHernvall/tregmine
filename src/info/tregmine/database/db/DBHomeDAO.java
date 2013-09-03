package info.tregmine.database.db;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.Server;
//import org.bukkit.entity.Player;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.IHomeDAO;
import info.tregmine.database.DAOException;

public class DBHomeDAO implements IHomeDAO
{
    private Connection conn;

    public DBHomeDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public void insertHome(TregminePlayer player, String name, Location loc)
    throws DAOException
    {
        String sql = "INSERT INTO player_home (player_id, home_name, " +
            "home_x, home_y, home_z, home_yaw, home_pitch, home_world, " +
            "home_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setString(2, name);
            stmt.setDouble(3, loc.getX());
            stmt.setDouble(4, loc.getY());
            stmt.setDouble(5, loc.getZ());
            stmt.setFloat(6, loc.getYaw());
            stmt.setFloat(7, loc.getPitch());
            stmt.setString(8, loc.getWorld().getName());
            stmt.setDouble(9, System.currentTimeMillis());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public Location getHome(TregminePlayer player)
    throws DAOException
    {
        return getHome(player.getId(), null, player.getServer());
    }

    @Override
    public Location getHome(TregminePlayer player, String name)
    throws DAOException
    {
        return getHome(player.getId(), name, player.getServer());
    }

    @Override
    public Location getHome(int playerId, String name, Server server)
    throws DAOException
    {
        String sql = "SELECT * FROM player_home " +
            "WHERE player_id = ? AND home_name = ? ORDER BY home_time DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            stmt.setString(2, name);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return null;
                }

                double x = rs.getDouble("home_x");
                double y = rs.getDouble("home_y");
                double z = rs.getDouble("home_z");
                float pitch = rs.getFloat("home_pitch");
                float yaw = rs.getFloat("home_yaw");
                String world = rs.getString("home_world");

                return new Location(server.getWorld(world), x, y, z, yaw, pitch);
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public List<String> getHomeNames(int playerId)
    throws DAOException
    {
        String sql = "SELECT * FROM player_home " +
            "WHERE player_id = ? AND NOT home_name IS NULL " +
            "GROUP BY home_name ORDER BY max(home_time) DESC LIMIT 20";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                List<String> names = new ArrayList<String>();
                while (rs.next()) {
                    String name = rs.getString("home_name");
                    names.add(name);
                }

                return names;
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void deleteHome(int playerId, String name)
    throws DAOException
    {
        String sql = "DELETE FROM player_home " +
            "WHERE player_id = ? AND home_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            stmt.setString(2, name);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
