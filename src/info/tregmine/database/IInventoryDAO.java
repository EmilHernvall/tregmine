package info.tregmine.database;

import info.tregmine.api.InventoryAccess;
import info.tregmine.api.TregminePlayer;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface IInventoryDAO
{
    public enum InventoryType
    {
        BLOCK,
        PLAYER,
        PLAYER_ARMOR;
    };

    public enum ChangeType
    {
        ADD,
        REMOVE;
    };

    public int getInventoryId(int playerId,
                              InventoryType type) throws DAOException;

    public int getInventoryId(Location loc) throws DAOException;

    public int insertInventory(TregminePlayer player,
                               Location loc,
                               InventoryType type) throws DAOException;

    public void insertAccessLog(TregminePlayer player,
                                int inventoryId) throws DAOException;

    public void insertChangeLog(TregminePlayer player,
                                int inventoryId,
                                int slot,
                                ItemStack slotContent,
                                ChangeType type) throws DAOException;

    public void insertStacks(int inventoryId,
                             ItemStack[] contents) throws DAOException;

    public ItemStack[] getStacks(int inventoryId,
                                 int size) throws DAOException;

    public List<InventoryAccess> getAccessLog(int inventoryId,
                                              int count) throws DAOException;
    
    
    public void saveInventory(TregminePlayer player,
                                int inventoryID,
                                String type) throws DAOException;
    
    public void loadInventory(TregminePlayer player,
                                int inventoryID,
                                String type) throws DAOException;
    
    public int fetchInventory(TregminePlayer player,
                                String inventoryName,
                                String type) throws DAOException;
    
    public void createInventory(TregminePlayer player,
                                String inventoryName,
                                String type) throws DAOException;
    
}
