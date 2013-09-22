package info.tregmine.spleef.cmds;

import info.tregmine.spleef.Arena;
import info.tregmine.spleef.ArenaManager;
import info.tregmine.spleef.MessageManager;

import org.bukkit.entity.Player;

public class Leave extends SubCommand {

	public void onCommand(Player p, String[] args) {
		Arena a = ArenaManager.getInstance().getArena(p);
		
		if (a == null) {
			MessageManager.getInstance().severe(p, "You are not in a game!");
			return;
		}

		a.removePlayer(p, false);
	}
	
	public String name() {
		return "leave";
	}
	
	public String info() {
		return "Leave an arena.";
	}
	
	public String[] aliases() {
		return new String[] { "l" };
	}
}