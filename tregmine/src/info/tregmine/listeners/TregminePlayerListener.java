package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.ChatColor;
//import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
//import org.bukkit.entity.Player;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
//import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class TregminePlayerListener extends PlayerListener {
	private final Tregmine plugin;
	public final info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();

	public TregminePlayerListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		
		if (event.getBucket() == Material.LAVA_BUCKET) {
			 event.setCancelled(true);
		}
		
		if (event.getBlockClicked().getType() == Material.LAVA) {
			event.setCancelled(true);
		}
		
		if (event.getBlockClicked().getType() == Material.STATIONARY_LAVA) {
			event.setCancelled(true);
		}
		
	}

	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
		if (event.getBucket() == Material.LAVA_BUCKET) {
		 event.setCancelled(true);
		}
	}

	
	public void onPlayerInteract(PlayerInteractEvent event) {
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());
		
		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
		}

		if (tregminePlayer.isAdmin() && tregminePlayer.isChild() && !event.getPlayer().getWorld().getName().matches("ChildsPlay") ) {
			event.setCancelled(true);
		}

		if (!tregminePlayer.isChild() && event.getPlayer().getWorld().getName().matches("ChildsPlay") ) {
			event.setCancelled(true);
		}
		
		if (tregminePlayer.isAdmin()) {
			event.setCancelled(false);
		}
		
		
		
//		if (event.getPlayer().getItemInHand().getTypeId() == Material.PAPER.getId() && event.getAction() == Action.RIGHT_CLICK_BLOCK && tregminePlayer.isAdmin() ) {
		
		if (event.getPlayer().getItemInHand().getTypeId() == Material.PAPER.getId() && event.getAction() == Action.RIGHT_CLICK_BLOCK ) {

			Location block = event.getClickedBlock().getLocation();
			java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
			String pos = block.getX() + "," + block.getY() + "," + block.getZ();
			crc32.update(pos.getBytes());
			long checksum = crc32.getValue();
			String timezone = tregminePlayer.getTimezone(); 

			SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
			dfm.setTimeZone(TimeZone.getTimeZone(timezone));

			try {
				this.mysql.connect();
				this.mysql.statement.executeQuery("SELECT * FROM  stats_blocks WHERE checksum='" +  checksum + "' ORDER BY time DESC LIMIT 5");
				ResultSet rs = this.mysql.statement.getResultSet();
				
				//TODO : Reverse the sorting order
				while (rs.next()) {
					Date date = new Date(rs.getLong("time"));
					long blockid = rs.getLong("blockid");
					String player =  rs.getString("player");
					boolean placed = rs.getBoolean("status");
					Material mat = Material.getMaterial((int) blockid);

					if (placed == true) {
						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + " placed by " + player + " at " + dfm.format(date));
					} else {
						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + " delete by " + player + " at " + dfm.format(date));    			        	
					}
				}
				this.mysql.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
//		event.getPlayer().setGameMode(GameMode.SURVIVAL);
	}    	

	public void onPlayerLogin(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		String playerName = player.getName();

		TregminePlayer tregPlayer = new TregminePlayer(player, playerName);

		if(tregPlayer.exists()) {
			tregPlayer.load();
		} else {
			tregPlayer.create();
			tregPlayer.load();
		}
		
		if (tregPlayer.isBanned()) {
			event.setKickMessage("You are not allowed on this server!");
			event.disallow(Result.KICK_BANNED, "You are not allowed on this server!");
		} else  {
			this.plugin.tregminePlayer.put(playerName, tregPlayer);
		}

	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		info.tregmine.api.TregminePlayer tregP = this.plugin.tregminePlayer.get(event.getPlayer().getName());
		if(!event.getPlayer().getName().matches("einand")) {
			this.plugin.getServer().broadcastMessage(tregP.getChatName() +ChatColor.YELLOW + " disconnected from the matrix, say hi to Morpheus!");
		}
		this.plugin.tregminePlayer.remove(event.getPlayer().getName());
		this.plugin.log.info("Unloaded settings for " + event.getPlayer().getName());
	}    	

	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = this.plugin.getServer().getPlayer(event.getName());
		if (player != null) {
			player.kickPlayer("Sorry, we don't allow clones on this server");
		}
	}
	
	public void onPlayerMove(PlayerMoveEvent event)	 { // if player move
	}

	public void onPlayerTeleport(PlayerMoveEvent event)	 { // if player teleport
	}

	public void onPlayerPickupItem (PlayerPickupItemEvent event){
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if (tregminePlayer.isAdmin()) {
			return;
		}

		
		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
			return;
		}

		if (tregminePlayer.isChild() && !event.getPlayer().getWorld().getName().matches("ChildsPlay") ) {
			event.setCancelled(true);
			return;
		}

		if (!tregminePlayer.isChild() && event.getPlayer().getWorld().getName().matches("ChildsPlay") ) {
			event.setCancelled(true);
			return;
		}


	}

	public void onPlayerDropItem (PlayerDropItemEvent event) {
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if (tregminePlayer.isAdmin()) {
			return;
		}

		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
			return;
		}

		if (tregminePlayer.isChild() && !event.getPlayer().getWorld().getName().matches("ChildsPlay") ) {
			event.setCancelled(true);
			return;
		}

		if (!tregminePlayer.isChild() && event.getPlayer().getWorld().getName().matches("ChildsPlay") ) {
			event.setCancelled(true);
			return;
		}
	}

	public void	onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}
}