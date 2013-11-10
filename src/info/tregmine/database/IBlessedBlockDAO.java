package info.tregmine.database;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;

import info.tregmine.api.TregminePlayer;

public interface IBlessedBlockDAO
{
    public void insert(TregminePlayer player, Location loc)
    throws DAOException;

    public Map<Location, Integer> load(Server server)
    throws DAOException;
}
