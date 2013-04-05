package info.tregmine.buttons.forabuttons;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.currency.Wallet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ForaChatButtons implements Listener{


	private final Tregmine plugin;

	public ForaChatButtons(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void foraButtons(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			TregminePlayer player = this.plugin.getPlayer(event.getPlayer());
			int hash = info.tregmine.api.math.Checksum.block(block);
			Wallet wallet = new Wallet(player.getName());
			if(Material.STONE_BUTTON.equals(block.getType())){
				if(hash == 156737597){
					if("notaro1997".equals(player.getName())){
						plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "You just got yo ass wooped by notaro1997!");
						// Is this okay to have Ein? ;)
					}else{
						if(wallet.take(15000)){
							plugin.getServer().broadcastMessage(ChatColor.YELLOW + "You got a bitchslap from " + player.getName() + "!");
							player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
							this.plugin.log.info(player.getName() + " :BitchslapButton");
						}else{
							player.sendMessage(ChatColor.RED + "You need at least 15,000 Tregs!");
						}
					}
				}
			}	
		} // Other buttons and plates, and whatnot will be added shortly.
	}
}