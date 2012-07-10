package info.tregmine.currency;


import java.text.NumberFormat;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import info.tregmine.Tregmine; 
import info.tregmine.zones.ZonesPlugin;

public class Main extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public ZonesPlugin zones = null;
	NumberFormat nf = NumberFormat.getNumberInstance();

	@Override
	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}

		
		Plugin zonesTest = this.getServer().getPluginManager().getPlugin("ZonesPlugin");

		if(this.zones == null) {
			if(test != null) {
				this.zones = ((ZonesPlugin)zonesTest);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find zones");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
		
		
		
		getServer().getPluginManager().registerEvents(new CurrencyPlayer(this), this);
	}

	@Override
	public void onDisable(){
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();

		if(!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(player.getName());

		if (commandName.matches("wallet") && args.length > 0) {
			
			if (args[0].matches("balance")) {
				Wallet wallet = new Wallet(player);
				long balance = wallet.balance();
				if (balance >= 0) {
						player.sendMessage("You have " + ChatColor.GOLD +  nf.format( balance ) + ChatColor.WHITE + " Tregs" );
				} else {
					player.sendMessage(ChatColor.RED + "You have have no wallet. Contact an administrator." );					
				}
				return true;
			}

			if (args[0].matches("tell")) {
				Wallet wallet = new Wallet(player);
//				ChatColor color = this.tregmine.playerSetting.get(player.getName()).getColor();
				long balance = wallet.balance();
				if (balance >= 0) {
					if (!tregminePlayer.isAdmin()) {
						this.getServer().broadcastMessage(tregminePlayer.getChatName() + ChatColor.AQUA + " has " + ChatColor.GOLD + nf.format( balance ) + ChatColor.AQUA + " Tregs." );
					} else {
						player.sendMessage(tregminePlayer.getChatName() + " has " + ChatColor.GOLD +  "more" + ChatColor.WHITE + " Tregs than you." );						
					}

				} else {
					this.getServer().broadcastMessage(tregminePlayer.getChatName() + ChatColor.RED + " has no wallet. Contact an administrator.");
				}
				return true;
			}

			if (args[0].matches("donate") && args.length > 2) {
				int amount;
				Player to;
				
				try {
					amount = Integer.parseInt(args[2]);
				} catch (Exception  e) {
					amount = 0;
				}
				
				try {
					to = this.getServer().matchPlayer(args[1]).get(0);
				} catch (Exception  e) {
					to = null;
				}
				
				if (to != null && amount > 0) {
					if  (info.tregmine.api.math.Distance.calc2d(player.getLocation(), to.getLocation()) > 5) {
						if (player.canSee(to)) {
							player.sendMessage(ChatColor.RED + to.getName() + " is to far away for a wallet transaction, please move closer");
						}
						return true;
					}
					
					
					info.tregmine.api.TregminePlayer toPlayer = this.tregmine.tregminePlayer.get(to.getName());
					Wallet wallet = new Wallet(player);
					Wallet toWallet = new Wallet(to);
					
					long newAmount = wallet.balance() - amount;
					if (newAmount >= 0) {
						toWallet.add(amount);
						wallet.take(amount);
						player.sendMessage(ChatColor.AQUA + "You donated to " + toPlayer.getChatName() + " " + ChatColor.GOLD + this.nf.format( amount ) + ChatColor.AQUA +" Tregs.");
						to.sendMessage(ChatColor.AQUA + "You received " + ChatColor.GOLD + this.nf.format( amount ) + ChatColor.AQUA +" Tregs from a secret admirer.");
//						this.log.info(amount+ ":TREG_DONATED " + player.getName() + " => " + to.getName());
						this.log.info(amount+ ":TREG_DONATED " + player.getName() + "(" + wallet.balance() + ")" + " => " + to.getName() + "(" + toWallet.balance() + ")");
					} else {
						player.sendMessage(ChatColor.RED + "You cant give more then you have!");
					}
				}
				return true;
			}
			if (args[0].matches("give") && args.length > 2) {
				int amount;
				Player to;
				
				try {
					amount = Integer.parseInt(args[2]);
				} catch (Exception  e) {
					amount = 0;
				}
				
				try {
					to = this.getServer().matchPlayer(args[1]).get(0);
				} catch (Exception  e) {
					to = null;
				}
				
				if (to != null && amount > 0) {
					info.tregmine.api.TregminePlayer toPlayer = this.tregmine.tregminePlayer.get(to.getName());
					Wallet wallet = new Wallet(player);
					Wallet toWallet = new Wallet(to);

					if  (info.tregmine.api.math.Distance.calc2d(player.getLocation(), to.getLocation()) > 5) {
						if (player.canSee(to)) {
							player.sendMessage(ChatColor.RED + to.getName() + " is to far away for a wallet transaction, please move closer");
						}
						return true;
					}

					
					long newAmount = wallet.balance() - amount;
					if (newAmount >= 0) {
						toWallet.add(amount);
						wallet.take(amount);
						player.sendMessage(ChatColor.AQUA + "You gave " + toPlayer.getChatName() + " " + ChatColor.GOLD + this.nf.format( amount ) + ChatColor.AQUA +" Tregs.");
						to.sendMessage(ChatColor.AQUA + "You received " + ChatColor.GOLD + this.nf.format( amount ) + ChatColor.AQUA +" Tregs from " + tregminePlayer.getChatName() + ".");
						this.log.info(amount+ ":TREG " + player.getName() + "(" + wallet.balance() + ")" + " => " + to.getName() + "(" + toWallet.balance() + ")");
					} else {
						player.sendMessage(ChatColor.RED + "You cant give more then you have!");
					}
				}
			}
			
			
			
			return true;
		}
		return false;
	}
	
	@Override
	public void onLoad() {
	}


}


//TODO: Add following messages to wallet help
/*		
event.getPlayer().sendMessage("/wallet " + "balance - to see your current balance");
event.getPlayer().sendMessage("/wallet " + "tell - to tell others your current balance");
event.getPlayer().sendMessage("/wallet give <player> <amount> - go give <amount> of tregs");
event.getPlayer().sendMessage("/wallet realtime - see in realtime when you gain or lose treg");
*/    
