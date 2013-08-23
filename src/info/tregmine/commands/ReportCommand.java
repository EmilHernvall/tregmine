package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.PlayerReport;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerReportDAO;
import info.tregmine.database.DAOException;

public class ReportCommand extends AbstractCommand
{
    public ReportCommand(Tregmine tregmine)
    {
        super(tregmine, "report");
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
        if (!player.getRank().canReport()) {
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(DARK_AQUA + "/report <player> <message>");
            return true;
        }

        String pattern = args[0];
        String message = argsToMessage(args);

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            // TODO: error message
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        try (IContext ctx = tregmine.createContext()) {
            PlayerReport report = new PlayerReport();
            report.setSubjectId(victim.getId());
            report.setIssuerId(player.getId());
            report.setAction(PlayerReport.Action.COMMENT);
            report.setMessage(message);

            IPlayerReportDAO reportDAO = ctx.getPlayerReportDAO();
            reportDAO.insertReport(report);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(YELLOW + "Report filed.");

        return true;
    }
}
