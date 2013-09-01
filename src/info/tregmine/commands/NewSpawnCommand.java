package info.tregmine.commands;

import org.bukkit.World;
import org.bukkit.Location;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class NewSpawnCommand extends AbstractCommand
{
    public NewSpawnCommand(Tregmine tregmine)
    {
        super(tregmine, "newspawn");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.isOp()) {
            return false;
        }

        World world = player.getWorld();
        if (world == null) {
            return false;
        }

        Location loc = player.getLocation();
        tregmine.getConfig().set("spawn.location.pitch", loc.getPitch());
        tregmine.getConfig().set("spawn.location.yaw", loc.getYaw());
        world.setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        return true;
    }
}
