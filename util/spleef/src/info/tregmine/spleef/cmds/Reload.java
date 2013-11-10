package info.tregmine.spleef.cmds;

import info.tregmine.spleef.ArenaManager;
import info.tregmine.spleef.MessageManager;

import org.bukkit.entity.Player;

public class Reload extends SubCommand {

	public void onCommand(Player p, String[] args) {
	    
        //TODO Remove when development finishes and add Tregmine Ranks
        if (!p.isOp() && !p.getName().equals("ice374")) {
            MessageManager.getInstance().severe(p, "Sorry, You dont have permission to perform this command.");
            return;
        }
	    
		ArenaManager.getInstance().setup();
		MessageManager.getInstance().good(p, "Reloaded!");
	}
	
	public String name() {
		return "reload";
	}
	
	public String info() {
		return "Reload spleef.";
	}
	
	public String[] aliases() {
		return new String[] { "r" };
	}
}