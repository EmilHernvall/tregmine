package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

public class VanishCommand extends AbstractCommand
{
    public VanishCommand(Tregmine tregmine)
    {
        super(tregmine, "vanish");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canVanish()) {
            return true;
        }

        if (args.length == 0) {
            if (player.hasFlag(TregminePlayer.Flags.INVISIBLE)) {
                player.sendMessage(DARK_AQUA + "You are currently invisible.");
            } else {
                player.sendMessage(DARK_AQUA + "You are currently visible.");
            }
            return true;
        }

        String state = args[0];
        boolean vanish = false;
        if ("on".equalsIgnoreCase(state)) {
            player.setFlag(TregminePlayer.Flags.INVISIBLE);
            vanish = true;
        }
        else if ("off".equalsIgnoreCase(state)) {
            player.removeFlag(TregminePlayer.Flags.INVISIBLE);
            vanish = false;
        }
        else {
            return false;
        }

        List<TregminePlayer> players = tregmine.getOnlinePlayers();
        for (TregminePlayer current : players) {
            if (vanish && !current.getRank().canVanish()) {
                current.hidePlayer(player);
            } else {
                current.showPlayer(player);
            }
        }

        try (IContext ctx = tregmine.createContext()) {
            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            playerDAO.updatePlayer(player);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (vanish) {
            player.sendMessage(YELLOW + "You are now invisible!");
        }
        else {
            player.sendMessage(YELLOW + "You are no longer hidden!");
        }

        return true;
    }
}
