package info.tregmine.api.url;

import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Url {

	public static String addDB(String _url){
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();
			
	    	stmt = conn.prepareStatement("insert shorturl (link) values (?)"); 
	    	stmt.setString(0, _url);
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
		
		return null;
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



}
