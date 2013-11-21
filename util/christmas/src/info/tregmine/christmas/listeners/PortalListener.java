package info.tregmine.christmas.listeners;

import info.tregmine.christmas.ChristmasMain;
import info.tregmine.christmas.Fireworks;
import info.tregmine.christmas.Packets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PortalListener implements Listener {

	private ChristmasMain plugin;

	public PortalListener(ChristmasMain instance)
	{
		this.plugin = instance;
	}

	ChatColor r = ChatColor.DARK_RED;
	ChatColor g = ChatColor.GREEN;
	ChatColor b = ChatColor.BOLD;
	//The spaces are to center the text
	String header = "           " + b + r + "=" + g + "=" + r + "= " + g;
	String footer = r + " =" + g + "=" + r + "=" + g;

	@EventHandler
	public void onPortalEntry(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(p.getLocation().subtract(0,1,0).getBlock().getType() == Material.ICE &&
				e.getTo().getBlock().isLiquid()){
			if(frameCheck(p, -1, 3, -1, 3) ||
					frameCheck(p, -1, 3, -2, 2) ||
					frameCheck(p, -1, 3, -3, 1) ||
					frameCheck(p, -2, 2, -1, 3) ||
					frameCheck(p, -2, 2, -2, 2) ||
					frameCheck(p, -2, 2, -3, 1) ||
					frameCheck(p, -3, 1, -1, 3) ||
					frameCheck(p, -3, 1, -2, 2) ||
					frameCheck(p, -3, 1, -3, 1)){
				if(p.getLocation().getWorld().getName() != "Christmas"){
					p.teleport(ChristmasMain.getMap().getSpawnLocation());
					Packets.displayTextBar(header + "Welcome to The Northpole!" + footer, p, plugin);
					Fireworks.Fireworks(p);
					SparklerListener.giveSparkler(p);
				}else{
					World world = Bukkit.getServer().getWorld("world");
					p.teleport(world.getSpawnLocation());

					for (ItemStack item : p.getInventory().getContents()) {
						if(item == null){return;}
						if(item.getType() == Material.STICK && item.getItemMeta().getDisplayName() == "§5Christmas Sparkler") {
							p.getInventory().remove(item);
							break;
						}
					}
				}

			}else{
				return;
			}
		}
	}

	public boolean frameCheck(Player p, int x1, int x2, int z1, int z2)
	{
		if(p.getLocation().add(x1, 0, 0).getBlock().getType() == Material.SNOW_BLOCK && 
				p.getLocation().add(0, 0, z1).getBlock().getType() == Material.SNOW_BLOCK &&
				p.getLocation().add(x2, 0, 0).getBlock().getType() == Material.SNOW_BLOCK && 
				p.getLocation().add(0, 0, z2).getBlock().getType() == Material.SNOW_BLOCK){ 
			return true;
		}else{
			return false;
		}
	}
}