package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public abstract class NotifyCommand extends AbstractCommand
{
    public NotifyCommand(Tregmine tregmine, String command)
    {
        super(tregmine, command);
    }

    protected abstract boolean isTarget(TregminePlayer player);

    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(args[0]);
        for (int i = 1; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length == 0) {
            return false;
        }

        Server server = player.getServer();
        String msg = argsToMessage(args);

        Player[] players = server.getOnlinePlayers();
        for (Player tp : players) {
            TregminePlayer to = tregmine.getPlayer(tp);
            if (!isTarget(to)) {
                continue;
            }
            to.sendMessage("* " + player.getChatName() + WHITE + msg);
        }

        return true;
    }
}
