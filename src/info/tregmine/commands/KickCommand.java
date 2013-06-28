package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class KickCommand extends AbstractCommand
{
    public KickCommand(Tregmine tregmine)
    {
        super(tregmine, "kick");
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

        if (!player.isAdmin() && !player.isGuardian()) {
            return false;
        }

        Server server = player.getServer();
        String pattern = args[0];

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            // TODO: error message
            return true;
        }

        TregminePlayer victim = candidates.get(0);
        server.broadcastMessage(player.getChatName() + AQUA + " kicked " +
                                victim.getChatName() + AQUA + ".");
        LOGGER.info(victim.getName() + " kicked by " + player.getName());
        victim.kickPlayer("kicked by " + player.getName());

        return true;
    }

    public boolean handleOther(Server server, String[] args)
    {
        if (args.length == 0) {
            return false;
        }

        String pattern = args[0];

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            // TODO: error message
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        server.broadcastMessage("GOD kicked " + victim.getChatName() + ".");
        victim.kickPlayer("kicked by GOD.");

        return true;
    }
}
