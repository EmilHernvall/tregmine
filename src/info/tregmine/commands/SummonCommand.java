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
        if (args.length == 0) {
            return false;
        }

        String pattern = args[0];

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            player.sendMessage(RED + "Can't find user.");
        }

        TregminePlayer victim = candidates.get(0);
        if(victim.getIsStaff()){
            victim.setLastPos(victim.getLocation());
        }

        // Mentors can summon their students, but nobody else. In those cases,
        // you need the canSummon-permission.
        if (victim != player.getStudent() && !player.getRank().canSummon()) {
            return true;
        }

        victim.setNoDamageTicks(200);

        victim.teleportWithHorse(player.getLocation());

        victim.sendMessage(player.getChatName() + AQUA + " summoned you.");
        player.sendMessage(AQUA + "You summoned " + victim.getChatName() + AQUA
                + " to yourself.");

        return true;
    }
}
