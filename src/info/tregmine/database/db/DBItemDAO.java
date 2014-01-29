package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import info.tregmine.database.IItemDAO;
import info.tregmine.database.DAOException;

public class DBItemDAO implements IItemDAO
{
    private Connection conn;

    public DBItemDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public int getItemValue(int itemId, byte itemData) throws DAOException
    {
        String sql = "SELECT * FROM item WHERE item_id = ? AND item_data = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.setByte(2, itemData);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return 0;
                }

                return rs.getInt("item_value");
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
