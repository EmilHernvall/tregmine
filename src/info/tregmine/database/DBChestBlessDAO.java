package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

import info.tregmine.database.ConnectionPool;
import info.tregmine.api.TregminePlayer;
import info.tregmine.Tregmine;

public class DBChestBlessDAO
{
    private Connection conn;

    public DBChestBlessDAO(Connection conn)
    {
        this.conn = conn;
    }

    public static int uglyLocationHash(Location loc)
    {
        int checksum = (loc.getBlockX() + "," +
                        loc.getBlockZ() + "," +
                        loc.getBlockY() + "," +
                        loc.getWorld().getName()).hashCode();
        return checksum;
    }

    /*public void deleteBless(int checksum, String player, String world)
    {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("DELETE FROM chestbless WHERE checksum = ?");
            stmt.setInt(1, checksum);
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }*/

    public void saveBless(TregminePlayer player, Location loc)
    {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO inventory (player_id, inventory_checksum, " +
                         "inventory_x, inventory_y, inventory_z, inventory_world)";
            sql += "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player.getId());
            stmt.setInt(2, uglyLocationHash(loc));
            stmt.setInt(3, loc.getBlockX());
            stmt.setInt(4, loc.getBlockY());
            stmt.setInt(5, loc.getBlockZ());
            stmt.setString(6, loc.getWorld().getName());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }

    private World getWorld(Server server, String name)
    {
        for (World world : server.getWorlds()) {
            System.out.println("cmp: " + name + " " + world.getName());
            if (name.equalsIgnoreCase(world.getName())) {
                return world;
            }
        }

        return null;
    }

    public Map<Location, Integer> loadBlessedChests(Server server)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Map<Location, Integer> chests = new HashMap<Location, Integer>();

            stmt = conn.prepareStatement("SELECT * FROM inventory");
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                String worldName = rs.getString("inventory_world");
                int x = rs.getInt("inventory_x");
                int y = rs.getInt("inventory_y");
                int z = rs.getInt("inventory_z");
                int playerId = rs.getInt("player_id");

                World world = getWorld(server, worldName);
                Location loc = new Location(world, x, y, z);

                chests.put(loc, playerId);
            }

            return chests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) {}
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }
}
