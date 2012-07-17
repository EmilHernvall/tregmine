package info.tregmine.commands;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class Invis {
	Tregmine tregmine = null;
	TregminePlayer tregPlayer = null;
	String[] flags = null;

	public Invis(TregminePlayer _tregPlayer, String _command, String[] _args,  Tregmine _tregmine) {
		this.tregmine = _tregmine;
		this.tregPlayer = _tregPlayer;
		this.flags = _args;
	}

	public Boolean allowed() {
		if (this.tregPlayer.isOp()) {
			return true;
		}
		return false;
	}

	
	
	public Boolean execute() {
		if (allowed()) {
			if ("status".equals(this.flags[0])) {
				this.tregPlayer.sendMessage("Your invis are currently: " + this.tregPlayer.getInvis());
			}
			return true;
		} 
		
		this.tregPlayer.sendMessage(ChatColor.RED + "You are not allowed to use this command");
		return true;
	}

}
