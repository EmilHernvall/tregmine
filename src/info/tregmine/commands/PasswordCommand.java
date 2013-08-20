package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

public class PasswordCommand extends AbstractCommand
{
    public PasswordCommand(Tregmine tregmine)
    {
        super(tregmine, "password");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        String password = args[0];

        if (password.length() < 1) {
            player.sendMessage(RED + "Your password must be at least "
                    + "6 characters long.");
            return true;
        }

        player.setPassword(password);
        player.sendMessage(YELLOW + "Your password has been changed.");

        try (IContext ctx = tregmine.createContext()) {
            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            playerDAO.updatePlayer(player);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
