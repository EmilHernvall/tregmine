package info.tregmine.stats;

import info.tregmine.Tregmine;
import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Date;
//import java.util.TimeZone;

//import org.bukkit.ChatColor;
import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.Sign;
//import org.bukkit.event.block.BlockDamageEvent;
//import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.event.block.BlockRightClickEvent;


public class BlockStats {
    private final Tregmine plugin;
    
    public BlockStats(Tregmine instance) {
        plugin = instance;
        plugin.getServer();
    }

    /*    		
	1	checksum		doubble		 	 	 	 	 	 	
	2	player			varchar(46)		 	 	 	 	 	 	 
	3	x		 	 	doubble 	 	 	 	
	4	y		 	 	doubble 	 	 	 	
	5	z		 	 	doubble 	 	 	 	
	6	time		 	doubble 	 	 	 	 	
	7	status			boolean		 	 	 	 	 	 	
	8	blockid			doubble
	9	world			varchar(16)
*/
    
    
    public void onBlockPlace (BlockPlaceEvent event)	{
    		Location block = event.getBlock().getLocation();
    		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
    		String pos = block.getX() + "," + block.getY() + "," + block.getZ(); //$NON-NLS-1$ //$NON-NLS-2$
    		crc32.update(pos.getBytes());
    		long checksum = crc32.getValue();

    		Connection conn = null;
    		PreparedStatement stmt = null;
    		try {
    			conn = ConnectionPool.getConnection();
    			
		    	stmt = conn.prepareStatement("insert into stats_blocks (checksum, player, x, y, z, " +
		    			"time, status, blockid, world) values (?,?,?,?,?,?,?,?,?)"); 
		    	stmt.setLong(1, checksum);
		    	stmt.setString(2, event.getPlayer().getName());
		    	stmt.setDouble(3, block.getX());
		    	stmt.setDouble(4, block.getY());
		    	stmt.setDouble(5, block.getZ());
		    	stmt.setDouble(6, System.currentTimeMillis());
		    	stmt.setBoolean(7, true);
		    	stmt.setDouble(8, event.getBlock().getTypeId());
		    	stmt.setString(9, event.getPlayer().getWorld().getName());
		    	stmt.execute();
		    	stmt.close();
    		} catch (SQLException e) {
    			throw new RuntimeException(e);
    		} finally {
    			if (stmt != null) {
    				try { stmt.close(); } catch (SQLException e) {}
    			}
    			if (conn != null) {
    				try { conn.close(); } catch (SQLException e) {}
    			}
    		}
    }
    
    public void onBlockBreak (BlockBreakEvent event) {	

		Location block = event.getBlock().getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();  //$NON-NLS-1$//$NON-NLS-2$
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();
	    			
	    	stmt = conn.prepareStatement("insert into stats_blocks (checksum, player, x, y, " +
	    			"z, time, status, blockid) values (?,?,?,?,?,?,?,?)");
	    	stmt.setLong(1, checksum);
	    	stmt.setString(2, event.getPlayer().getName());
	    	stmt.setDouble(3, block.getX());
	    	stmt.setDouble(4, block.getY());
	    	stmt.setDouble(5, block.getZ());
	    	stmt.setDouble(6, System.currentTimeMillis());
	    	stmt.setBoolean(7, false);
	    	stmt.setDouble(8, event.getBlock().getTypeId());
	    	stmt.execute();
	    	stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}
    }
    
    
    public boolean isPlaced (org.bukkit.block.Block inBlock) {
		Location block = inBlock.getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
		String world = block.getWorld().getName();
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();
    	
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();
			
			String sql = "SELECT * FROM stats_blocks WHERE checksum = ? and status = '1'";
			
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, checksum);
//			stmt.setString(2, world);
			stmt.execute();
			
			rs = stmt.getResultSet();
			return rs.next();
			
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
    }
}
