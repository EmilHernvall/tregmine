package info.tregmine.commands;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class LockdownCommand extends AbstractCommand{
	Tregmine plugin;
	public LockdownCommand(Tregmine tregmine){
		super(tregmine, "lockdown");
		plugin = tregmine;
	}
	public boolean handlePlayer(TregminePlayer player, String[] args){
		if(!player.getIsAdmin()){
			player.nopermsMessage(true, "lockdown");
			return true;
		}
		if(args.length == 0){
			player.sendMessage(ChatColor.RED + "You must specify <on|off>");
			return true;
		}
		System.out.println(args[0]);
		boolean state;
		if(args[0].equalsIgnoreCase("on")){
			state = true;
		}else if(args[0].equalsIgnoreCase("off")){
			state = false;
		}else{
			//Something really bad happened.
			return false;
		}
		plugin.setLockdown(state);
		return true;
	}

}
