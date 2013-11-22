package info.tregmine.commands;

import static org.bukkit.ChatColor.DARK_AQUA;
import static org.bukkit.ChatColor.YELLOW;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

import java.util.List;

import org.bukkit.ChatColor;

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
            
            for (TregminePlayer to : tregmine.getOnlinePlayers()) {
                if (player.getQuitMessage() != null) {
                    to.sendMessage(player.getChatName() + " quit: " + ChatColor.YELLOW + player.getQuitMessage());
                }
                if (to.getRank().canSeeHiddenInfo()) {
                    to.sendMessage(player.getChatName() + ChatColor.DARK_AQUA + " just went invisible!");
                }
            }
        }
        else {
            player.sendMessage(YELLOW + "You are no longer hidden!");
            
            for (TregminePlayer to : tregmine.getOnlinePlayers()) {
                if (player.getCountry() == null) {
                    to.sendMessage(
                            ChatColor.DARK_AQUA + "Welcome " + player.getChatName());
                } else {
                    to.sendMessage(
                            ChatColor.DARK_AQUA + "Welcome " + player.getChatName() +
                            ChatColor.DARK_AQUA + " from " + player.getCountry() + "!");
                }
                if (to.getRank().canSeeHiddenInfo()) {
                    to.sendMessage(player.getChatName() + ChatColor.DARK_AQUA + " is no longer invisible!");
                }
            }
        }

        return true;
    }
}
