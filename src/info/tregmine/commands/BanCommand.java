package info.tregmine.commands;

import java.util.List;
import java.util.Date;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.PlayerReport;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerReportDAO;

public class BanCommand extends AbstractCommand
{
	private Tregmine plugin;
    public BanCommand(Tregmine tregmine)
    {
        super(tregmine, "ban");
        plugin = tregmine;
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

        victim.kickPlayer(plugin, "Banned by " + player.getName() + ": " + message);

        try (IContext ctx = tregmine.createContext()) {
            PlayerReport report = new PlayerReport();
            report.setSubjectId(victim.getId());
            report.setIssuerId(player.getId());
            report.setAction(PlayerReport.Action.BAN);
            report.setMessage(message);
            // three days default
            report.setValidUntil(new Date(
                    System.currentTimeMillis() + 3 * 86400 * 1000l));

            IPlayerReportDAO reportDAO = ctx.getPlayerReportDAO();
            reportDAO.insertReport(report);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        Server server = tregmine.getServer();
        server.broadcastMessage(victim.getChatName() + RED + " was banned by "
                + player.getChatName() + ".");

        LOGGER.info(victim.getName() + " Banned by " + player.getName());

        return true;
    }
}
