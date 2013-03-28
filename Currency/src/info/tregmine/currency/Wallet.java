package info.tregmine.currency;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

//import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class Wallet {
	String player;

	public Wallet(Player player) {
		this.player = player.getName();
	}
	
	public Wallet(String player) {
		this.player = player;
	}

	public long balance() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();
			
			stmt = conn.prepareStatement("SELECT w.`value` FROM wallet w WHERE w.player = ?");
			stmt.setString(1, player);
			stmt.execute();
			
			rs = stmt.getResultSet();
			if (rs.next()) {
				return rs.getLong("value");
			}
		} catch (SQLException e) {
//			throw new RuntimeException(e);
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
		
		return -1;
	}

	public String formatBalance() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		return ChatColor.GOLD +  nf.format( this.balance() ) + ChatColor.WHITE + " Tregs";
	}
	
	public boolean exist() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();
			
			String sql = "SELECT * FROM wallet w WHERE w.player = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, player);
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

	public void create() {
		if (exist()) {
			return;
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();
			
			String sql = "INSERT INTO wallet (player, value) VALUES (?,'10000')";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, player);
			stmt.execute();
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

	public boolean add(long amount) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();
			
			String sql = "UPDATE wallet SET value = value + ? WHERE player = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, amount);
			stmt.setString(2, player);
			stmt.execute();
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
		
		return true;
	}

	public boolean take(long amount){
		long newBalance = balance() - amount;
		if (newBalance < 0) {
			return false;
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();
			
			String sql = "UPDATE wallet SET value = value - ? WHERE player = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, amount);
			stmt.setString(2, player);
			stmt.execute();
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
		
		return true;
	}

	public static int getBlockValue(int block) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();
			
			String sql = "SELECT i.`value` FROM items_destroyvalue i WHERE i.itemid = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, block);
			stmt.execute();
			
			rs = stmt.getResultSet();
			if (rs.next()) {
				return rs.getInt("value");
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
		
		return 0;
	}
}
