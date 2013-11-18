package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

public class ActionCommand extends AbstractCommand
{
    public ActionCommand(Tregmine tregmine)
    {
        super(tregmine, "action");
    }

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
        String channel = player.getChatChannel();
        String msg = argsToMessage(args);

        Player[] players = server.getOnlinePlayers();
        for (Player tp : players) {
            TregminePlayer to = tregmine.getPlayer(tp);
            if (!channel.equals(to.getChatChannel())) {
                continue;
            }
            
            boolean ignored;
            try (IContext ctx = tregmine.createContext()) {
                IPlayerDAO playerDAO = ctx.getPlayerDAO();
                ignored = playerDAO.doesIgnore(to, player);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            if (player.getRank().canNotBeIgnored()) ignored = false;
            if (ignored == true) continue;

            to.sendMessage("* " + player.getChatName() + " " + WHITE + msg);
        }

        return true;
    }
}
