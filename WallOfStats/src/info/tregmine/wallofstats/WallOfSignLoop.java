package info.tregmine.wallofstats;

import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class WallOfSignLoop  implements Runnable{
	World world;

	public WallOfSignLoop(World world) {
		this.world = world;
	}

	public void run() {
		Connection conn = null;
		try {
			conn = ConnectionPool.getConnection();
			
			if (getBlockState(528, 20, -184) instanceof Sign) {
				Sign sign = (Sign)getBlockState(528, 20, -184);
				
				PreparedStatement stmt = null;
				ResultSet rs = null;
				try {
					// placed blocks
					sign.setLine(0, "\u00A74PLACED BLOCKS");
					
					String SQL = "SELECT COUNT(status) as count FROM stats_blocks WHERE status = 1";
					stmt = conn.prepareStatement(SQL);
					rs = stmt.getResultSet();
					if (rs.next()) {
						sign.setLine(1, "\u00A71" + rs.getInt("count"));
					} else {
						sign.setLine(1, "\u00A71 - ERROR - ");
					}
						
					rs.close();
					rs = null;
					
					stmt.close();
					stmt = null;
	
					// deleted blocks
					sign.setLine(2, "\u00A74DELETED BLOCKS");
					
					String SQL1 = "SELECT COUNT(status) as count FROM stats_blocks WHERE status = 0";
					stmt = conn.prepareStatement(SQL1);
					rs = stmt.getResultSet();
					if (rs.next()) {
						sign.setLine(3, "\u00A71" + rs.getInt("count"));
					} else {
						sign.setLine(3, "\u00A71 - ERROR - ");
					}
					
					rs.close();
					
					sign.update();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					if (rs != null) {
						try { rs.close(); } catch (SQLException e) {} 
					}
					if (stmt != null) {
						try { stmt.close(); } catch (SQLException e) {}
					}
				}
			}
	
			if (getBlockState(528, 21, -184) instanceof Sign) {
				Sign sign = (Sign)getBlockState(528, 21, -184);
				
				PreparedStatement stmt = null;
				ResultSet rs = null;
				try {
					// total blocks
					sign.setLine(0, "\u00A74TOTAL BLOCKS");
					
					String SQL = "SELECT COUNT(status) as count FROM stats_blocks";
					stmt = conn.prepareStatement(SQL);
					rs = stmt.getResultSet();
					if (rs.next()) {
						sign.setLine(1, "\u00A71" + rs.getInt("count"));
					} else {
						sign.setLine(1, "\u00A71 - ERROR - ");
					}
					
					rs.close();
					rs = null;
					
					stmt.close();
					stmt = null;
					
					// warps
					sign.setLine(2, "\u00A74WARPS");
	
					String SQL1 = "SELECT COUNT(name) as count FROM warps";
					stmt = conn.prepareStatement(SQL1);
					rs = stmt.getResultSet();
					if (rs.next()) {
						sign.setLine(3, "\u00A71" + rs.getInt("count"));
					} else {
						sign.setLine(3, "\u00A71 - ERROR - ");
					}
					
					rs.close();
					rs = null;
					
					stmt.close();
					stmt = null;
					
					sign.update();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					if (rs != null) {
						try { rs.close(); } catch (SQLException e) {} 
					}
					if (stmt != null) {
						try { stmt.close(); } catch (SQLException e) {}
					}
				}
			}
	
			if (getBlockState(529, 21, -183) instanceof Sign) {
				Sign sign = (Sign)getBlockState(529, 21, -183);
				PreparedStatement stmt = null;
				ResultSet rs = null;
				try {
					// unique visitors
					sign.setLine(0, "\u00A74UNIQE VISITORS");
					
					String SQLplayers = "SELECT COUNT(player) as count FROM user";
					stmt = conn.prepareStatement(SQLplayers);
					rs = stmt.getResultSet();
					if (rs.next()) {
						sign.setLine(1, "\u00A71" + rs.getInt("count"));
					} else {
						sign.setLine(1, "\u00A71 - ERROR - ");
					}
					
					rs.close();
					rs = null;
					
					stmt.close();
					stmt = null;
	
					// banned
					sign.setLine(2, "\u00A74BANNED");
					
					String SQLBanned = "SELECT COUNT(value) as count FROM user_settings WHERE `key`='banned' AND `value`='true'";
					stmt = conn.prepareStatement(SQLBanned);
					rs = stmt.getResultSet();
					if (rs.next()) {
						sign.setLine(3, "\u00A71" + rs.getInt("count"));
					} else {
						sign.setLine(3, "\u00A71 - ERROR - ");
					}
					
					rs.close();
					rs = null;
					
					stmt.close();
					stmt = null;
					
					sign.update();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					if (rs != null) {
						try { rs.close(); } catch (SQLException e) {} 
					}
					if (stmt != null) {
						try { stmt.close(); } catch (SQLException e) {}
					}
				}
			}
	
			if (getBlockState(529, 20, -183) instanceof Sign) {
				Sign sign = (Sign)getBlockState(529, 20, -183);
				
				PreparedStatement stmt = null;
				ResultSet rs = null;
				try {
					// trusted
					sign.setLine(0, "\u00A74TRUSTED");
	
					String SQL1 = "SELECT COUNT(value) as count FROM user_settings WHERE `key`='trusted' AND `value`='true'";
					stmt = conn.prepareStatement(SQL1);
					rs = stmt.getResultSet();
					if (rs.next()) {
						sign.setLine(1, "\u00A71" + rs.getInt("count"));
					} else {
						sign.setLine(1, "\u00A71 - ERROR - ");
					}
					
					rs.close();
					rs = null;
					
					stmt.close();
					stmt = null;
					
					// donators
					sign.setLine(2, "\u00A74DONATORS");
					
					String SQL2 = "SELECT COUNT(value) as count FROM user_settings WHERE `key`='donator' AND `value`='true'";
					stmt = conn.prepareStatement(SQL2);
					rs = stmt.getResultSet();
					if (rs.next()) {
						sign.setLine(3, "\u00A71" + rs.getInt("count"));
					} else {
						sign.setLine(3, "\u00A71 - ERROR - ");
					}
					
					rs.close();
					rs = null;
					
					stmt.close();
					stmt = null;
					
					sign.update();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				} finally {
					if (rs != null) {
						try { rs.close(); } catch (SQLException e) {} 
					}
					if (stmt != null) {
						try { stmt.close(); } catch (SQLException e) {}
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (SQLException e) {}
			}
		}
	}


	private BlockState getBlockState(int x, int y, int z) {
		Block block = world.getBlockAt(x,y,z);
		return block.getState();
	}

}
