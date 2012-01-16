package info.tregmine.listeners;

import info.tregmine.Tregmine;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;


public class TregmineBlockListener extends BlockListener {
	private final Tregmine plugin;

	public TregmineBlockListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	public void onBlockPlace (BlockPlaceEvent event)	{
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if(tregminePlayer.getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}
		
		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
		}

		if (tregminePlayer.isAdmin()) {
			event.setCancelled(false);
		}

		if(tregminePlayer.getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}
		
		if(tregminePlayer.getWorld().getName().matches("vanilla")) {
			event.setCancelled(false);
		}
		
		plugin.blockStats.onBlockPlace(event);
	}

	public void onBlockBurn (BlockBurnEvent event) {
		if(event.getBlock().getWorld().getName().matches("world")) {
			event.setCancelled(true);
		}

		if(event.getBlock().getWorld().getName().matches("citadel")) {
			event.setCancelled(true);
		}
		
		if(event.getBlock().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}
	}

	public void onBlockBreak	(BlockBreakEvent event) {
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());
		
		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
		}

		if (tregminePlayer.isAdmin()) {
			event.setCancelled(false);
		}

		if(tregminePlayer.getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}

		if(tregminePlayer.getWorld().getName().matches("vanilla")) {
			event.setCancelled(false);
		}
		
		
		plugin.blockStats.onBlockBreak(event);
	}

	public void onBlockCanBuild (BlockCanBuildEvent event) {
		//Test
	}

	public void onLeavesDecay	(LeavesDecayEvent event) {
		Location l = event.getBlock().getLocation();
		Block  fence = event.getBlock().getWorld().getBlockAt(l.getBlockX(),l.getBlockY()-1, l.getBlockZ());

		if (fence.getType() == Material.FENCE) {
			event.setCancelled(true);
		}
		
		if(event.getBlock().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}

	}

	public void onBlockFlow	(BlockFromToEvent event) {
		if(event.getBlock().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}
	}

	public void onSignChange(SignChangeEvent event)	{
		if(event.getBlock().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}
	}


	public void onBlockIgnite(BlockIgniteEvent event) {
		if(event.getBlock().getWorld().getName().matches("world")) {
			event.setCancelled(true);
		}

		if(event.getBlock().getWorld().getName().matches("citadel")) {
			event.setCancelled(true);
		}
		
		if(event.getBlock().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}

	}
}
