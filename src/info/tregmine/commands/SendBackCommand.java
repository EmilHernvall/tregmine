package info.tregmine.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class SendBackCommand extends AbstractCommand{
	Tregmine plugin;
	public SendBackCommand(Tregmine tregmine){
		super(tregmine, "sendback");
		plugin = tregmine;
	}
	public boolean handlePlayer(TregminePlayer player, String[] args){
		if(!player.getIsAdmin()){
			player.nopermsMessage(false, "sendback");
			return true;
		}
		if(args.length != 1){
			player.sendMessage(ChatColor.RED + "Arguments error: /sendback <player name>");
			return true;
		}
		List<TregminePlayer> players = plugin.matchPlayer(args[0]);
		if(players.size() != 1){
			player.sendMessage(ChatColor.RED + "Player is not online!");
			return true;
		}
		TregminePlayer target = players.get(0);
		if(target.getLastPos() == null){
			player.sendMessage(ChatColor.RED + "Player does not have a last location.");
			return true;
		}
		if(!target.teleport(target.getLastPos())){
			Location pos = target.getLastPos();
			player.sendMessage(ChatColor.RED + "Player could not be teleported. Here are the coordinates:");
			player.sendMessage(ChatColor.AQUA + "X" + pos.getBlockX() + " Y" + pos.getBlockY() + " Z" + pos.getBlockZ());
			return true;
		}
		return true;
	}
}
