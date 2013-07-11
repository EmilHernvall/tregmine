package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class VanishCommand extends AbstractCommand
{
    public VanishCommand(Tregmine tregmine)
    {
        super(tregmine, "vanish");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.isAdmin()) {
            return false;
        }
        if (args.length == 0) {
            player.sendMessage(DARK_AQUA + "/vanish <on|off>");
            return false;
        }

        String state = args[0];
        boolean vanish = false;
        if ("on".equalsIgnoreCase(state)) {
            vanish = true;
        }
        else if ("off".equalsIgnoreCase(state)) {
            vanish = false;
        }
        else {
            player.sendMessage(DARK_AQUA + "/vanish <on|off>");
            return false;
        }

        player.setInvisible(vanish);

        List<TregminePlayer> players = tregmine.getOnlinePlayers();
        for (TregminePlayer current : players) {
            if (vanish && !current.isOp()) {
                current.hidePlayer(player);
            }
            else {
                current.showPlayer(player);
            }
        }

        if (vanish) {
            player.sendMessage(YELLOW + "You are now invisible!");
        }
        else {
            player.sendMessage(YELLOW + "You no longer hidden!");
        }

        return true;
    }
}
