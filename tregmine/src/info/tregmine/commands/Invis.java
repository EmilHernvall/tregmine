package info.tregmine.commands;

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
		if (tregPlayer.isOp()) {
			return true;
		}

		return false;
	}

	
	
	public Boolean execute() {
		if (allowed()) {
			if (this.flags[0].equals("status")) {
				this.tregPlayer.sendMessage("Your invis are currently: " + this.tregPlayer.getInvis());
			}
			return true;
		}
		
		return false;
	}

}
