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
        if (!player.getRank().canSpawnTools()){ 
        	player.nopermsMessage(false, "tool");
        	return true;}
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /tool <lumber/vein/gravity>");
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
            case "gravity":
                tool = ToolsRegistry.GravityGun();
                break;
        }
        
        if (tool == null) {
            player.sendMessage(ChatColor.RED + "Usage: /tool <lumber/vein/gravity>");
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
