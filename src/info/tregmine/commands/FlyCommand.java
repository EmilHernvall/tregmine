package info.tregmine.commands;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

public class FlyCommand extends AbstractCommand
{
    private Tregmine tregmine;
    public FlyCommand(Tregmine tregmine)
    {
        super(tregmine, "fly");
        this.tregmine = tregmine;
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String args[])
    {
        if (!player.getRank().canFly()) return false;

        if ( player.hasFlag(TregminePlayer.Flags.FLY_ENABLED) ) {
            player.sendMessage(ChatColor.YELLOW +
                    "Flying Disabled!");
            player.removeFlag(TregminePlayer.Flags.FLY_ENABLED);
            player.setAllowFlight(false);
        } else {

            player.sendMessage(ChatColor.YELLOW +
                    "Flying Enabled!");
            player.setFlag(TregminePlayer.Flags.FLY_ENABLED);
            player.setAllowFlight(true);
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
