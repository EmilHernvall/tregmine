package info.tregmine.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import info.tregmine.hub.Hub;

public class HeadCommand implements CommandExecutor {
	Hub plugin;
	public HeadCommand(Hub plugin) {
		this.plugin = plugin;
	}

	//TODO Remove this file class before moving to GitHub.
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("head")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if(!(player.isOp())){
					player.sendMessage(ChatColor.RED + "Sorry, You have insufficient permissions to execute this command.");
					return true;
				}
				if (args.length == 1) {
					ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
					SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
					itemMeta.setOwner(args[0]);
					itemMeta.setDisplayName(ChatColor.YELLOW + args[0] + "'s head");
					item.setItemMeta(itemMeta);
					PlayerInventory inventory = player.getInventory();
					inventory.addItem(item);
					player.sendMessage(ChatColor.YELLOW + "You received the head of " + args[0]);
				} else {
					player.sendMessage(ChatColor.RED + "Type /head <player>");
				}

			} else {
				plugin.getLogger().info("You can't use this command from the console!");
			}
		}
		return true;
	}
}