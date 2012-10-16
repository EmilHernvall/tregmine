package info.tregmine.world.citadel;


import info.tregmine.Tregmine;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;


public class CitadelLimit implements Listener {
	private final Tregmine plugin;

	public CitadelLimit(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}


	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)	 {

		if (event.getPlayer().getWorld().getName().matches("citadel")) {		
			Player p = event.getPlayer();

			if(event.getTo().getBlockX() > 2048) {
				Location glLoc = new Location (p.getWorld(), p.getEyeLocation().getBlockX()+1,  p.getEyeLocation().getBlockY(),  p.getEyeLocation().getBlockZ() );
				event.getPlayer().sendBlockChange(glLoc, Material.GLASS, (byte) 0);
				movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}

			if(event.getTo().getBlockX() < -2048) {
				Location glLoc = new Location (p.getWorld(), p.getEyeLocation().getBlockX()-1,  p.getEyeLocation().getBlockY(),  p.getEyeLocation().getBlockZ() );
				event.getPlayer().sendBlockChange(glLoc, Material.GLASS, (byte) 0);
				movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}


			if(event.getTo().getBlockZ() > 2048) {
				Location glLoc = new Location (p.getWorld(), p.getEyeLocation().getBlockX(),  p.getEyeLocation().getBlockY(),  p.getEyeLocation().getBlockZ()+1 );
				event.getPlayer().sendBlockChange(glLoc, Material.GLASS, (byte) 0);
				event.getPlayer().sendBlockChange(event.getPlayer().getEyeLocation(), Material.GLASS, (byte) 0);
				movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}

			if(event.getTo().getBlockZ() < -2048) {
				Location glLoc = new Location (p.getWorld(), p.getEyeLocation().getBlockX(),  p.getEyeLocation().getBlockY(),  p.getEyeLocation().getBlockZ()-1 );
				event.getPlayer().sendBlockChange(glLoc, Material.GLASS, (byte) 0);
				event.getPlayer().sendBlockChange(event.getPlayer().getEyeLocation(), Material.GLASS, (byte) 0);
				movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location toLoc = event.getTo();
		if ("citadel".equals(toLoc.getWorld())) {		

			if(toLoc.getBlockX() > 2048) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
                return;
			}

			if(toLoc.getBlockX() < -2048) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
                return;
			}

			if(toLoc.getBlockZ() > 2048) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
                return;
			}

			if(toLoc.getBlockZ() < -2048) {
				event.getPlayer().sendMessage(ChatColor.YELLOW + "Sorry but your world is limited!");
				event.setCancelled(true);
                return;
			}
		}

	}

	@EventHandler
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
}
