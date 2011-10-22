package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.bukkit.ChatColor;
//import org.bukkit.GameMode;
import org.bukkit.GameMode;
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
    private final static String[] quitMessages = new String[] {
            ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " deserted from the battlefield with a hearty good bye!",
    		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " stole the cookies and ran!",
    		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " was eaten by a teenage mutant ninja platypus!",
    		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " parachuted of the plane and into the unknown!",
            ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " stole the cookies and ran!",
            ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " was eaten by a teenage mutant ninja creeper!",
            ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " jumped off the plane with a cobble stone parachute!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " built Rome in one day and now deserves a break!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " will come back soon because Tregmine is awesome!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " leaves the light and enter darkness.",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " disconnects from a better life.",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " already miss the best friends in the world!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " will build something epic next time.",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " is not banned yet!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " has left our world!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " went to browse Tregmine's forums instead!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " was scared away by einand! :(",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + "'s CPU was killed by the Rendermen!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " fell off the map!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " logged out on accident!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " found the IRL warp!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " left the game due to IRL chunk error issues!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " left the Matrix. Say hi to Morpheus!",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " <preserved space for ads. Contact a Senior Admin. Only 200k!>",
			ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " disconnected? What is this!? Impossibru!"
        };

	private final Tregmine plugin;

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

		if (tregminePlayer.isAdmin()) {
			event.setCancelled(false);
		}

		if (event.getPlayer().getItemInHand().getTypeId() == Material.PAPER.getId() 
				&& event.getAction() == Action.RIGHT_CLICK_BLOCK ) {

			Location block = event.getClickedBlock().getLocation();
			java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
			String pos = block.getX() + "," + block.getY() + "," + block.getZ();
			crc32.update(pos.getBytes());
			long checksum = crc32.getValue();
			String timezone = tregminePlayer.getTimezone(); 

			SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
			dfm.setTimeZone(TimeZone.getTimeZone(timezone));

			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = ConnectionPool.getConnection();

				stmt = conn.prepareStatement("SELECT * FROM  stats_blocks WHERE checksum = ? " +
						"ORDER BY time DESC LIMIT 5");
				stmt.setLong(1, checksum);
				stmt.execute();

				rs = stmt.getResultSet();
				
				//TODO : Reverse the sorting order
				while (rs.next()) {
					Date date = new Date(rs.getLong("time"));
					long blockid = rs.getLong("blockid");
					String player =  rs.getString("player");
					boolean placed = rs.getBoolean("status");
					Material mat = Material.getMaterial((int) blockid);

					if (placed == true) {
						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + 
								" placed by " + player + " at " + dfm.format(date));
					} else {
						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + 
								" delete by " + player + " at " + dfm.format(date));    			        	
					}
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

	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		if (!event.getPlayer().isOp()) {
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
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
			event.disallow(Result.KICK_BANNED, "You shall not pass!");
		} else  {
			this.plugin.tregminePlayer.put(playerName, tregPlayer);
		}

	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		TregminePlayer tregP = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if(!event.getPlayer().isOp()) {
			Random rand = new Random();
			int msgIndex = rand.nextInt(quitMessages.length);
			String message = String.format(quitMessages[msgIndex], tregP.getChatName());
			this.plugin.getServer().broadcastMessage(message);
		}

		this.plugin.tregminePlayer.remove(event.getPlayer().getName());
		this.plugin.log.info("Unloaded settings for " + event.getPlayer().getName() + ".");
	}    	

	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = this.plugin.getServer().getPlayer(event.getName());
		if (player != null) {
			player.kickPlayer("Sorry, we don't allow clones on this server.");
		}
	}

	public void onPlayerMove(PlayerMoveEvent event)	 { // if player move
	}

	public void onPlayerTeleport(PlayerMoveEvent event)	 { // if player teleport
	}

	public void onPlayerPickupItem (PlayerPickupItemEvent event){
		TregminePlayer tregminePlayer;

		try {
			tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());
		} catch (Exception e) {
			e.printStackTrace();
			event.setCancelled(true);
			return;
		}

		if (tregminePlayer.isAdmin()) {
			return;
		}


		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
			return;
		}
	}

	public void onPlayerDropItem (PlayerDropItemEvent event) {
		TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if (tregminePlayer.isAdmin()) {
			return;
		}

		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
			return;
		}
	}

	public void	onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}
}
