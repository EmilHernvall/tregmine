package info.tregmine.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import info.tregmine.hub.Hub;

public class SpawnCommand implements CommandExecutor {
	Hub plugin;
	public SpawnCommand(Hub plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if(!(player.isOp())){
					player.sendMessage(ChatColor.RED + "Sorry, You have insufficient permissions to execute this command.");
					return true;
				}
				World world = player.getWorld();
				Location loc = player.getLocation();
				world.setSpawnLocation(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
				player.sendMessage(ChatColor.GREEN + "Spawn Set!");
			} else {
				plugin.getLogger().info("You can't use this command from the console!");
			}
		} else if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				Location spawn = p.getWorld().getSpawnLocation().add(0.5,0,0.5);
				p.teleport(spawn);
			} else {
				plugin.getLogger().info("You can't use this command from the console!");
			}
		}
		return true;
	}
}