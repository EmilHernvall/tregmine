package info.tregmine.currency;

import info.tregmine.api.TregminePlayer;
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
import org.bukkit.event.player.PlayerPickupItemEvent;


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
	public void onPlayerPickupItem (PlayerPickupItemEvent event){


		if (
				event.getItem().getLocation().getBlockX() < 509 &&
				event.getItem().getLocation().getBlockX() > 498 &&
				event.getItem().getLocation().getBlockZ() > -165 &&
				event.getItem().getLocation().getBlockZ() < -153
				){
			event.setCancelled(true);
			event.getItem().remove();
		}

	}


	@EventHandler
	public void onPlayerDropItem (PlayerDropItemEvent event) {
		if (
				event.getItemDrop().getLocation().getBlockX() < 509 &&
				event.getItemDrop().getLocation().getBlockX() > 495 &&
				event.getItemDrop().getLocation().getBlockZ() > -165 &&
				event.getItemDrop().getLocation().getBlockZ() < -150
				){
			
			if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
				event.setCancelled(true);
				return;
			}
			
			
			TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(event.getPlayer().getName());
			if (!tregminePlayer.isTrusted()) {
				event.setCancelled(true);
				return;
			}
			
			
			
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


				int total = 0;
				if (rs.first() ) {

					total = rs.getInt("value") * event.getItemDrop().getItemStack().getAmount();
					//				event.getPlayer().sendMessage("value: " + total);
				}


				if (total > 0) {
					Wallet wallet = new Wallet(event.getPlayer().getName());
					event.getItemDrop().getItemStack().setAmount(0);
					event.getItemDrop().setPickupDelay(Integer.MAX_VALUE);
					event.getItemDrop().setFireTicks(Integer.MAX_VALUE);
					wallet.add(total);
					event.getPlayer().sendMessage(ChatColor.AQUA + "You got " + ChatColor.GOLD + total + " Tregs " + ChatColor.AQUA +"(You have " + wallet.formatBalance() + ")");
					//					event.getItemDrop().remove();
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