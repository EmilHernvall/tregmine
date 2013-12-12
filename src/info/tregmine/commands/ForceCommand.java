package info.tregmine.commands;

import java.util.List;

import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IMentorLogDAO;

public class ForceCommand extends AbstractCommand
{
    public ForceCommand(Tregmine tregmine)
    {
        super(tregmine, "force");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 2) {
            return false;
        }

        String playerPattern = args[0];
        String channel = args[1];

        List<TregminePlayer> matches = tregmine.matchPlayer(playerPattern);
        if (matches.size() != 1) {
            // TODO: List candidates
            return true;
        }

        TregminePlayer toPlayer = matches.get(0);
        
        if (toPlayer.hasFlag(TregminePlayer.Flags.FORCESHIELD) &&
                !player.getRank().canOverrideForceShield()) {
            toPlayer.sendMessage(ChatColor.AQUA + player.getChatName() + " tried to force you into a channel!");
            player.sendMessage(ChatColor.AQUA + "Can not force " + toPlayer.getChatName() + " into a channel!");
            return true;
        }
        String oldChannel = player.getChatChannel();
        player.setChatChannel(channel);
        toPlayer.setChatChannel(channel);

        toPlayer.sendMessage(YELLOW + player.getChatName()
                + " forced you into " + "channel " + channel.toUpperCase()
                + ".");
        toPlayer.sendMessage(YELLOW
                + "Write /channel global to switch back to "
                + "the global chat.");
        player.sendMessage(YELLOW + "You are now in a forced chat "
                + channel.toUpperCase() + " with " + toPlayer.getDisplayName()
                + ".");
        LOGGER.info(player.getName() + " FORCED CHAT WITH "
                + toPlayer.getDisplayName() + " IN CHANNEL "
                + channel.toUpperCase());
        
        for (TregminePlayer players : tregmine.getOnlinePlayers()) {
            if (oldChannel.equalsIgnoreCase(players.getChatChannel())) {
                players.sendMessage(player.getChatName() + ChatColor.YELLOW + " and " + toPlayer.getChatName() + ChatColor.YELLOW +
                        " have left channel " + oldChannel);
            } else if (channel.equalsIgnoreCase(players.getChatChannel())) {
                players.sendMessage(player.getChatName() + ChatColor.YELLOW + " and " + toPlayer.getChatName() + ChatColor.YELLOW +
                        " have joined channel " + channel);
            }
        }

        // If this is a mentor forcing his student, log it in the mentorlog
        TregminePlayer student = player.getStudent();
        if (student != null && student.getId() == toPlayer.getId() &&
            !"global".equalsIgnoreCase(channel)) {
            try (IContext ctx = tregmine.createContext()) {
                IMentorLogDAO mentorLogDAO = ctx.getMentorLogDAO();
                int mentorLogId = mentorLogDAO.getMentorLogId(student, player);
                mentorLogDAO.updateMentorLogChannel(mentorLogId, channel);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
