package info.tregmine.christmas;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

public class LightingFix implements Listener {

	private ChristmasMain plugin;

	public LightingFix(ChristmasMain instance)
	{
		this.plugin = instance;
	}


	@EventHandler
	public void onLightingGlitchOccur(final ChunkPopulateEvent e) {

		if(e.getWorld().getName() != "Christmas"){
			return;
		}

		if(plugin.getConfig().getString("smartlighting") == null){
			return;
		}
		
		if(plugin.getConfig().getString("smartlighting") != "true"){
			return;
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				World world = e.getWorld();
				Chunk chunk = e.getChunk();
				int x = chunk.getX();
				int z = chunk.getZ();
				world.refreshChunk(x-1, z-1);
				world.refreshChunk(x-1, z);
				world.refreshChunk(x-1, z+1);
				world.refreshChunk(x, z-1);
				world.refreshChunk(x, z);
				world.refreshChunk(x, z+1);
				world.refreshChunk(x+1, z-1);
				world.refreshChunk(x+1, z);
				world.refreshChunk(x+1, z+1);
			}
		},20 * 5);
	}
}
