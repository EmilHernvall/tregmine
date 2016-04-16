package info.tregmine.commands;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class BackCommand extends AbstractCommand{
	Tregmine plugin;
	public BackCommand(Tregmine tregmine){
		super(tregmine, "back");
		plugin = tregmine;
	}
	public boolean handlePlayer(TregminePlayer player, String[] args){
		if(!player.getIsStaff()){
			player.nopermsMessage(false, "back");
			return true;
		}
		if(player.getLastPos() == null){
			player.sendMessage("%warning%" + ChatColor.RED + "You don't have a last location!");
			return true;
		}
		boolean success = player.teleport(player.getLastPos());
		if(!success){
			player.sendMessage(ChatColor.RED + "Failed to teleport back. Sorry!");
			if(!player.getLastPos().toString().isEmpty()){
				player.sendMessage(ChatColor.RED + "But... I can give you your coordinates. X" + player.getLastPos().getBlockX() + " Y" + player.getLastPos().getBlockY() + " Z" + player.getLastPos().getBlockZ());
			}
		}
		return true;
	}
}
