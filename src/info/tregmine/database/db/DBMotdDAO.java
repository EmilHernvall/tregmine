package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.IMotdDAO;
import info.tregmine.database.DAOException;

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
}
