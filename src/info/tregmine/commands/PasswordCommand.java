package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

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
            player.sendMessage(RED + "Your password must be at least " +
                               "6 characters long.");
            return true;
        }

        player.setPassword(password);
        player.sendMessage(YELLOW + "Your password has been changed.");

        return true;
    }
}

