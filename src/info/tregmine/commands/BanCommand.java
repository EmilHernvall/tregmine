package info.tregmine.commands;

import java.util.List;
import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.PlayerReport;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerReportDAO;

public class BanCommand extends AbstractCommand
{
    public BanCommand(Tregmine tregmine)
    {
        super(tregmine, "ban");
    }

    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 1; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canBan()) {
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(DARK_AQUA + "/ban <player> <message>");
            return false;
        }

        String pattern = args[0];
        String message = argsToMessage(args);

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            // TODO: List users
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        victim.kickPlayer("Banned by " + player.getName() + ": " + message);

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            PlayerReport report = new PlayerReport();
            report.setSubjectId(victim.getId());
            report.setIssuerId(player.getId());
            report.setAction(PlayerReport.Action.BAN);
            report.setMessage(message);
            // three days default
            report.setValidUntil(new Date(
                    System.currentTimeMillis() + 3 * 86400 * 1000l));

            DBPlayerReportDAO reportDAO = new DBPlayerReportDAO(conn);
            reportDAO.insertReport(report);
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

        Server server = tregmine.getServer();
        server.broadcastMessage(victim.getChatName() + RED + " was banned by "
                + player.getChatName() + ".");

        LOGGER.info(victim.getName() + " Banned by " + player.getName());

        return true;
    }
}
