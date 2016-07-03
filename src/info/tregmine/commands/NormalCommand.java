package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.TregminePlayer.GuardianState;
import info.tregmine.api.Rank;

public class NormalCommand extends AbstractCommand
{
    public NormalCommand(Tregmine tregmine)
    {
        super(tregmine, "normal");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (player.getRank() == Rank.JUNIOR_ADMIN ||
            player.getRank() == Rank.SENIOR_ADMIN) {
        	String requestedRank = args[0];
        	if(requestedRank.equalsIgnoreCase("donator")){
        		player.setTemporaryRank(Rank.DONATOR);
                player.setTemporaryChatName(GOLD + player.getName());
                player.sendMessage(YELLOW + "You are now donator until you reconnect.");
                return true;
        	}else if(requestedRank.equalsIgnoreCase("guardian")){
        		player.setTemporaryRank(Rank.GUARDIAN);
                player.setTemporaryChatName(BLUE + player.getName());
                player.setGuardianState(GuardianState.ACTIVE);
                player.sendMessage(YELLOW + "You are now guardian until you reconnect.");
        		return true;
        	}else{
        		player.sendMessage(ChatColor.RED + "Arguments must be /normal <guardian, donator>");
        		return true;
        	}
            
        }
        else if (player.getRank() == Rank.BUILDER) {
            player.setTemporaryRank(Rank.DONATOR);
            player.setTemporaryChatName(GOLD + player.getName());
            player.sendMessage(YELLOW + "You are no longer builder, until you "
                    + "reconnect!");
        }
        else if (player.getRank() == Rank.GUARDIAN) {
            Collection<? extends Player> players = tregmine.getServer().getOnlinePlayers();
            TregminePlayer maxRank = null;
            for (Player srvPlayer : players) {
                TregminePlayer guardian = tregmine.getPlayer(srvPlayer);
                if (guardian.getRank() != Rank.GUARDIAN) {
                    continue;
                }

                TregminePlayer.GuardianState state =
                        guardian.getGuardianState();
                if (state == TregminePlayer.GuardianState.QUEUED) {
                    if (maxRank == null
                            || guardian.getGuardianRank() > maxRank
                                    .getGuardianRank()) {
                        maxRank = guardian;
                    }
                }
            }

            if (maxRank != null) {
                player.setGuardianState(TregminePlayer.GuardianState.INACTIVE);
                player.sendMessage(BLUE
                        + "You are now in normal mode, and no longer have to response to help requests.");
                maxRank.setGuardianState(TregminePlayer.GuardianState.ACTIVE);
                maxRank.sendMessage(BLUE
                        + "You are now on active duty and should respond to help requests.");
            }
            else {
                player.sendMessage(BLUE
                        + "Not enough guardians are on to manage the server. We need you to keep working. Sorry. :/");
            }
        }

        return true;
    }
}
