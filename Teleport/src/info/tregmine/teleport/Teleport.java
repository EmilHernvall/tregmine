package info.tregmine.teleport;


import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Teleport extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;

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
	}

	public void onDisable(){
	}


	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player from = null;
		if(!(sender instanceof Player)) {
			return false;
		} else {
			from = (Player) sender;
		}
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(from.getName());

		if(commandName.equals("tpto") && tregminePlayer.isAdmin()) {
			Location loc = new Location(from.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]),Integer.parseInt(args[2]) );

			loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));

			if (loc.getWorld().isChunkLoaded(loc.getWorld().getChunkAt(loc))){
				from.teleport(loc);
			}
			return true;
		}

//		if(commandName.equals("alpha") && tregminePlayer.isDonator()) {
//			from.teleport(this.getServer().getWorld("alpha").getSpawnLocation());
//			return true;
//		}

		if(commandName.equals("skyland") && tregminePlayer.getMetaBoolean("skyland") && tregminePlayer.isDonator()) {
			from.teleport(this.getServer().getWorld("skyland").getSpawnLocation());
			return true;
		}

//		if(commandName.equals("matrix")) {
//			from.teleport(this.getServer().getWorld("matrix").getSpawnLocation());
//			return true;
//		}

		if(commandName.equals("home") && tregminePlayer.isDonator()) {
			Home home = new Home(from.getName(), getServer());

			if (args.length == 0) {
				Location loc = home.get();
                if (loc == null) {
                    from.sendMessage(ChatColor.RED + "Telogric lift malfunctioned. Teleportation failed.");
                    return true;
                }
                
				loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));

				if (loc.getWorld().isChunkLoaded(loc.getWorld().getChunkAt(loc))){
					from.teleport(loc);
					from.sendMessage(ChatColor.AQUA + "Hoci poci, little gnome. Magic worked, you're in your home!");
				} else {
					from.sendMessage(ChatColor.RED + "Loading your home chunk failed, try /home again.");
				}
			} else if ("save".matches(args[0])) {
                home.save(from.getLocation());
                from.sendMessage(ChatColor.AQUA + "Home saved!");
            } else if ("to".equals(args[0]) && (tregminePlayer.getMetaBoolean("mentor") || tregminePlayer.isAdmin())) {
                if (args.length < 2) {
                    from.sendMessage(ChatColor.RED + "Usage: /home to <player>.");
                    return true;
                }
                
                String playerName = args[1];
                Home playersHome = new Home(playerName, getServer());
                
				Location loc = playersHome.get();
                if (loc == null) {
                    from.sendMessage(ChatColor.RED + "Telogric lift malfunctioned. Teleportation failed.");
                    return true;
                }

				loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));

				if (loc.getWorld().isChunkLoaded(loc.getWorld().getChunkAt(loc))){
					from.teleport(loc);
					from.sendMessage(ChatColor.AQUA + "Like a drunken gnome, you fly across the world to " + playerName 
                        + "'s home. Try not to hit any birds.");
				} else {
					from.sendMessage(ChatColor.RED + "Loading of home chunk failed, try /home again");
				}
            }
			return true;
		}

		if(commandName.equals("tp") && tregminePlayer.isTrusted()) {
			try {
				List<Player> to = this.getServer().matchPlayer(args[0]);
				new Tp(from, to.get(0), this);
				return true;			
			} catch (Exception e) {
				return false;
			}
		}

		if(commandName.equals("s") && tregminePlayer.isAdmin()) {
			Player victim = this.getServer().matchPlayer(args[0]).get(0);
			if (victim != null ){

				if (victim.getName().matches("einand")) {
					from.sendMessage(ChatColor.RED + "Forbidden command.");
					victim.sendMessage(from.getName() + " tried to summon you.");
					return true;
				}
				info.tregmine.api.TregminePlayer victimPlayer = this.tregmine.tregminePlayer.get(victim.getName());

				victim.setNoDamageTicks(200);
				victim.teleport(from.getLocation());
				victim.sendMessage(tregminePlayer.getChatName() + ChatColor.AQUA + " summoned you.");
				from.sendMessage(ChatColor.AQUA + "You summoned " +  victimPlayer.getChatName() + ChatColor.AQUA + " to yourself.");
			} else {
				from.sendMessage(ChatColor.RED + "Can't find user.");
			}
			return true;
		}


		if(commandName.equals("warp") && tregminePlayer.isTrusted()) {


			if(from.getWorld().getName().matches("alpha")) {
				Warp warp = new Warp(this, from, args);
				warp.wrun();
			} else {
				Warp warp = new Warp(this, from, args);
				warp.run();
			}
			return true;
		}


		if (commandName.matches("spawn")) {
			from.teleport(this.getServer().getWorld("world").getSpawnLocation());
			return true;
		}

		return false;
	}

	public void onLoad() {
	}
}
