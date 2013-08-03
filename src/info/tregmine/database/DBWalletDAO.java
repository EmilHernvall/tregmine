package info.tregmine.database;

import org.bukkit.ChatColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;

public class DBWalletDAO
{
    private Connection conn;

    public DBWalletDAO(Connection conn)
    {
        this.conn = conn;
    }

    public long balance(TregminePlayer player)
    throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT player_wallet FROM player "
                            + "WHERE player_id = ?");
            stmt.setInt(1, player.getId());
            stmt.execute();

            rs = stmt.getResultSet();
            if (rs.next()) {
                return rs.getLong("player_wallet");
            }
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

        return -1;
    }

    public String formattedBalance(TregminePlayer player)
    throws SQLException
    {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return ChatColor.GOLD + nf.format(balance(player)) + ChatColor.WHITE
                + " Tregs";
    }

    public boolean add(TregminePlayer player, long amount)
    throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE player SET player_wallet = player_wallet + ? "
                            + "WHERE player_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, amount);
            stmt.setInt(2, player.getId());
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        return true;
    }

    public boolean take(TregminePlayer player, long amount)
    throws SQLException
    {
        long newBalance = balance(player) - amount;
        if (newBalance < 0) {
            return false;
        }

        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE player SET player_wallet = player_wallet - ? "
                            + "WHERE player_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, amount);
            stmt.setInt(2, player.getId());
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        return true;
    }

    public void insertTransaction(int srcId, int recvId, int amount)
    throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO player_transaction (sender_id, recipient_id, "
                            + "transaction_timestamp, transaction_amount) ";
            sql += "VALUES (?, ?, unix_timestamp(), ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, srcId);
            stmt.setInt(2, recvId);
            stmt.setInt(3, amount);
            stmt.execute();
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
