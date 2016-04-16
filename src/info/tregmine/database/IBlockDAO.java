package info.tregmine.database;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import info.tregmine.api.TregminePlayer;

public interface IBlockDAO {
	public boolean isPlaced(Block a) throws DAOException;
	int blockValue(Block a) throws DAOException;
}
