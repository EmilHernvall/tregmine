package info.tregmine.death;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;


public class DeathPlayer extends PlayerListener {
	public final Death plugin;

	public DeathPlayer(Death instance) {
		this.plugin = instance;
		plugin.getServer();
	}
	
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
		
		this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin,new Runnable() {
			public void run() {
				event.getPlayer().teleport(plugin.getServer().getWorld("world").getSpawnLocation());
				
			}},20L);
			
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		event.getPlayer().teleport(this.plugin.getServer().getWorld("world").getSpawnLocation());
//		plugin.getServer().broadcastMessage(event.getPlayer().getName() + " respawned!");
	}
}
