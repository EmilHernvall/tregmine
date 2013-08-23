package info.tregmine.database;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;

import info.tregmine.api.TregminePlayer;

public interface IInventoryDAO
{
    public enum InventoryType {
        BLOCK, PLAYER, PLAYER_ARMOR;
    };

    public int getInventoryId(int playerId, InventoryType type)
    throws DAOException;

    public int insertInventory(TregminePlayer player, Location loc,
            InventoryType type) throws DAOException;

    public void insertStacks(int inventoryId, ItemStack[] contents)
    throws DAOException;

    public ItemStack[] getStacks(int inventoryId, int size) throws DAOException;

    public Map<Location, Integer> loadBlessedBlocks(Server server)
    throws DAOException;
}
