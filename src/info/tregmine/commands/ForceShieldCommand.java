package info.tregmine.commands;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.RED;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

public class ForceShieldCommand extends AbstractCommand
{
    public ForceShieldCommand(Tregmine tregmine)
    {
        super(tregmine, "forceblock");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length < 1) {
            player.sendMessage("Your forceblock is set to " +
                (player.hasFlag(TregminePlayer.Flags.FORCESHIELD) ? "on" : "off") + ".");
            return true;
        }

        String state = args[0];

        if ("on".equalsIgnoreCase(state)) {
            player.setFlag(TregminePlayer.Flags.FORCESHIELD);
            player.sendMessage(AQUA + "Channel Forcing is now blocked for you.");
        }
        else if ("off".equalsIgnoreCase(state)) {
            player.removeFlag(TregminePlayer.Flags.FORCESHIELD);
            player.sendMessage(AQUA + "Channel Forcing is now allowed for you.");
        }
        else if ("status".equalsIgnoreCase(state)) {
            player.sendMessage("Your forceblock is set to " +
                (player.hasFlag(TregminePlayer.Flags.FORCESHIELD) ? "on" : "off") + ".");
        }
        else {
            player.sendMessage(RED
                    + "The commands are /forceblock on, /forceblock off and /forceblock status.");
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
