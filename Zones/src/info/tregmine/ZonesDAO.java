package info.tregmine;

import info.tregmine.api.Zone;
import info.tregmine.quadtree.Rectangle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZonesDAO 
{
	private Connection conn;
	
	public ZonesDAO(Connection conn)
	{
		this.conn = conn;
	}
	
	public int getUserId(String player)
	throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT uid FROM user WHERE player = ?");
			stmt.setString(1, player);
			stmt.execute();
			
			rs = stmt.getResultSet();
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		finally {
			if (rs != null) { 
				try { rs.close(); } catch (SQLException e) {}
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
		
		return -1;
	}
	
	private List<Rectangle> getZoneRectangles(int zoneId)
	throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Rectangle> rects = new ArrayList<Rectangle>();
		try {
			stmt = conn.prepareStatement("SELECT * FROM zone_rect WHERE zone_id = ?");
			stmt.setInt(1, zoneId);
			stmt.execute();
			
			rs = stmt.getResultSet();
			while (rs.next()) {
				int x1 = rs.getInt("rect_x1");
				int y1 = rs.getInt("rect_y1");
				int x2 = rs.getInt("rect_x2");
				int y2 = rs.getInt("rect_y2");
				
				rects.add(new Rectangle(x1, y1, x2, y2));
			}
		}
		finally {
			if (rs != null) { 
				try { rs.close(); } catch (SQLException e) {}
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
		
		return rects;
	}
	
	private Map<String, Zone.Permission> getZonePermissions(int zoneId)
	throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Zone.Permission> permissions = new HashMap<String, Zone.Permission>();
		try {
			stmt = conn.prepareStatement("SELECT * FROM zone_user INNER JOIN user ON user_id = uid WHERE zone_id = ?");
			stmt.setInt(1, zoneId);
			stmt.execute();
			
			rs = stmt.getResultSet();
			while (rs.next()) {
				String player = rs.getString("player");
				Zone.Permission permission = 
						Zone.Permission.fromString(rs.getString("user_perm"));
				
				permissions.put(player, permission);
			}
		}
		finally {
			if (rs != null) { 
				try { rs.close(); } catch (SQLException e) {}
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
		
		return permissions;
	}
	
	public List<Zone> getZones()
	throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Zone> zones = new ArrayList<Zone>();
		try {
			stmt = conn.prepareStatement("SELECT * FROM zone");
			stmt.execute();
			
			rs = stmt.getResultSet();
			while (rs.next()) {
				Zone zone = new Zone();
				zone.setId(rs.getInt("zone_id"));
				zone.setName(rs.getString("zone_name"));
				zone.setEnterDefault("1".equals(rs.getString("zone_enterdefault")));
				zone.setBuildDefault("1".equals(rs.getString("zone_builddefault")));
				zone.setPvp("1".equals(rs.getString("zone_pvp")));
				zone.setTextEnter(rs.getString("zone_entermessage"));
				zone.setTextExit(rs.getString("zone_exitmessage"));
				
				zones.add(zone);
			}
		}
		finally {
			if (rs != null) { 
				try { rs.close(); } catch (SQLException e) {}
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
		
		for (Zone zone : zones) {
			zone.setRects(getZoneRectangles(zone.getId()));
			zone.setUsers(getZonePermissions(zone.getId()));
		}
		
		return zones;
	}
	
	public int createZone(Zone zone)
	throws SQLException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int id = 0;
		try {
			String sql = "INSERT INTO zone (zone_name, zone_enterdefault, zone_builddefault, " + 
					"zone_pvp, zone_entermessage, zone_exitmessage) VALUES (?,?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, zone.getName());
			stmt.setString(2, zone.getEnterDefault() ? "1" : "0");
			stmt.setString(3, zone.getBuildDefault() ? "1" : "0");
			stmt.setString(4, zone.isPvp() ? "1" : "0");
			stmt.setString(5, zone.getTextEnter());
			stmt.setString(6, zone.getTextExit());
			stmt.execute();
			
			stmt.execute("SELECT LAST_INSERT_ID()");
			
			rs = stmt.getResultSet();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		}
		finally {
			if (rs != null) { 
				try { rs.close(); } catch (SQLException e) {}
			}
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
		
		zone.setId(id);
		
		return id;
	}
	
	public void updateZone(Zone zone)
	throws SQLException
	{
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE zone SET zone_name = ?, zone_enterdefault = ?, zone_builddefault = ?, " + 
					"zone_pvp = ?, zone_entermessage = ?, zone_exitmessage = ? WHERE zone_id = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, zone.getName());
			stmt.setString(2, zone.getEnterDefault() ? "1" : "0");
			stmt.setString(3, zone.getBuildDefault() ? "1" : "0");
			stmt.setString(4, zone.isPvp() ? "1" : "0");
			stmt.setString(5, zone.getTextEnter());
			stmt.setString(6, zone.getTextExit());
			stmt.setInt(7, zone.getId());
			stmt.execute();
		}
		finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
	}
	
	public void addRectangle(int zoneId, Rectangle rect)
	throws SQLException
	{
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO zone_rect (zone_id, rect_x1, rect_y1, rect_x2, rect_y2) ";
			sql += "VALUES (?, ?, ?, ?, ?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, zoneId);
			stmt.setInt(2, rect.getLeft());
			stmt.setInt(3, rect.getTop());
			stmt.setInt(4, rect.getRight());
			stmt.setInt(5, rect.getBottom());
			stmt.execute();
		}
		finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
	}
	
	public void addUser(int zoneId, int userId, Zone.Permission perm)
	throws SQLException
	{
		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO zone_user (zone_id, user_id, user_perm) ";
			sql += "VALUES (?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, zoneId);
			stmt.setInt(2, userId);
			stmt.setString(3, perm.toString());
			stmt.execute();
		}
		finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
	}
	
	public void deleteUser(int zoneId, int userId)
	throws SQLException
	{
		PreparedStatement stmt = null;
		try {
			String sql = "DELETE FROM zone_user WHERE zone_id = ? AND user_id = ? ";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, zoneId);
			stmt.setInt(2, userId);
			stmt.execute();
		}
		finally {
			if (stmt != null) {
				try { stmt.close(); } catch (SQLException e) {}
			}
		}
	}
}
