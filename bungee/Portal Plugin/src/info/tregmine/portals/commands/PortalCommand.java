package info.tregmine.portals.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import info.tregmine.portals.Capitalize;
import info.tregmine.portals.Portals;

public class PortalCommand implements CommandExecutor {
	Portals plugin;

	public PortalCommand(Portals plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("portal")) {

			if (sender instanceof Player) {
				final Player p = (Player) sender;

				if(!p.isOp()){
					p.sendMessage("Sorry, you have insufficient permissions to execute this command!");
					return true;
				}
				
				if(args.length != 2){
					p.sendMessage(ChatColor.RED + "Correct Usage: /portal add|remove|trigger <world>");
					return true;
				}
				if (args[0].equalsIgnoreCase("add")){

					if (p.getItemInHand() == null) {
						p.sendMessage(ChatColor.RED
								+ "You must be holding a block in you hand!");
						return true;
					}

					if (p.getItemInHand().getType() == Material.AIR) {
						p.sendMessage(ChatColor.RED
								+ "You must be holding a block in you hand!");
						return true;
					}

					final ItemStack m = p.getItemInHand();

					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("GetServers");
					p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

					@SuppressWarnings("unused")
					int task = 0;
					task = Bukkit.getServer().getScheduler()
							.scheduleSyncDelayedTask(plugin, new Runnable() {

								@Override
								public void run() {
									if (Arrays.asList(Portals.serverList).contains(
											args[1])) {
										plugin.getConfig().set(
												"portals." + m.getType() + ":" + m.getDurability() + ".server", args[1].toLowerCase());
										plugin.getConfig().set(
												"servers." + args[1].toLowerCase() + ".trigger", m.getType() + ":" + m.getDurability());
										plugin.saveConfig();

										p.sendMessage(
												ChatColor.AQUA + "Portal Added!"
														+ ChatColor.GREEN + " Trigger: "
														+ ChatColor.GOLD + m.getType() + ":" + m.getDurability()
														+ ChatColor.GREEN + " Server: "
														+ ChatColor.GOLD + Capitalize.string(args[1].toLowerCase()));
									} else {
										p.sendMessage(
												ChatColor.RED + "Sorry, '"
														+ ChatColor.GOLD + args[1]
																+ ChatColor.RED + "' insn't a valid server name!");
										p.sendMessage(
												ChatColor.GREEN + "Here is a list of valid (case sensative) server names: ");
										p.sendMessage(
												ChatColor.GOLD + Portals.serverName);
									}
								}
							}, 10L);
				}else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem")){

					if (Arrays.asList(Portals.serverList).contains(
							args[1])) {

						String trigger = plugin.getConfig().getString("servers." + args[1].toLowerCase() + ".trigger");

						plugin.getConfig().set("portals." + trigger, null);
						plugin.getConfig().set("servers." + args[1].toLowerCase(), null);
						plugin.saveConfig();

						p.sendMessage(
								ChatColor.DARK_RED + "Portal Removed!"
										+ ChatColor.RED + " Trigger: "
										+ ChatColor.GOLD + trigger
										+ ChatColor.RED + " Server: "
										+ ChatColor.GOLD + Capitalize.string(args[1].toLowerCase()));
					} else {
						p.sendMessage(
								ChatColor.RED + "Sorry, '"
										+ ChatColor.GOLD + args[1]
												+ ChatColor.RED + "' insn't a valid server name!");
						p.sendMessage(
								ChatColor.GREEN + "Here is a list of valid (case sensative) server names: ");
						p.sendMessage(
								ChatColor.GOLD + Portals.serverName);
					}
				}else if (args[0].equalsIgnoreCase("trigger") || args[0].equalsIgnoreCase("info")){

					if (Arrays.asList(Portals.serverList).contains(
							args[1])) {

						String trigger = plugin.getConfig().getString("servers." + args[1].toLowerCase() + ".trigger");

						p.sendMessage(
								ChatColor.GOLD + "Portal Info:"
										+ ChatColor.AQUA + " Server: "
										+ ChatColor.GOLD + Capitalize.string(args[1].toLowerCase())
										+ ChatColor.AQUA + " Trigger: "
										+ ChatColor.GOLD + trigger);
					} else {
						p.sendMessage(
								ChatColor.RED + "Sorry, '"
										+ ChatColor.GOLD + args[1]
												+ ChatColor.RED + "' insn't a valid server name!");
						p.sendMessage(
								ChatColor.GREEN + "Here is a list of valid (case sensative) server names: ");
						p.sendMessage(
								ChatColor.GOLD + Portals.serverName);
					}
				}
			} else {
				plugin.getLogger().info("You can't use this command from the console!");
			}
		}
		return true;
	}
}