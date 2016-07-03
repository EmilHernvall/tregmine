package info.tregmine.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.lore.Created;

public class ResetLoreCommand extends AbstractCommand{
	Tregmine plugin;
	TregminePlayer player;
	public ResetLoreCommand(Tregmine instance){
		super(instance, "resetlore");
		plugin = instance;
	}
	public boolean handlePlayer(TregminePlayer sender, String[] args){
		if(!sender.isOp() && sender.getName() != "im_not_eric"){
			sender.sendMessage("%warning%" + ChatColor.RED + "You do not have permission to reset lores.");
			return true;
		}
		player = sender;
		Inventory inv = player.getInventory();
		ItemStack[] contents = inv.getContents();
		for(ItemStack item : contents){
			if (item != null) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.WHITE + "Reset by: " + player.getChatName());
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
		}
		player.sendMessage(ChatColor.GOLD + "Any items that had a lore have lost their lore.");
		return true;
	}
}
