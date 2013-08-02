package info.tregmine.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.World;

import com.maxmind.geoip.Location;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerDAO;

public class QuitMessageCommand extends AbstractCommand
{
    public QuitMessageCommand(Tregmine tregmine)
    {
        super(tregmine, "quitmessage");
    }

    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(args[0]);
        for (int i = 1; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length == 0) {
            return getOwnMsg(player);
        }

        if (args[0].matches("player") && args[1].matches("get")) {
            return getElseMsg(player, args);
        } else if (args[0].matches("player") && args[1].matches("set")) {
            return setElseMsg(player, args);
        } else if (args[0].matches("help")) {
            return help(player, args);
        } else {
            return setOwnMsg(player, args);
        }
    }

    private boolean help(TregminePlayer player, String[] args)
    {
        if (player.getRank().canSetOthersQuitMessage()) {
            player.sendMessage(DARK_GRAY + "-----------------------------------------");
            player.sendMessage(GRAY + "Get your Quit Message: " + GREEN + "/quitmessage");
            player.sendMessage(GRAY + "Set your Quit Message: " + GREEN + "/quitmessage <message>");
            player.sendMessage(GRAY + "Get another player's Quit Message: ");
            player.sendMessage(GREEN + "/quitmessage player get <player>");
            player.sendMessage(GRAY + "Set another player's Quit Message: ");
            player.sendMessage(GREEN + "/quitmessage player set <player> <message>");
            player.sendMessage(DARK_GRAY + "-----------------------------------------");
        }
        else if (player.getRank().canSetQuitMessage()) {
            player.sendMessage(DARK_GRAY + "-----------------------------------------");
            player.sendMessage(GRAY + "Get your Quit Message: " + GREEN + "/quitmessage");
            player.sendMessage(GRAY + "Set your Quit Message: " + GREEN + "/quitmessage <message>");
            player.sendMessage(DARK_GRAY + "-----------------------------------------");
        }
        else {
            player.sendMessage(DARK_GRAY + "-----------------------------------------");
            player.sendMessage(RED + "Sorry, Quit Messages are only for players who");
            player.sendMessage(RED + "donate to keep the server running.");
            player.sendMessage(DARK_GRAY + "-----------------------------------------");
        }
        return true;
    }

    private boolean setOwnMsg(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canSetQuitMessage()) {
            return true;
        }

        String message = null;

        message = argsToMessage(args);

        player.setQuitMessage(message);

        player.sendMessage(YELLOW + "Your quit message has been set to:");
        player.sendMessage(YELLOW + message);

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayerInfo(player);
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

    private boolean setElseMsg(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canSetOthersQuitMessage()) {
            return true;
        }

        if (args.length == 3 ) {
            player.sendMessage(RED + "Correct Usage: /quitmessage player set <player> <message>");
            return true;
        }

        String pattern = args[2];

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        if (victim == null) {
            return true;
        }

        if (victim.isOp()) {
            player.sendMessage(RED + "Thou shall not mess with the Gods!");

            World world = player.getWorld();
            org.bukkit.Location location = player.getLocation();
            world.strikeLightning(location);
            return true;
        }

        StringBuilder newQuitMessage = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            newQuitMessage.append(args[i] + " ");
        }
        String quitmsgString = newQuitMessage.toString();
        quitmsgString.trim();

        victim.setQuitMessage(quitmsgString);

        player.sendMessage(victim.getChatName() + "'s" + YELLOW
                + " quit message has been set to:");
        player.sendMessage(YELLOW + quitmsgString);

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayerInfo(player);
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

    private boolean getElseMsg(TregminePlayer player, String[] args)
    {
        if (args.length > 3 ) {
            player.sendMessage(RED + "Correct Usage: /quitmessage player get <player>");
            return true;
        }

        String pattern = args[2];

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        if (victim == null) {
            return true;
        }
        String victimName = victim.getChatName();

        player.sendMessage(victimName + "'s " + YELLOW
                + "current Quit Message is:");
        player.sendMessage(YELLOW + victim.getQuitMessage());
        return true;
    }

    private boolean getOwnMsg(TregminePlayer player)
    {
        if (!player.getRank().canSetQuitMessage()) {
            return true;
        }

        player.sendMessage(YELLOW + "Your current Quit Message is:");
        player.sendMessage(YELLOW + player.getQuitMessage());
        return true;
    }
}
