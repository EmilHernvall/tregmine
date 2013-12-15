package info.tregmine.christmas.grinch;

import info.tregmine.christmas.ChristmasMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

public class GrinchDefeatSpeech implements Listener {

	private static ChristmasMain plugin;
	
	public GrinchDefeatSpeech(ChristmasMain instance)
	{
		this.plugin = instance;
	}
	
	static String string1 = "Ok, you win! You can have your presents!";
	static String string2 = "Now leave me in peace!";

	public static void speech(final Player p){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string1);
				speechPt2(p);
			}
        }, 20L);
	}
	
	public static void speechPt2(final Player p){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string2);
				speechPt3(p);
			}
        }, 20L);
	}
	
	public static void speechPt3(final Player p){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				Bukkit.broadcastMessage(ChatColor.GREEN + "Please put you hands together for " +  ChatColor.GOLD + p.getName() + ChatColor.GREEN + " who has just defeated The Grinch!");
			}
        }, 20L);
	}
}
