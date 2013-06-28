package info.tregmine.database;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

//import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class DBWalletDAO
{
    private Connection conn;

    public DBWalletDAO(Connection conn)
    {
        this.conn = conn;
    }

    public long balance(String player)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();

            stmt = conn.prepareStatement("SELECT w.`value` FROM wallet w WHERE w.player = ?");
            stmt.setString(1, player);
            stmt.execute();

            rs = stmt.getResultSet();
            if (rs.next()) {
                return rs.getLong("value");
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

        return -1;
    }

    public String formattedBalance(String player)
    {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return ChatColor.GOLD + nf.format(balance(player)) +
               ChatColor.WHITE + " Tregs";
    }

    public boolean exists(String player)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM wallet w WHERE w.player = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player);
            stmt.execute();

            rs = stmt.getResultSet();
            return rs.next();
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
    }

    public void create(String player)
    {
        if (exists(player)) {
            return;
        }

        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO wallet (player, value) VALUES (?,'10000')";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }

    public boolean add(String player, long amount)
    {
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE wallet SET value = value + ? WHERE player = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, amount);
            stmt.setString(2, player);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }

        return true;
    }

    public boolean take(String player, long amount)
    {
        long newBalance = balance(player) - amount;
        if (newBalance < 0) {
            return false;
        }

        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE wallet SET value = value - ? WHERE player = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, amount);
            stmt.setString(2, player);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }

        return true;
    }

    public int getBlockValue(int block)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT i.`value` FROM items_destroyvalue i WHERE i.itemid = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, block);
            stmt.execute();

            rs = stmt.getResultSet();
            if (rs.next()) {
                return rs.getInt("value");
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

        return 0;
    }
}
