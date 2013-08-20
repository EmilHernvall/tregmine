package info.tregmine.commands;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Server;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.ILogDAO;

public class SeenCommand extends AbstractCommand
{

    public SeenCommand(Tregmine tregmine)
    {
        super(tregmine, "seen");
    }

    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        TregminePlayer target = tregmine.getPlayerOffline(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Could not find player: "
                    + ChatColor.YELLOW + args[0]);
            return true;
        }

        try (IContext ctx = tregmine.createContext()) {
            ILogDAO logDAO = ctx.getLogDAO();
            Date seen = logDAO.getLastSeen(target);

            if (seen != null) {
                player.sendMessage(ChatColor.GREEN + target.getChatName() + ChatColor.YELLOW
                        + " was last seen on: " + ChatColor.AQUA + seen);
            } else {
                player.sendMessage(ChatColor.GREEN + target.getChatName() + ChatColor.YELLOW
                        + " hasn't been seen for a while.");
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public boolean handleOther(Server server, String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        TregminePlayer target = tregmine.getPlayerOffline(args[0]);
        if (target == null) {
            server.getConsoleSender()
                  .sendMessage("Could not find player: " + args[0]);
            return true;
        }

        try (IContext ctx = tregmine.createContext()) {
            ILogDAO logDAO = ctx.getLogDAO();
            Date seen = logDAO.getLastSeen(target);

            server.getConsoleSender()
                  .sendMessage(args[0] + " was last seen on: " + seen);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

}
