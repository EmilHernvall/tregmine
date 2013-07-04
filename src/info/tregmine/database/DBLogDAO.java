package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;

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
            String sql = "INSERT INTO player_login (player_id, login_timestamp, " +
                         "login_action) ";
            sql += "VALUES (?, unix_timestamp(), ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player.getId());
            stmt.setString(2, logout ? "logout" : "login");
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }

    public void insertChatMessage(TregminePlayer player,
                                  String channel,
                                  String message)
    {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO player_chatlog (player_id, chatlog_timestamp, " +
                         "chatlog_channel, chatlog_message) ";
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
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }
}
