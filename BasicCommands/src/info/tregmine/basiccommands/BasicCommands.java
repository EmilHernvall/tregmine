package info.tregmine.basiccommands;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
//import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
//import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
//import org.bukkit.entity.CreatureType;
//import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Firework;
//import org.bukkit.entity.Firework;
//import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
//import org.bukkit.Color;

import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.EnderDragon;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.FireworkMeta;

import info.tregmine.Tregmine; 
import info.tregmine.api.TregminePlayer;
//import info.tregmine.api.TregminePlayer;


public class BasicCommands extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;

	//	public Map<String, FireworkEffect.Builder> fireWorkEffect = new HashMap<String, FireworkEffect.Builder>();
	//	public Map<String, FireworkMeta> fireWorkMeta = new HashMap<String, FireworkMeta>();
	//	public Map<String, ItemStack> fireWork = new HashMap<String, ItemStack>();
	//	public Map<String, Boolean> property = new HashMap<String, Boolean>();

	public Map<String, info.tregmine.api.firework.createFirework> firework = new HashMap<String, info.tregmine.api.firework.createFirework>();


	@Override
	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}

		getServer().getPluginManager().registerEvents(new BasicCommandsBlock(this), this);
	}

	@Override
	public void onDisable(){
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();

		if(!(sender instanceof Player)) {

			if (commandName.matches("kick")) {
				Player victim = this.getServer().matchPlayer(args[0]).get(0);

				if (victim != null) {
					info.tregmine.api.TregminePlayer victimPlayer = this.tregmine.tregminePlayer.get(victim.getName());
					this.getServer().broadcastMessage("GOD kicked " + victimPlayer.getChatName() + ChatColor.AQUA + " for 1 min.");
					victim.kickPlayer("kicked by GOD.");
				}
				return true;
			}


			return false;
		}

		final Player player = (Player) sender;		
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(player.getName());
		boolean isAdmin = tregminePlayer.isAdmin();
		boolean isDonator = tregminePlayer.isDonator();
		boolean isMentor = tregminePlayer.getMetaBoolean("mentor");


		if (commandName.matches("fw")) {

		}


		if (commandName.matches("keyword")) {
			if (args.length != 1) {
				return false; 
			}
			if (args[0].length() < 1) {
				tregminePlayer.sendMessage(ChatColor.RED + "Your keyword must be at least " +
						"1 characters long.");
				return true;
			}

			tregminePlayer.setMetaString("keyword", args[0].toLowerCase());
			tregminePlayer.sendMessage(ChatColor.YELLOW + "From now on you can only log in by using ip " + args[0].toLowerCase() + ".mc.tregmine.info");
			return true;
		}


		if (commandName.matches("password")) {
			if (args.length != 1) {
				return false; 
			}
			if (args[0].length() < 6) {
				tregminePlayer.sendMessage(ChatColor.RED + "Your password must be at least " +
						"6 characters long.");
				return true;
			}

			tregminePlayer.setPassword(args[0]);
			tregminePlayer.sendMessage(ChatColor.YELLOW + "Your password has been changed.");
			return true;
		}

		if (commandName.matches("creative") && (tregminePlayer.isAdmin() || tregminePlayer.getMetaBoolean("builder"))) {
			final TregminePlayer tregPlayer = tregmine.getPlayer(player);
			tregPlayer.setGameMode(GameMode.CREATIVE);
			tregPlayer.sendMessage(ChatColor.YELLOW + "You are now in creative mode.");
		}

		if (commandName.matches("survival") && (tregminePlayer.isAdmin() || tregminePlayer.getMetaBoolean("builder"))) {
			final TregminePlayer tregPlayer = tregmine.getPlayer(player);
			tregPlayer.setGameMode(GameMode.SURVIVAL);
			tregPlayer.sendMessage(ChatColor.YELLOW + "You are now in survival mode.");
		}


		if (commandName.matches("pos")) {
			Location loc = player.getLocation();
			Location spawn = player.getWorld().getSpawnLocation();
			double distance = info.tregmine.api.math.Distance.calc2d(spawn, loc);


			player.sendMessage(ChatColor.DARK_AQUA + "World: " + ChatColor.WHITE + player.getWorld().getName());
			this.log.info("World: " + player.getWorld().getName());
			player.sendMessage(ChatColor.DARK_AQUA + "X: " + ChatColor.WHITE + loc.getX() + ChatColor.RED + " (" + loc.getBlockX() + ")" );
			this.log.info("X: "  + loc.getX()  + " (" + loc.getBlockX() + ")" );
			player.sendMessage(ChatColor.DARK_AQUA + "Y: " + ChatColor.WHITE + loc.getY() + ChatColor.RED + " (" + loc.getBlockY() + ")" );
			this.log.info("Y: "  + loc.getY()  + " (" + loc.getBlockY() + ")" );
			player.sendMessage(ChatColor.DARK_AQUA + "Z: " + ChatColor.WHITE + loc.getZ() + ChatColor.RED + " (" + loc.getBlockZ() + ")" );
			this.log.info("Z: "  + loc.getZ()  + " (" + loc.getBlockZ() + ")" );
			player.sendMessage(ChatColor.DARK_AQUA + "Yaw: " + ChatColor.WHITE + loc.getYaw());
			this.log.info("Yaw: " +  loc.getYaw());
			player.sendMessage(ChatColor.DARK_AQUA + "Pitch: " + ChatColor.WHITE + loc.getPitch());
			this.log.info("Pitch: " + loc.getPitch() );
			player.sendMessage(ChatColor.DARK_AQUA + "Blocks from spawn: " + ChatColor.WHITE + distance);
			return true;
		}

		if (commandName.matches("cname") && tregminePlayer.isAdmin()) {
			ChatColor color = ChatColor.getByChar(args[0]);
			tregminePlayer.setTemporaryChatName(color + args[1]);
			tregminePlayer.sendMessage("You are now: " + tregminePlayer.getChatName());
			this.log.info(tregminePlayer.getName() + "changed name to" + tregminePlayer.getChatName());
		}

		if (commandName.matches("t") && tregminePlayer.isAdmin()) {
			Player victim = this.getServer().matchPlayer(args[0]).get(0);
			victim.getWorld().strikeLightningEffect(victim.getLocation());
			return true;
		}

		if (commandName.matches("td") && tregminePlayer.isOp()) {
			Player victim = this.getServer().matchPlayer(args[0]).get(0);
			victim.getWorld().strikeLightningEffect(victim.getLocation());
			victim.setHealth(0);
			return true;
		}

		if (commandName.matches("time") && tregminePlayer.isDonator()) {
			if (args.length == 1) {

				if (args[0].matches("day")) {
					player.setPlayerTime(6000, false);
				} else if (args[0].matches("night")) {
					player.setPlayerTime(18000, false);
				} else if (args[0].matches("normal")) {
					player.resetPlayerTime();
				}
			}
			else {
				player.sendMessage(ChatColor.YELLOW + "Say /time day|night|normal");
			}

			log.info(player.getName() + "TIME");
			return true;
		}

		if (commandName.matches("tpblock") && isDonator) {

			if (args.length < 1) {
				if (tregminePlayer.getMetaBoolean("tpblock")) {
					tregminePlayer.setMetaString("tpblock", "false");
					player.sendMessage(ChatColor.AQUA + "Teleportation is now allowed to you.");
					return true;
				} else {
					tregminePlayer.setMetaString("tpblock", "true");
					player.sendMessage(ChatColor.AQUA +"Teleportation is now blocked to you.");
					return true;
				}
			}


			if ("on".matches(args[0])) {
				tregminePlayer.setMetaString("tpblock", "true");
				player.sendMessage(ChatColor.AQUA +"Teleportation is now blocked to you.");
				return true;
			}

			if ("off".matches(args[0])) {
				tregminePlayer.setMetaString("tpblock", "false");
				player.sendMessage(ChatColor.AQUA +"Teleportation is now allowed to you.");
				return true;
			}

			if ("status".matches(args[0])) {
				player.sendMessage("Your tpblock is set to " + tregminePlayer.getMetaString("tpblock") + ".");
				return true;
			}
			player.sendMessage(ChatColor.RED + "The commands are /tpblock on, /tpblock off and /tpblock status.");
			return true;
		}

		if (commandName.matches("normal")) {
			info.tregmine.api.TregminePlayer tregPlayer = this.tregmine.tregminePlayer.get(player.getName());
			if (isAdmin) {
				tregPlayer.setTempMetaString("admin", "false");
				tregPlayer.setTemporaryChatName(ChatColor.GOLD + tregPlayer.getName());
				player.sendMessage(ChatColor.YELLOW + "You are no longer admin, until you reconnect!");
			} else if (tregPlayer.getMetaBoolean("builder")) {
				tregPlayer.setTempMetaString("builder", "false");
				tregPlayer.setTemporaryChatName(ChatColor.GOLD + tregPlayer.getName());
				player.sendMessage(ChatColor.YELLOW + "You are no longer builder, until you reconnect!");
			} else if (tregPlayer.isGuardian()) {
				Player[] players = tregmine.getServer().getOnlinePlayers();
				TregminePlayer maxRank = null;
				for (Player srvPlayer : players) {
					TregminePlayer guardian = tregmine.getPlayer(srvPlayer);
					if (!guardian.isGuardian()) {
						continue;
					}

					TregminePlayer.GuardianState state = guardian.getGuardianState();
					if (state == TregminePlayer.GuardianState.QUEUED) {
						if (maxRank == null || guardian.getGuardianRank() > maxRank.getGuardianRank()) {
							maxRank = guardian;
						}
					}
				}

				if (maxRank != null) {
					tregPlayer.setGuardianState(TregminePlayer.GuardianState.INACTIVE);
					tregPlayer.sendMessage(ChatColor.BLUE + "You are now in normal mode, and no longer have to response to help requests.");
					maxRank.setGuardianState(TregminePlayer.GuardianState.ACTIVE);
					maxRank.sendMessage(ChatColor.BLUE + "You are now on active duty and should respond to help requests.");
				} else {
					tregPlayer.sendMessage(ChatColor.BLUE + "Not enough guardians are on to manage the server. We need you to keep working. Sorry. :/");
				}
			}

			return true;
		}

		info.tregmine.api.TregminePlayer tP = this.tregmine.tregminePlayer.get(player.getName());

		if (commandName.matches("nuke") && (tP.isGuardian() || tP.isAdmin())) {
			player.sendMessage("You nuked all mobs in this world!");
			for (Entity ent : player.getWorld().getLivingEntities()) {
				if(ent instanceof Monster) {
					Monster mob = (Monster) ent;
					mob.setHealth(0);
				}

				//				if(ent instanceof Chicken) {
				//					Chicken chicken = (Chicken) ent;
				//					chicken.setHealth(0);
				//				}

				if(ent instanceof org.bukkit.entity.Animals) {
					org.bukkit.entity.Animals animal = (org.bukkit.entity.Animals) ent;
					animal.setHealth(0);
				}

				if(ent instanceof Slime) {
					Slime slime = (Slime) ent;
					slime.setHealth(0);
				}

				if (ent instanceof EnderDragon) {
					EnderDragon dragon = (EnderDragon)ent;
					dragon.setHealth(0);
				}

			}
			return true;
		}


		if (commandName.matches("user") && args.length > 0  && (isAdmin || isMentor)) {

			if (args[0].matches("reload")) {
				this.tregmine.tregminePlayer.get(this.getServer().matchPlayer(args[1]).get(0).getDisplayName()).load();
				player.sendMessage("Player reloaded "+ this.getServer().matchPlayer(args[1]).get(0).getDisplayName());
				return true;
			}

			if (args[0].matches("make")) {
				Player victim = this.getServer().matchPlayer(args[2]).get(0);
				info.tregmine.api.TregminePlayer victimPlayer = this.tregmine.tregminePlayer.get(victim.getName());
				TregminePlayer vtregPlayer = this.tregmine.tregminePlayer.get(victim.getName());

				if (args[1].matches("settler")) {
					vtregPlayer.setMetaString("color", "trial");
					vtregPlayer.setMetaString("trusted", "true");
					vtregPlayer.setTemporaryChatName(vtregPlayer.getNameColor() + vtregPlayer.getName());

					player.sendMessage(ChatColor.AQUA + "You made " + victimPlayer.getChatName() + ChatColor.AQUA + " settler of this server." );
					victim.sendMessage("Welcome! You are now made settler.");
					this.log.info(victim.getName() + " was given settler rights by " + player.getName() + ".");
					return true;
				}

				if (args[1].matches("warn")) {
					vtregPlayer.setMetaString("color", "warned");
					player.sendMessage(ChatColor.AQUA + "You warned " + victimPlayer.getChatName() + ".");
					victim.sendMessage("You are now warned");
					this.log.info(victim.getName() + " was warned by " + player.getName() + ".");
					vtregPlayer.setTemporaryChatName(vtregPlayer.getNameColor() + vtregPlayer.getName());
					return true;
				}

				if (args[1].matches("hardwarn")) {
					vtregPlayer.setMetaString("color", "warned");
					vtregPlayer.setMetaString("trusted", "false");
					player.sendMessage(ChatColor.AQUA + "You warned " + victimPlayer.getChatName() + " and removed his building rights." );
					victim.sendMessage("You are now warned and bereft of your building rights.");
					this.log.info(victim.getName() + " was hardwarned by " + player.getName() + ".");
					vtregPlayer.setTemporaryChatName(vtregPlayer.getNameColor() + vtregPlayer.getName());
					return true;
				}

				if (args[1].matches("trial")) {
					player.sendMessage(ChatColor.RED + "Please use /user make settler name");
				}

				if (args[1].matches("resident") && player.isOp() ) {
					vtregPlayer.setMetaString("color", "trusted");
					vtregPlayer.setMetaString("trusted", "true");
					this.log.info(victim.getName() + " was given trusted rights by " + tregminePlayer.getChatName() + ".");
					player.sendMessage(ChatColor.AQUA + "You made " + victimPlayer.getChatName() + ChatColor.AQUA + " a resident." );
					victim.sendMessage("Welcome! You are now a resident");
					vtregPlayer.setTemporaryChatName(vtregPlayer.getNameColor() + vtregPlayer.getName());
					return true;
				}

				if (args[1].matches("donator") && player.isOp() ) {
					vtregPlayer.setMetaString("donator", "true");
					//					vtregPlayer.setMetaString("compass", "true");
					vtregPlayer.setFlying(true);
					vtregPlayer.setMetaString("color", "donator");
					player.sendMessage(ChatColor.AQUA + "You made  " + vtregPlayer.getChatName() + " a donator." );
					this.log.info(victim.getName() + " was made donator by" + tregminePlayer.getChatName() + ".");
					victim.sendMessage("Congratulations, you are now a donator!");
					vtregPlayer.setTemporaryChatName(vtregPlayer.getNameColor() + vtregPlayer.getName());
					return true;
				}

				if (args[1].matches("child")) {
					vtregPlayer.setMetaString("color", "child");
					player.sendMessage(ChatColor.AQUA + "You made  " + vtregPlayer.getChatName() + " a child." );
					this.log.info(victim.getName() + " was made child by" + tregminePlayer.getChatName() + ".");
					vtregPlayer.setTemporaryChatName(vtregPlayer.getNameColor() + vtregPlayer.getName());
					return true;
				}

				if (args[1].matches("guardian") && player.isOp() ) {
					if(vtregPlayer.isDonator()) {
						vtregPlayer.setMetaString("police", "true");
						vtregPlayer.setMetaString("color", "police");

						player.sendMessage(ChatColor.AQUA + "You made  " + vtregPlayer.getChatName() + " a police." );
						this.log.info(victim.getName() + " was made police by" + tregminePlayer.getChatName() + ".");
						victim.sendMessage("Congratulations, you are now a police!");
					} else {
						player.sendMessage(ChatColor.AQUA + "Sorry this person is not a  " + ChatColor.GOLD + " donator." );
					}
					vtregPlayer.setTemporaryChatName(vtregPlayer.getNameColor() + vtregPlayer.getName());
					return true;
				}

			}
		}

		if (commandName.matches("kick") && (isAdmin || isMentor)) {
			Player victim = this.getServer().matchPlayer(args[0]).get(0);

			if (victim != null) {
				info.tregmine.api.TregminePlayer victimPlayer = this.tregmine.tregminePlayer.get(victim.getName());

				if(victim.getName().matches("einand")) {
					this.getServer().broadcastMessage("Never try to kick a god!");
					player.kickPlayer("Never kick a god!");
					return true;
				}

				this.getServer().broadcastMessage(tregminePlayer.getChatName() + ChatColor.AQUA + " kicked " + victimPlayer.getChatName() + ChatColor.AQUA + " for 1 minute.");
				this.log.info(victim.getName() + " kicked by " + player.getName());
				victim.kickPlayer("kicked by " + player.getName());
			}
			return true;
		}

		if (commandName.matches("ride.") && tregminePlayer.isOp() ) {
			Player v = this.getServer().matchPlayer(args[0]).get(0);
			Player v2 = this.getServer().matchPlayer(args[1]).get(0);
			v2.setPassenger(v);
			return true;
		}

		if (commandName.matches("eject")) {
			Player v = this.getServer().matchPlayer(args[0]).get(0);
			v.eject();
		}

		if (commandName.matches("newspawn") && isAdmin) {
			player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			return true;
		}

		if (commandName.matches("sendto") && isAdmin) {
			Player victim = this.getServer().matchPlayer(args[0]).get(0);
			if (victim != null){
				Location cpspawn = this.getServer().getWorld(args[1]).getSpawnLocation();
				player.teleport(cpspawn);
			}
			return true;
		}

		if (commandName.matches("test")) {
			player.sendMessage("Admin: " + tregminePlayer.isAdmin());
			player.sendMessage("Donator: " +tregminePlayer.isDonator());
			player.sendMessage("Trusted: " +tregminePlayer.isTrusted());
		}

		if (commandName.matches("createmob") && isAdmin) {
			int amount = 1;
			EntityType mobtyp;

			try {
				amount = Integer.parseInt( args[1] );
			} catch (Exception e) {
				amount = 1;
			}

			try {
				String mobname = args[0]; //args[0].substring(0,1).toUpperCase() + args[0].substring(1).toLowerCase(); 
				mobtyp = EntityType.fromName(mobname);
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Sorry that mob doesn't exist.");
				return true;
			}

			if (mobtyp != null) {
				for (int i = 0; i<amount;i++) {

					if (mobtyp.isSpawnable() && mobtyp.isAlive()) {
						//						player.getWorld().spawnCreature(player.getLocation(), mobtyp);
						player.getWorld().spawnEntity(player.getLocation(), mobtyp);
					}

				} 

				player.sendMessage(ChatColor.YELLOW + "You created " + amount + " " + mobtyp.getName() + ".");
				this.log.info(player.getName() + " created " + amount + " " + mobtyp.getName());
			}
			else {
				StringBuilder buf = new StringBuilder();
				String delim = "";
				for (EntityType mob : EntityType.values()) {
					if (mob.isSpawnable() && mob.isAlive()) {
						buf.append(delim);
						buf.append(mob.getName());
						delim = ", ";
					}
				}

				player.sendMessage("Valid names are: ");
				player.sendMessage(buf.toString());
			}
			return true;

		}

		if (commandName.matches("ban") && (isAdmin || isMentor)) {

			if (this.getServer().matchPlayer(args[0]).size() > 1) {
				player.sendMessage("Found more then one player that contain that letters");
			}

			Player victim = this.getServer().matchPlayer(args[0]).get(0);
			if (victim != null) {
				info.tregmine.api.TregminePlayer victimPlayer = this.tregmine.tregminePlayer.get(victim.getName());

				if(victim.getName().matches("einand")) {
					this.getServer().broadcastMessage("Never ban a god!");
					player.kickPlayer("Never ban a god!");
					return true;
				}

				victimPlayer.setMetaString("banned", "true");
				this.getServer().broadcastMessage(victimPlayer.getChatName() + ChatColor.RED + " was banned by " + tregminePlayer.getChatName() + ".");
				this.log.info(victim.getName() + " Banned by " + player.getName());
				victim.kickPlayer("banned by " + player.getName());
			}
			return true;
		}

		if (commandName.matches("clean")) {
			player.getInventory().clear();
			return true;
		}

		return false;
	}

	@Override
	public void onLoad() {
		final World world = this.getServer().getWorld("world");

		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			public void run() {



				Location loc = world.getSpawnLocation();
				info.tregmine.api.firework.createFirework firework = new info.tregmine.api.firework.createFirework();
				firework.addColor(Color.BLUE);
				firework.addColor(Color.YELLOW);
				firework.addType(FireworkEffect.Type.STAR);
				firework.shot(loc);
			}


		},100L,200L);

	}
}
