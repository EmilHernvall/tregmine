package info.tregmine.commands;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import info.tregmine.Tregmine;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IWarpDAO;
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

        try (IContext ctx = tregmine.createContext()) {
            IWarpDAO warpDAO = ctx.getWarpDAO();

            Warp foundWarp = warpDAO.getWarp(args[0], tregmine.getServer());
            if (foundWarp != null) {
                player.sendMessage(ChatColor.RED + "Warp already exists!");
                return true;
            }

            Location loc = player.getLocation();
            warpDAO.insertWarp(name, loc);

            player.sendMessage(ChatColor.GREEN + "Warp " + args[0] + " created");
            LOGGER.info("WARPCREATE: " + args[0] + " by " + player.getName());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
