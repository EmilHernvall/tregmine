package info.tregmine.commands;

import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class BlockHereCommand extends AbstractCommand
{
    public BlockHereCommand(Tregmine tregmine)
    {
        super(tregmine, "blockhere");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canFill()) {
            return true;
        }

        Location loc = player.getLocation();
        Block block = player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        block.setType(Material.DIRT);

        return true;
    }
}
