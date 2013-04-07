package info.tregmine.buttons.forabuttons;

import java.text.NumberFormat;
import java.util.Random;

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

	Random random = new Random();

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
					// Bitchslap button.
					if(wallet.take(15000)){
						plugin.getServer().broadcastMessage(ChatColor.YELLOW + "You got a bitchslap from " + player.getName() + "!");
						player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
						this.plugin.log.info(player.getName() + ": BitchslapButton");
					}else{
						player.sendMessage(ChatColor.RED + "You need at least 15,000 Tregs for this button!");
					}
				}
				if(hash == -432802918){
					// Hug button.
					if(player.getName().equals("Camrenn")){
						plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "You got a hugged by princess Camrenn! Be happy, but not too happy.");
					}else{
						if(wallet.take(15000)){
							plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "You got a hug from " + player.getName() + "!");
							player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
							this.plugin.log.info(player.getName() + ": HugButton");
						}else{
							player.sendMessage(ChatColor.RED + "You need at least 15,000 Tregs for this button!");
						}
					}
				}
				if(hash == -1484999952){
					// Lottery Button!!!!
					int amount = this.plugin.lottery.size() * 5000;
					if(wallet.take(5000)){
						if(!this.plugin.lottery.contains(player.getName())){
							plugin.lottery.add(player.getName());
							player.sendMessage(ChatColor.YELLOW + "You have been added to the lottery! 5,000 Tregs were taken from your wallet.");
							player.sendMessage(ChatColor.YELLOW + "Amount of money in lottery: " + ChatColor.DARK_AQUA + amount + " Tregs.");
						}else{
							player.sendMessage(ChatColor.RED + "You have already joined. Amount in lottery: " + amount + " Tregs.");
						}
					}else{
						player.sendMessage(ChatColor.RED + "This button costs 5,000 Tregs!");
					}
					this.plugin.log.info(player.getName() + ": LotteryButton");
				}
				if(hash == -1352574340){
					String lottery = this.plugin.lottery.get(random.nextInt(this.plugin.lottery.size()));
					int amount = this.plugin.lottery.size() * 5000;
					NumberFormat format = NumberFormat.getNumberInstance();
					Wallet lotteryWallet = new Wallet(lottery);
					if(player.isAdmin() || player.getChatName().contains(ChatColor.DARK_PURPLE.toString())){	
						try{
							lotteryWallet.add(amount);
							plugin.getPlayer(lottery).sendMessage(ChatColor.DARK_AQUA + "You won the lottery! You have been given " + format.format(amount) + " Tregs!");
							player.sendMessage(ChatColor.DARK_AQUA + "The winner is:" + lottery);
							this.plugin.lottery.clear();
							player.sendMessage(ChatColor.GREEN + "The lottery has been cleared.");
							plugin.getServer().broadcastMessage(ChatColor.AQUA + "The winner of the lottery is: " + plugin.getPlayer(lottery).getChatName());
						}catch(Exception error){
							player.sendMessage(ChatColor.RED + "There has been an error. The winner may now be online.");
						}
					}else{
						player.sendMessage(ChatColor.RED + "You must be admin to press this!");
					}
				}
				if(hash == -2074540467){
					// Heaven and Hell button.
					if(player.getWorld().getBlockAt(614, 33, -102).getType().equals(Material.WATER)){

						player.getWorld().getBlockAt(614, 33, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 33, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 33, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 33, -102).setType(Material.AIR);

						player.getWorld().getBlockAt(614, 33, -105).setType(Material.LAVA);
						player.getWorld().getBlockAt(614, 33, -104).setType(Material.LAVA);
						player.getWorld().getBlockAt(614, 33, -103).setType(Material.LAVA);
						player.getWorld().getBlockAt(614, 33, -102).setType(Material.LAVA);

						player.getWorld().getBlockAt(614, 32, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 31, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 30, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 29, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 28, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 27, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 26, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 25, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 24, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 23, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 22, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 21, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 20, -105).setType(Material.AIR);

						player.getWorld().getBlockAt(614, 32, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 31, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 30, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 29, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 28, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 27, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 26, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 25, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 24, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 23, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 22, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 21, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 20, -104).setType(Material.AIR);

						player.getWorld().getBlockAt(614, 32, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 31, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 30, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 29, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 28, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 27, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 26, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 25, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 24, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 23, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 22, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 21, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 20, -103).setType(Material.AIR);

						player.getWorld().getBlockAt(614, 32, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 31, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 30, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 29, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 28, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 27, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 26, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 25, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 24, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 23, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 22, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 21, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 20, -102).setType(Material.AIR);

						this.plugin.log.info(player.getName() + ": HeavenHellButton");
					}

					if(player.getWorld().getBlockAt(614, 33, -102).getType().equals(Material.LAVA)){

						player.getWorld().getBlockAt(614, 33, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 33, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 33, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 33, -102).setType(Material.AIR);

						player.getWorld().getBlockAt(614, 33, -105).setType(Material.WATER);
						player.getWorld().getBlockAt(614, 33, -104).setType(Material.WATER);
						player.getWorld().getBlockAt(614, 33, -103).setType(Material.WATER);
						player.getWorld().getBlockAt(614, 33, -102).setType(Material.WATER);

						player.getWorld().getBlockAt(614, 32, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 31, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 30, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 29, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 28, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 27, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 26, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 25, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 24, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 23, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 22, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 21, -105).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 20, -105).setType(Material.AIR);

						player.getWorld().getBlockAt(614, 32, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 31, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 30, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 29, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 28, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 27, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 26, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 25, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 24, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 23, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 22, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 21, -104).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 20, -104).setType(Material.AIR);

						player.getWorld().getBlockAt(614, 32, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 31, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 30, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 29, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 28, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 27, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 26, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 25, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 24, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 23, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 22, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 21, -103).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 20, -103).setType(Material.AIR);

						player.getWorld().getBlockAt(614, 32, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 31, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 30, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 29, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 28, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 27, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 26, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 25, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 24, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 23, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 22, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 21, -102).setType(Material.AIR);
						player.getWorld().getBlockAt(614, 20, -102).setType(Material.AIR);

						this.plugin.log.info(player.getName() + ": HeavenHellButton");
					}
				}
				if(hash == 1799070005){
					// Admin/Guardian button.
					if(player.isAdmin() || player.isGuardian() || player.getChatName().contains(ChatColor.DARK_PURPLE.toString())){
						if(player.getName().equals("notaro1997")){
							plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "You just got yo' ass wooped by notaro1997!");
						}
						if(player.getName().equals("sumerian45")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("eukey1337")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("Will_owns")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("stevoe")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("EpicOstrich")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("JAYT8")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("LilKiw")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("sweetheart3445")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("Camrenn")){
							plugin.getServer().broadcastMessage(ChatColor.RED + "You just got run over by Cammy's pony");	
						}
						if(player.getName().equals("frazerp123")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("mguerra12")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("chrille0100")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("OrangeSlurpie")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("mejjad")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("einand")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("rweiand")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("BlackX")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("GeorgeBombadil")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("Mksen")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("NoraDraft")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("Tix92")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("lachieb")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("alphacarter")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("cko_germany")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("Jaeee")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("The_Odd_Mexican")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("mrturnip45")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("Zwips")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("toddtedd")){
							plugin.getServer().broadcastMessage(ChatColor.BLUE + "Your friend just got assassinated by toddtedd");
						}
						if(player.getName().equals("Annaschwander")){
							plugin.getServer().broadcastMessage(ChatColor.BLUE + "You just got Baby SHEEEEEEP'd by Annaschwander");
						}
						if(player.getName().equals("TSubstance")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("RudyCortez2")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("Seelenlos")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("Silver_Shadow166")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("sucage")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("HiTiHiTi")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
						if(player.getName().equals("baku1999")){
							plugin.getServer().broadcastMessage(ChatColor.BLUE + "Your ass just got dropped by baku1999");
						}
						if(player.getName().equals("klonrocks")){
							player.sendMessage(ChatColor.DARK_AQUA + player.getName() + ", please message notaro1997 what you want your message to be.");
						}
					}else{
						player.sendMessage(ChatColor.RED + "You dont have permission for this button!");
					}
				}	
			}
		}
	}
}