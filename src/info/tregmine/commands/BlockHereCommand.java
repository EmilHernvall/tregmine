package info.tregmine.commands;

import org.bukkit.Util;
import org.bukkit.block.Block;
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

        Block block = player.getWorld().getBlockAt(player.getLocation().subtract(new Vector(0, 1, 0)));
        block.setType(Material.DIRT);

        return true;
    }
}
