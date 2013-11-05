package info.tregmine.database.db;

import info.tregmine.database.DAOException;
import info.tregmine.database.IEnchantmentDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBEnchantmentDAO implements IEnchantmentDAO
{
    private Connection conn;

    public DBEnchantmentDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public String localize(String enchantmentName)
    throws DAOException
    {
        String sql = "SELECT enchantment_title FROM enchantment " +
                "WHERE enchantment_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, enchantmentName);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    // If can't find the localized name - Return the original
                    // enchantment name
                    return enchantmentName;
                }
                return rs.getString("enchantment_title");
            }

        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
