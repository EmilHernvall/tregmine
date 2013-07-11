package info.tregmine.commands;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.ChatColor.*;
import org.bukkit.entity.Player;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerDAO;

public class UserCommand extends AbstractCommand
{
    public UserCommand(Tregmine tregmine)
    {
        super(tregmine, "user");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.isGuardian() && !player.isAdmin()) {
            return false;
        }
        if (args.length == 0) {
            return false;
        }

        if ("make".equalsIgnoreCase(args[0])) {
            return make(player, args);
        }
        else if ("reload".equalsIgnoreCase(args[0]) && args.length == 2) {
            try {
                return reload(player, args[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }

        return true;
    }

    private boolean reload(TregminePlayer player, String pattern)
    {
        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            return true;
        }

        Player candidate = candidates.get(0);
        tregmine.reloadPlayer(player);
        player.sendMessage("Player reloaded " + candidate.getDisplayName());
        return true;
    }

    private boolean make(TregminePlayer player, String[] args)
    {
        List<TregminePlayer> candidates = tregmine.matchPlayer(args[2]);
        if (candidates.size() != 1) {
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        if ("settler".equalsIgnoreCase(args[1])) {
            victim.setNameColor("trial");
            victim.setTrusted(true);
            victim.setTemporaryChatName(victim.getNameColor()
                    + victim.getName());

            player.sendMessage(AQUA + "You made " + victim.getChatName() + AQUA
                    + " settler of this server.");
            victim.sendMessage("Welcome! You are now made settler.");
            LOGGER.info(victim.getName() + " was given settler rights by "
                    + player.getName() + ".");
        }
        else if ("resident".equalsIgnoreCase(args[1]) && player.isOp()) {
            victim.setNameColor("trusted");
            victim.setTrusted(true);
            victim.setTemporaryChatName(victim.getNameColor()
                    + victim.getName());

            LOGGER.info(victim.getName() + " was given trusted rights by "
                    + player.getChatName() + ".");
            player.sendMessage(AQUA + "You made " + victim.getChatName() + AQUA
                    + " a resident.");
            victim.sendMessage("Welcome! You are now a resident");
        }
        else if ("donator".equalsIgnoreCase(args[1])) {
            victim.setDonator(true);
            victim.setAllowFlight(true);
            victim.setNameColor("donator");
            victim.setTemporaryChatName(victim.getNameColor()
                    + victim.getName());

            player.sendMessage(AQUA + "You made  " + victim.getChatName()
                    + " a donator.");
            LOGGER.info(victim.getName() + " was made donator by"
                    + player.getChatName() + ".");
            victim.sendMessage("Congratulations, you are now a donator!");
        }
        else if ("child".equalsIgnoreCase(args[1])) {
            victim.setChild(true);
            victim.setNameColor("child");

            player.sendMessage(AQUA + "You made  " + victim.getChatName()
                    + " a child.");
            LOGGER.info(victim.getName() + " was made child by"
                    + player.getChatName() + ".");
            victim.setTemporaryChatName(victim.getNameColor()
                    + victim.getName());
        }
        else {
            return false;
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayerPermissions(victim);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        return true;
    }
}
