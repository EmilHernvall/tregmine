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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.FishyBlock;
import info.tregmine.database.IFishyBlockDAO;
import info.tregmine.database.DAOException;

public class DBFishyBlockDAO implements IFishyBlockDAO
{
    private Connection conn;

    public DBFishyBlockDAO(Connection conn)
    {
        this.conn = conn;
    }

    private String serializeEnchants(Map<Enchantment, Integer> enchants)
    {
        StringBuilder buffer = new StringBuilder();
        String delim = "";
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            buffer.append(delim);
            buffer.append(entry.getKey().getName());
            buffer.append("=");
            buffer.append(entry.getValue().toString());
            delim = "&";
        }

        return buffer.toString();
    }

    private Map<Enchantment, Integer> deserializeEnchants(String data)
    {
        String[] entries = data.split("&");
        Map<Enchantment, Integer> result = new HashMap<>();
        for (String entry : entries) {
            String[] kv = entry.split("=");
            if (kv.length != 2) {
                continue;
            }

            try {
                Enchantment ench = Enchantment.getByName(kv[0]);
                if (ench == null) {
                    continue;
                }
                Integer lvl = Integer.parseInt(kv[1]);
                result.put(ench, lvl);
            } catch (NumberFormatException e) { }
        }

        return result;
    }

    @Override
    public void insert(FishyBlock block) throws DAOException
    {
        String sql = "INSERT INTO fishyblock (player_id, fishyblock_created, " +
            "fishyblock_material, fishyblock_data, fishyblock_enchantments, " +
            "fishyblock_cost, fishyblock_inventory, fishyblock_world, " +
            "fishyblock_blockx, fishyblock_blocky, fishyblock_blockz, " +
            "fishyblock_signx, fishyblock_signy, fishyblock_signz, " +
            "fishyblock_storedenchants) ";
        sql += "VALUES (?, unix_timestamp(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, block.getPlayerId());
            stmt.setInt(2, block.getMaterial().getItemTypeId());
            stmt.setInt(3, block.getMaterial().getData());
            stmt.setString(4, serializeEnchants(block.getEnchantments()));
            stmt.setInt(5, block.getCost());
            stmt.setInt(6, block.getAvailableInventory());
            stmt.setString(7, block.getBlockLocation().getWorld().getName());
            stmt.setInt(8, block.getBlockLocation().getBlockX());
            stmt.setInt(9, block.getBlockLocation().getBlockY());
            stmt.setInt(10, block.getBlockLocation().getBlockZ());
            stmt.setInt(11, block.getSignLocation().getBlockX());
            stmt.setInt(12, block.getSignLocation().getBlockY());
            stmt.setInt(13, block.getSignLocation().getBlockZ());
            stmt.setString(14, block.hasStoredEnchantments() ? "1" : "0");
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    throw new DAOException("Failed to get insert_id!", sql);
                }

                block.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void update(FishyBlock block) throws DAOException
    {
        String sql = "UPDATE fishyblock SET player_id = ?, " +
            "fishyblock_material = ?, fishyblock_data = ?, " +
            "fishyblock_enchantments = ?, fishyblock_cost = ?, " +
            "fishyblock_inventory = ?, fishyblock_world = ?, " +
            "fishyblock_blockx = ?, fishyblock_blocky = ?, " +
            "fishyblock_blockz = ?, fishyblock_signx = ?, " +
            "fishyblock_signy = ?, fishyblock_signz = ?, " +
            "fishyblock_storedenchants = ? ";
        sql += "WHERE fishyblock_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, block.getPlayerId());
            stmt.setInt(2, block.getMaterial().getItemTypeId());
            stmt.setInt(3, block.getMaterial().getData());
            stmt.setString(4, serializeEnchants(block.getEnchantments()));
            stmt.setInt(5, block.getCost());
            stmt.setInt(6, block.getAvailableInventory());
            stmt.setString(7, block.getBlockLocation().getWorld().getName());
            stmt.setInt(8, block.getBlockLocation().getBlockX());
            stmt.setInt(9, block.getBlockLocation().getBlockY());
            stmt.setInt(10, block.getBlockLocation().getBlockZ());
            stmt.setInt(11, block.getSignLocation().getBlockX());
            stmt.setInt(12, block.getSignLocation().getBlockY());
            stmt.setInt(13, block.getSignLocation().getBlockZ());
            stmt.setString(14, block.hasStoredEnchantments() ? "1" : "0");
            stmt.setInt(15, block.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void delete(FishyBlock block) throws DAOException
    {
        String sql = "UPDATE fishyblock SET fishyblock_status = 'deleted' ";
        sql += "WHERE fishyblock_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, block.getId());
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
    public void insertTransaction(FishyBlock fishyBlock,
                                  TregminePlayer player,
                                  TransactionType type,
                                  int amount)
    throws DAOException
    {
        String sql = "INSERT INTO fishyblock_transaction (fishyblock_id, " +
            "player_id, transaction_type, transaction_timestamp, " +
            "transaction_amount, transaction_unitcost, transaction_totalcost) ";
        sql += "VALUES (?, ?, ?, unix_timestamp(), ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fishyBlock.getId());
            stmt.setInt(2, player.getId());
            stmt.setString(3, type.toString());
            stmt.setInt(4, amount);
            stmt.setInt(5, fishyBlock.getCost());
            stmt.setInt(6, amount * fishyBlock.getCost());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void insertCostChange(FishyBlock fishyBlock, int oldCost)
    throws DAOException
    {
        String sql = "INSERT INTO fishyblock_costlog (fishyblock_id, " +
            "costlog_timestamp, costlog_newcost, costlog_oldcost) ";
        sql += "VALUES (?, unix_timestamp(), ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fishyBlock.getId());
            stmt.setInt(2, fishyBlock.getCost());
            stmt.setInt(3, oldCost);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public Map<Location, FishyBlock> loadFishyBlocks(Server server)
    throws DAOException
    {
        String sql = "SELECT * FROM fishyblock WHERE fishyblock_status = 'active'";

        Map<Location, FishyBlock> fishyBlocks = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    int id = rs.getInt("fishyblock_id");
                    int playerId = rs.getInt("player_id");

                    int material = rs.getInt("fishyblock_material");
                    byte data = (byte)rs.getInt("fishyblock_data");
                    Map<Enchantment, Integer> enchants =
                        deserializeEnchants(rs.getString("fishyblock_enchantments"));
                    MaterialData materialData = new MaterialData(material, data);

                    int cost = rs.getInt("fishyblock_cost");
                    int inventory = rs.getInt("fishyblock_inventory");

                    String worldName = rs.getString("fishyblock_world");
                    int blockX = rs.getInt("fishyblock_blockx");
                    int blockY = rs.getInt("fishyblock_blocky");
                    int blockZ = rs.getInt("fishyblock_blockz");
                    int signX = rs.getInt("fishyblock_signx");
                    int signY = rs.getInt("fishyblock_signy");
                    int signZ = rs.getInt("fishyblock_signz");
                    boolean storedEnchants =
                        "1".equals(rs.getString("fishyblock_storedenchants"));

                    World world = getWorld(server, worldName);
                    Location blockLoc =
                        new Location(world, blockX, blockY, blockZ);
                    Location signLoc =
                        new Location(world, signX, signY, signZ);

                    FishyBlock block = new FishyBlock();
                    block.setId(id);
                    block.setPlayerId(playerId);
                    block.setMaterial(materialData);
                    block.setEnchantments(enchants);
                    block.setCost(cost);
                    block.setAvailableInventory(inventory);
                    block.setBlockLocation(blockLoc);
                    block.setSignLocation(signLoc);
                    block.setStoredEnchantments(storedEnchants);

                    fishyBlocks.put(blockLoc, block);
                    fishyBlocks.put(signLoc, block);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return fishyBlocks;
    }
}
