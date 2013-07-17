package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Location;

public class DBItemDAO
{
    private Connection conn;

    public DBItemDAO(Connection conn)
    {
        this.conn = conn;
    }

    public int getItemValue(int itemId)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM item WHERE item_id = ?");
            stmt.setInt(1, itemId);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return 0;
            }

            return rs.getInt("item_value");
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
}
