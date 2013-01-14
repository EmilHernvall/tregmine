package info.tregmine.api;

import info.tregmine.api.encryption.BCrypt;
import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
//import java.util.List;

import org.bukkit.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.Location;
//import org.bukkit.Sound;
//import org.bukkit.Effect;
//import org.bukkit.Location;
import org.bukkit.block.Block;
//import org.bukkit.entity.Entity;
//import org.bukkit.conversations.Conversation;
//import org.bukkit.entity.Arrow;
//import org.bukkit.entity.Egg;
//import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.EntityEquipment;

public class TregminePlayer extends PlayerDelegate
{
	public enum GuardianState
	{
		ACTIVE, // nuvarande aktiv
		INACTIVE, // har kört /normal
		QUEUED; // väntar på att aktiveras
	};

	private HashMap<String,String> settings = new HashMap<String,String>();
	private HashMap<String,Block> block = new HashMap<String,Block>();
	private HashMap<String,Integer> integer = new HashMap<String,Integer>();

	private int id = 0;
	private String name;
	private Zone currentZone = null;
	private GuardianState guardianState = null;
	private String password;

	public TregminePlayer(Player player, String _name) 
	{
		super(player);
		this.name = _name;
	}

	public boolean exists() 
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();

			String sql = "SELECT * FROM user WHERE player = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
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

	public void load() 
	{
		settings.clear();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.getConnection();

			stmt = conn.prepareStatement("SELECT * FROM user JOIN  (user_settings) WHERE uid=id and player = ?");
			stmt.setString(1, name);
			stmt.execute();

			rs = stmt.getResultSet();
			while (rs.next()) {
				System.out.println(rs.getString("key") +"->" + rs.getString("value"));
				this.id = rs.getInt("uid");
				settings.put("uid", rs.getString("uid"));
				settings.put(rs.getString("key"), rs.getString("value"));
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
		this.setTemporaryChatName(getNameColor() + name);
		//		this.resendTexture();
	}

	public void create() 
	{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();

			String sql = "INSERT INTO user (player) VALUE (?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
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

	private boolean getBoolean(String key) 
	{
		String value = this.settings.get(key);

		try {
			if (value.contains("true")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception  e) {
			return false;
		}
	}

	public int getId()
	{
		return id;
	}

	public boolean isAdmin() 
	{
		return getBoolean("admin");
	}

	public boolean isDonator() 
	{
		return getBoolean("donator");
	}

	@Override
	public boolean isBanned() 
	{
		return getBoolean("banned");
	}

	public boolean isTrusted() 
	{
		return getBoolean("trusted");
	}

	public boolean isChild() 
	{
		return getBoolean("child");
	}

	public boolean isImmortal() 
	{
		return getBoolean("immortal");
	}

	public boolean isGuardian()
	{
		return getMetaString("guardian") != null;
	}

	public int getGuardianRank()
	{
		try {
			return Integer.parseInt(getMetaString("guardian"));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public GuardianState getGuardianState()
	{
		return guardianState;
	}

	public void setGuardianState(GuardianState v)
	{
		this.guardianState = v;
		switch (v) {
		case ACTIVE:
			setTempMetaString("color", "police");
			break;
		case INACTIVE:
		case QUEUED:
			setTempMetaString("color", "donator");
			break;
		}
		setTemporaryChatName(getNameColor() + getName());
	}

	public boolean getMetaBoolean(String _key) 
	{
		return getBoolean(_key);
	}

	public void setMetaBoolean(String _key, Boolean _value) 	{
		if (_value == true) {
			this.setMetaString(_key, "true");
		}

		if (_value == false) {
			this.setMetaString(_key, "false");
		}

	}


	public void setMetaString(String _key, String _value) 	{
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();

			String sqlDelete = "DELETE FROM `minecraft`.`user_settings` " +
					"WHERE `user_settings`.`id` = ? AND `user_settings`.`key` = ?";
			stmt = conn.prepareStatement(sqlDelete);
			stmt.setString(1, settings.get("uid"));
			stmt.setString(2, _key);
			stmt.execute();

			stmt.close();
			stmt = null;

			if (_value == null) {
				this.settings.remove(_key);
				return;
			}

			String sqlInsert = "INSERT INTO user_settings (id,`key`,`value`) " +
					"VALUE ((SELECT uid FROM user WHERE player = ?),?,?)";
			stmt = conn.prepareStatement(sqlInsert);
			stmt.setString(1, this.getName());
			stmt.setString(2, _key);
			stmt.setString(3, _value);
			stmt.execute();

			stmt.close();
			stmt = null;

			this.settings.put(_key, _value);

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

	public void setTempMetaString(String _key, String _value) 
	{
		try {
			this.settings.put(_key, _value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	

	public String getMetaString(String _key) 
	{
		return this.settings.get(_key);
	}

	public String getTimezone() 
	{
		String value = this.settings.get("timezone");
		if (value == null) {
			return "Europe/Stockholm";
		} else {
			return value;
		}
	}

	public void setBlock(String _key, Block _block) 
	{
		this.block.put(_key, _block);
	}

	public Block getBlock(String _key) 
	{
		return this.block.get(_key);
	}

	public Integer getMetaInt(String _key) 
	{
		return this.integer.get(_key);
	}

	public void setMetaInt(String _key, Integer _value) 
	{
		this.integer.put(_key, _value);
	}

	public ChatColor getNameColor() 
	{
		String color = this.settings.get("color");

		if (this.settings.get("color") != null ) {
			if (color.toLowerCase().matches("admin")) {
				return ChatColor.RED;
			}

			if (color.toLowerCase().matches("broker")) {
				return ChatColor.DARK_RED;
			}

			if (color.toLowerCase().matches("helper")) {
				return ChatColor.YELLOW;
			}

			if (color.toLowerCase().matches("purle")) {
				return ChatColor.DARK_PURPLE;
			}

			if (color.toLowerCase().matches("donator")) {
				return ChatColor.GOLD;
			}

			if (color.toLowerCase().matches("trusted")) {
				return ChatColor.DARK_GREEN;
			}

			if (color.toLowerCase().matches("warned")) {
				return ChatColor.GRAY;
			}

			if (color.toLowerCase().matches("trial")) {
				return ChatColor.GREEN;
			}

			if (color.toLowerCase().matches("vampire")) {
				return ChatColor.DARK_RED;
			}

			if (color.toLowerCase().matches("hunter")) {
				return ChatColor.BLUE;
			}

			if (color.toLowerCase().matches("pink")) {
				return ChatColor.LIGHT_PURPLE;
			}

			if (color.toLowerCase().matches("child")) {
				return ChatColor.AQUA;
			}

			if (color.toLowerCase().matches("mentor")) {
				return ChatColor.DARK_AQUA;
			}

			if (color.toLowerCase().matches("police")) {
				return ChatColor.BLUE;
			}

		}
		return ChatColor.WHITE;
	}


	public String getSayName() 	{
		return name;
	}

	public void setSayName() 	{
		//		return name;
	}

	public String getChatName() 	{
		return name;
	}


	public void setInvis(Boolean _value)	{
		this.setMetaBoolean("invis", _value);
	}

	public Boolean getInvis()	{
		return this.getBoolean("invis");
	}

	public void setTemporaryChatName(String _name)	{
		name = _name;

		if (getChatName().length() > 16) {
			this.setPlayerListName(name.substring(0, 15));
		} else {
			this.setPlayerListName(name);
		}
	}

	public void setCurrentZone(Zone zone) {
		this.currentZone = zone;
	}

	public Zone getCurrentZone() {
		return currentZone;
	}

	public void setPassword(String newPassword)
	{
		String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionPool.getConnection();

			String sql = "UPDATE user SET password = ? WHERE uid = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, hash);
			stmt.setInt(2, this.id);
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

	public void resendTexture() {

		if(this.getMetaString("text") == null) {
			this.setMetaString("text", "https://dl.dropbox.com/u/5405236/mc/df.zip");
			this.setTexturePack("https://dl.dropbox.com/u/5405236/mc/df.zip");
		} else {
			this.setTexturePack(this.getMetaString("text"));
		}

	}

	public void setCurrentTexture(String _url) {

		if(this.getMetaString("text") == null) {
			this.setMetaString("text", "https://dl.dropbox.com/u/5405236/mc/df.zip");
			this.setTexturePack("https://dl.dropbox.com/u/5405236/mc/df.zip");
		}

		if (!this.getMetaString("text").matches(_url)) {
			this.setMetaString("text", _url);
			this.setTexturePack(_url);
		}
	}

	public void setChatChannel(String _channel) {
		this.setMetaString("channel", _channel.toUpperCase());
	}

	public String getChatChannel() {
		if (this.getMetaString("channel") == null) {
			return "GLOBAL";
		}    	
		return this.getMetaString("channel").toUpperCase();
	}

	public boolean verifyPassword(String attempt) {
		return BCrypt.checkpw(attempt, this.password);
	}
}
