package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
        if (!player.isAdmin() && !player.isBuilder()) {
            return false;
        }

        Block block = player.getWorld().getBlockAt(player.getLocation());
        block.setType(Material.DIRT);

        return true;
    }
}
