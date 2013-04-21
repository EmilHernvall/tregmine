package info.tregmine.donatesigns;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.currency.Wallet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DonateSigns implements Listener {

	private final Tregmine plugin;
	
	public DonateSigns(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void buttons(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			Block signBlock = event.getPlayer().getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ());
			Sign sign = (Sign) signBlock.getState();
			String donate = sign.getLine(0);
			int amount = Integer.parseInt(sign.getLine(1));
			String player = sign.getLine(3);
			TregminePlayer tregminePlayer = plugin.getPlayer(event.getPlayer());
			Wallet wallet = new Wallet(tregminePlayer.getPlayer().getName());
			Wallet targetWallet = new Wallet(player);
			if(Material.STONE_BUTTON.equals(block.getType()) || Material.WOOD_BUTTON.equals(block.getType())){
				if(signBlock.getState() instanceof Sign){
					if(donate.equals(sign.getLine(0))){
						if(wallet.take(amount)){
							targetWallet.add(amount);
							tregminePlayer.sendMessage(ChatColor.GOLD + "You donated " + ChatColor.AQUA + amount + ChatColor.GOLD + " Tregs to " + player);
							try{
								Player targetPlayer = Bukkit.getPlayer(player);
								targetPlayer.sendMessage(tregminePlayer.getDisplayName() + ChatColor.GOLD + " donated " + ChatColor.AQUA + amount + ChatColor.GOLD + " Tregs to you");
							}catch(Exception error){ // In case the target player isn't online.
								error.printStackTrace();
							}
							plugin.log.info(tregminePlayer.getName() + ": DonateSign");
						}else{
							tregminePlayer.sendMessage(ChatColor.RED + "You need at least" + amount + " Tregs");
						}
					}
				}
			}
		}
	}
}