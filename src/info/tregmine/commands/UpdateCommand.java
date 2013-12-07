package info.tregmine.commands;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IMotdDAO;

public class UpdateCommand extends AbstractCommand
{
    public UpdateCommand(Tregmine tregmine)
    {
        super(tregmine, "update");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String args[])
    {
        String version;
        if (args.length == 1) {
            version = args[0];
            player.sendMessage(ChatColor.AQUA + "Searching for Version " + version);
        } else {
            version = tregmine.getDescription().getVersion();
            player.sendMessage(ChatColor.AQUA + "No Arguments detected, Searching for latest: " + version);
        }
        
        try (IContext ctx = tregmine.createContext()) {
            IMotdDAO motdDAO = ctx.getMotdDAO();
            String message = motdDAO.getUpdates(version);
            if (message == null) {
                player.sendMessage(ChatColor.AQUA + "Version not found!");
                return true;
            }
            String[] lines = message.split("::");
            for (String line : lines) {
                player.sendMessage(ChatColor.GOLD + line);
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        
        return true;
    }
}
