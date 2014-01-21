package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.Notification;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

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
        
        String[] receivingPlayers = args[0].split(",");
        try (IContext ctx = tregmine.createContext()) {
            for (String possiblePlayer : receivingPlayers) {
                TregminePlayer receivingPlayer = tregmine.getPlayer(possiblePlayer);
                if (receivingPlayer == null) {
                    player.sendNotification(Notification.COMMAND_FAIL, ChatColor.RED + "No player found by the name of " + args[0]);
                    continue;
                }
                
                boolean ignored;

                IPlayerDAO playerDAO = ctx.getPlayerDAO();
                ignored = playerDAO.doesIgnore(receivingPlayer, player);

                if (player.getRank().canNotBeIgnored()) ignored = false;
                if (ignored) continue;
                
                // Show message in senders terminal, as long as the recipient isn't
                // invisible, to prevent /msg from giving away hidden players presence
                if (!receivingPlayer.hasFlag(TregminePlayer.Flags.INVISIBLE) || player.getRank().canSeeHiddenInfo()) {
                    player.sendMessage(GREEN + "(to) " + receivingPlayer.getChatName()
                            + GREEN + ": " + message);
                }
                
                // Send message to recipient
                receivingPlayer.sendNotification(Notification.MESSAGE, GREEN + "(msg) " + player.getChatName() + GREEN
                        + ": " + message);
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
