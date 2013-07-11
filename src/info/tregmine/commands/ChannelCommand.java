package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class ChannelCommand extends AbstractCommand
{
    public ChannelCommand(Tregmine tregmine)
    {
        super(tregmine, "channel");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        String channel = args[0];

        player.sendMessage(YELLOW + "You are now talking in channel " + channel
                + ".");
        player.sendMessage(YELLOW + "Write /channel global to switch to "
                + "the global chat.");
        player.setChatChannel(channel);

        return true;
    }
}
