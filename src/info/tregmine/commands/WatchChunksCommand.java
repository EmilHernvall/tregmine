package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.*;

public class WatchChunksCommand extends AbstractCommand
{
    public WatchChunksCommand(Tregmine tregmine)
    {
        super(tregmine, "watchchunks");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length < 1) {
            player.sendMessage("Your WatchChunks is set to " +
                (player.hasFlag(TregminePlayer.Flags.WATCHING_CHUNKS) ? "on" : "off") + ".");
            return true;
        }

        String state = args[0];

        if ("on".equalsIgnoreCase(state)) {
            player.setFlag(TregminePlayer.Flags.WATCHING_CHUNKS);
            player.sendMessage(AQUA + "Watching Chunks is now turned on for you.");
        }
        else if ("off".equalsIgnoreCase(state)) {
            player.removeFlag(TregminePlayer.Flags.WATCHING_CHUNKS);
            player.sendMessage(AQUA + "Watching Chunks is now turned off for you.");
        }
        else if ("status".equalsIgnoreCase(state)) {
            player.sendMessage("Your Watching Chunks is set to " +
                (player.hasFlag(TregminePlayer.Flags.WATCHING_CHUNKS) ? "on" : "off") + ".");
        }
        else {
            player.sendMessage(RED
                    + "The commands are /watchchunks on, /watchchunks off and /watchchunks status.");
        }

        try (IContext ctx = tregmine.createContext()) {
            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            playerDAO.updatePlayer(player);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

}
