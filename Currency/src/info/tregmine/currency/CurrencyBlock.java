package info.tregmine.currency;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class CurrencyBlock extends BlockListener {
    private final Main plugin;
	
    public CurrencyBlock(Main instance) {
        this.plugin = instance;
        plugin.getServer();
    }

    public void onBlockBreak (BlockBreakEvent event) {
		if (!this.plugin.tregmine.blockStats.isPlaced(event.getBlock()) && !event.isCancelled() ) {
			Wallet wallet = new Wallet(event.getPlayer());
    		int value = Wallet.getBlockValue(event.getBlock().getTypeId());
    		wallet.add(value);
		}
    }
    
}
