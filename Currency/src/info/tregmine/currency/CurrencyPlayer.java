package info.tregmine.currency;

import info.tregmine.quadtree.Point;
import info.tregmine.zones.Lot;
import info.tregmine.zones.ZoneWorld;
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
		info.tregmine.api.TregminePlayer tregPlayer = this.plugin.tregmine.tregminePlayer.get(event.getPlayer().getName());

		ZoneWorld world = this.plugin.zones.getWorld(tregPlayer.getWorld());

		Point p = new info.tregmine.quadtree.Point(tregPlayer.getLocation().getBlockX(),tregPlayer.getLocation().getBlockZ());
		Lot lot = world.findLot(p);

		if (lot != null) {
			tregPlayer.sendMessage(lot.getName());
		}
	}

}