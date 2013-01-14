package info.tregmine.listeners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import info.tregmine.Tregmine;
//import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
//import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
//import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class TregmineBlockListener implements Listener {
	private final Tregmine plugin;

	public TregmineBlockListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void onBlockPlace (BlockPlaceEvent event)	{		
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
		}

		if (tregminePlayer.isAdmin()) {
			event.setCancelled(false);
		}

		plugin.blockStats.onBlockPlace(event);
	}

	@EventHandler
	public void onBlockBurn (BlockBurnEvent event) {		
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak	(BlockBreakEvent event) {
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
		}

		if (tregminePlayer.isAdmin()) {
			event.setCancelled(false);
		}
		plugin.blockStats.onBlockBreak(event);

		for (ItemStack item : event.getBlock().getDrops() ) {
			ItemMeta meta = item.getItemMeta();

			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = ConnectionPool.getConnection();

				String sql = "SELECT value FROM items_destroyvalue WHERE itemid = ?";

				stmt = conn.prepareStatement(sql);
				stmt.setLong(1, item.getTypeId());
				stmt.execute();

				rs = stmt.getResultSet();

				if (rs.first() ) {
					List<String> lore = new ArrayList<String>();
					tregminePlayer.sendMessage(item.toString());
					lore.add(ChatColor.GREEN + "MINED");
					lore.add(ChatColor.WHITE + "by: " + tregminePlayer.getChatName() );
					lore.add(ChatColor.WHITE + "Value: " + ChatColor.GOLD + rs.getInt("value") + ChatColor.WHITE + " Treg" );
					meta.setLore(lore);
					item.setItemMeta(meta);
				}

			} catch (SQLException e) {
				throw new RuntimeException(e);
			} finally {
				if (rs != null) {
					try { rs.close(); } catch (SQLException e) {} 
				}
				if (stmt != null) {
					try { stmt.close(); } catch (SQLException e) {}
				}
				if (conn != null) {
					try { conn.close(); } catch (SQLException e) {}
				}
			}
			
			
		}

	}

	@EventHandler
	public void onBlockCanBuild (BlockCanBuildEvent event) {
		//Test
	}

	@EventHandler
	public void onLeavesDecay	(LeavesDecayEvent event) {
		Location l = event.getBlock().getLocation();
		Block  fence = event.getBlock().getWorld().getBlockAt(l.getBlockX(),l.getBlockY()-1, l.getBlockZ());

		if (fence.getType() == Material.FENCE) {
			event.setCancelled(true);
		}
	}

	//	@EventHandler
	//	public void onBlockFlow	(BlockFromToEvent event) {
	//	}

	//	@EventHandler
	//	public void onSignChange(SignChangeEvent event)	{
	//	}


	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		event.setCancelled(true);


		Location loc = event.getBlock().getLocation();
		Block block = event.getBlock().getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ());

		if(block.getType() == Material.OBSIDIAN) {
			event.setCancelled(false);
		}

	}
}
