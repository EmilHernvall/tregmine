package info.tregmine.database.db;

import org.bukkit.ChatColor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.IWalletDAO;
import info.tregmine.database.DAOException;

public class DBWalletDAO implements IWalletDAO
{
    private Connection conn;

    public DBWalletDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public long balance(TregminePlayer player)
    throws DAOException
    {
        String sql = "SELECT player_wallet FROM player WHERE player_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return -1;
                }

                return rs.getLong("player_wallet");
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public String formattedBalance(TregminePlayer player)
    throws DAOException
    {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return ChatColor.GOLD + nf.format(balance(player)) + ChatColor.WHITE
                + " Tregs";
    }

    @Override
    public boolean add(TregminePlayer player, long amount)
    throws DAOException
    {
        String sql = "UPDATE player SET player_wallet = player_wallet + ? " +
            "WHERE player_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, amount);
            stmt.setInt(2, player.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return true;
    }

    @Override
    public boolean take(TregminePlayer player, long amount)
    throws DAOException
    {
        String sql = "UPDATE player SET player_wallet = player_wallet - ? " +
            "WHERE player_id = ?";

        long newBalance = balance(player) - amount;
        if (newBalance < 0) {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, amount);
            stmt.setInt(2, player.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return true;
    }

    @Override
    public void insertTransaction(int srcId, int recvId, int amount)
    throws DAOException
    {
        String sql = "INSERT INTO player_transaction (sender_id, recipient_id, " +
            "transaction_timestamp, transaction_amount) ";
        sql += "VALUES (?, ?, unix_timestamp(), ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, srcId);
            stmt.setInt(2, recvId);
            stmt.setInt(3, amount);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
