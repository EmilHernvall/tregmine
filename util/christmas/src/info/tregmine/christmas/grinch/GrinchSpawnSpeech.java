package info.tregmine.christmas.grinch;

import info.tregmine.christmas.ChristmasMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

public class GrinchSpawnSpeech implements Listener {

	private static ChristmasMain plugin;
	
	public GrinchSpawnSpeech(ChristmasMain instance)
	{
		this.plugin = instance;
	}

	/**
	 * Possibly the ugliest piece of code that i have ever written
	 */
	
	static String string1 = ChatColor.GRAY + "Why have you summoned me you fool?!?";
	static String string2 = ChatColor.GRAY + "You want to tell me what Christmas is really about?";
	static String string3 = ChatColor.GRAY + "I know what Christmas is 'about'!";
	static String string4 = ChatColor.GRAY + "I know what it's always been 'about'!";
	static String string5 = ChatColor.GRAY + "Gifts, gifts... gifts, gifts, gifts, gifts, gifts!";
	static String string6 = ChatColor.GRAY + "You wanna know what's happened to your presents?";
	static String string7 = ChatColor.GRAY + "I took them all, because Christmas is one thing i resent!";
	static String string8 = ChatColor.GRAY + "Look, I don't wanna make waves, but this whole Christmas season is STUPID!";
	static String string9 = ChatColor.GRAY + "If you wish to ever see your presents again, then im afraid you will have to kill me!";
	
	public static void speech(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string1);
				speechPt2(p, witch, loc);
			}
        }, 60L);
	}
	
	public static void speechPt2(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string2);
				speechPt3(p, witch, loc);
			}
        }, 60L);
	}
	
	public static void speechPt3(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string3);
				speechPt4(p, witch, loc);
			}
        }, 60L);
	}
	
	public static void speechPt4(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string4);
				speechPt5(p, witch, loc);
			}
        }, 60L);
	}
	
	public static void speechPt5(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string5);
				speechPt6(p, witch, loc);
			}
        }, 60L);
	}
	
	public static void speechPt6(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string6);
				speechPt7(p, witch, loc);
			}
        }, 60L);
	}
	
	public static void speechPt7(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string7);
				speechPt8(p, witch, loc);
			}
        }, 60L);
	}
	
	public static void speechPt8(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string8);
				speechPt9(p, witch, loc);
			}
        }, 60L);
	}

	public static void speechPt9(final Player p, final Witch witch, final Location loc){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(string9);
				witch.remove();
				GrinchListener.createTornado(loc.subtract(0,1,0), p);
			}
        }, 60L);
	}
}