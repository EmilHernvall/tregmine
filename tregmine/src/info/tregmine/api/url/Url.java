package info.tregmine.api.url;

import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Url {

	public static Integer urlID(String _url){

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();

			String sql = "SELECT urlID FROM shorturl WHERE link = ?";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, _url); 
			stmt.execute();

			rs = stmt.getResultSet();

			if (rs.next()) {
				return rs.getInt(1);
			}

			return null;

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


	public static Integer addDB(String _url){

		if (urlID(_url) != null) {
			return urlID(_url);
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();

			stmt = conn.prepareStatement("insert shorturl (link) values (?)"); 
			stmt.setString(1, _url);
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

		return urlID(_url);
	}


	public static String getURL(String _text){
		String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(_text);

		String g = null;

		while (m.find()) {
			g = m.group(0);
		}


		return g;
	}



	public static String newURL(String _text){
		String url = info.tregmine.api.url.Url.getURL(_text);
		Integer urlID = null;
		if (url != null) {
			urlID = info.tregmine.api.url.Url.addDB(url);
		}
		return "http://treg.co/" + urlID;
	}
	
	public static String replaceURL(String _text){
			String url = newURL(_text);
			return _text.replace(_text, url);
	}


}
