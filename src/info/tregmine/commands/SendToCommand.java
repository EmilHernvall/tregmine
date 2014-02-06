package info.tregmine.commands;

import java.util.List;

import org.bukkit.*;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class SendToCommand extends AbstractCommand
{
    public SendToCommand(Tregmine tregmine)
    {
        super(tregmine, "sendto");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canSendPeopleToOtherWorlds()) {
            return true;
        }

        if (args.length != 2) {
            return false;
        }

        List<TregminePlayer> candidates = tregmine.matchPlayer(args[0]);
        if (candidates.size() != 1) {
            // TODO: List users
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        Server server = tregmine.getServer();
        World world = server.getWorld(args[1]);
        if (world == null) {
            // TODO: error message
            return false;
        }

        Location cpspawn = world.getSpawnLocation();
        victim.teleportWithHorse(cpspawn);

        return true;
    }
}
