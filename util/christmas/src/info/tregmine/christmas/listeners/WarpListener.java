package info.tregmine.christmas.listeners;

import info.tregmine.christmas.ChristmasMain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WarpListener implements Listener {

	private ChristmasMain plugin;

	public WarpListener(ChristmasMain instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void onWarp(PlayerChangedWorldEvent e) {

		Player p = e.getPlayer();

		if(p.getWorld().getName() != "Christmas"){
			return;
		}

		plugin.getConfig().set("player." + p.getName() + ".warp", plugin.getConfig().getBoolean("player." + p.getName() + ".warp"));
		plugin.saveConfig();

		if(plugin.getConfig().getBoolean("whitelist.warp.status") == true
				&& plugin.getConfig().getBoolean("player." + p.getName() + ".warp") == false){
			World world = Bukkit.getServer().getWorld(plugin.getConfig().getString("portal" + ".returnWorld"));
			p.teleport(world.getSpawnLocation());
			p.sendMessage(ChatColor.RED + plugin.getConfig().getString("whitelist.warp.message"));
		}
	}
}
