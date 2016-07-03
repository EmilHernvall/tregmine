package info.tregmine.database.db;

import info.tregmine.database.IMailDAO;
import info.tregmine.database.IPlayerDAO;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.Server;
//import org.bukkit.entity.Player;

import info.tregmine.api.StaffNews;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.IHomeDAO;
import info.tregmine.database.DAOException;

public class DBMailDAO implements IMailDAO{
	private Connection conn;
	public DBMailDAO(Connection v){
		conn = v;
	}
	
	@Override
	public int getMailTotal(String username) throws DAOException {
		int total = 0;
        String rawstmt = "SELECT * FROM player_mail WHERE receiver_name = ? AND deleted = 'false'";
	        try (PreparedStatement stmt = conn.prepareStatement(rawstmt)) {
	            stmt.setString(1, username);
	            stmt.execute();
				try(ResultSet rs = stmt.getResultSet()){
					while(rs.next()){
					total = total + 1;
					}
				}
	        } catch (SQLException e) {
	            throw new DAOException(rawstmt, e);
	        }
        return total;
	}
	
	@Override
	public int getMailTotalEver(String username) throws DAOException {
		int total = 0;
        String rawstmt = "SELECT * FROM player_mail WHERE receiver_name = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(rawstmt)) {
	            stmt.setString(1, username);
	            stmt.execute();
				try(ResultSet rs = stmt.getResultSet()){
					while(rs.next()){
					total = total + 1;
					}
				}
	        } catch (SQLException e) {
	            throw new DAOException(rawstmt, e);
	        }
        return total;
	}
	
	@Override
	public boolean sendMail(TregminePlayer player, String sendTo, String message) throws DAOException {
		String username = player.getName();
		DBPlayerDAO playerdao = new DBPlayerDAO(conn);
		TregminePlayer receiver = playerdao.getPlayer(sendTo);
		if(receiver == null){
			return false;
		}
		String rawstmt = "INSERT INTO player_mail (sender_name, receiver_name, message, deleted) VALUES (?, ?, ?, ?)";
		try(PreparedStatement stmt = conn.prepareStatement(rawstmt)){
			stmt.setString(1, username);
			stmt.setString(2, sendTo);
			stmt.setString(3, message);
			stmt.setString(4, "false");
			stmt.execute();
		} catch (SQLException e) {
			throw new DAOException(rawstmt, e);
		}
		return true;
	}
	@Override
	public List<String[]> getAllMail(String username) throws DAOException {
		List<String[]> mail = new ArrayList<String[]>();
        String rawstmt = "SELECT * FROM player_mail WHERE receiver_name = ? AND deleted = 'false'";
	        try (PreparedStatement stmt = conn.prepareStatement(rawstmt)) {
	            stmt.setString(1, username);
	            stmt.execute();
				try(ResultSet rs = stmt.getResultSet()){
					while(rs.next()){
					String[] tempMail = new String[6];
					tempMail[0] = rs.getString("sender_name");
					tempMail[1] = rs.getString("deleted");
					tempMail[2] = rs.getString("receiver_name");
					tempMail[3] = rs.getString("message");
					int mailid = rs.getInt("mail_id");
					String s_mailid = Integer.toString(mailid);
					tempMail[4] = s_mailid;
					tempMail[5] = rs.getString("timestamp");
					mail.add(tempMail);
					}
				}
	        } catch (SQLException e) {
	            throw new DAOException(rawstmt, e);
	        }
        return mail;
	}
	@Override
	public boolean deleteMail(String username, int mailId) throws DAOException {
		String rawstmt = "UPDATE player_mail SET deleted='true' WHERE receiver_name = ? AND mail_id = ? AND deleted = 'false'";
		try(PreparedStatement stmt = conn.prepareStatement(rawstmt)){
			stmt.setString(1, username);
			stmt.setInt(2, mailId);
			int amount = stmt.executeUpdate();
			if(amount != 1){
				return false;
			}
		}catch(SQLException e) {
			throw new DAOException(rawstmt, e);
		}
		return true;
	}
	//ERIC WOZ HER 2K16 4/16 FUK YALL
}
