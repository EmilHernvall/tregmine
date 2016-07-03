package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBlockDAO;

public class DBBlockDAO implements IBlockDAO{
	private Connection conn;
	public DBBlockDAO(Connection conn)
    {
        this.conn = conn;
    }
	@Override
	public boolean isPlaced(Block a) throws DAOException {
		Location block = a.getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();
		String sql = "SELECT * FROM stats_blocks WHERE checksum = ? AND status = '1'";
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setLong(1, checksum);
	    	stmt.execute();
	    	ResultSet rs = stmt.getResultSet();
	    	return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public int blockValue(Block a) throws DAOException {
		String sql = "SELECT * FROM item WHERE item_id = ?";
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setDouble(1, a.getTypeId());
	    	stmt.execute();
	    	ResultSet rs = stmt.getResultSet();
	    	rs.next();
	    	return rs.getInt("mine_value");
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
