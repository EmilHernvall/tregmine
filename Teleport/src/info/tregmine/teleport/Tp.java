package info.tregmine.teleport;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Tp {

	public Tp(Player from, Player to, Teleport plugin) {
		if (to != null ){
			info.tregmine.api.TregminePlayer tregminePlayerFrom = plugin.tregmine.tregminePlayer.get(from.getName());
			info.tregmine.api.TregminePlayer tregminePlayerTo = plugin.tregmine.tregminePlayer.get(to.getName());
			boolean succeed = false;


			if (tregminePlayerTo.getMetaBoolean("invis")) {
				return;
			}


			if (tregminePlayerTo.getMetaBoolean("tpblock") && !tregminePlayerFrom.isAdmin() ) {
				from.sendMessage(ChatColor.RED + to.getName() + ChatColor.AQUA + "'s teloptical deflector absorbed all motion. Teleportation failed.");
				to.sendMessage(from.getName() + ChatColor.AQUA + "'s teleportation spell cannot bypass your sophisticated defenses.");
				return;
			}

			double distance = info.tregmine.api.math.Distance.calc2d(from.getLocation(), to.getLocation());

			if (tregminePlayerFrom.isAdmin() && !succeed) {
				from.sendMessage(ChatColor.AQUA + "You started teleport to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName() + ".");

				final Player tempfrom = from;
				final Player tempto = to;
				final Plugin teleport = from.getServer().getPluginManager().getPlugin("Teleport");
				
				from.getServer().getScheduler().scheduleSyncDelayedTask(teleport, new Runnable() {
					@Override
					public void run() {
//						tempto.sendMessage(ChatColor.AQUA + tempfrom.getName() + " teleported to you!");
						tempfrom.teleport(tempto.getLocation());
						tempfrom.setNoDamageTicks(200);
						PotionEffect ef = new PotionEffect(PotionEffectType.BLINDNESS, 60, 100);
						tempfrom.getPlayer().addPotionEffect(ef);
					}},20*0);
				succeed = true;
			}

			//TODO uglu soltution but it will change with the new permisson system
			if (tregminePlayerFrom.getMetaBoolean("mentor") && !succeed) {
				from.sendMessage(ChatColor.AQUA + "You started teleport to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName() + ".");

				final Player tempfrom = from;
				final Player tempto = to;
				final Plugin teleport = from.getServer().getPluginManager().getPlugin("Teleport");
				
				from.getServer().getScheduler().scheduleSyncDelayedTask(teleport, new Runnable() {
					@Override
					public void run() {
						tempto.sendMessage(ChatColor.AQUA + tempfrom.getName() + " teleported to you!");
						tempfrom.teleport(tempto.getLocation());
						tempfrom.setNoDamageTicks(200);
						PotionEffect ef = new PotionEffect(PotionEffectType.BLINDNESS, 60, 100);
						tempfrom.getPlayer().addPotionEffect(ef);
					}},20*1);
				succeed = true;
			}

			if ((from.getWorld().getName().matches(to.getWorld().getName()))) {

				if (tregminePlayerFrom.isDonator() && distance < 10000 && !succeed) {
					from.sendMessage(ChatColor.AQUA + "You started teleport to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName() + ".");

					final Player tempfrom = from;
					final Player tempto = to;
					final Plugin teleport = from.getServer().getPluginManager().getPlugin("Teleport");
					
					from.getServer().getScheduler().scheduleSyncDelayedTask(teleport, new Runnable() {
						@Override
						public void run() {
							tempto.sendMessage(ChatColor.AQUA + tempfrom.getName() + " teleported to you!");
							tempfrom.teleport(tempto.getLocation());
							tempfrom.setNoDamageTicks(200);
							PotionEffect ef = new PotionEffect(PotionEffectType.BLINDNESS, 60, 100);
							tempfrom.getPlayer().addPotionEffect(ef);
						}},20*30);
					succeed = true;
				}

				if (tregminePlayerFrom.isTrusted() && distance < 100 && !succeed) {
					from.sendMessage(ChatColor.AQUA + "You started teleport to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName() + ".");

					final Player tempfrom = from;
					final Player tempto = to;
					final Plugin teleport = from.getServer().getPluginManager().getPlugin("Teleport");
					
					from.getServer().getScheduler().scheduleSyncDelayedTask(teleport, new Runnable() {
						@Override
						public void run() {
							tempto.sendMessage(ChatColor.AQUA + tempfrom.getName() + " teleported to you!");
							tempfrom.teleport(tempto.getLocation());
							tempfrom.setNoDamageTicks(200);
							PotionEffect ef = new PotionEffect(PotionEffectType.BLINDNESS, 60, 100);
							tempfrom.getPlayer().addPotionEffect(ef);
						}},20*30);
					succeed = true;
				}

				if(!succeed) {
					from.sendMessage(ChatColor.RED + "Your teleportation spell is not strong enough for the longer distances.");			
				}

			} else {
				from.sendMessage(ChatColor.RED + "The user is in another world called " + ChatColor.BLUE + to.getWorld().getName() + ".");				
			}
		} else {
			from.sendMessage(ChatColor.RED + "Can't find user.");
		}
	}
}
