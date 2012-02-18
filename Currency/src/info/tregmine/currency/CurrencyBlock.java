package info.tregmine.currency;

//import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockBreakEvent;

public class CurrencyBlock implements Listener {
    private final Main plugin;
	
    public CurrencyBlock(Main instance) {
        this.plugin = instance;
        plugin.getServer();
    }

    /*
	@EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
		if (!this.plugin.tregmine.blockStats.isPlaced(event.getBlock()) && !event.isCancelled() ) {
			Wallet wallet = new Wallet(event.getPlayer());
    		int value = Wallet.getBlockValue(event.getBlock().getTypeId());
    		wallet.add(value);
		}
    }
    */
    
}
