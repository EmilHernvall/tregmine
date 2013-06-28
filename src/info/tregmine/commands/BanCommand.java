package info.tregmine.commands;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerDAO;

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

        victim.setBanned(true);
        victim.kickPlayer("banned by " + player.getName());

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayerPermissions(victim);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {}
            }
        }

        Server server = tregmine.getServer();
        server.broadcastMessage(victim.getChatName() + RED + " was banned by " +
                                player.getChatName() + ".");

        LOGGER.info(victim.getName() + " Banned by " + player.getName());

        return true;
    }
}
