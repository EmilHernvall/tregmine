package info.tregmine.server.playerlist;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import info.tregmine.client.playerlist.PlayerListService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;



public class PlayerListServiceImpl extends RemoteServiceServlet implements
		PlayerListService {
	private static final long serialVersionUID = 7844358096109326161L;

	public String greetServer(String names) throws IllegalArgumentException {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		
		  if (session.getAttribute("test") == null) {
			  return "ERROR: Not loged in";
		  }
		
		info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql(); 

		mysql.connect();
		String name = new String();
		try {
			mysql.statement.executeQuery("SELECT * FROM user WHERE player LIKE '" + names + "%' ORDER BY player LIMIT 15");
			ResultSet rs = mysql.statement.getResultSet();
			while (rs.next()) {
				name = rs.getString("player") + "," + name;
			}
			
		} catch (SQLException e) {
			return "SQL-SERVER-ERROR:" + e.getMessage();
		}
		
		catch (Exception e) {
			return "OTHER-SERVER-ERROR:" + e.getMessage();
		}

		
		mysql.close();
		return name;
	}
}
