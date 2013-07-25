package info.tregmine.commands;

import org.bukkit.ChatColor;
import org.bukkit.Server;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class SeenCommand extends AbstractCommand
{

    public SeenCommand(Tregmine tregmine)
    {
        super(tregmine, "seen");
    }

    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1)
            return false;

        TregminePlayer target = tregmine.getPlayerOffline(args[0]);

        if (target != null) {
            player.sendMessage(ChatColor.GREEN + args[0] + ChatColor.YELLOW
                    + " was last seen on: " + ChatColor.AQUA
                    + tregmine.getLastSeen(target));
        }
        else {
            player.sendMessage(ChatColor.RED + "Could not find player: "
                    + ChatColor.YELLOW + args[0]);
        }

        return true;
    }

    public boolean handleOther(Server server, String[] args)
    {
        if (args.length != 1)
            return false;

        TregminePlayer target = tregmine.getPlayerOffline(args[0]);

        if (target != null) {
            server.getConsoleSender().sendMessage(
                    args[0] + " was last seen on: "
                            + tregmine.getLastSeen(target));
        }
        else {
            server.getConsoleSender().sendMessage(
                    "Could not find player: " + args[0]);
        }

        return true;
    }

}
