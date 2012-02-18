package info.tregmine.listeners;


import info.tregmine.Tregmine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class TregmineWorldListener implements Listener {
	private final Tregmine plugin;

	public TregmineWorldListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void onChunkUnloaded	(ChunkUnloadEvent event) {
		plugin.log.info("unload:" + event.getChunk().getWorld().toString());
	}
}
