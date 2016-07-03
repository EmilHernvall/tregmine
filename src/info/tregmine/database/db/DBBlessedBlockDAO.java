package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.IBlessedBlockDAO;
import info.tregmine.database.DAOException;

public class DBBlessedBlockDAO implements IBlessedBlockDAO
{
    private Connection conn;

    public DBBlessedBlockDAO(Connection conn)
    {
        this.conn = conn;
    }

    public static int uglyLocationHash(Location loc)
    {
        return (loc.getBlockX() + "," +
                loc.getBlockZ() + "," +
                loc.getBlockY() + "," +
                loc.getWorld().getName()).hashCode();
    }
    
    @Override
    public void delete(Location loc) throws DAOException{
    	String sql = "DELETE FROM blessedblock WHERE blessedblock_x = ? AND blessedblock_y = ? AND blessedblock_z = ?";
    	try(PreparedStatement stmt = conn.prepareStatement(sql)){
    		stmt.setInt(1, loc.getBlockX());
    		stmt.setInt(2, loc.getBlockY());
    		stmt.setInt(3, loc.getBlockZ());
    		stmt.execute();
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public int owner(Location loc) throws DAOException{
		int id = -1;
		String sql = "SELECT * FROM blessedblock WHERE blessedblock_x = ? AND blessedblock_y = ? AND blessedblock_z = ?";
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
    		stmt.setInt(1, loc.getBlockX());
    		stmt.setInt(2, loc.getBlockY());
    		stmt.setInt(3, loc.getBlockZ());
    		stmt.execute();
    		ResultSet results = stmt.getResultSet();
    		try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return -1;
                }
                return rs.getInt("player_id");
                
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
    }

    @Override
    public void insert(TregminePlayer player, Location loc)
    throws DAOException
    {
        String sql = "INSERT INTO blessedblock (player_id, blessedblock_checksum, " +
            "blessedblock_x, blessedblock_y, blessedblock_z, blessedblock_world) ";
        sql += "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setInt(2, uglyLocationHash(loc));
            stmt.setInt(3, loc.getBlockX());
            stmt.setInt(4, loc.getBlockY());
            stmt.setInt(5, loc.getBlockZ());
            stmt.setString(6, loc.getWorld().getName());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    private World getWorld(Server server, String name)
    {
        for (World world : server.getWorlds()) {
            if (name.equalsIgnoreCase(world.getName())) {
                return world;
            }
        }

        return null;
    }

    @Override
    public Map<Location, Integer> load(Server server)
    throws DAOException
    {
        String sql = "SELECT * FROM blessedblock";

        Map<Location, Integer> chests = new HashMap<Location, Integer>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    String worldName = rs.getString("blessedblock_world");
                    int x = rs.getInt("blessedblock_x");
                    int y = rs.getInt("blessedblock_y");
                    int z = rs.getInt("blessedblock_z");
                    int playerId = rs.getInt("player_id");

                    World world = getWorld(server, worldName);
                    Location loc = new Location(world, x, y, z);

                    chests.put(loc, playerId);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return chests;
    }
}
