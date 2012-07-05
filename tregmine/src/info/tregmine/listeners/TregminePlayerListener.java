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
//import java.util.Random;
import java.util.TimeZone;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import org.bukkit.ChatColor;
//import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ocpsoft.pretty.time.PrettyTime;


public class TregminePlayerListener implements Listener {

	private static class RankComparator implements Comparator<TregminePlayer>
	{
		private int order;

		public RankComparator()
		{
			this.order = 1;
		}

		public RankComparator(boolean reverseOrder)
		{
			this.order = reverseOrder ? -1 : 1;
		}

		@Override
		public int compare(TregminePlayer a, TregminePlayer b)
		{
			return order * (a.getGuardianRank() - b.getGuardianRank());
		}
	}
/*
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
		ChatColor.YELLOW + "Quit - " + "%s" + "'s" + ChatColor.YELLOW + " CPU was killed by the Rendermen!",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " fell off the map!",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " logged out on accident!",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " found the IRL warp!",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " left the game due to IRL chunk error issues!",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " left the Matrix. Say hi to Morpheus!",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " <reserved space for ads. Contact a Senior Admin. Only 200k!>",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " disconnected? What is this!? Impossibru!",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " Be sure to visit the rifton general store! Follow the red line at /warp rifton",
		ChatColor.YELLOW + "Quit - " + "%s" + ChatColor.YELLOW + " Come to Exon (Near sunspot)"
	};
*/
	private final Tregmine plugin;

	public TregminePlayerListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {

		if (event.getPlayer().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
		if (event.getBucket() == Material.LAVA_BUCKET) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
		}

		if (tregminePlayer.isAdmin()) {
			event.setCancelled(false);
		}

		if (tregminePlayer.getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
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
					PrettyTime p = new PrettyTime();
					long blockid = rs.getLong("blockid");
					String player =  rs.getString("player");
					boolean placed = rs.getBoolean("status");
					Material mat = Material.getMaterial((int) blockid);

					if (placed == true) {
//						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + 
//								" placed by " + player + " at " + dfm.format(date));
						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + 
						" placed by " + player + " " + p.format(date));
						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + timezone + ": " + dfm.format(date));
					} else {
						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + mat.name().toLowerCase() + 
								" delete by " + player + " " + p.format(date));
						event.getPlayer().sendMessage(ChatColor.DARK_AQUA + timezone + ": " + dfm.format(date));
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

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);

		activateGuardians();
	}        

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		String playerName = player.getName();

		TregminePlayer tregPlayer = new TregminePlayer(player, playerName);

		if (player.getLocation().getWorld().getName().matches("world_the_end")) {
			player.teleport(this.plugin.getServer().getWorld("world").getSpawnLocation());
		}

		
		
		
		if(tregPlayer.exists()) {
			tregPlayer.load();
		} else {
			tregPlayer.create();
			tregPlayer.load();
		}

		if (tregPlayer.isBanned()) {
//			event.setKickMessage("You are not allowed on this server!");
			event.disallow(Result.KICK_BANNED, "You shall not pass!");
		} else  {
			this.plugin.tregminePlayer.put(playerName, tregPlayer);
		}

		if (event.getPlayer().getServer().getPort() == 1337 && tregPlayer.getNameColor() == ChatColor.GRAY) {
			event.disallow(Result.KICK_BANNED, "Sorry warned players are not allowed on creative server!");
		}

		
		
		if (tregPlayer.isGuardian()) {
			tregPlayer.setGuardianState(TregminePlayer.GuardianState.QUEUED);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
//		TregminePlayer tregP = this.plugin.tregminePlayer.get(event.getPlayer().getName());

//		if(!event.getPlayer().isOp()) {
//			Random rand = new Random();
//			int msgIndex = rand.nextInt(quitMessages.length);
//			String message = String.format(quitMessages[msgIndex], tregP.getChatName());
//			this.plugin.getServer().broadcastMessage(message);
//		}

		this.plugin.tregminePlayer.remove(event.getPlayer().getName());
		this.plugin.log.info("Unloaded settings for " + event.getPlayer().getName() + ".");

		activateGuardians();
	}        

	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		Player player = this.plugin.getServer().getPlayer(event.getName());
		if (player != null) {
			player.kickPlayer("Sorry, we don't allow clones on this server.");
		}
	}

	public void onPlayerMove(PlayerMoveEvent event)     { // if player move
	}

	public void onPlayerTeleport(PlayerMoveEvent event)     { // if player teleport
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event){
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

		if (event.getPlayer().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		TregminePlayer tregminePlayer = this.plugin.tregminePlayer.get(event.getPlayer().getName());

		if (tregminePlayer.isAdmin()) {
			return;
		}

		if (!tregminePlayer.isTrusted()) {
			event.setCancelled(true);
			return;
		}

		if (event.getPlayer().getWorld().getName().matches("alpha")) {
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		event.setLeaveMessage(null);
	}

	private void activateGuardians() {

		// Identify all guardians and categorize them based on their current state
		Player[] players = plugin.getServer().getOnlinePlayers();
		Set<TregminePlayer> guardians = new HashSet<TregminePlayer>();
		List<TregminePlayer> activeGuardians = new ArrayList<TregminePlayer>();
		List<TregminePlayer> inactiveGuardians = new ArrayList<TregminePlayer>();
		List<TregminePlayer> queuedGuardians = new ArrayList<TregminePlayer>();
		for (Player srvPlayer : players) {
			TregminePlayer guardian = plugin.getPlayer(srvPlayer.getName());
			if (guardian == null || !guardian.isGuardian()) {
				continue;
			}

			TregminePlayer.GuardianState state = guardian.getGuardianState();
			if (state == null) {
				state = TregminePlayer.GuardianState.QUEUED;
			}

			switch (state) {
			case ACTIVE:
				activeGuardians.add(guardian);    
				break;
			case INACTIVE:
				inactiveGuardians.add(guardian);
				break;
			case QUEUED:
				queuedGuardians.add(guardian);
				break;
			}

			guardian.setGuardianState(TregminePlayer.GuardianState.QUEUED);
			guardians.add(guardian);
		}

		Collections.sort(activeGuardians, new RankComparator());
		Collections.sort(inactiveGuardians, new RankComparator(true));
		Collections.sort(queuedGuardians, new RankComparator());

		int idealCount = (int)Math.ceil(Math.sqrt(players.length)/2);
		// There are not enough guardians active, we need to activate a few more
		if (activeGuardians.size() <= idealCount) {
			// Make a pool of every "willing" guardian currently online
			List<TregminePlayer> activationList = new ArrayList<TregminePlayer>();
			activationList.addAll(activeGuardians);
			activationList.addAll(queuedGuardians);

			// If the pool isn't large enough to satisfy demand, we add the guardians
			// that have made themselves inactive as well.
			if (activationList.size() < idealCount) {
				int diff = idealCount - activationList.size();
				// If there aren't enough of these to satisfy demand, we add all of them
				if (diff >= inactiveGuardians.size()) {
					activationList.addAll(inactiveGuardians);
				}
				// Otherwise we just add the lowest ranked of the inactive
				else {
					activationList.addAll(inactiveGuardians.subList(0, diff));
				}
			}

			// If there are more than necessarry guardians online, only activate
			// the most highly ranked.
			Set<TregminePlayer> activationSet;
			if (activationList.size() > idealCount) {
				Collections.sort(activationList, new RankComparator());
				activationSet = new HashSet<TregminePlayer>(activationList.subList(0, idealCount));
			} else {
				activationSet = new HashSet<TregminePlayer>(activationList);
			}

			// Perform activation
			StringBuffer globalMessage = new StringBuffer();
			String delim = "";
			for (TregminePlayer guardian : activationSet) {
				guardian.setGuardianState(TregminePlayer.GuardianState.ACTIVE);
				globalMessage.append(delim);
				globalMessage.append(guardian.getName());
				delim = ", ";
			}

			Set<TregminePlayer> oldActiveGuardians = new HashSet<TregminePlayer>(activeGuardians);
			if (!activationSet.containsAll(oldActiveGuardians) || activationSet.size() != oldActiveGuardians.size()) {

				plugin.getServer().broadcastMessage(ChatColor.BLUE + "Active guardians are: " + globalMessage + ". Please contact any of them if you need help.");

				// Notify previously active guardian of their state change
				for (TregminePlayer guardian : activeGuardians) {
					if (!activationSet.contains(guardian)) {
						guardian.sendMessage(ChatColor.BLUE + "You are no longer on active duty, and should not respond to help requests, unless asked by an admin or active guardian.");
					}
				}

				// Notify previously inactive guardians of their state change
				for (TregminePlayer guardian : inactiveGuardians) {
					if (activationSet.contains(guardian)) {
						guardian.sendMessage(ChatColor.BLUE + "You have been restored to active duty and should respond to help requests.");
					}
				}

				// Notify previously queued guardians of their state change
				for (TregminePlayer guardian : queuedGuardians) {
					if (activationSet.contains(guardian)) {
						guardian.sendMessage(ChatColor.BLUE + "You are now on active duty and should respond to help requests.");
					}
				}
			}
		}

	}
}
