package info.tregmine.commands;

import java.util.List;

import org.bukkit.entity.Horse;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class SummonCommand extends AbstractCommand
{
    public SummonCommand(Tregmine tregmine)
    {
        super(tregmine, "summon");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
	if (!player.isAdmin()) {
		return true;
	}
        if (args.length == 0) {
            return false;
        }

        String pattern = args[0];

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            player.sendMessage(RED + "Can't find user.");
        }

        TregminePlayer victim = candidates.get(0);

        victim.setNoDamageTicks(200);
        
        Horse horse = null;
        
        if((victim.getVehicle() != null) && (victim.getVehicle() instanceof Horse)){
        	horse = (Horse)victim.getVehicle();
        }
        
        if(horse != null){
        	horse.eject();
        	horse.teleport(victim.getLocation());
        	victim.teleport(victim.getLocation());
        	horse.setPassenger(victim);
        }else{
        	victim.teleport(player.getLocation());
        }
        
        victim.sendMessage(player.getChatName() + AQUA + " summoned you.");
        player.sendMessage(AQUA + "You summoned " + victim.getChatName() + AQUA
                + " to yourself.");

        return true;
    }
}
