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

public class WarnCommand extends AbstractCommand
{
    public WarnCommand(Tregmine tregmine)
    {
        super(tregmine, "warn");
    }

    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 2; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canWarn()) {
            return true;
        }

        if (args.length < 3) {
            player.sendMessage(DARK_AQUA
                    + "/warn <hard|soft> <player> <message>");
            return true;
        }

        Server server = player.getServer();
        String type = args[0].toLowerCase();
        String pattern = args[1];
        String message = argsToMessage(args);

        boolean hard = false;
        if ("soft".equals(type)) {
        }
        else if ("hard".equals(type)) {
            hard = true;
        }
        else {
            player.sendMessage(DARK_AQUA
                    + "/warn <hard|soft> <player> <message>");
            return true;
        }

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            // TODO: error message
            return true;
        }

        TregminePlayer victim = candidates.get(0);
        if (hard) {
            player.sendMessage(GREEN + "You hardwarned " + victim.getChatName() + GREEN + ": "
                    + message);
            LOGGER.info(victim.getName() + " hardwarned by " + player.getName());
        }
        else {
            player.sendMessage(GREEN + "You warned "
                    + victim.getChatName() + GREEN + ": " + message);
            LOGGER.info(victim.getName() + " warned by " + player.getName());
        }

        victim.setTemporaryChatName(victim.getNameColor() + victim.getName());
        if (hard) {
            victim.setFlag(TregminePlayer.Flags.HARDWARNED);
        } else {
            victim.setFlag(TregminePlayer.Flags.SOFTWARNED);
        }

        try (IContext ctx = tregmine.createContext()) {
            PlayerReport report = new PlayerReport();
            report.setSubjectId(victim.getId());
            report.setIssuerId(player.getId());
            report.setAction(hard ? PlayerReport.Action.HARDWARN
                    : PlayerReport.Action.SOFTWARN);
            report.setMessage(message);
            // three days default
            report.setValidUntil(new Date(
                    System.currentTimeMillis() + 3 * 86400 * 1000l));

            IPlayerReportDAO reportDAO = ctx.getPlayerReportDAO();
            reportDAO.insertReport(report);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
