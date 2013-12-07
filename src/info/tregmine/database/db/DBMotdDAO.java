package info.tregmine.database.db;

import info.tregmine.database.DAOException;
import info.tregmine.database.IMotdDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBMotdDAO implements IMotdDAO
{
    private Connection conn;

    public DBMotdDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public String getMotd()
    throws DAOException
    {
        String sql = "SELECT * FROM motd ";
        sql += "ORDER BY motd_timestamp DESC LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return null;
                }

                return rs.getString("motd_message");
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public String getUpdates(String version) throws DAOException
    {
        String sql = "SELECT * FROM version " +
                "WHERE version_number = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, version);
            stmt.execute();
            
            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return null;
                }

                return rs.getString("version_string");
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
