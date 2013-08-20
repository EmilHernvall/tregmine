package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ILogDAO;
import info.tregmine.database.DAOException;

public class DBLogDAO implements ILogDAO
{
    private Connection conn;

    public DBLogDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public void insertLogin(TregminePlayer player, boolean logout, int onlinePlayers)
    throws DAOException
    {
        String sql = "INSERT INTO player_login (player_id, login_timestamp, " +
                     "login_action, login_country, login_city, login_ip, " +
                     "login_hostname, login_onlineplayers) ";
        sql += "VALUES (?, unix_timestamp(), ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setString(2, logout ? "logout" : "login");
            stmt.setString(3, player.getCountry());
            stmt.setString(4, player.getCity());
            stmt.setString(5, player.getIp());
            stmt.setString(6, player.getHost());
            stmt.setInt(7, onlinePlayers);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertChatMessage(TregminePlayer player, String channel,
            String message)
    throws DAOException
    {
        String sql = "INSERT INTO player_chatlog (player_id, chatlog_timestamp, " +
            "chatlog_channel, chatlog_message) ";
        sql += "VALUES (?, unix_timestamp(), ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setString(2, channel);
            stmt.setString(3, message);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertOreLog(TregminePlayer player, Location loc, int material)
    throws DAOException
    {
        String sql = "INSERT INTO player_orelog (player_id, orelog_material, " +
            "orelog_timestamp, orelog_x, orelog_y, orelog_z, orelog_world) ";
        sql += "VALUES (?, ?, unix_timestamp(), ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setInt(2, material);
            stmt.setInt(3, loc.getBlockX());
            stmt.setInt(4, loc.getBlockY());
            stmt.setInt(5, loc.getBlockZ());
            stmt.setString(6, loc.getWorld().getName());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertGiveLog(TregminePlayer sender,
                              TregminePlayer recipient,
                              ItemStack stack)
    throws DAOException
    {
        String sql = "INSERT INTO player_givelog (sender_id, recipient_id, " +
                     "givelog_material, givelog_data, givelog_meta, " +
                     "givelog_count, givelog_timestamp) ";
        sql += "VALUES (?, ?, ?, ?, ?, ?, unix_timestamp())";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sender.getId());
            stmt.setInt(2, recipient.getId());
            stmt.setInt(3, stack.getTypeId());
            stmt.setInt(4, stack.getData().getData());
            if (stack.hasItemMeta()) {
                YamlConfiguration config = new YamlConfiguration();
                config.set("meta", stack.getItemMeta());
                stmt.setString(5, config.saveToString());
            }
            else {
                stmt.setString(5, "");
            }
            stmt.setInt(6, stack.getAmount());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertWarpLog(TregminePlayer player,
                              int warpId)
    throws DAOException
    {
        String sql = "INSERT INTO warp_log (player_id, warp_id, log_timestamp) ";
        sql += "VALUES (?, ?, unix_timestamp())";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setInt(2, warpId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public Date getLastSeen(TregminePlayer player)
    throws DAOException
    {
        String sql = "SELECT * FROM player_login WHERE player_id= ? ";
        sql += "ORDER BY login_timestamp DESC LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return null;
                }

                return new Date(rs.getInt("login_timestamp") * 1000L);
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public Set<String> getAliases(TregminePlayer player)
    throws DAOException
    {
        String sql = "SELECT DISTINCT player_name FROM player " +
            "INNER JOIN player_login USING (player_id) " +
            "WHERE login_ip = ? ";

        Set<String> aliases = new HashSet<String>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, player.getIp());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    aliases.add(rs.getString("player_name"));
                }
            }
        }
        catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return aliases;
    }
}
