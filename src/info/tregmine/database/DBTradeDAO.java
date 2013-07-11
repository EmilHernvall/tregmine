package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

import info.tregmine.api.TregminePlayer;

public class DBTradeDAO
{
    private Connection conn;

    public DBTradeDAO(Connection conn)
    {
        this.conn = conn;
    }

    public int insertTrade(int srcId, int recvId, int amount)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "INSERT INTO trade (sender_id, recipient_id, "
                            + "trade_timestamp, trade_amount) ";
            sql += "VALUES (?, ?, unix_timestamp(), ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, srcId);
            stmt.setInt(2, recvId);
            stmt.setInt(3, amount);
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            rs = stmt.getResultSet();
            if (!rs.next()) {
                throw new SQLException("Failed to get insert_id!");
            }

            return rs.getInt(1);
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
    }

    public void insertStacks(int tradeId, ItemStack[] contents)
            throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "INSERT INTO trade_item (trade_id, "
                            + "item_material, item_data, item_meta, item_count) ";
            sql += "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);

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
