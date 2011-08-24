package info.tregmine.timemachine;

import java.sql.ResultSet;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class TimeMachine {


	public static Material Restore(String _name, Block _block, info.tregmine.database.Mysql mysql){

		Location block = _block.getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();

		try {

			mysql.statement.executeQuery("SELECT * FROM  `stats_blocks` WHERE  `checksum` ="+ checksum + " AND  `player` LIKE '"+_name+"'  LIMIT 1");
			ResultSet rs = mysql.statement.getResultSet();
			rs.last();
			long blockid = rs.getLong("blockid");
	        boolean placed = rs.getBoolean("status");
	        rs.close();
	        
	        if (placed == false) {
				return Material.getMaterial((int) blockid);
	        } else {
	        	return Material.AIR;
	        }

			
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Material Do(String _name, Block _block, info.tregmine.database.Mysql mysql){

		Location block = _block.getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();

		try {

			mysql.statement.executeQuery("SELECT * FROM  `stats_blocks` WHERE  `checksum` ="+ checksum + " AND  `player` LIKE '"+_name+"'  LIMIT 1");
			ResultSet rs = mysql.statement.getResultSet();
			rs.last();
			long blockid = rs.getLong("blockid");
	        boolean placed = rs.getBoolean("status");
	        rs.close();
	        
	        if (placed == true) {
				return Material.getMaterial((int) blockid);
	        } else {
				return Material.getMaterial((int) 0);
	        }

			
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}
	
	
}