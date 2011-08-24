package info.tregmine.currency;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.text.NumberFormat;

public class Wallet {
	public final info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();
	String player;

	public Wallet(Player player) {
		this.player = player.getName();
	}

	public Wallet(String player) {
		this.player = player;
	}

	public long balance() {
		this.mysql.connect();
		if (this.mysql.connect != null) {
			try {
				this.mysql.statement.executeQuery("SELECT w.`value` FROM wallet w WHERE w.player='" +  player + "'");
				ResultSet rs = this.mysql.statement.getResultSet();
				rs.first();
				return rs.getLong("value");
			} catch (Exception e) {
				e.printStackTrace();
				this.mysql.close();
				return -2;
			}
		}
		this.mysql.close();
		return -1;
	}

	public String formatBalance() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		return ChatColor.GOLD +  nf.format( this.balance() ) + ChatColor.WHITE + " Tregs";
//		return "";
	}
	
	public boolean exist() {
			String sql = "SELECT count(w.`player`) as exist FROM wallet w WHERE w.player='" +  player + "'";
			this.mysql.connect();
			try {
				this.mysql.statement.executeQuery(sql);
				ResultSet rs = this.mysql.statement.getResultSet();
				rs.first();
				int v = Integer.parseInt( rs.getString("exist") );
				if (v == 1) {
					this.mysql.close();
					return true;}
			} catch (Exception e) {
				  	e.printStackTrace();
			}
			this.mysql.close();
		return false;
	}

	public void create(){
			try {
				if (!exist()) {
					this.mysql.connect();
					String sql = "INSERT INTO wallet (player, value) VALUES ('" + player + "','1000')";
					this.mysql.statement.execute(sql);
					this.mysql.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public boolean add(long amount){
		this.mysql.connect();
		String sql = "UPDATE wallet SET value=value+" + amount + " WHERE player='" + player + "'";

		try {
			this.mysql.statement.execute(sql);
			this.mysql.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		this.mysql.close();
		return false;
	}

	public boolean take(long amount){
		long newBalance = this.balance() - amount;
		if (newBalance < 0) {
			return false;
		}
		
		String sql = "UPDATE wallet SET value=value-" + amount + " WHERE player='" + player + "'";

		this.mysql.connect();
		try {
			this.mysql.statement.execute(sql);
			this.mysql.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			this.mysql.close();
			return false;
		}
	}

	public static int getBlockValue(int block) {
		info.tregmine.database.Mysql mysql2 = new info.tregmine.database.Mysql();
		mysql2.connect();
			try {
				String sql = "SELECT i.`value` FROM items_destroyvalue i WHERE i.itemid='" +  block + "'";
				mysql2.statement.executeQuery(sql);
				ResultSet rs = mysql2.statement.getResultSet();
				rs.first();
				int value = Integer.parseInt(rs.getString("value"));
				mysql2.close();
				return value;
			} catch (Exception e) {
//				e.printStackTrace();
				mysql2.close();
				return 0;
			}
	}
}
