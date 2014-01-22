package info.tregmine.portals.listeners;

import java.util.ArrayList;
import info.tregmine.portals.Portals;

import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalListener implements Listener {
	private Portals plugin;

	public PortalListener(Portals instance) {
		this.plugin = instance;
	}

	ArrayList<String> players = new ArrayList<String>();
	int taskID = 0;

	@EventHandler
	public void onUseServerPortal(PlayerMoveEvent e) {

		Player p = e.getPlayer();
		World world = p.getWorld();
		Location playerLoc = p.getLocation();
		Material portal = playerLoc.getWorld().getBlockAt(playerLoc).getRelative(0, 0, 0).getType();

		if (portal != Material.PORTAL) {
			return;
		}

		int X = p.getLocation().getBlockX();
		int Z = p.getLocation().getBlockZ();
		int Y = p.getLocation().getBlockY();
		Block b = world.getBlockAt(X, Y, Z);

		findBlock(world, b, p);
	}

	public void findBlock(World w, Block b, Player p) {
		int x = b.getX();
		int z = b.getZ();
		int y = p.getLocation().getBlockY();
		int take = 0;
		Block check = w.getBlockAt(x, y, z);

		while (w.getBlockAt(x, y, z).getType() == Material.PORTAL) {
			y--;
			take++;
			continue;
		}
		Block m = check.getLocation().subtract(0, take + 1, 0).getBlock();

		checkTypes(m, p);
	}

	@SuppressWarnings("deprecation")
	public void checkTypes(Block m, Player p){

		if(!players.contains(p.getName())){
			if(m.getType() == Material.BEDROCK){
				p.sendMessage(ChatColor.DARK_RED + "Sorry, But this portal isnt open yet!");
				taskID++;
				players.add(p.getName());
				remPlayer(p, taskID);
				return;
			}
		}

		if(plugin.getConfig().getString("portals." + m.getType() + ":" + m.getData()) == null){
			return;
		}

		String server = plugin.getConfig().getString("portals." + m.getType() + ":" + m.getData() + ".server");

		if(!players.contains(p.getName())){

			taskID++;
			players.add(p.getName());

			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(server);
			p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

			remPlayer(p, taskID);
		}
	}

	public void remPlayer(final Player p, int task) {
		task = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				players.remove(p.getName());
				taskID--;
			}
		}, 100L);
	}
}