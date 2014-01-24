package info.tregmine.database.db;

import info.tregmine.database.*;

import java.sql.*;
import java.util.*;

public class DBMiscDAO implements IMiscDAO
{
    private Connection conn;

    public DBMiscDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public List<String> loadInsults() throws DAOException
    {
        // Structure: [message_id, message_type (enum: 'INSULT','QUIT'), message_value]
        String sql = "SELECT * FROM misc_message WHERE message_type = 'INSULT'";
        List<String> insults = new ArrayList<String>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            
            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    insults.add(rs.getString("message_value"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        
        return insults;
    }

    @Override
    public List<String> loadQuitMessages() throws DAOException
    {
        // Structure: [message_id, message_type (enum: 'INSULT','QUIT'), message_value]
        String sql = "SELECT * FROM misc_message WHERE message_type = 'QUIT'";
        List<String> messages = new ArrayList<String>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            
            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    messages.add(rs.getString("message_value"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        
        return messages;
    }

}
