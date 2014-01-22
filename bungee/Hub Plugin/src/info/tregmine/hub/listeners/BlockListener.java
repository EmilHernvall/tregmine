package info.tregmine.hub.listeners;

import info.tregmine.hub.Hub;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener
{
	private Hub plugin;

	public BlockListener(Hub instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void onHubBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		//TODO hook up to tregmine's DB
		if(!(p.isOp())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHubBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		//TODO hook up to tregmine's DB
		if(!(p.isOp())){
			e.setCancelled(true);
		}
	}
}