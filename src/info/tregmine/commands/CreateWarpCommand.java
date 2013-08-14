package info.tregmine.commands;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import info.tregmine.Tregmine;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBWarpDAO;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Warp;

public class CreateWarpCommand extends AbstractCommand
{
    public CreateWarpCommand(Tregmine tregmine)
    {
        super(tregmine, "createwarp");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }
        if (!player.getRank().canCreateWarps()) {
            return true;
        }

        String name = args[0];

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBWarpDAO warpDAO = new DBWarpDAO(conn);

            Warp foundWarp = warpDAO.getWarp(args[0], tregmine.getServer());
            if (foundWarp != null) {
                player.sendMessage(ChatColor.RED + "Warp already exists!");
                return true;
            }

            Location loc = player.getLocation();
            warpDAO.insertWarp(name, loc);

            player.sendMessage(ChatColor.GREEN + "Warp " + args[0] + " created");
            LOGGER.info("WARPCREATE: " + args[0] + " by " + player.getName());
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
