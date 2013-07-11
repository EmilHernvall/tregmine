package info.tregmine.commands;

import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerDAO;

public class KeywordCommand extends AbstractCommand
{
    public KeywordCommand(Tregmine tregmine)
    {
        super(tregmine, "keyword");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        String keyword = args[0];

        if (keyword.length() < 1) {
            player.sendMessage(RED + "Your keyword must be at least "
                    + "1 characters long.");
            return true;
        }

        player.setKeyword(keyword.toLowerCase());
        player.sendMessage(YELLOW
                + "From now on you can only log in by using ip "
                + keyword.toLowerCase() + ".mc.tregmine.info");

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayerKeyword(player);
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
