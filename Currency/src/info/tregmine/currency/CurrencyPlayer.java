package info.tregmine.currency;

import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLoginEvent;


public class CurrencyPlayer implements Listener  {
	private final Main plugin;

	public CurrencyPlayer(Main instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void onPlayerLogin (PlayerLoginEvent event){
		Wallet wallet = new Wallet(event.getPlayer());
		wallet.create();
	}

	@EventHandler
	public void onPlayerDropItem (PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
		if (
				event.getItemDrop().getLocation().getBlockX() < 509 &&
				event.getItemDrop().getLocation().getBlockX() > 498 &&
				event.getItemDrop().getLocation().getBlockZ() > -165 &&
				event.getItemDrop().getLocation().getBlockZ() < -153
			){
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = ConnectionPool.getConnection();

				String sql = "SELECT value FROM items_destroyvalue WHERE itemid = ?";
				
				stmt = conn.prepareStatement(sql);
				stmt.setLong(1, event.getItemDrop().getItemStack().getTypeId());
				stmt.execute();
				
				rs = stmt.getResultSet();
				
				rs.first();
				
				int total = rs.getInt("value") * event.getItemDrop().getItemStack().getAmount();
//				event.getPlayer().sendMessage("value: " + total);
				
				
				if (total > 0) {
					Wallet wallet = new Wallet(event.getPlayer().getName());
					event.getItemDrop().remove();
					wallet.add(total);
					event.getPlayer().sendMessage(ChatColor.AQUA + "You got " + ChatColor.GOLD + total + " Tregs");
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
			
			
			
//			event.getPlayer().sendMessage("If you see this, you are in a dropzone");

		}
	}

}