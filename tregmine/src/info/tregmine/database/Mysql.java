package info.tregmine.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.sql.*;

public class Mysql {
	
	public Connection connect = null;
	public Statement statement = null;
//	private PreparedStatement preparedStatement = null;
	public ResultSet resultSet = null;
	private String url = "jdbc:mysql://192.168.0.21/minecraft";
	private String user = "root";
	private String pw = "cUqeWemUGeYaquxUpHaye8rUcrAteWre";
	// cUqeWemUGeYaquxUpHaye8rUcrAteWre
	
    public Mysql() {
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
        		
        		
    	    } catch (ClassNotFoundException ex) {
    	    	ex.printStackTrace();
    		}
//       		this.connect();
    }
    
    public void connect(){
    	try {
    		this.connect = DriverManager.getConnection( url, user, pw );
    		this.statement = this.connect.createStatement();
			} catch (Exception e) {
				e.printStackTrace();
			}
    }
    
    public void close(){
    	try {
    		this.connect.close();
    		this.statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
    }

}
