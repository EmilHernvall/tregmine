package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

import info.tregmine.api.TregminePlayer;

public class DBLogDAO
{
    private Connection conn;

    public DBLogDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insertLogin(TregminePlayer player, boolean logout)
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "INSERT INTO player_login (player_id, login_timestamp, "
                            + "login_action) ";
            sql += "VALUES (?, unix_timestamp(), ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player.getId());
            stmt.setString(2, logout ? "logout" : "login");
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

    public void insertChatMessage(TregminePlayer player, String channel,
            String message)
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "INSERT INTO player_chatlog (player_id, chatlog_timestamp, "
                            + "chatlog_channel, chatlog_message) ";
            sql += "VALUES (?, unix_timestamp(), ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player.getId());
            stmt.setString(2, channel);
            stmt.setString(3, message);
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

    public void insertOreLog(TregminePlayer player, Location loc, int material)
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "INSERT INTO player_orelog (player_id, orelog_material, "
                            + "orelog_timestamp, orelog_x, orelog_y, orelog_z, "
                            + "orelog_world) ";
            sql += "VALUES (?, ?, unix_timestamp(), ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player.getId());
            stmt.setInt(2, material);
            stmt.setInt(3, loc.getBlockX());
            stmt.setInt(4, loc.getBlockY());
            stmt.setInt(5, loc.getBlockZ());
            stmt.setString(6, loc.getWorld().getName());
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

    public void insertGiveLog(TregminePlayer sender,
                              TregminePlayer recipient,
                              ItemStack stack)
    {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO player_givelog (sender_id, recipient_id, " +
                         "givelog_material, givelog_data, givelog_meta, " +
                         "givelog_count, givelog_timestamp) ";
            sql += "VALUES (?, ?, ?, ?, ?, ?, unix_timestamp())";
            stmt = conn.prepareStatement(sql);
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
}
