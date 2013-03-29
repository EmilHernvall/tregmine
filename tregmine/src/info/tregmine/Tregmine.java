package info.tregmine;


import info.tregmine.api.TregminePlayer;
import info.tregmine.currency.Wallet;
import info.tregmine.database.ConnectionPool;
import info.tregmine.listeners.TregmineBlockListener;
import info.tregmine.listeners.TregmineEntityListener;
import info.tregmine.listeners.TregminePlayerListener;
import info.tregmine.listeners.TregmineWeatherListener;
import info.tregmine.stats.BlockStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Ein Andersson - www.tregmine.info
 */
public class Tregmine extends JavaPlugin 
{
	public final Logger log = Logger.getLogger("Minecraft");

	public final BlockStats blockStats = new BlockStats(this);

	public Map<String, TregminePlayer> tregminePlayer = new HashMap<String, TregminePlayer>();

	//	public Map<String, Boolean> hasVoted = new HashMap<String, Boolean>();
	public LinkedList <String> hasVoted = new LinkedList<String>();


	public int version = 0;
	public int amount = 0;

	@Override
	public void onEnable() 	{		
		WorldCreator citadelCreator = new WorldCreator("citadel"); 
		citadelCreator.environment(Environment.NORMAL);
		citadelCreator.createWorld();

		WorldCreator world = new WorldCreator("world"); 
		world.environment(Environment.NORMAL);
		world.createWorld();

		WorldCreator treton = new WorldCreator("treton"); 
		treton.environment(Environment.NORMAL);
		treton.createWorld();

		WorldCreator vanilla = new WorldCreator("elva"); 
		vanilla.environment(Environment.NORMAL);
		vanilla.createWorld();		
		
		WorldCreator einhome = new WorldCreator("einhome"); 
		einhome.environment(Environment.NORMAL);
		einhome.createWorld();
		
		WorldCreator alpha = new WorldCreator("alpha"); 
		alpha.environment(Environment.NORMAL);
		alpha.createWorld();

		
		WorldCreator NETHER = new WorldCreator("world_nether"); 
		NETHER.environment(Environment.NETHER);
		NETHER.createWorld();

		getServer().getPluginManager().registerEvents(new info.tregmine.lookup.LookupPlayer(this), this);


		getServer().getPluginManager().registerEvents(new TregminePlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineBlockListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineEntityListener(this), this);
		getServer().getPluginManager().registerEvents(new TregmineWeatherListener(this), this);

		getServer().getPluginManager().registerEvents(new info.tregmine.invis.InvisPlayer(this), this);


		getServer().getPluginManager().registerEvents(new info.tregmine.death.DeathEntity(this), this);
		getServer().getPluginManager().registerEvents(new info.tregmine.death.DeathPlayer(this), this);


		getServer().getPluginManager().registerEvents(new info.tregmine.chat.Chat(this), this);


		getServer().getPluginManager().registerEvents(new info.tregmine.vote.voter(this), this);


		getServer().getPluginManager().registerEvents(new info.tregmine.sign.Color(), this);

		getServer().getPluginManager().registerEvents(new info.tregmine.vendings.Machines(this), this);
		getServer().getPluginManager().registerEvents(new info.tregmine.portals.Portals(this), this);
		
	}

	@Override
	public void onDisable() { //run when plugin is disabled
		this.getServer().getScheduler().cancelTasks(this);

		Player[] players = this.getServer().getOnlinePlayers();

		for (Player player : players) {
			player.sendMessage(ChatColor.AQUA + "Tregmine successfully unloaded build: " + this.getDescription().getVersion() );
		}	
	}

	@Override
	public void onLoad() 
	{
		Player[] players = this.getServer().getOnlinePlayers();

		for (Player player : players) {
			String onlineName = player.getName();
			TregminePlayer tregPlayer = new TregminePlayer(player, onlineName);
			tregPlayer.load();
			this.tregminePlayer.put(onlineName, tregPlayer);
			player.sendMessage(ChatColor.GRAY + "Tregmine successfully loaded to build: " + this.getDescription().getVersion() );
			//			player.sendMessage(ChatColor.GRAY + "Version explanation: X.Y.Z.G");
			//			player.sendMessage(ChatColor.GRAY + "X new stuff added, When i make a brand new thing");
			//			player.sendMessage(ChatColor.GRAY + "Y new function added, when i extend what current stuff can do");
			//			player.sendMessage(ChatColor.GRAY + "Z bugfix that may change how function and stuff works");
			//			player.sendMessage(ChatColor.GRAY + "G small bugfix like spelling errors");
		}

		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			public void run() {

				while(hasVoted.size() > 0) {
					String name = hasVoted.removeFirst();


					getServer().broadcastMessage(ChatColor.YELLOW + name + " has voted and will now receive 2,000 Tregs");
					getServer().broadcastMessage(ChatColor.YELLOW + name + " Read more at http://treg.co/82 what you can get");

					Wallet wallet = new Wallet(name);
					wallet.add(2000);
					log.info(name + " got " + name + " Tregs for VOTING");

				}

			}


		},100L,20L);
	}



	public TregminePlayer getPlayer(String name)	{
		return tregminePlayer.get(name);
	}

	public TregminePlayer getPlayer(Player player)	{
		return tregminePlayer.get(player.getName());
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, final String[] args) {
		String commandName = command.getName().toLowerCase();
		Player from = null;
		TregminePlayer player = null;
		if(!(sender instanceof Player)) {
			if(commandName.equals("say")) {
				StringBuffer buf = new StringBuffer();
				buf.append(args[0]);
				for (int i = 1; i < args.length; ++i) {
					buf.append(" " + args[i]);
				}
				String buffMsg = buf.toString();
				this.getServer().broadcastMessage("<" + ChatColor.BLUE + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
				this.log.info("CONSOLE: <GOD> " + buffMsg);
				return true;
			}

			return false;
		} else {
			from = (Player) sender;
			player = this.getPlayer(from);
		}

		if("book".matches(commandName)) {

			if("player".matches(args[0]) && player.isOp()) {

				final TregminePlayer fPlayer = player;

				this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

					public void run() {

//						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date date = new Date();

						fPlayer.sendMessage(ChatColor.YELLOW + "Starting to generate book for " + args[1] );
						ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
						BookMeta bookmeta = (BookMeta) book.getItemMeta();
						//						TregminePlayer p = getPlayer(args[1]);

						long placed = 0;
						long destroyed = 0;
						long total = 0;
						int  id = 0;
						String joinDate = "";

						bookmeta.setAuthor("Tregmine");
						bookmeta.setTitle(ChatColor.GREEN + args[1] + "'s Profile");

						bookmeta.addPage(ChatColor.GREEN + args[1] + "'s profile" + '\n' + "date:"+'\n'+ date.toString());

						Connection conn = null;
						PreparedStatement stmt = null;
						ResultSet rs = null;
						try {
							conn = ConnectionPool.getConnection();
							stmt = conn.prepareStatement("SELECT count(player) as count FROM stats_blocks WHERE player=?");
							stmt.setString(1, args[1]);
							stmt.execute();
							rs = stmt.getResultSet();
							if (!rs.next()) {

							}

							total = rs.getInt("count");

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

						try {
							conn = ConnectionPool.getConnection();
							stmt = conn.prepareStatement("SELECT count(player) as count FROM stats_blocks WHERE player=? AND status=1");
							stmt.setString(1, args[1]);
							stmt.execute();
							rs = stmt.getResultSet();
							if (!rs.next()) {

							}

							placed = rs.getInt("count");

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


						try {
							conn = ConnectionPool.getConnection();
							stmt = conn.prepareStatement("SELECT count(player) as count FROM stats_blocks WHERE player=? AND status=0");
							stmt.setString(1, args[1]);
							stmt.execute();
							rs = stmt.getResultSet();
							if (!rs.next()) {

							}

							destroyed = rs.getInt("count");

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

						try {
							conn = ConnectionPool.getConnection();
							stmt = conn.prepareStatement("SELECT * FROM user WHERE player=?");
							stmt.setString(1, args[1]);
							stmt.execute();
							rs = stmt.getResultSet();
							if (!rs.next()) {

							}

							joinDate = rs.getDate("time").toString();
							id = rs.getInt("uid");

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


						bookmeta.addPage(
								ChatColor.BLUE + "ID:"							+'\n' +
								ChatColor.BLACK + id							+'\n' +
								ChatColor.BLUE + "JOIN-TIME (GMT):"				+'\n' +
								ChatColor.BLACK + joinDate						+'\n' +
								ChatColor.BLUE + "BLOCK DESTROYED:"				+'\n' +
								ChatColor.BLACK + destroyed						+'\n' +
								ChatColor.BLUE + "BLOCK PLACED:"				+'\n' +
								ChatColor.BLACK + placed 						+'\n' +
								ChatColor.BLUE + "TOTAL:"						+'\n' +
								ChatColor.BLACK + total 						+'\n' +
								"EOF");


						book.setItemMeta(bookmeta);



						PlayerInventory inv = fPlayer.getInventory();
						inv.addItem(book);
					}
				},20L);

			}
		}
		
		if("blackout".matches(commandName) && player.isAdmin()) {
			Player target = getServer().getPlayer(args[1]);

			player.sendMessage("blackout");
			
			if ("blind".matches(args[0])) {
				PotionEffect ef = new PotionEffect(PotionEffectType.BLINDNESS, 60, 100);
				target.getPlayer().addPotionEffect(ef);
				player.sendMessage("Blind");
			}
			
			if ("invis".matches(args[0])) {
				PotionEffect ef = new PotionEffect(PotionEffectType.INVISIBILITY, 1600000, 100);
				target.getPlayer().addPotionEffect(ef);
				player.sendMessage("Blind");
			}

			
			if ("confuse".matches(args[0])) {
				PotionEffect ef = new PotionEffect(PotionEffectType.CONFUSION, 600
						, 10);
				target.getPlayer().addPotionEffect(ef);
				player.sendMessage("Confuse");
			}
			
			
		}


		if("te".matches(commandName) && player.isOp()) {

			
			ItemStack drop = new ItemStack(Material.MONSTER_EGG, 1, (byte)65);
			ItemMeta meta = drop.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "EASTER EGG");
			drop.setItemMeta(meta);
			player.getWorld().dropItemNaturally(player.getLocation(), drop);

		}


		if ("TregDev".matches(this.getServer().getServerName())) {

			if("te".matches(commandName)) {

				ItemStack item = new ItemStack(Material.PAPER, amount, (byte) 0);
//				PlayerInventory inv = player.getInventory();

				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				lore.add(info.tregmine.api.lore.Created.PURCHASED.toColorString());
				TregminePlayer p = this.getPlayer(player);
				lore.add(ChatColor.WHITE + "by: " + p.getName() );
				lore.add(ChatColor.WHITE + "Value: 25.000" + ChatColor.WHITE + " Tregs" );
				meta.setLore(lore);
				meta.setDisplayName(ChatColor.GREEN + "DIRT -> SPONG Coupon");

				item.setItemMeta(meta);


			}


			if("op".matches(commandName)) {
				player.setMetaBoolean("admin", true);
				player.setMetaBoolean("donator", true);
				player.setMetaBoolean("trusted", true);
				player.setTemporaryChatName(ChatColor.RED + player.getName());
				player.setAllowFlight(true);
				player.setMetaString("color", ""+ChatColor.RED);
			}

			if("donator".matches(commandName)) {
				player.setAllowFlight(true);
				player.setMetaBoolean("admin", false);
				player.setMetaBoolean("donator", true);
				player.setMetaBoolean("trusted", true);
				player.setTemporaryChatName(ChatColor.GOLD + player.getName());
				player.setMetaString("color", ""+ChatColor.GOLD);
			}

			if("settler".matches(commandName)) {
				player.setAllowFlight(true);
				player.setMetaBoolean("admin", false);
				player.setMetaBoolean("donator", false);
				player.setMetaBoolean("trusted", true);
				player.setTemporaryChatName(ChatColor.GREEN + player.getName());
				player.setMetaString("color", ""+ChatColor.GREEN);
			}
		}

		if("invis".matches(commandName) && player.isOp()) {
			if ("off".matches(args[0])) {
				for (Player p : this.getServer().getOnlinePlayers()) {					
					p.showPlayer(player.getPlayer());
				}
				player.setMetaBoolean("invis", false);
				player.sendMessage(ChatColor.YELLOW + "You can now be seen!");
			} else if ("on".matches(args[0])) {
				for (Player p : this.getServer().getOnlinePlayers()) {	
					if (!p.isOp()) {
						p.hidePlayer(player.getPlayer());
					} else {
						p.showPlayer(player.getPlayer());
					}
				}
				player.setMetaBoolean("invis", true);
				player.sendMessage(ChatColor.YELLOW + "*poof* no one knows where you are!");
			} else {
				player.sendMessage("Try /invis [on|off]");
			}

		}


		if(commandName.equals("text") && player.isAdmin()) {
			Player[] players = this.getServer().getOnlinePlayers();

			//			player.setTexturePack(args[0]);
			for (Player p : players) {
				p.setTexturePack(args[0]);
			}


		}




		if(commandName.equals("head") && player.isOp()) {
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwner(args[0]);
			meta.setDisplayName(ChatColor.YELLOW + "Head of " + args[0]);
			item.setItemMeta(meta);

			PlayerInventory inv = player.getInventory();
			inv.addItem(item);
			player.updateInventory();
			player.sendMessage("Skull of " + args[0]);
		} else if(commandName.equals("head")) {
			player.kickPlayer("");
		}


		if(commandName.equals("say") && player.isAdmin()) {
			StringBuffer buf = new StringBuffer();
			buf.append(args[0]);
			for (int i = 1; i < args.length; ++i) {
				buf.append(" " + args[i]);
			}
			String buffMsg = buf.toString();
			if(from.getName().matches("BlackX")) {
				this.getServer().broadcastMessage("<" + ChatColor.BLACK + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);				
			} else if (from.getName().matches("mejjad")){
				this.getServer().broadcastMessage("<" + ChatColor.DARK_AQUA + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			} else if (from.getName().matches("einand")){
				this.getServer().broadcastMessage("<" + ChatColor.DARK_GREEN + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			} else if (from.getName().matches("LilKiw")){
				this.getServer().broadcastMessage("<" + ChatColor.AQUA + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			} else if (from.getName().matches("Camrenn")){
				this.getServer().broadcastMessage("<" + ChatColor.LIGHT_PURPLE + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			} else if (from.getName().matches("Mksen")){
				this.getServer().broadcastMessage("<" + ChatColor.YELLOW + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			} else if (from.getName().matches("josh121297")){
				this.getServer().broadcastMessage("<" + ChatColor.GREEN + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			} else if (from.getName().matches("GeorgeBombadil")){
				this.getServer().broadcastMessage("<" + ChatColor.DARK_RED + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			} else if (from.getName().matches("rweiand")){
				this.getServer().broadcastMessage("<" + ChatColor.GOLD + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);
			} else {
				this.getServer().broadcastMessage("<" + ChatColor.RED + "GOD" + ChatColor.WHITE + "> " + ChatColor.LIGHT_PURPLE +  buffMsg);				
			} 

			this.log.info(from.getName() + ": <GOD> " + buffMsg);

			Player[] players =  this.getServer().getOnlinePlayers();

			for (Player p : players) {
				info.tregmine.api.TregminePlayer locTregminePlayer = this.getPlayer((p.getName()));
				if (locTregminePlayer.isAdmin()) {
					p.sendMessage(ChatColor.DARK_AQUA + "/say used by: " + player.getChatName());
				}
			}
			return true;
		}		



		if(commandName.equals("who") || commandName.equals("playerlist") || commandName.equals("list")){
			info.tregmine.commands.Who.run(this, player, args);
			return true;
		}

		if(commandName.equals("tp")){
			info.tregmine.commands.Tp.run(this, player, args);
			return true;
		}

		if(commandName.equals("channel")){
			if (args.length != 1) {
				return false;
			}

			from.sendMessage(ChatColor.YELLOW + "You are now talking in channel " + args[0] + ".");
			from.sendMessage(ChatColor.YELLOW + "Write /channel global to switch to the global chat." );
			player.setChatChannel(args[0]);
			return true;
		}


		if(commandName.equals("force")  && args.length == 2 ){
			player.setChatChannel(args[1]);
			Player to = getServer().matchPlayer(args[0]).get(0);
			info.tregmine.api.TregminePlayer toPlayer = this.tregminePlayer.get(to.getName());

			toPlayer.setChatChannel(args[1]);

			to.sendMessage(ChatColor.YELLOW + player.getChatName() + " forced you into channel " + args[1].toUpperCase() + ".");
			to.sendMessage(ChatColor.YELLOW + "Write /channel global to switch back to the global chat." );
			from.sendMessage(ChatColor.YELLOW + "You are now in a forced chat " + args[1].toUpperCase()+ " with " + to.getDisplayName() + ".");
			this.log.info(from.getName() + " FORCED CHAT WITH " + to.getDisplayName() + " IN CHANNEL " + args[1].toUpperCase());
			return true;
		}

		if(commandName.equals("msg") || commandName.equals("m") || commandName.equals("tell")) {			
			Player to = getServer().getPlayer(args[0]);

			if (to != null) {
				info.tregmine.api.TregminePlayer toPlayer = this.tregminePlayer.get(to.getName());

				StringBuffer buf = new StringBuffer();
				for (int i = 1; i < args.length; ++i) {
					buf.append(" " + args[i]);
				}
				String buffMsg = buf.toString();

				if (!toPlayer.getMetaBoolean("invis")) {
					from.sendMessage(ChatColor.GREEN + "(to) " + toPlayer.getChatName() + ChatColor.GREEN + ": "  + buffMsg);
				}
				to.sendMessage(ChatColor.GREEN + "(msg) " + player.getChatName() + ChatColor.GREEN + ": " + buffMsg);
				log.info(from.getName() + " => " + to.getName() + buffMsg);
				return true;
			}
		}

		if(commandName.equals("me")  && args.length > 0 ){
			StringBuffer buf = new StringBuffer();
			Player[] players = getServer().getOnlinePlayers();

			for (int i = 0; i < args.length; ++i) {
				buf.append(" " + args[i]);
			}

			for (Player tp : players) {
				TregminePlayer to = this.getPlayer(tp);

				if (player.getChatChannel().equals(to.getChatChannel())) {
					to.sendMessage("* " + player.getChatName() + ChatColor.WHITE + buf.toString() );
				}
			}
			return true;
		}


		return false;
	}
}
