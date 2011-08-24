package info.tregmine.chestbless;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Store {

	
	public static void deletebless(int checksum, String player, String world, info.tregmine.database.Mysql mysql){
		try {		
//	    	mysql.statement.executeQuery("DELETE FROM chestbless WHERE checksum='" + checksum + "'");
	    	mysql.statement.execute("DELETE FROM chestbless WHERE checksum='" + checksum + "'");
//	    	ResultSet rs = mysql.statement.getResultSet();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static void savebless(int checksum, String player, String world, info.tregmine.database.Mysql mysql){
		try {
			PreparedStatement ps = mysql.connect.prepareStatement("insert into chestbless (checksum, world,  player) values (?,?,?)");
			ps.setInt(1, checksum);
			ps.setString(2, world);
			ps.setString(3, player);
//			ps.setBoolean(4, true);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
					e.printStackTrace();
		}

	}

	public static HashMap<Integer, String> loadbless(info.tregmine.database.Mysql mysql){
		try {
		HashMap<Integer, String> chests = new HashMap<Integer, String>();
	
    	mysql.statement.executeQuery("SELECT * FROM  chestbless");
    	ResultSet rs = mysql.statement.getResultSet();

    	while (rs.next()) {
    		chests.put(rs.getInt("checksum"), rs.getString("player"));
    	}
    	return chests;
    	
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
