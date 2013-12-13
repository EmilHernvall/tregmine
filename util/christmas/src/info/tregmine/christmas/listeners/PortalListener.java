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
import org.bukkit.scheduler.BukkitScheduler;

public class PortalListener implements Listener {

	private static ChristmasMain plugin;

	public PortalListener(ChristmasMain instance)
	{
		PortalListener.plugin = instance;
	}

	static ChatColor r = ChatColor.DARK_RED;
	static ChatColor g = ChatColor.GREEN;
	static ChatColor b = ChatColor.BOLD;
	static ChatColor y = ChatColor.GOLD;
	static String header0 = "           " + b + r + "=" + g + "=" + y + "= " + g;
	static String footer0 = y + " =" + g + "=" + r + "=";
	static String header1 = "           " + b + g + "=" + y + "=" + r + "= " + g;
	static String footer1 = r + " =" + y + "=" + g + "=";
	static String header2 = "           " + b + y + "=" + r + "=" + g + "= " + g;
	static String footer2 = g + " =" + r + "=" + y + "=";

	@EventHandler
	public void onPortalEntry(PlayerMoveEvent e) {
		final Player p = e.getPlayer();
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
					
					final String s = ChatColor.RED + "=" + ChatColor.GREEN + "=";
					final String end = ChatColor.RED + "=";
					
					if(p.getWorld().getName() == "Christmas"){
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							p.sendMessage(s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+end);
							p.sendMessage(" ");
							p.sendMessage(" ");
							p.sendMessage("    " + header0 + "Welcome to The Northpole!" + footer0);
							p.sendMessage(" ");
							p.sendMessage(" ");
							p.sendMessage(s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+end);
						}
					},20 * 2);
					}
				}else{
					World world = Bukkit.getServer().getWorld(plugin.getConfig().getString("portal" + ".returnWorld"));
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
	public int TaskID;

	//Method not used at the moment due to 1.7.2 Dev Limitations.
	/*public void welcome(final Player p){
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				if(p.getWorld().getName() == "Christmas"){

					TaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){

						int Color = 0;
						int Count = 0;
						public void run(){

							if (Count == 50){
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									public void run() {
										plugin.getServer().getScheduler().cancelTask(TaskID);
									}
								},20 * 2);
							}else if(Color  == 0){
								Packets.displayTextBar(header0 + "Welcome to The Northpole!" + footer0, p, plugin);
								Color++;
								Count++;
							}else if (Color == 1){
								Packets.displayTextBar(header1 + "Welcome to The Northpole!" + footer1, p, plugin);
								Color++;
								Count++;
							}else if (Color == 2){
								Packets.displayTextBar(header2 + "Welcome to The Northpole!" + footer2, p, plugin);
								Color = 0;
								Count++;
							}
						}
					}, 0, 3);

					Fireworks.Fireworks(p);
					SparklerListener.giveSparkler(p);
				}
			}
		}, 40L);
	}
	 */

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