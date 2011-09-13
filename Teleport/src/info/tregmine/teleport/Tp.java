package info.tregmine.teleport;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Tp {

	public Tp(Player from, Player to, Teleport plugin) {
		if (to != null ){
			info.tregmine.api.TregminePlayer tregminePlayerFrom = plugin.tregmine.tregminePlayer.get(from.getName());
			info.tregmine.api.TregminePlayer tregminePlayerTo = plugin.tregmine.tregminePlayer.get(to.getName());
			boolean succeed = false;

			
			if (tregminePlayerTo.getMetaBoolean("invis")) {
				return;
			}

			
			if (tregminePlayerTo.getMetaBoolean("tpblock") && !tregminePlayerFrom.isAdmin()) {
				from.sendMessage(ChatColor.RED + "FFS! Annoy someone else -- TP to them is prohibited.");
				to.sendMessage(from.getName() + ChatColor.AQUA + " tried to tp to you, laugh at him, as he failed and hurt im self hard.");
				return;
			}
			
			
			double distance = info.tregmine.api.math.Distance.calc2d(from.getLocation(), to.getLocation());



				if (tregminePlayerFrom.isAdmin() && !succeed) {
					from.sendMessage(ChatColor.AQUA + "You teleported to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName());
					from.setNoDamageTicks(200);
					from.teleport(to.getLocation());
					succeed = true;
				}

				//TODO uglu soltution but it will change with the new permisson system
				if (tregminePlayerFrom.getMetaBoolean("mentor") && !succeed) {
					from.sendMessage(ChatColor.AQUA + "You teleported to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName());
					from.setNoDamageTicks(200);
					from.teleport(to.getLocation());
					succeed = true;
				}

				if(from.getWorld().getName().matches("matrix")) {
					from.sendMessage("Sorry you can't teleport in matrix");
					return;
				}

				if ((from.getWorld().getName().matches(to.getWorld().getName()))) {
					
				if (tregminePlayerFrom.isDonator() && distance < 10000 && !succeed) {
					from.sendMessage(ChatColor.AQUA + "You teleported to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName());
					to.sendMessage(ChatColor.AQUA + from.getName() + " teleported to you!");
					from.setNoDamageTicks(200);
					from.teleport(to.getLocation());
					succeed = true;
				}

				if (tregminePlayerFrom.isTrusted() && distance < 100 && !succeed) {
					from.sendMessage(ChatColor.AQUA + "You teleported to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName());
					to.sendMessage(ChatColor.AQUA + from.getName() + " teleported to you!");
					from.teleport(to.getLocation());
					from.setNoDamageTicks(200);
					succeed = true;
				}

				if(!succeed) {
					from.sendMessage(ChatColor.RED + "The person you try to teleport to is to far away.");				
				}

			} else {
				from.sendMessage(ChatColor.RED + "The user is in another world called " + ChatColor.BLUE + to.getWorld().getName());				
			}
		} else {
			from.sendMessage(ChatColor.RED + "Can't find user");
		}
	}
}
