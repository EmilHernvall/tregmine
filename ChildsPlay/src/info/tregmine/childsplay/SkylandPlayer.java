package info.tregmine.childsplay;

import info.tregmine.api.TregminePlayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;


public class SkylandPlayer extends PlayerListener {
	private final Main plugin;

	public SkylandPlayer(Main instance) {
		plugin = instance;
		plugin.getServer();
	}


	public void onPlayerMove(PlayerMoveEvent event)	 {

		if (event.getPlayer().getWorld().getName().matches("citadel")) {		
			Player p = event.getPlayer();

			if(event.getTo().getBlockX() > 1024) {
				Location glLoc = new Location (p.getWorld(), p.getEyeLocation().getBlockX()+1,  p.getEyeLocation().getBlockY(),  p.getEyeLocation().getBlockZ() );
				event.getPlayer().sendBlockChange(glLoc, Material.GLASS, (byte) 0);
				movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}

			if(event.getTo().getBlockX() < -1024) {
				Location glLoc = new Location (p.getWorld(), p.getEyeLocation().getBlockX()-1,  p.getEyeLocation().getBlockY(),  p.getEyeLocation().getBlockZ() );
				event.getPlayer().sendBlockChange(glLoc, Material.GLASS, (byte) 0);
				movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}


			if(event.getTo().getBlockZ() > 1024) {
				Location glLoc = new Location (p.getWorld(), p.getEyeLocation().getBlockX(),  p.getEyeLocation().getBlockY(),  p.getEyeLocation().getBlockZ()+1 );
				event.getPlayer().sendBlockChange(glLoc, Material.GLASS, (byte) 0);
				event.getPlayer().sendBlockChange(event.getPlayer().getEyeLocation(), Material.GLASS, (byte) 0);
				movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}

			if(event.getTo().getBlockZ() < -1024) {
				Location glLoc = new Location (p.getWorld(), p.getEyeLocation().getBlockX(),  p.getEyeLocation().getBlockY(),  p.getEyeLocation().getBlockZ()-1 );
				event.getPlayer().sendBlockChange(glLoc, Material.GLASS, (byte) 0);
				event.getPlayer().sendBlockChange(event.getPlayer().getEyeLocation(), Material.GLASS, (byte) 0);
				movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}
		}
	}


	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getPlayer().getWorld().getName().matches("citadel")) {		

			if(event.getTo().getBlockX() > 1024) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}

			if(event.getTo().getBlockX() < -1024) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}

			if(event.getTo().getBlockZ() > 1024) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}

			if(event.getTo().getBlockZ() < -1024) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}
		}

	}

	private void movePlayerBack(Player player, Location movingFrom, Location movingTo)
	{
		Vector a = new Vector(movingFrom.getX(), movingFrom.getY(), movingFrom.getZ());
		Vector b = new Vector(movingTo.getX(), movingTo.getY(), movingTo.getZ());

		Vector diff = b.subtract(a);
		diff = diff.multiply(-5);

		Vector newPosVector = a.add(diff);
		Location newPos = new Location(player.getWorld(), newPosVector.getX(), newPosVector.getY(), newPosVector.getZ());
		player.teleport(newPos);
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