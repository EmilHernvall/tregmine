package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;

import info.tregmine.api.PlayerReport;
import info.tregmine.api.StaffNews;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IStaffNewsDAO;

public class DBNewsDAO implements IStaffNewsDAO {
    private Connection conn;

    public DBNewsDAO(Connection conn)
    {
        this.conn = conn;
    }

	@Override
	public void insertNews(StaffNews news) throws DAOException {
		String sql = "INSERT INTO staffnews (username, text, timestamp) VALUES (?, ?, ?)";
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, news.getUsername());
			stmt.setString(2, news.getText());
			long timestamp = new Date().getTime();
			stmt.setLong(3, timestamp / 1000l);
			stmt.execute();
		} catch (SQLException e){
			throw new DAOException(sql, e);
		}
	}

	@Override
	public List<StaffNews> getStaffNews() throws DAOException {
		List<StaffNews> news = new ArrayList<StaffNews>();
		String sql = "SELECT * FROM staffnews";
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.execute();
			try(ResultSet rs = stmt.getResultSet()){
				while(rs.next()){
				StaffNews fetchedNews = new StaffNews();
				fetchedNews.setId(rs.getInt("id"));
				fetchedNews.setUsername(rs.getString("username"));
				fetchedNews.setText(rs.getString("text"));
				fetchedNews.setTimestamp(rs.getLong("timestamp"));
				news.add(fetchedNews);
				}
			}
		}catch(SQLException e){
			throw new DAOException(sql, e);
		}
		return news;
	}

}