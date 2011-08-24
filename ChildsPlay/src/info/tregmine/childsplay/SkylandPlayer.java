package info.tregmine.childsplay;

import org.bukkit.Location;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;


public class SkylandPlayer extends PlayerListener {
	private final Main plugin;

	public SkylandPlayer(Main instance) {
		plugin = instance;
		plugin.getServer();
	}


	public void onPlayerMove(PlayerMoveEvent event)	 { // if player move


		if(
				event.getPlayer().getWorld().getName().matches("skyland")  &&
				event.getPlayer().getLocation().getBlockY() <= 1 &&
				event.getPlayer().getLocation().getBlockX() <= 537 &&
				event.getPlayer().getLocation().getBlockX() >= 533 &&
				event.getPlayer().getLocation().getBlockZ() == -1093 &&
				!this.plugin.skylandBlock.containsKey(event.getPlayer().getName())
		) {

			Location loc = new Location(this.plugin.getServer().getWorld("world"), 760.625, 123.5, -357.0625);
			event.getPlayer().teleport(loc);
			this.plugin.skylandBlock.put(event.getPlayer().getName(), true);
		}
		
		
		
		if (this.plugin.skylandBlock.containsKey(event.getPlayer().getName()) && event.getPlayer().getWorld().getName().matches("skyland")) {
			event.setCancelled(true);
		}
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if(
				event.getAction() == Action.PHYSICAL &&
				(info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == -1343863112 ||
						info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == 1597794487 ||
						info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == 244484790 ||
						info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == -1108824907 ||
						info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == 1832832692
			    )
		) {
			
			Location loc = new Location(this.plugin.getServer().getWorld("world"), 760.625, 123.5, -357.0625, -270.1499F, 46.95F);
			event.getPlayer().teleport(loc);
			
		}

		
		if(
				event.getAction() == Action.PHYSICAL &&
				(info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == 2096737327 ||
						info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == -401278610 ||
						info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == 1395672749 ||
						info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == -1102343188 ||
						info.tregmine.api.math.Checksum.block(event.getClickedBlock()) == 694608171
			    )
		) {
			
//			Location loc = new Location(this.plugin.getServer().getWorld("world"), 760.625, 123.5, -357.0625);
			Location loc = this.plugin.getServer().getWorld("skyland").getSpawnLocation();
			loc.setPitch(-10.800016F);
			loc.setYaw(-177.7501F);

			event.getPlayer().teleport(loc);
			
		}

	}
}