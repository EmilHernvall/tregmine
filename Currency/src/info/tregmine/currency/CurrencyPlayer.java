package info.tregmine.currency;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;


public class CurrencyPlayer implements Listener {
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
}