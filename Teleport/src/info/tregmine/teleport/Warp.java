package info.tregmine.teleport;

import java.sql.ResultSet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
//import org.bukkit.entity.Player;
import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerChatEvent;
//import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.Location;

//import com.tregdev.tregmine.Tregmine;

public class Warp {
	private final Teleport plugin;
	//	private PlayerChatEvent event;
	Player from;
	private String[] args;

	public final info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();

	public Warp(Teleport instance, Player player, String[] args) {
		plugin = instance;
		//		this.event = event;
		this.args = args;
		plugin.getServer();
		this.from = player;

	}

	private World getWorld(String name) {

		for (World world : plugin.getServer().getWorlds())

			if (world.getName().matches(name))
			{
				return world;
			}

		return null;
	}

	public void run() {
		Location warppoint = null;

		this.mysql.connect();

		if (this.mysql.connect != null) {
			try {
				this.mysql.statement.executeQuery("SELECT * FROM warps WHERE name='" +  args[0] + "'");
				ResultSet rs = this.mysql.statement.getResultSet();
				rs.first();

				double x = rs.getDouble("x");
				double y = rs.getDouble("y");
				double z = rs.getDouble("z");
				World world = getWorld(rs.getString("world"));

				if (from.getLocation().getWorld().getName().matches("ChildsPlay")) {
					warppoint = new Location(from.getLocation().getWorld(),x,y,z);
					from.sendMessage(ChatColor.AQUA + "You teleported to " + ChatColor.DARK_GREEN + args[0] + ChatColor.AQUA + " in " + ChatColor.BLUE +  "Alpha." );
					warppoint.getWorld().loadChunk(warppoint.getWorld().getChunkAt(warppoint));

					if (warppoint.getWorld().isChunkLoaded(warppoint.getWorld().getChunkAt(warppoint))){
						from.teleport(warppoint);
						plugin.log.info("[warp] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
					} else {
						from.sendMessage(ChatColor.RED + "Chunk failed to load. Please try to warp again.");
					}
					return;
				}


				if(world.getName().matches(from.getWorld().getName())) {					
					warppoint = new Location(world,x,y,z);
					from.sendMessage(ChatColor.AQUA + "You teleported to " + ChatColor.DARK_GREEN + args[0] + ChatColor.AQUA + " in " + ChatColor.BLUE +  world.getName() + "." );
					plugin.log.info("[warp] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
				} else {
					from.sendMessage(ChatColor.DARK_RED + "Sorry, that warp is in another world!");
					warppoint = from.getLocation();		        	
					plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
				}

			} catch (Exception e) {
				from.sendMessage("Warp not found!");
				try {
					plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  " -- not found");
				} catch (Exception ee) {
					e.printStackTrace();
				}
				warppoint = null;
			}
		}
		if (warppoint != null) {
			from.setNoDamageTicks(200);
			
			warppoint.getWorld().loadChunk(warppoint.getWorld().getChunkAt(warppoint));

			if (warppoint.getWorld().isChunkLoaded(warppoint.getWorld().getChunkAt(warppoint))){
				from.teleport(warppoint);
			} else {
				from.sendMessage(ChatColor.RED + "Chunk failed to load. Please try to warp again");
			}

			
		}

		this.mysql.close();

	}

	public void wrun() {
		Location warppoint = null;

		this.mysql.connect();

		if (this.mysql.connect != null) {
			try {
				this.mysql.statement.executeQuery("SELECT * FROM warps WHERE name='" +  args[0] + "'");
				ResultSet rs = this.mysql.statement.getResultSet();
				rs.first();

				double x = rs.getDouble("x");
				double y = rs.getDouble("y");
				double z = rs.getDouble("z");
				World world = getWorld(rs.getString("world"));

				if(world.getName().matches(from.getWorld().getName())) {					
					warppoint = new Location(world,x,y,z);
					from.sendMessage(ChatColor.AQUA + "You teleported to " + ChatColor.DARK_GREEN + args[0] + ChatColor.AQUA + " in " + ChatColor.BLUE +  world.getName() + "." );
					plugin.log.info("[warp] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
				} else {
					from.sendMessage(ChatColor.DARK_RED + "Sorry, that warp is in another world!");
					warppoint = from.getLocation();		        	
					plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  ":" +  world.getName() );
				}

			} catch (Exception e) {
				from.sendMessage("Warp not found!");
				try {
					plugin.log.info("[warp failed] + <" + from.getName() + "> " + args[0] +  " -- not found");
				} catch (Exception ee) {
					e.printStackTrace();
				}
				from.setNoDamageTicks(200);
				warppoint = null;
			}
		}
		if (warppoint != null) {
			//			Location spawn = new Location(this.getWorld("alpha"), -116217, 84, -234971);
			
			warppoint.getWorld().loadChunk(warppoint.getWorld().getChunkAt(warppoint));

			if (warppoint.getWorld().isChunkLoaded(warppoint.getWorld().getChunkAt(warppoint))){
				from.getWorld().setSpawnLocation(warppoint.getBlockX(), warppoint.getBlockY(), warppoint.getBlockZ());
				from.teleport(warppoint);
				from.teleport(warppoint);
				this.getWorld("alpha").setSpawnLocation(-116217, 84, -234971);
			} else {
				from.sendMessage(ChatColor.RED + "Chunk failed to load, please try to warp again");
			}

			
		}

		this.mysql.close();

	}

}
