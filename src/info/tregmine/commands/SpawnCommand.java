package info.tregmine.commands;

import org.bukkit.entity.Horse;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class SpawnCommand extends AbstractCommand
{
    public SpawnCommand(Tregmine tregmine)
    {
        super(tregmine, "spawn");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        player.teleportWithHorse(player.getWorld().getSpawnLocation());
        return true;
    }
}
