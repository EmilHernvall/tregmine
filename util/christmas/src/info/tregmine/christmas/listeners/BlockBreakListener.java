package info.tregmine.christmas.listeners;

import info.tregmine.christmas.ChristmasMain;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

	private ChristmasMain plugin;

	public BlockBreakListener(ChristmasMain instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {

		Player p = e.getPlayer();

		if(p.getWorld().getName() != "Christmas"){
			return;
		}
		
		plugin.getConfig().set("player." + p.getName() + ".build", plugin.getConfig().getBoolean("player." + p.getName() + ".build"));
		plugin.saveConfig();

		if(plugin.getConfig().getBoolean("whitelist.build.status") == true
				&& plugin.getConfig().getBoolean("player." + p.getName() + ".build") == false){
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + plugin.getConfig().getString("whitelist.build.message"));
		}
	}
}
