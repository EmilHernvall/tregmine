package info.tregmine.commands;

import java.util.List;

import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.Notification;
import info.tregmine.api.TregminePlayer;

public class BlessCommand extends AbstractCommand
{
    public BlessCommand(Tregmine tregmine)
    {
        super(tregmine, "bless");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length == 0) {
            return false;
        }
        if (!player.getRank().canBless()) {
            return true;
        }

        List<TregminePlayer> candidates = tregmine.matchPlayer(args[0]);
        if (candidates.size() != 1) {
            player.sendNotification(Notification.COMMAND_FAIL, ChatColor.RED + "No player found");
            return false;
        }

        TregminePlayer candidate = candidates.get(0);
        player.sendMessage(AQUA + "You will bless following " + "blocks to "
                + candidate.getChatName() + ".");
        player.setBlessTarget(candidate.getId());

        return true;
    }
}
