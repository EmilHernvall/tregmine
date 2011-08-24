package info.tregmine.listeners;

//import java.io.FileOutputStream;
//import java.io.PrintStream;

import info.tregmine.Tregmine;

import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;


public class TregmineWorldListener extends WorldListener {
	private final Tregmine plugin;

	public TregmineWorldListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	public void onChunkUnloaded	(ChunkUnloadEvent event) {
		//	plugin.log.info("unload:" + event.getChunk().getWorld().toString());
	}
}
