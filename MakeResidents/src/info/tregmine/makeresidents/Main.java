package info.tregmine.makeresidents;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Main {
	public static Connection connect = null;
	public static Statement statement = null;
	public ResultSet resultSet = null;
	private static String url = "jdbc:mysql://127.0.0.1:3306/minecraft";
	private static String user = "minecraft";
	private static String pw = "mine";
	
	public static void main(String[] args) {
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
	    } catch (ClassNotFoundException ex) {
	    	ex.printStackTrace();
	    	return;
		}

    	try {
    		
    		connect = DriverManager.getConnection( url, user, pw );
    		statement = connect.createStatement();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

	}

}
