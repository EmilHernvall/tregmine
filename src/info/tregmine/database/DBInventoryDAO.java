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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

import info.tregmine.api.TregminePlayer;

public class DBInventoryDAO
{
    public enum InventoryType {
        BLOCK, PLAYER, PLAYER_ARMOR;
    };

    private Connection conn;

    public DBInventoryDAO(Connection conn)
    {
        this.conn = conn;
    }

    public static int uglyLocationHash(Location loc)
    {
        int checksum =
                (loc.getBlockX() + "," + loc.getBlockZ() + ","
                        + loc.getBlockY() + "," + loc.getWorld().getName())
                        .hashCode();
        return checksum;
    }

    /*
     * public void deleteBless(int checksum, String player, String world) {
     * PreparedStatement stmt = null; try { stmt =
     * conn.prepareStatement("DELETE FROM chestbless WHERE checksum = ?");
     * stmt.setInt(1, checksum); stmt.execute(); } catch (SQLException e) {
     * throw new RuntimeException(e); } finally { if (stmt != null) { try {
     * stmt.close(); } catch (SQLException e) {} } } }
     */

    public int getInventoryId(int playerId, InventoryType type)
            throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sql = "SELECT * FROM inventory " +
                         "WHERE inventory_type = ? AND player_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, type.toString());
            stmt.setInt(2, playerId);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return -1;
            }

            return rs.getInt("inventory_id");
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

    public int insertInventory(TregminePlayer player, Location loc,
            InventoryType type) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "INSERT INTO inventory (player_id, inventory_checksum, " +
                    "inventory_x, inventory_y, inventory_z, inventory_world, " +
                    "inventory_type) ";
            sql += "VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player.getId());
            if (loc == null) {
                stmt.setInt(2, 0);
                stmt.setInt(3, 0);
                stmt.setInt(4, 0);
                stmt.setInt(5, 0);
                stmt.setString(6, "");
            }
            else {
                stmt.setInt(2, uglyLocationHash(loc));
                stmt.setInt(3, loc.getBlockX());
                stmt.setInt(4, loc.getBlockY());
                stmt.setInt(5, loc.getBlockZ());
                stmt.setString(6, loc.getWorld().getName());
            }
            stmt.setString(7, type.toString());
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            rs = stmt.getResultSet();
            if (!rs.next()) {
                throw new SQLException("Failed to get insert_id!");
            }

            return rs.getInt(1);
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

    public void insertStacks(int inventoryId, ItemStack[] contents)
            throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM inventory_item WHERE inventory_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, inventoryId);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
                stmt = null;
            }
        }

        try {
            String sql = "INSERT INTO inventory_item (inventory_id, item_slot, " +
                         "item_material, item_data, item_meta, item_count) ";
            sql += "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);

            int counter = 0;
            for (ItemStack stack : contents) {
                if (stack == null) {
                    counter++;
                    continue;
                }

                stmt.setInt(1, inventoryId);
                stmt.setInt(2, counter);
                stmt.setInt(3, stack.getTypeId());
                stmt.setInt(4, stack.getData().getData());
                if (stack.hasItemMeta()) {
                    YamlConfiguration config = new YamlConfiguration();
                    config.set("meta", stack.getItemMeta());
                    stmt.setString(5, config.saveToString());
                }
                else {
                    stmt.setString(5, "");
                }
                stmt.setInt(6, stack.getAmount());
                stmt.execute();

                counter++;
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public ItemStack[] getStacks(int inventoryId, int size) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String sql = "SELECT * FROM inventory_item " +
                         "WHERE inventory_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, inventoryId);
            stmt.execute();

            rs = stmt.getResultSet();
            ItemStack[] stacks = new ItemStack[size];
            while (rs.next()) {

                int slot = rs.getInt("item_slot");
                int materialId = rs.getInt("item_material");
                int data = rs.getInt("item_data");
                int count = rs.getInt("item_count");
                String meta = rs.getString("item_meta");

                ItemMeta metaObj = null;
                if (!"".equals(meta)) {
                    YamlConfiguration config = new YamlConfiguration();
                    config.loadFromString(meta);
                    metaObj = (ItemMeta) config.get("meta");
                }

                stacks[slot] = new ItemStack(materialId, count, (short) data);
                if (metaObj != null) {
                    stacks[slot].setItemMeta(metaObj);
                }
            }

            return stacks;
        } catch (InvalidConfigurationException e) {
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

    private World getWorld(Server server, String name)
    {
        for (World world : server.getWorlds()) {
            if (name.equalsIgnoreCase(world.getName())) {
                return world;
            }
        }

        return null;
    }

    public Map<Location, Integer> loadBlessedBlocks(Server server)
            throws SQLException
    {
        Map<Location, Integer> chests = new HashMap<Location, Integer>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM inventory " +
                         "WHERE inventory_type = 'block'";

            stmt = conn.prepareStatement(sql);
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

        return chests;
    }
}
