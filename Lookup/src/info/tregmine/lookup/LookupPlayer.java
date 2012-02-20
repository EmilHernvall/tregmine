package info.tregmine.lookup;


import java.io.IOException;
import java.net.InetSocketAddress;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;


public class LookupPlayer implements  Listener  {
	private final Lookup plugin;
	private LookupService cl = null;

	public LookupPlayer(Lookup instance) {
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
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(player.getName());

		if (cl != null) {
			Location l1 = cl.getLocation(ip);
			if (l1 == null) {
				return;
			}

			plugin.log.info(event.getPlayer().getName() + ": " + l1.countryName + ", " + l1.city + ", " + ip + ", " + l1.postalCode + ", " + l1.region + ", " + host);
			tregminePlayer.setMetaString("countryName", l1.countryName);
			tregminePlayer.setMetaString("city", l1.city);
			tregminePlayer.setMetaString("ip", ip);
			tregminePlayer.setMetaString("postalCode", l1.postalCode);
			tregminePlayer.setMetaString("region", l1.region);
			tregminePlayer.setMetaString("hostname", host);
			if(!event.getPlayer().isOp()) {
				this.plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Welcome! " + tregminePlayer.getChatName() + ChatColor.DARK_AQUA + " from " +l1.countryName);
				event.getPlayer().sendMessage(ChatColor.DARK_AQUA + l1.city + " - " + l1.postalCode);
			} 

		}

		if(tregminePlayer.isDonator()) {
			event.getPlayer().sendMessage("You are allowed to fly on this server");
			event.getPlayer().setAllowFlight(true);
//			tregminePlayer.setAllowFlight(true);
		} else {
			event.getPlayer().sendMessage("no-z-cheat");
			event.getPlayer().sendMessage("You are NOT allowed to fly on this server");
			event.getPlayer().setAllowFlight(false);
		}

	}    
}
