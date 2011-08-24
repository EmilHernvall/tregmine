package info.tregmine.stats;

import info.tregmine.Tregmine;

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
    
	public final info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();

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

       	try {
       		this.mysql.connect();
	    	PreparedStatement ps = this.mysql.connect.prepareStatement("insert into stats_blocks (checksum, player, x, y, z, time, status, blockid, world) values (?,?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
	    	ps.setLong(1, checksum);
	    	ps.setString(2, event.getPlayer().getName());
	    	ps.setDouble(3, block.getX());
	    	ps.setDouble(4, block.getY());
	    	ps.setDouble(5, block.getZ());
	    	ps.setDouble(6, System.currentTimeMillis());
	    	ps.setBoolean(7, true);
	    	ps.setDouble(8, event.getBlock().getTypeId());
	    	ps.setString(9, event.getPlayer().getWorld().getName());
	    	ps.execute();
	    	ps.close();
	    	this.mysql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
public void onBlockBreak (BlockBreakEvent event) {	

	    		Location block = event.getBlock().getLocation();
	    		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
	    		String pos = block.getX() + "," + block.getY() + "," + block.getZ();  //$NON-NLS-1$//$NON-NLS-2$
	    		crc32.update(pos.getBytes());
	    		long checksum = crc32.getValue();
	
	       	try {
	       		this.mysql.connect();
		    	PreparedStatement ps = this.mysql.connect.prepareStatement("insert into stats_blocks (checksum, player, x, y, z, time, status, blockid) values (?,?,?,?,?,?,?,?)"); //$NON-NLS-1$
		    	ps.setLong(1, checksum);
		    	ps.setString(2, event.getPlayer().getName());
		    	ps.setDouble(3, block.getX());
		    	ps.setDouble(4, block.getY());
		    	ps.setDouble(5, block.getZ());
		    	ps.setDouble(6, System.currentTimeMillis());
		    	ps.setBoolean(7, false);
		    	ps.setDouble(8, event.getBlock().getTypeId());
		    	ps.execute();
		    	ps.close();
	       		this.mysql.close();		    	
			} catch (SQLException e) {
				e.printStackTrace();
			}
//    	}
    }
    
    
    public boolean isPlaced (org.bukkit.block.Block inBlock) {
		Location block = inBlock.getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
		crc32.update(pos.getBytes());
		long checksum = crc32.getValue();
    	
		String SQL = "SELECT COUNT(*) as count FROM stats_blocks WHERE checksum = '" + checksum + "' and status = '1'";
		this.mysql.connect();
		try {
			this.mysql.statement.executeQuery(SQL);
			ResultSet rs = this.mysql.statement.getResultSet();
			rs.first();
			if (rs.getInt("count") > 0) {
				this.mysql.close();
				return true;
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
		this.mysql.close();
		return false;
    }
    
    
    /*
    public void onBlockRightClick (BlockRightClickEvent event) {
    	if (event.getPlayer().getItemInHand().getTypeId() == Material.PAPER.getId()) {
    		Location block = event.getBlock().getLocation();
    		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
    		String pos = block.getX() + "," + block.getY() + "," + block.getZ();
    		crc32.update(pos.getBytes());
    		long checksum = crc32.getValue();
    		String timezone = plugin.playerSetting.get(event.getPlayer().getName()).getTimezone();
    		
    	    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
    		dfm.setTimeZone(TimeZone.getTimeZone(timezone));
    		
    		if (this.mysql.connect != null) {
    		    try {
    	   			this.mysql.connect();
    		    	this.mysql.statement.executeQuery("SELECT * FROM  stats_blocks WHERE checksum='" +  checksum + "' ORDER BY time");
    		    	ResultSet rs = this.mysql.statement.getResultSet();
    		    	while (rs.next()) {
    			        Date date = new Date(rs.getLong("time"));
    			        long blockid = rs.getLong("blockid");
    			        String player =  rs.getString("player");
    			        boolean placed = rs.getBoolean("status");
    			        Material mat = Material.getMaterial((int) blockid);
    			        
    			        if (placed == true) {
    			        	event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + " placed by " + player + " at " + dfm.format(date));
    			        } else {
    			        	event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + " delete by " + player + " at " + dfm.format(date));    			        	
    			        }
    		    	}
    		    	this.mysql.close();
    		    } catch (Exception e) {
    		    	e.printStackTrace();
    		    }
    		}
    	}
    }
*/
    
}
