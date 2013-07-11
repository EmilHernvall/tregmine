package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

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

        return true;
    }
}
