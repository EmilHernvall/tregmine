package info.tregmine.timemachine;

import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class TimeMachine {

	public static Material Restore(String _name, Block _block) {

		Location block = _block.getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();

			stmt = conn.prepareStatement("SELECT * FROM  `stats_blocks` WHERE " +
					"`checksum` = ? AND `player` LIKE ?  LIMIT 1");
			stmt.setLong(1, checksum);
			stmt.setString(2, _name);
			stmt.execute();
			
			rs = stmt.getResultSet();
			if (rs.last()) {
				long blockid = rs.getLong("blockid");
		        boolean placed = rs.getBoolean("status");

		        if (!placed) {
					return Material.getMaterial((int)blockid);
		        }
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) {} 
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}
		
		return Material.AIR;
	}
	
	
	public static Material Do(String _name, Block _block) {

		Location block = _block.getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();

			stmt = conn.prepareStatement("SELECT * FROM  `stats_blocks` WHERE " +
					"`checksum` = ? AND `player` LIKE ? LIMIT 1");
			stmt.setLong(1, checksum);
			stmt.setString(2, _name);
			stmt.execute();
			
			rs = stmt.getResultSet();
			if (rs.last()) {
				long blockid = rs.getLong("blockid");
		        boolean placed = rs.getBoolean("status");

		        if (placed) {
					return Material.getMaterial((int) blockid);
		        }
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try { rs.close(); } catch (SQLException e) {} 
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}
		
		return Material.getMaterial((int) 0);
	}
	
	
}