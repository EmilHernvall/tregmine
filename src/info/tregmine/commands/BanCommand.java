package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class BanCommand extends AbstractCommand
{
    public BanCommand(Tregmine tregmine)
    {
        super(tregmine, "ban");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.isGuardian() && !player.isAdmin()) {
            return false;
        }

        if (args.length != 1) {
            return false;
        }

        List<TregminePlayer> candidates = tregmine.matchPlayer(args[0]);
        if (candidates.size() != 1) {
            // TODO: List users
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        victim.setMetaString("banned", "true");
        victim.kickPlayer("banned by " + player.getName());

        Server server = tregmine.getServer();
        server.broadcastMessage(victim.getChatName() + RED + " was banned by " +
                                player.getChatName() + ".");

        LOGGER.info(victim.getName() + " Banned by " + player.getName());

        return true;
    }
}
