package info.tregmine.hub.listeners;

import java.util.ArrayList;

import info.tregmine.hub.Hub;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class BoostListener implements Listener
{
	private Hub plugin;

	public BoostListener(Hub instance)
	{
		this.plugin = instance;
	}

	private ArrayList<Player> boosters = new ArrayList<Player>();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location playerLoc = player.getLocation();
		Material check = playerLoc.getWorld().getBlockAt(playerLoc).getRelative(0, -2, 0).getType();
		Material plate = playerLoc.getWorld().getBlockAt(playerLoc).getType();

		if (check != Material.EMERALD_BLOCK){
			return;
		}
		if (plate != Material.GOLD_PLATE && 
				plate != Material.IRON_PLATE &&
				plate != Material.STONE_PLATE &&
				plate != Material.WOOD_PLATE){
			return;
		}
		//Launch the player
		player.setVelocity(player.getLocation().getDirection().multiply(4));
		player.setVelocity(new Vector(player.getVelocity().getX(), 1.5D, player.getVelocity().getZ()));

		boosters.add(player);
	}


	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL && boosters.contains(p)) {
				e.setDamage(0.0);
				boosters.remove(p);
			}
		}
	}

	@EventHandler
	public void onPortalCreate(BlockPhysicsEvent e) {
		if(e.getBlock().getType() == Material.PORTAL){
			e.setCancelled(true);
		}
	}
}