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
					if(wallet.take(15000)){
						plugin.getServer().broadcastMessage(ChatColor.YELLOW + "You got a bitchslap from " + player.getName() + "!");
						player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
						this.plugin.log.info(player.getName() + ": BitchslapButton");
					}else{
						player.sendMessage(ChatColor.RED + "You need at least 15,000 Tregs for thit button!");
					}
				}
				if(hash == -432802918){
					if(wallet.take(15000)){
						plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "You got a hug from " + player.getName() + "!");
						player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
						this.plugin.log.info(player.getName() + ": HugButton");
					}else{
						player.sendMessage(ChatColor.RED + "You need at least 15,000 Tregs for thit button!");
					}
				}
				if(hash == -1484999952){
					player.sendMessage(ChatColor.DARK_AQUA + "Have an idea for this button? Message notaro1997 when he is online.");
				}
				if(hash == -2074540467){
					if(player.getWorld().getBlockAt(614, 33, -105).getType().equals(Material.WATER)){
						player.getWorld().getBlockAt(614, 33, -105).setType(Material.LAVA);
						player.getWorld().getBlockAt(614, 33, -104).setType(Material.LAVA);
						player.getWorld().getBlockAt(614, 33, -103).setType(Material.LAVA);
						player.getWorld().getBlockAt(614, 33, -102).setType(Material.LAVA);
					}else{
						player.getWorld().getBlockAt(614, 33, -105).setType(Material.WATER);
						player.getWorld().getBlockAt(614, 33, -104).setType(Material.WATER);
						player.getWorld().getBlockAt(614, 33, -103).setType(Material.WATER);
						player.getWorld().getBlockAt(614, 33, -102).setType(Material.WATER);
					}
				}
				if(hash == 1799070005){
					if(player.isAdmin() || player.isGuardian() || player.getChatName().contains(ChatColor.DARK_PURPLE.toString())){
						if(player.getName().equals("notaro1997")){
							plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "You just got your ass wooped by notaro1997!");
						}else if(player.getName().equals("sumerian45")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("eukey1337")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Will_owns")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("stevoe")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("EpicOstrich")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("JAYT8")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("LilKiw")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("sweetheart3445")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Camrenn")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("frazerp123")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("mguerra12")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("chrille0100")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("OrangeSlurpie")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("mejjad")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("einand")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("rweiand")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("BlackX")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("GeorgeBombadil")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Mksen")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("NoraDraft")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Tix92")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("lachieb")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("alphacarter")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("cko_germany")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Jaeee")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("The_Odd_Mexican")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("mrturnip45")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Zwips")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("toddtedd")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Annaschwander")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("TSubstance")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("RudyCortez2")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Seelenlos")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("Silver_Shadow166")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("sucage")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("HiTiHiTi")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("baku1999")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}else if(player.getName().equals("klonrocks")){
							player.sendMessage(ChatColor.DARK_AQUA + "Please message notaro1997 what you want your message to be, " + player.getName());
						}
					}
				}	
			}
		}
	}
}