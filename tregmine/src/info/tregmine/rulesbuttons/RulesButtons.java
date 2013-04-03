package info.tregmine.rulesbuttons;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class RulesButtons implements Listener {

	private final Tregmine plugin;

	public RulesButtons(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	private void rulesButton(int hash, Block block, TregminePlayer player, Location location){
		if(hash == info.tregmine.api.math.Checksum.block(block)){
			if(location.getWorld().isChunkLoaded(location.getWorld().getChunkAt(location))){
				player.teleport(location);
				player.sendMessage(ChatColor.YELLOW + "Please answer all of the questions truthfully.");
			}else{
				player.sendMessage(ChatColor.RED + "Please press the button again.");
			}
		}
	}

	@EventHandler
	public void buttons(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			TregminePlayer player = this.plugin.getPlayer(event.getPlayer());
			int hash = info.tregmine.api.math.Checksum.block(block);
			Location testing = new Location(this.plugin.getServer().getWorld("world"), 518438, 35, -19339);	
			if(Material.STONE_BUTTON.equals(block.getType()) && 
					(player.getChatName().contains(ChatColor.DARK_PURPLE.toString()) || 
							player.isAdmin() || (!player.isTrusted() && 
									!player.getChatName().contains(ChatColor.GRAY.toString())))){	
				// Only admins, and coders can go here. (Coders to test the buttons)
				rulesButton(1877332415, block, player, testing);
				// Button in /warp rules
				rulesButton(-924947481, block, player, testing);
				rulesButton(85949858, block, player, testing);
				rulesButton(-1429498554, block, player, testing);
				rulesButton(-971352064, block, player, testing);
				rulesButton(1849106396, block, player, testing);
				rulesButton(-2127468414, block, player, testing);
				rulesButton(-247322806, block, player, testing);
				rulesButton(-1305526942, block, player, testing);
				rulesButton(-2067561090, block, player, testing);
				rulesButton(1202164071, block, player, testing);
				rulesButton(-902824856, block, player, testing);
				rulesButton(-934884643, block, player, testing);
				rulesButton(-172850495, block, player, testing);
				rulesButton(2076302251, block, player, testing);
				this.plugin.log.info(player.getName() + " :TestingButton");
				if(hash == 897221221){
					if(!player.isTrusted() && !player.getChatName().contains(ChatColor.GRAY.toString())){ 
						// This prevents the hardwarned from becoming "un-wanred"
						player.setMetaString("color", "trial");
						player.setMetaString("trusted", "true");
						player.setTemporaryChatName(player.getNameColor() + player.getName());
						player.sendMessage(ChatColor.GREEN + "Welcome! You are now a settler!");
						this.plugin.getServer().broadcastMessage(ChatColor.GREEN + player.getName() + " was made a settler!");
						this.plugin.log.info(player.getName() + " Was made a settler via a rules button.");
					}else{
						player.sendMessage(ChatColor.RED + "You dont have permission to press this button.");
					}
				}
				if(hash == -440366243){
					if(!player.isTrusted() && !player.getChatName().contains(ChatColor.GRAY.toString())){ 
						// This prevents the hardwarned from becoming "un-wanred"
						player.setMetaString("color", "child");
						player.setTemporaryChatName(player.getNameColor() + player.getName());
						player.sendMessage(ChatColor.AQUA + "You were made a child.");
						this.plugin.getServer().broadcastMessage(ChatColor.AQUA + player.getName() + " was made a child.");
						this.plugin.log.info(player.getName() + " Was made a child via a rules button.");
					}else{
						player.sendMessage(ChatColor.RED + "You dont have permission to press this button.");
					}
				}
			}else{
				player.sendMessage(ChatColor.RED + "You have already taken the test!");
			}
		}
	}
}	