package info.tregmine.commands;

import org.bukkit.ChatColor;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class ChangeNameCommand extends AbstractCommand
{
    public ChangeNameCommand(Tregmine tregmine)
    {
        super(tregmine, "cname");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 2) {
            return false;
        }
        if (!player.getRank().canChangeName()) {
            return true;
        }

        ChatColor color = ChatColor.getByChar(args[0]);
        player.setTemporaryChatName(color + args[1]);
        player.sendMessage("You are now: " + player.getChatName());
        LOGGER.info(player.getName() + " changed name to "
                + player.getChatName());

        return true;
    }
}
