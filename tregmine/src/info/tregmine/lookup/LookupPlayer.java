package info.tregmine.lookup;


import info.tregmine.Tregmine;
import info.tregmine.database.ConnectionPool;

import java.io.IOException;
//import java.net.InetAddress;
import java.net.InetSocketAddress;
//import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;


public class LookupPlayer implements  Listener  {
	private final Tregmine plugin;
	private LookupService cl = null;

	public LookupPlayer(Tregmine instance) {
		plugin = instance;
		try {
			cl = new LookupService("GeoIPCity.dat", LookupService.GEOIP_MEMORY_CACHE );
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();		
		InetSocketAddress sock = player.getAddress();
		String ip = sock.getAddress().getHostAddress();
		String host = sock.getAddress().getCanonicalHostName();
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(player.getName());


		
		
		if (cl != null) {
			Location l1 = cl.getLocation(ip);
			if (l1 == null) {
				return;
			}

			plugin.log.info(event.getPlayer().getName() + ": " + l1.countryName + ", " + l1.city + ", " + ip + ", " + l1.postalCode + ", " + l1.region + ", " + host);
			try {
				tregminePlayer.setMetaString("countryName", l1.countryName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tregminePlayer.setMetaString("city", l1.city);
			tregminePlayer.setMetaString("ip", ip);
			tregminePlayer.setMetaString("postalCode", l1.postalCode);
			tregminePlayer.setMetaString("region", l1.region);
			tregminePlayer.setMetaString("hostname", host);
			
			
			
			if(!event.getPlayer().isOp()) {
				if(tregminePlayer.getMetaBoolean("hiddenlocation")) {
				} else {
					this.plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Welcome! " + tregminePlayer.getChatName() + ChatColor.DARK_AQUA + " from " +l1.countryName);
					event.getPlayer().sendMessage(ChatColor.DARK_AQUA + l1.city + " - " + l1.postalCode);
				}
			} 

		}

		if(tregminePlayer.getNameColor() == ChatColor.GOLD || tregminePlayer.isAdmin() || tregminePlayer.isGuardian() || tregminePlayer.getMetaBoolean("builder")) {
			event.getPlayer().sendMessage("You are allowed to fly");
			event.getPlayer().setAllowFlight(true);
			//			tregminePlayer.setAllowFlight(true);
		} else {
			event.getPlayer().sendMessage("no-z-cheat");
			event.getPlayer().sendMessage("You are NOT allowed to fly");
			event.getPlayer().setAllowFlight(false);
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String all = "";
		
		try {
			conn = ConnectionPool.getConnection();

			stmt = conn.prepareStatement("SELECT minecraft.user.player FROM minecraft.user, minecraft.user_settings WHERE user.uid=user_settings.id AND minecraft.user_settings.value=?" +
					"ORDER BY time DESC LIMIT 5");
			stmt.setString(1, ip);
			stmt.execute();

			rs = stmt.getResultSet();

			while (rs.next()) {
				String name =  rs.getString("player");
				all = name + ", "+ all;
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
				
		this.plugin.log.info("ALIAS: " + all);
		
		Player[] players = plugin.getServer().getOnlinePlayers();
		for (Player allplayer : players) {
			info.tregmine.api.TregminePlayer allP = this.plugin.tregminePlayer.get(allplayer.getName());

			if (allP.isAdmin() || allP.isGuardian()) {
				if(!tregminePlayer.getMetaBoolean("hiddenlocation")) {
					allP.sendMessage(ChatColor.YELLOW + "This player have also used names: " + all);
				}
			}
		} 
				
		if (tregminePlayer.getMetaBoolean("builder")) {
			event.getPlayer().setGameMode(GameMode.CREATIVE);
		} else {
			if (!tregminePlayer.isOp()) {
				event.getPlayer().setGameMode(GameMode.SURVIVAL);
			}			
		}
	}    
}
