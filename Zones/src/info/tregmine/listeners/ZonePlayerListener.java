package info.tregmine.listeners;

import java.sql.Connection;
import java.sql.SQLException;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Zone;
import info.tregmine.currency.Wallet;
import info.tregmine.database.ConnectionPool;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.ZoneWorld;
import info.tregmine.zones.ZonesDAO;
import info.tregmine.zones.ZonesPlugin;
import info.tregmine.zones.Lot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
//import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
//import org.bukkit.event.painting.PaintingBreakByEntityEvent;
//import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

public class ZonePlayerListener implements Listener
{
	private final ZonesPlugin plugin;
	private final Tregmine tregmine;

	public ZonePlayerListener(ZonesPlugin instance) 
	{
		this.plugin = instance;
		this.tregmine = instance.tregmine;
	}


	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
		TregminePlayer player = tregmine.getPlayer(event.getPlayer());
		if (player.isAdmin()) {
			return;
		}

		ZoneWorld world = plugin.getWorld(player.getWorld());

		Block block = event.getBlockClicked();
		Location location = block.getLocation();
		Point pos = new Point(location.getBlockX(), location.getBlockZ());

		Zone currentZone = player.getCurrentZone();
		if (currentZone == null || !currentZone.contains(pos)) {
			currentZone = world.findZone(pos);
			player.setCurrentZone(currentZone);
		}

		if (currentZone != null) {
			Zone.Permission perm = currentZone.getUser(player.getName());

			Lot lot = world.findLot(pos);
			if (lot != null) {
				if (perm != Zone.Permission.Owner && !lot.isOwner(player.getName())) {
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are not allowed to break blocks in lot " + lot.getName() + ".");
					event.setCancelled(true);
					return;
				}

				return;
			}

			// if everyone is allowed to build in this zone...
			if (currentZone.getDestroyDefault()) {
				// ...the only people that can't build are those that are banned
				if (perm != null && perm == Zone.Permission.Banned) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are banned from " + currentZone.getName() + ".");	    			
				}
			} 
			// if this zone has limited building privileges...
			else {
				// ...we only allow builders and owners to make changes.
				if (perm == null || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
					player.setFireTicks(50);
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are not allowed to break blocks in " + currentZone.getName() + ".");
				}
			}
		}

	}


	@EventHandler
	public void onHangingPlace(HangingPlaceEvent event){
		TregminePlayer player = tregmine.getPlayer(event.getPlayer());
		if (player.isAdmin()) {
			return;
		}

		ZoneWorld world = plugin.getWorld(player.getWorld());


		//		Block block = event.getBlockClicked();
		Location location = event.getBlock().getLocation();
		Point pos = new Point(location.getBlockX(), location.getBlockZ());

		Zone currentZone = player.getCurrentZone();
		if (currentZone == null || !currentZone.contains(pos)) {
			currentZone = world.findZone(pos);
			player.setCurrentZone(currentZone);
		}

		if (currentZone != null) {
			Zone.Permission perm = currentZone.getUser(player.getName());

			Lot lot = world.findLot(pos);
			if (lot != null) {
				if (perm != Zone.Permission.Owner && !lot.isOwner(player.getName())) {
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are not allowed to place paintings in lot " + lot.getName() + ".");
					event.setCancelled(true);
					return;
				}

				return;
			}

			// if everyone is allowed to build in this zone...
			if (currentZone.getDestroyDefault()) {
				// ...the only people that can't build are those that are banned
				if (perm != null && perm == Zone.Permission.Banned) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are banned from " + currentZone.getName() + ".");	    			
				}
			} 
			// if this zone has limited building privileges...
			else {
				// ...we only allow builders and owners to make changes.
				if (perm == null || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
					player.setFireTicks(50);
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are not allowed to place paintings in " + currentZone.getName() + ".");
				}
			}
		}

	}

	@EventHandler
	public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event){

		Entity entity = event.getRemover();

		if (!(entity instanceof Player)) {
			return;
		}


		TregminePlayer player = tregmine.getPlayer((Player) event.getRemover());
		if (player.isAdmin()) {
			return;
		}

		ZoneWorld world = plugin.getWorld(player.getWorld());


		//		Block block = event.getBlockClicked();



		Location location = player.getLocation();
		Point pos = new Point(location.getBlockX(), location.getBlockZ());

		Zone currentZone = player.getCurrentZone();
		if (currentZone == null || !currentZone.contains(pos)) {
			currentZone = world.findZone(pos);
			player.setCurrentZone(currentZone);
		}

		if (currentZone != null) {
			Zone.Permission perm;

			try {
				perm = currentZone.getUser(player.getName());
			} catch (Exception e) {
				player.sendMessage("ERROR");
				e.printStackTrace();
				return;
			}


			Lot lot = world.findLot(pos);
			if (lot != null) {
				if (perm != Zone.Permission.Owner && !lot.isOwner(player.getName())) {
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are not allowed to destroy paintings in lot " + lot.getName() + ".");
					event.setCancelled(true);
					return;
				}

				return;
			}

			// if everyone is allowed to build in this zone...
			if (currentZone.getDestroyDefault()) {
				// ...the only people that can't build are those that are banned
				if (perm != null && perm == Zone.Permission.Banned) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are banned from " + currentZone.getName() + ".");	    			
				}
			} 
			// if this zone has limited building privileges...
			else {
				// ...we only allow builders and owners to make changes.
				if (perm == null || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
					player.setFireTicks(50);
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
							"You are not allowed to destroy paintings in " + currentZone.getName() + ".");
				}
			}
		}

	}


	//	@SuppressWarnings("null")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) 
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		if(event.getClickedBlock().getState() instanceof Sign) {

			TregminePlayer player = tregmine.getPlayer(event.getPlayer());

			Sign block = (Sign) event.getClickedBlock().getState();

			Point currentPos = new Point(block.getX(), block.getZ());

			ZoneWorld world = plugin.getWorld(block.getWorld());
			//			Zone zone = world.findZone(currentPos);
			Lot lot = world.findLot(currentPos);

			//			player.sendMessage("" + lot.getName());				

			if (block.getLine(0).contains("FOR SALE")) {
				if (lot.isOwner(player.getName())) {
					player.sendMessage(ChatColor.YELLOW + "You already owns this " + lot.getName());
					return;
				} else {
					Wallet wallet = new Wallet(player.getName());
					int price = Integer.parseInt(ChatColor.stripColor( block.getLine(1) ).trim() );
					String  seller = ChatColor.stripColor( block.getLine(2) ).trim();
					Wallet swallet = new Wallet(seller);

					if (wallet.take(price))  {

						Connection conn = null;
						try {
							conn = ConnectionPool.getConnection();
							ZonesDAO dao = new ZonesDAO(conn);
							int userId = dao.getUserId(player.getName());
							lot.addOwner(player.getName());
							lot.deleteOwner(seller);
							dao.addLotUser(lot.getId(), userId);
							player.sendMessage(ChatColor.YELLOW + "You are now owner of " + lot.getName());
							this.plugin.tregmine.log.info(player.getName() + " got " + lot.getName() + " for " + price);
							this.plugin.tregmine.log.info(seller + " sold " + lot.getName() + " for " + price);

							swallet.add(price);

							if(this.plugin.getServer().getPlayer(seller).isOnline()) {
								this.plugin.getServer().getPlayer(seller).sendMessage(ChatColor.YELLOW + player.getChatName() + ChatColor.YELLOW + " just got your lot " + lot.getName());
							}
							event.getClickedBlock().breakNaturally();

						} catch (SQLException e) {
							throw new RuntimeException(e);
						} finally {
							if (conn != null) {
								try { conn.close(); } catch (SQLException e) {}
							}
						}

					}


				}



				player.sendMessage("for sale" + lot.getName());				
			}

		}

		if (event.getPlayer().getItemInHand().getType() != Material.STICK) {
			return;
		}

		TregminePlayer player = tregmine.getPlayer(event.getPlayer());

		Block block = event.getClickedBlock();
		Point currentPos = new Point(block.getX(), block.getZ());

		ZoneWorld world = plugin.getWorld(block.getWorld());
		if (world == null) {
			return;
		}

		Zone zone = world.findZone(currentPos);
		Lot lot = world.findLot(currentPos);

		// within a zone, lots can be created by zone owners or people with
		// the zones permission.
		String type = null;
		if (zone != null) {
			Zone.Permission perm = zone.getUser(player.getName());
			if (perm != Zone.Permission.Owner && !player.getMetaBoolean("zones")) {
				return;
			}
			if (lot != null) {
				player.sendMessage("This lot is called " + lot.getName() + ".");
				return;
			}
			type = "lot";
		}
		// outside of a zone 
		else {
			// outside of any existing zone, this can only be used by people
			// with zones permission.
			if (!player.getMetaBoolean("zones")) {
				return;
			}
			type = "zone";
		}

		int count;
		try {
			count = player.getMetaInt("zcf");
		} catch (Exception  e) {
			count = 0;
		}

		if (count == 0) {
			player.setBlock("zb1", block);
			player.setBlock("zb2", null);
			event.getPlayer().sendMessage("First block set of new " + type + ".");
			player.setMetaInt("zcf", 1);
			if (zone != null) {
				player.setMetaInt("zone", zone.getId());
			} else {
				player.setMetaInt("zone", 0);
			}
		}

		if (count == 1) {
			int zf = player.getMetaInt("zone");
			if (zf != 0 && zf != zone.getId()) {
				player.sendMessage("The full extent of the lot must be in the same zone.");
				return;
			}

			player.setBlock("zb2", block);
			player.sendMessage("Second block set of new " + type + ".");
			player.setMetaInt("zcf", 0);
		}
	}

	private void movePlayerBack(TregminePlayer player, Location movingFrom, Location movingTo)
	{
		Vector a = new Vector(movingFrom.getX(), movingFrom.getY(), movingFrom.getZ());
		Vector b = new Vector(movingTo.getX(), movingTo.getY(), movingTo.getZ());

		Vector diff = b.subtract(a);
		diff = diff.multiply(-5);

		Vector newPosVector = a.add(diff);
		Location newPos = new Location(player.getWorld(), newPosVector.getX(), newPosVector.getY(), newPosVector.getZ());
		player.teleport(newPos);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		TregminePlayer player = tregmine.getPlayer(event.getPlayer());
		ZoneWorld world = plugin.getWorld(player.getWorld());

		Location movingFrom = event.getFrom();
		Point oldPos = new Point(movingFrom.getBlockX(), movingFrom.getBlockZ());

		Location movingTo = event.getTo();
		Point currentPos = new Point(movingTo.getBlockX(), movingTo.getBlockZ());

		Zone currentZone = player.getCurrentZone();
		if (currentZone == null || !currentZone.contains(currentPos)) {

			if (currentZone != null && currentZone.contains(oldPos)) {
				player.setCurrentTexture("https://dl.dropbox.com/u/5405236/mc/df.zip");	
				player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + currentZone.getTextExit());
			}

			currentZone = world.findZone(currentPos);
			if (currentZone != null) {
				Zone.Permission perm = currentZone.getUser(player.getName());

				// if anyone is allowed to enter by default...
				if (currentZone.getEnterDefault()) {
					// ...we only need to reject banned players
					if (player.isAdmin()) {
						// never applies to admins
					}
					else if (perm != null && perm == Zone.Permission.Banned) {
						bannedMessage(currentZone, player);
						player.teleport( player.getWorld().getSpawnLocation() );
						//movePlayerBack(player, movingFrom, movingTo);
						return;
					}
				}
				// if this is a whitelist zone...
				else {
					// ...reject people not in the user list, as well as banned people
					if (player.isAdmin()) {
						// never applies to admins
					}
					else if (perm == null) {
						disallowedMessage(currentZone, player);
						movePlayerBack(player, movingFrom, movingTo);
						return;
					}
					else if (perm == Zone.Permission.Banned) {
						bannedMessage(currentZone, player);
						//						bannedMessage(currentZone, player);
						player.teleport( player.getWorld().getSpawnLocation() );
						return;			    		
					}
				}

				welcomeMessage(currentZone, player, perm);
			}
			player.setCurrentZone(currentZone);
		}
	}


	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		//		event.getPlayer().sendMessage("ChangeWorld " + event.getPlayer().getWorld().getName());
		TregminePlayer player = tregmine.getPlayer(event.getPlayer());
		ZoneWorld world = plugin.getWorld(player.getWorld());

		//		Location movingFrom = player.getLocation();
		//		Point oldPos = new Point(movingFrom.getBlockX(), movingFrom.getBlockZ());

		Location movingTo = player.getLocation();
		Point currentPos = new Point(movingTo.getBlockX(), movingTo.getBlockZ());

		Zone currentZone = null;


		if (currentZone == null || !currentZone.contains(currentPos)) {

			//			if (currentZone != null && currentZone.contains(oldPos)) {
			//				player.sendMessage(currentZone.getTextExit());
			//			}

			currentZone = world.findZone(currentPos);
			if (currentZone != null) {
				Zone.Permission perm = currentZone.getUser(player.getName());

				if (currentZone.getEnterDefault()) {
					if (player.isAdmin()) {
						// never applies to admins
					}
					else if (perm != null && perm == Zone.Permission.Banned) {
						bannedMessage(currentZone, player);
						player.teleport(this.plugin.getServer().getWorld("world").getSpawnLocation());
						player.sendMessage(ChatColor.RED + "You are not allowed in this zone");
						//						event.setCancelled(true);
						return;
					}
				} else {
					if (player.isAdmin()) {
						// never applies to admins
					}
					else if (perm == null) {
						disallowedMessage(currentZone, player);
						player.teleport(this.plugin.getServer().getWorld("world").getSpawnLocation());
						player.sendMessage(ChatColor.RED + "You are not allowed in this zone");
						//						event.setCancelled(true);
						return;
					}
					else if (perm == Zone.Permission.Banned) {
						bannedMessage(currentZone, player);
						player.teleport(this.plugin.getServer().getWorld("world").getSpawnLocation());
						player.sendMessage(ChatColor.RED + "You are not allowed in this zone");
						//						event.setCancelled(true);
						return;			    		
					}
				}

				if (currentZone.isPvp() && !player.isAdmin()) {
					player.teleport(this.plugin.getServer().getWorld("world").getSpawnLocation());
					player.sendMessage(ChatColor.RED + "You are not allowed in this zone");
					//					event.setCancelled(true);
					return;
				}
				welcomeMessage(currentZone, player, perm);
			}
			player.setCurrentZone(currentZone);
		}
	}


	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{	
		if (event.getCause().equals(TeleportCause.END_PORTAL)) {
			this.plugin.tregmine.log.info("END PORTAL BY: " + event.getPlayer().getName());

			int nrEnt = event.getTo().getWorld().getLivingEntities().size();
			int max = 1000;
			int newEnt = max - nrEnt;

			if (nrEnt <= max) {

				for (int i = 0; i<newEnt;i++) {
					event.getTo().getWorld().spawnEntity(event.getTo(), EntityType.GHAST);
				}

			} 


		}

		//			for (Entity ent : event.getTo().getWorld().getLivingEntities()) {

		//			event.getTo().getWorld().getEntities()

		//		event.getPlayer().sendMessage("Teleport " + event.getPlayer().getWorld().getName());
		TregminePlayer player = tregmine.getPlayer(event.getPlayer());

		if (event.getTo().getWorld().getName().matches("world_the_end") && !player.isOp()) {
			event.getPlayer().sendMessage(ChatColor.RED + "You can't teleport to someone in The End");
			event.setCancelled(true);
			return;
		}

		//		if (event.getPlayer().getWorld().getName().matches("world_the_end")) {
		//			event.getPlayer().sendMessage(ChatColor.RED + "You can't teleport to someone in The End");
		//			event.setCancelled(true);
		//			return;
		//		}

		if (event.getFrom().getWorld().getName().matches(event.getTo().getWorld().getName())) {
			//			TregminePlayer player = tregmine.getPlayer(event.getPlayer());
			ZoneWorld world = plugin.getWorld(player.getWorld());

			Location movingFrom = event.getFrom();
			Point oldPos = new Point(movingFrom.getBlockX(), movingFrom.getBlockZ());

			Location movingTo = event.getTo();
			Point currentPos = new Point(movingTo.getBlockX(), movingTo.getBlockZ());

			Zone currentZone = player.getCurrentZone();


			if (currentZone == null || !currentZone.contains(currentPos)) {

				if (currentZone != null && currentZone.contains(oldPos)) {
					player.setCurrentTexture("https://dl.dropbox.com/u/5405236/mc/df.zip");			
					player.sendMessage(currentZone.getTextExit());
				}

				currentZone = world.findZone(currentPos);
				if (currentZone != null) {
					Zone.Permission perm = currentZone.getUser(player.getName());

					if (currentZone.getEnterDefault()) {
						if (player.isAdmin()) {
							// never applies to admins
						}
						else if (perm != null && perm == Zone.Permission.Banned) {
							bannedMessage(currentZone, player);
							event.setCancelled(true);
							return;
						}
					} else {
						if (player.isAdmin()) {
							// never applies to admins
						}
						else if (perm == null) {
							disallowedMessage(currentZone, player);
							event.setCancelled(true);
							return;
						}
						else if (perm == Zone.Permission.Banned) {
							bannedMessage(currentZone, player);
							event.setCancelled(true);
							return;			    		
						}
					}

					if (currentZone.isPvp() && !player.isAdmin()) {
						event.setCancelled(true);
						return;
					}


					welcomeMessage(currentZone, player, perm);
				}
				player.setCurrentZone(currentZone);
			}
		}
	}

	private void disallowedMessage(Zone currentZone, TregminePlayer player)
	{
		player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
				"You are not allowed in this zone. Contact the zone owner.");
	}

	private void bannedMessage(Zone currentZone, TregminePlayer player)
	{
		player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
				"You are banned from " + currentZone.getName() + ".");
	}

	private void welcomeMessage(Zone currentZone, TregminePlayer player, Zone.Permission perm)
	{
		if (currentZone.getTexture() == "") {
			if (currentZone.isPvp()) {
				player.setCurrentTexture("https://dl.dropbox.com/u/5405236/mc/pvp.zip");
			} else {
				player.setCurrentTexture("https://dl.dropbox.com/u/5405236/mc/df.zip");			
			}

		} else {
			player.setCurrentTexture( currentZone.getTexture() );
		}

		String text = "";
		if (currentZone.getMainOwner() != null) {
			//			text = " MainOwner:" + currentZone.getMainOwner();
		}

		final Player mcplayer = player.getPlayer();
		if (mcplayer.isOnline())
		{

			final ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();

			Objective objective = board.registerNewObjective(currentZone.getName(), "2");

			objective.setDisplaySlot(DisplaySlot.SIDEBAR);

			if(currentZone.getName().length() > 13) {
				objective.setDisplayName(ChatColor.AQUA + currentZone.getName().substring(1 ,13));
			} else {
				objective.setDisplayName(ChatColor.AQUA + currentZone.getName());
			}


			Score score = null;
			
			if (currentZone.getMainOwner() == null) {
				 score = objective.getScore(Bukkit.getOfflinePlayer("Unkown"));
			} else {

				if(currentZone.getMainOwner().length() > 13) {
					score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + currentZone.getMainOwner().substring(1,13))); //Get a fake offline player
				} else {
					score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + currentZone.getMainOwner())); //Get a fake offline player     
				}
			}

			score.setScore(0);
			mcplayer.setScoreboard(board);


			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { 
				public void run() {
					mcplayer.setScoreboard(manager.getNewScoreboard());
				}
			}, 400); //400 = 20 seconds. 1 second = 20 ticks, 20*20=400

		}


		player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
				currentZone.getTextEnter() + text);

		if (currentZone.isPvp()) {
			player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
					"Warning! This is a PVP zone! Other players can damage or kill you here."  + text);		
		} else {
		}

		if (perm != null) {
			String permNotification = perm.getPermissionNotification();
			player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] " + 
					permNotification);
		}
	}


	private String substring(String name, int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
}
