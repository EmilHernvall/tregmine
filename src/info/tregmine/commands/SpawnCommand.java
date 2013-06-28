package info.tregmine.commands;

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
        player.teleport(player.getWorld().getSpawnLocation());
        return true;
    }
}
