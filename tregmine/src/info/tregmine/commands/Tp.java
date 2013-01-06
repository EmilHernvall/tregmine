package info.tregmine.commands;

//import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class Tp {

	public static void run(Tregmine _plugin, TregminePlayer _player, String[] _args){
		Player pto = _plugin.getServer().getPlayer(_args[0]);
		
		if (pto == null) {
			_player.sendMessage(ChatColor.RED + "Can no find a user with name" + _args[0] );
			return;
		}
		info.tregmine.api.TregminePlayer to = _plugin.tregminePlayer.get(pto.getName());
		
		if (to.getMetaBoolean("invis")) {
			_player.sendMessage(ChatColor.RED + "Can no find a user with name" + _args[0] );
			return;
		}

		if (to.getMetaBoolean("tpblock") && !_player.isAdmin() ) {
			_player.sendMessage(ChatColor.RED + to.getName() + ChatColor.AQUA + "'s teloptical deflector absorbed all motion. Teleportation failed.");
			to.sendMessage(_player.getChatName() + ChatColor.AQUA + "'s teleportation spell cannot bypass your sophisticated defenses.");
			return;
		}
		
		double distance = info.tregmine.api.math.Distance.calc2d(_player.getLocation(), to.getLocation());
		
		if (_player.isAdmin()) {
			_player.sendMessage(ChatColor.AQUA + "You started teleport to " + to.getChatName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName() + ".");
			_player.teleport(to);
			return;
		}

		if (_player.getMetaBoolean("mentor")) {
			_player.sendMessage(ChatColor.AQUA + "You started teleport to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName() + ".");
			to.sendMessage(ChatColor.AQUA + _player.getChatName() + " teleported to you!");
			_player.teleport(to);
			return;
		}

		if ((_player.getWorld().getName().matches(to.getWorld().getName()))) {
			
			if (_player.isDonator() && distance < 10000) {
				_player.sendMessage(ChatColor.AQUA + "You started teleport to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName() + ".");
				to.sendMessage(ChatColor.AQUA + _player.getChatName() + " teleported to you!");
				_player.teleport(to);
				return;
			}

			if (_player.isTrusted() && distance < 100) {
				_player.sendMessage(ChatColor.AQUA + "You started teleport to " + to.getName() + ChatColor.AQUA + " in " + ChatColor.BLUE + to.getWorld().getName() + ".");
				to.sendMessage(ChatColor.AQUA + _player.getChatName() + " teleported to you!");
				_player.teleport(to);
				return;
			}
		}
	}

}
