package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class KeywordCommand extends AbstractCommand
{
    public KeywordCommand(Tregmine tregmine)
    {
        super(tregmine, "keyword");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        String keyword = args[0];

        if (keyword.length() < 1) {
            player.sendMessage(RED + "Your keyword must be at least " +
                               "1 characters long.");
            return true;
        }

        player.setMetaString("keyword", keyword.toLowerCase());
        player.sendMessage(YELLOW + "From now on you can only log in by using ip " + 
                           keyword.toLowerCase() + ".mc.tregmine.info");

        return true;
    }
}
