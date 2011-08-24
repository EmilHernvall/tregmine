package info.tregmine.currency;
	import org.bukkit.event.player.PlayerEvent;
	import org.bukkit.event.player.PlayerLoginEvent;
	import org.bukkit.event.player.PlayerListener;


	public class CurrencyPlayer extends PlayerListener {
	    private final Main plugin;
			
	    public CurrencyPlayer(Main instance) {
	        plugin = instance;
	    	plugin.getServer();
	    }

	    public void onPlayerJoin(PlayerEvent event) {
	    }    	
	    
	    public void onPlayerLogin (PlayerLoginEvent event){
	    	Wallet wallet = new Wallet(event.getPlayer());
	    	wallet.create();
	     }
	    
	    public void onPlayerQuit(PlayerEvent event) {
	    }    	
	}