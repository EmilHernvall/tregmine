package info.tregmine.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class Who {

	public static void run(Tregmine _plugin, TregminePlayer _player, String[] _args){
		TregminePlayer from = _player;

		int i = 0;
		int x = 0;
		int c = 0;
		
		String[] buff = new String[100];

		
		from.sendMessage(ChatColor.DARK_AQUA + "********** PLAYERLIST **********");
		for (Player player : _plugin.getServer().getOnlinePlayers()) {
			
			if(i>=4) {
				i=0;
				x++;
			}
			if (buff[x] == null ) {
				buff[x] = "";
			}
			
			
			info.tregmine.api.TregminePlayer locTregminePlayer = _plugin.tregminePlayer.get(player.getName());
			if (!locTregminePlayer.getMetaBoolean("invis")) {
				buff[x] = buff[x] + locTregminePlayer.getChatName() + ChatColor.WHITE + ", ";
				i++;
				c++;
			}
		}
		for (String line : buff) {
			if (line != null) {
				from.sendMessage(line);
			}
		}
		from.sendMessage(ChatColor.DARK_AQUA + "********** "+ ChatColor.DARK_PURPLE + c + ChatColor.DARK_AQUA +" players online **********");
	}
}
