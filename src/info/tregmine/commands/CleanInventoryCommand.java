package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class CleanInventoryCommand extends AbstractCommand
{
    public CleanInventoryCommand(Tregmine tregmine)
    {
        super(tregmine, "clean");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        player.getInventory().clear();
        return true;
    }
}
