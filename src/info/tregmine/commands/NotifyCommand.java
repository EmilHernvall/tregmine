package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public abstract class NotifyCommand extends AbstractCommand
{
    public NotifyCommand(Tregmine tregmine, String command)
    {
        super(tregmine, command);
    }

    protected abstract boolean isTarget(TregminePlayer player);
    protected abstract ChatColor getColor();

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

        String msg = argsToMessage(args);

        // Don't send it twice
        if (!isTarget(player)) {
            player.sendMessage(getColor() + " + " + player.getChatName() + " " + WHITE + msg);
        }

        for (TregminePlayer to : tregmine.getOnlinePlayers()) {
            if (!isTarget(to)) {
                continue;
            }
            to.sendMessage(getColor() + " + " + player.getChatName() + " " + WHITE + msg);
        }

        return true;
    }
}
