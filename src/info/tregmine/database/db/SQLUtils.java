package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils
{
    public static void close(ResultSet obj)
    {
        if (obj != null) {
            try { obj.close(); } catch (SQLException e) {}
        }
    }

    public static void close(Statement obj)
    {
        if (obj != null) {
            try { obj.close(); } catch (SQLException e) {}
        }
    }

    public static void close(PreparedStatement obj)
    {
        if (obj != null) {
            try { obj.close(); } catch (SQLException e) {}
        }
    }

    public static void close(Connection obj)
    {
        if (obj != null) {
            try { obj.close(); } catch (SQLException e) {}
        }
    }
}
