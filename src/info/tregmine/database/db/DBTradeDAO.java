package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ITradeDAO;
import info.tregmine.database.DAOException;

public class DBTradeDAO implements ITradeDAO
{
    private Connection conn;

    public DBTradeDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public int insertTrade(int srcId, int recvId, int amount)
    throws DAOException
    {
        String sql = "INSERT INTO trade (sender_id, recipient_id, " +
            "trade_timestamp, trade_amount) ";
        sql += "VALUES (?, ?, unix_timestamp(), ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, srcId);
            stmt.setInt(2, recvId);
            stmt.setInt(3, amount);
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    throw new DAOException("Failed to get insert_id!", sql);
                }

                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertStacks(int tradeId, ItemStack[] contents)
            throws DAOException
    {
        String sql = "INSERT INTO trade_item (trade_id, item_material, " +
            "item_data, item_meta, item_count) ";
        sql += "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ItemStack stack : contents) {
                if (stack == null) {
                    continue;
                }

                stmt.setInt(1, tradeId);
                stmt.setInt(2, stack.getTypeId());
                stmt.setInt(3, stack.getData().getData());
                if (stack.hasItemMeta()) {
                    YamlConfiguration config = new YamlConfiguration();
                    config.set("meta", stack.getItemMeta());
                    stmt.setString(4, config.saveToString());
                }
                else {
                    stmt.setString(4, "");
                }
                stmt.setInt(5, stack.getAmount());
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public int getAmountofTrades(int id)
    throws DAOException
    {
        String sql = "SELECT * FROM trade ";
        sql += "WHERE sender_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                int amount = 0;
                while(rs.next()) {
                    amount++;
                }
                return amount;
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

}
