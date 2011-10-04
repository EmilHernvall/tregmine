package info.tregmine.api;

import info.tregmine.database.Mysql;

import java.sql.ResultSet;
import java.util.HashMap;

import org.bukkit.ChatColor;
//import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TregminePlayer extends PlayerDelegate
{
	private HashMap<String,String> settings = new HashMap<String,String>();
	private HashMap<String,Block> block = new HashMap<String,Block>();
	private HashMap<String,Integer> integer = new HashMap<String,Integer>();
	//	private HashMap<String,Location> location = new HashMap<String,Location>();

	private int id = 0;
	private String name;
	private final Mysql mysql = new Mysql();
	private Zone currentZone = null;

	public TregminePlayer(Player player, String _name) 
	{
		super(player);
		this.name = _name;
	}

	public boolean exists() 
	{
		this.mysql.connect();
		if (this.mysql.connect != null) {
			try {
				String SQL = "SELECT COUNT(*) as count FROM user WHERE player = '"+ name +"';";
				this.mysql.statement.executeQuery(SQL);
				ResultSet rs = this.mysql.statement.getResultSet();
				rs.first();
				if ( rs.getInt("count") != 1 ) {
					this.mysql.close();
					return false;
				} else { 
					this.mysql.close();
					return true;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		this.mysql.close();
		return false;
	}

	public void load() 
	{
		settings.clear();
		System.out.println("Loading settings for " + name);

		mysql.connect();
		if (this.mysql.connect != null) {
			try {
				this.mysql.connect();
				this.mysql.statement.executeQuery("SELECT * FROM user JOIN  (user_settings) WHERE uid=id and player = '" + name + "';");
				ResultSet rs = this.mysql.statement.getResultSet();

				while (rs.next()) {
					//TODO: Make this much nicer, this is bad code
					this.id = rs.getInt("uid");
					settings.put("uid", rs.getString("uid"));
					settings.put(rs.getString("key"), rs.getString("value"));
				}
				this.mysql.close();	
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		mysql.close();
	}

	public void create() 
	{
		this.mysql.connect();
		if (this.mysql.connect != null) {
			try {
				String SQL = "INSERT INTO user (player) VALUE ('"+ name +"')";
				this.mysql.statement.execute(SQL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.mysql.close();
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

	public boolean getMetaBoolean(String _key) 
	{
		return getBoolean(_key);
	}

	public void setMetaString(String _key, String _value) 
	{
		try {
			this.mysql.connect();
			String SQLD = "DELETE FROM `minecraft`.`user_settings` WHERE `user_settings`.`id` = "+ settings.get("uid") +" AND `user_settings`.`key` = '" + _key  +"'";
			this.mysql.statement.execute(SQLD);

			
			String SQLU = "INSERT INTO user_settings (id,`key`,`value`) VALUE ((SELECT uid FROM user WHERE player='" + this.name + "'),'"+ _key +"','"+ _value +"')";
			this.settings.put(_key, _value);
			this.mysql.statement.execute(SQLU);
			this.mysql.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
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

	public String getChatName() 
	{
		return getNameColor() + name;
	}
	
	public void setCurrentZone(Zone zone)
	{
		this.currentZone = zone;
	}
	
	public Zone getCurrentZone()
	{
		return currentZone;
	}

}
