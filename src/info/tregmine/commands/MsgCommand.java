package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.Notification;
import info.tregmine.api.TregminePlayer;

public class MsgCommand extends AbstractCommand
{
    public MsgCommand(Tregmine tregmine)
    {
        super(tregmine, "msg");
    }

    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 1; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length < 2) {
            return false;
        }

        Server server = player.getServer();
        String recvPattern = args[0];
        String message = argsToMessage(args);

        Player recv = server.getPlayer(recvPattern);
        if (recv == null) {
            // TODO: Error message...
            return true;
        }

        TregminePlayer recvPlayer = tregmine.getPlayer(recv);
        if (recvPlayer == null) {
            player.sendNotification(Notification.COMMAND_FAIL, ChatColor.RED + "No player found by the name of " + args[0]);
            return true;
        }

        // Show message in senders terminal, as long as the recipient isn't
        // invisible, to prevent /msg from giving away hidden players presence
        if (!recvPlayer.hasFlag(TregminePlayer.Flags.INVISIBLE)) {
            player.sendMessage(GREEN + "(to) " + recvPlayer.getChatName()
                    + GREEN + ": " + message);
        }

        // Send message to recipient
        recvPlayer.sendNotification(Notification.MESSAGE, GREEN + "(msg) " + player.getChatName() + GREEN
                + ": " + message);

        return true;
    }
}
