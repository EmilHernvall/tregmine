package info.tregmine.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import info.tregmine.Tregmine;

public class BlockStats implements Listener{
	private Tregmine plugin;
	public BlockStats(Tregmine instance){
		plugin = instance;
	}
	
	public void onBlockPlace(BlockPlaceEvent event){
		Location block = event.getBlock().getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		try {
//			conn = ConnectionPool.getConnection();
//			
//	    	stmt = conn.prepareStatement("insert into stats_blocks (checksum, player, x, y, z, " +
//	    			"time, status, blockid, world) values (?,?,?,?,?,?,?,?,?)"); 
//	    	stmt.setLong(1, checksum);
//	    	stmt.setString(2, event.getPlayer().getName());
//	    	stmt.setDouble(3, block.getX());
//	    	stmt.setDouble(4, block.getY());
//	    	stmt.setDouble(5, block.getZ());
//	    	stmt.setDouble(6, System.currentTimeMillis());
//	    	stmt.setBoolean(7, true);
//	    	stmt.setDouble(8, event.getBlock().getTypeId());
//	    	stmt.setString(9, event.getPlayer().getWorld().getName());
//	    	stmt.execute();
//	    	stmt.close();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		} finally {
//			if (stmt != null) {
//				try { stmt.close(); } catch (SQLException e) {}
//			}
//			if (conn != null) {
//				try { conn.close(); } catch (SQLException e) {}
//			}
//		}
	}
}
