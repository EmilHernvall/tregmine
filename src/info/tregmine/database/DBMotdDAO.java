package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

import info.tregmine.api.TregminePlayer;

public class DBMotdDAO
{
    private Connection conn;

    public DBMotdDAO(Connection conn)
    {
        this.conn = conn;
    }

    public String getMotd()
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM motd ";
            sql += "ORDER BY motd_timestamp DESC LIMIT 1";

            stmt = conn.prepareStatement(sql);
            stmt.execute();

            rs = stmt.getResultSet();
            if (rs.next()) {
                return rs.getString("motd_message");
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { }
            }
        }
    }
}
