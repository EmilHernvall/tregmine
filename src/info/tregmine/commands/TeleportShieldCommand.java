package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class TeleportShieldCommand extends AbstractCommand
{
    public TeleportShieldCommand(Tregmine tregmine)
    {
        super(tregmine, "tpshield");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.isDonator()) {
            return false;
        }

        if (args.length < 1) {
            if (player.getMetaBoolean("tpblock")) {
                player.setMetaString("tpblock", "false");
                player.sendMessage(AQUA + "Teleportation is now allowed to you.");
            } else {
                player.setMetaString("tpblock", "true");
                player.sendMessage(AQUA +"Teleportation is now blocked to you.");
            }
            return true;
        }

        String state = args[0];

        if ("on".equalsIgnoreCase(state)) {
            player.setMetaString("tpblock", "true");
            player.sendMessage(AQUA +"Teleportation is now blocked to you.");
            return true;
        }
        else if ("off".equalsIgnoreCase(state)) {
            player.setMetaString("tpblock", "false");
            player.sendMessage(AQUA +"Teleportation is now allowed to you.");
            return true;
        }
        else if ("status".equalsIgnoreCase(state)) {
            player.sendMessage("Your tpblock is set to " + player.getMetaString("tpblock") + ".");
            return true;
        }

        player.sendMessage(RED + "The commands are /tpblock on, /tpblock off and /tpblock status.");

        return true;
    }
}
