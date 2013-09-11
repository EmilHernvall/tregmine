package info.tregmine.database;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.FishyBlock;

public interface IFishyBlockDAO
{
    public void insert(FishyBlock fishyBlock) throws DAOException;

    public void update(FishyBlock fishyBlock) throws DAOException;

    public void delete(FishyBlock fishyBlock) throws DAOException;

    public Map<Location, FishyBlock> loadFishyBlocks(Server server)
    throws DAOException;
}
