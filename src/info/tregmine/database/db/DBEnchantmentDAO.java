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
        String sql = "SELECT enchantment.new FROM enchantment " +
                "WHERE old = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, enchantmentName);
            stmt.execute();
            
            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return enchantmentName; // If can't find the localized name - Return the original enchantment name
                }
                return rs.getString(1);
            }
            
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
