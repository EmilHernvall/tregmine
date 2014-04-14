package info.tregmine.commands;

import java.util.List;

// import org.bukkit.entity.Horse;
import org.bukkit.entity.*;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class PKillCommand extends AbstractCommand
{
    public PKillCommand(Tregmine tregmine)
    {
        super(tregmine, "pkill");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
	// Shall we kill their horse and wipe their inventory?
	boolean nice = false;
        String pattern = args[0];

        if (args.length == 0) {
            return false;
        }
	else if (args.length == 1) {
	    if ("drops".equals(args[0])) {
		nice = true;
	    }
            pattern = args[1];
	}

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            player.sendMessage(RED + "Can't find user.");
        }

        TregminePlayer victim = candidates.get(0);

        // Only seniors can use pkill, cannot target guardians and above
        if (victim.getRank().canKick() || !player.getRank().canPKill()) {
            player.sendMessage(AQUA + "Error: You are not allowed to smite " + victim.getChatName());
            return false;
        }
	
	if (!nice) {
	    // Also kill the victim's horse
	    Entity v = victim.getVehicle();
	    if (v != null && v instanceof Horse) {
	      Horse vh = (Horse)v;
	      vh.eject();
	      vh.setHealth(0);
	    }

	    // Wipe the target's inventory
	    victim.getInventory().clear();
	}
	// Kill them
        victim.setHealth(0);

        victim.sendMessage(AQUA + "You have been smited!");
        player.sendMessage(AQUA + "You smited " + victim.getChatName());

        return true;
    }
}
