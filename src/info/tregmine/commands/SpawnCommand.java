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
        Horse horse = null;

        if((player.getVehicle() != null) && (player.getVehicle() instanceof Horse)){
            horse = (Horse)player.getVehicle();
        }

        if(horse != null){
            horse.eject();
            horse.teleport(player.getWorld().getSpawnLocation());
            player.teleport(player.getWorld().getSpawnLocation());
            horse.setPassenger(player.getDelegate());
        }else{
            player.teleport(player.getWorld().getSpawnLocation());
        }
        return true;
    }
}
