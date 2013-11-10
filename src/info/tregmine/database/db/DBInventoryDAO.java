package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.InventoryAccess;
import info.tregmine.database.IInventoryDAO;
import info.tregmine.database.DAOException;

public class DBInventoryDAO implements IInventoryDAO
{
    private Connection conn;

    public DBInventoryDAO(Connection conn)
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
    public int getInventoryId(int playerId, InventoryType type)
    throws DAOException
    {
        String sql = "SELECT * FROM inventory " +
                     "WHERE inventory_type = ? AND player_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.toString());
            stmt.setInt(2, playerId);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return -1;
                }

                return rs.getInt("inventory_id");
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public int getInventoryId(Location loc)
    throws DAOException
    {
        String sql = "SELECT * FROM inventory " +
                     "WHERE inventory_x = ? " +
                     "AND inventory_y = ? " +
                     "AND inventory_z = ? " +
                     "AND inventory_world = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loc.getBlockX());
            stmt.setInt(2, loc.getBlockY());
            stmt.setInt(3, loc.getBlockZ());
            stmt.setString(4, loc.getWorld().getName());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return -1;
                }

                return rs.getInt("inventory_id");
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public int insertInventory(TregminePlayer player, Location loc,
            InventoryType type) throws DAOException
    {
        String sql = "INSERT INTO inventory (player_id, inventory_checksum, " +
            "inventory_x, inventory_y, inventory_z, inventory_world, " +
            "inventory_type) ";
        sql += "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    throw new DAOException("Failed to get insert_id!", sql);
                }

                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertAccessLog(TregminePlayer player, int inventoryId)
    throws DAOException
    {
        String sql = "INSERT INTO inventory_accesslog (inventory_id, " +
            "player_id, accesslog_timestamp) ";
        sql += "VALUES (?, ?, unix_timestamp())";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventoryId);
            stmt.setInt(2, player.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertChangeLog(TregminePlayer player,
                                int inventoryId,
                                int slot,
                                ItemStack slotContent,
                                IInventoryDAO.ChangeType type)
    throws DAOException
    {
        String sql = "INSERT INTO inventory_changelog (inventory_id, " +
            "player_id, changelog_timestamp, changelog_slot, changelog_material, " +
            "changelog_data, changelog_meta, changelog_amount, changelog_type) ";
        sql += "VALUES (?, ?, unix_timestamp(), ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventoryId);
            stmt.setInt(2, player.getId());
            stmt.setInt(3, slot);
            stmt.setInt(4, slotContent.getTypeId());
            stmt.setInt(5, slotContent.getData().getData());
            if (slotContent.hasItemMeta()) {
                YamlConfiguration config = new YamlConfiguration();
                config.set("meta", slotContent.getItemMeta());
                stmt.setString(6, config.saveToString());
            }
            else {
                stmt.setString(6, "");
            }
            stmt.setInt(7, slotContent.getAmount());
            stmt.setString(8, type.toString());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertStacks(int inventoryId, ItemStack[] contents)
            throws DAOException
    {
        String sqlDelete = "DELETE FROM inventory_item WHERE inventory_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
            stmt.setInt(1, inventoryId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sqlDelete, e);
        }

        String sqlInsert = "INSERT INTO inventory_item (inventory_id, item_slot, " +
            "item_material, item_data, item_meta, item_count) ";
        sqlInsert += "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
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
        } catch (SQLException e) {
            throw new DAOException(sqlInsert, e);
        }
    }

    @Override
    public ItemStack[] getStacks(int inventoryId, int size) throws DAOException
    {
        String sql = "SELECT * FROM inventory_item " +
                     "WHERE inventory_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventoryId);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
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
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<InventoryAccess> getAccessLog(int inventoryId, int count)
    throws DAOException
    {
        String sql = "SELECT * FROM inventory_accesslog " +
                     "WHERE inventory_id = ? " +
                     "ORDER BY accesslog_timestamp DESC LIMIT " + count;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventoryId);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                List<InventoryAccess> accessLog = new ArrayList<>();
                while (rs.next()) {
                    InventoryAccess access = new InventoryAccess();
                    access.setInventoryId(inventoryId);
                    access.setPlayerId(rs.getInt("player_id"));
                    access.setTimestamp(new Date(rs.getInt("accesslog_timestamp")*1000l));

                    accessLog.add(access);
                }

                return accessLog;
            }
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
}
