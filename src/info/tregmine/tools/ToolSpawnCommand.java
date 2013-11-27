package info.tregmine.tools;

import java.util.HashMap;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.commands.AbstractCommand;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class ToolSpawnCommand extends AbstractCommand
{
    public ToolSpawnCommand(Tregmine tregmine)
    {
        super(tregmine, "tool");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String args[])
    {
        if (!player.getRank().canSpawnTools()) return true;
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /tool <lumber/vein/aoe>");
            return true;
        }
        
        ItemStack tool = null;
        
        switch (args[0]) {
            case "lumber":
                tool = ToolsRegistry.LumberAxe();
                break;
            case "vein":
                tool = ToolsRegistry.VeinMiner();
                break;
            case "aoe":
                tool = ToolsRegistry.AreaOfEffect();
                break;
        }
        
        if (tool == null) {
            player.sendMessage(ChatColor.RED + "Usage: /tool <lumber/vein/aoe>");
            return true;
        }
        
        HashMap<Integer, ItemStack> failedItems = player.getInventory().addItem(tool);
        
        if (failedItems.size() > 0) {
            player.sendMessage(ChatColor.RED + "You have a full inventory, Can't add tool!");
            return true;
        }
        
        player.sendMessage(ChatColor.GREEN + "Spawned in tool token successfully!");
        return true;
    }
}
