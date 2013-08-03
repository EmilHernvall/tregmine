package info.tregmine.commands;

import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerDAO;

public class PasswordCommand extends AbstractCommand
{
    public PasswordCommand(Tregmine tregmine)
    {
        super(tregmine, "password");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }

        String password = args[0];

        if (password.length() < 1) {
            player.sendMessage(RED + "Your password must be at least "
                    + "6 characters long.");
            return true;
        }

        player.setPassword(password);
        player.sendMessage(YELLOW + "Your password has been changed.");

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayer(player);
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
