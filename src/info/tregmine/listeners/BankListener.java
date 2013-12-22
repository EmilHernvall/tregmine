package info.tregmine.listeners;

import java.util.Map;

import info.tregmine.Tregmine;
import info.tregmine.api.Account;
import info.tregmine.api.Bank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.TregminePlayer.ChatState;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Lot.Flags;
import info.tregmine.zones.ZoneWorld;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.common.collect.Maps;

public class BankListener implements Listener
{

    private Tregmine plugin;

    public BankListener(Tregmine plugin)
    {
        this.plugin = plugin;
    }
    
    Map<TregminePlayer, Account> accounts = Maps.newHashMap();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getChatState() != ChatState.BANK)
            return;
        ZoneWorld world = new ZoneWorld(player.getWorld());
        Point p =  new Point(player.getLocation().getBlockX(), player
                        .getLocation().getBlockY());
        if (world.findLot(p) == null)return;
        Lot lot = world.findLot(p);
        if(!lot.hasFlag(Flags.BANK))return;
        String[] args = event.getMessage().split(" ");
        if(args[0].equalsIgnoreCase("help")){
            player.sendMessage(ChatColor.RED + "[BANK] "+ "Type \"exit\" to go back to chat");
            player.sendMessage(ChatColor.GREEN + "[BANK] " + "Type \"withdraw x\" to withdraw x from your account");
            player.sendMessage(ChatColor.GREEN + "[BANK] " + "Type \"deposit x\" to deposit x into your account, \n"
                    + "if you do not have an account, a new one will be created.");
            player.sendMessage(ChatColor.GREEN + "[BANK] " + "Type \"balance\" to check your current balance");
            player.sendMessage(ChatColor.GREEN + "[BANK] " + "Type \"account x\" to switch account number");
            player.sendMessage(ChatColor.GREEN + "[BANK] " + "Type \"pin [change|x]\" to verify or change your pin");
            return;
        }
        
        
        if(args[0].equalsIgnoreCase("exit")){
            player.setChatState(ChatState.CHAT);
            accounts.remove(player);
            return;
        }
        
        try(IContext ctx = plugin.createContext()){
            IBankDAO dao = ctx.getBankDAO();
            IWalletDAO wDao = ctx.getWalletDAO();
            Bank bank = dao.getBank(lot.getName());
            Account acct;
            if(accounts.containsKey(player)){
            	acct = accounts.get(player);
            }else{
            	acct = dao.getAccount(bank, player.getName());
            	acct.setVerified(true);
            	accounts.put(player, acct);
            }
            
            if(args[0].equalsIgnoreCase("withdraw")){
            	if(!acct.isVerified()){
                	player.sendMessage(ChatColor.RED + "[BANK] Please verify pin before continuing.");
                	return;
                }
                if(args.length == 2){
                    try{
                        long amount = Long.parseLong(args[1]);
                        if(dao.withdraw(bank, dao.getAccount(bank, player.getName()), amount)){
                            wDao.add(player, amount);
                            player.sendMessage(ChatColor.AQUA + "[BANK] " + "You withdrew " + ChatColor.GOLD + 
                                    amount + ChatColor.AQUA + "[BANK] " + " tregs from your bank.");
                            player.sendMessage(ChatColor.AQUA + "[BANK] " + "You now have " + ChatColor.GOLD + 
                                    + dao.getAccount(bank, player.getName()).getBalance() + " tregs in your bank");
                        }else{
                            player.sendMessage(ChatColor.RED + "[BANK] " + "You do not have that much money in your bank");
                        }
                    }catch(NumberFormatException e){
                        player.sendMessage(ChatColor.RED + "[BANK] " + args[1] + " is not a number");
                    }
                }
                return;
            }
            
            if(args[0].equalsIgnoreCase("deposit")){
                if(args.length == 2){
                    try{
                        long amount = Long.parseLong(args[1]);
                        if(wDao.take(player, amount)){
                            if(dao.getAccount(bank, player.getName()) == null){
                                Account acct1 = new Account();
                                acct.setBank(bank);
                                acct.setPlayer(player.getName());
                                dao.createAccount(acct1, player.getName(), amount);
                                player.sendMessage(ChatColor.AQUA + "[BANK] " + "You created a new account with a balance of"
                                        + ChatColor.GOLD + amount + ChatColor.AQUA + " tregs");
                                player.sendMessage(ChatColor.GREEN + "[BANK] Your account number is " + ChatColor.GOLD + acct.getAccountNumber());
                                player.sendMessage(ChatColor.GREEN + "[BANK] Your pin number is " + ChatColor.GOLD + acct.getPin()
                                		+ ChatColor.GREEN + " WRITE IT DOWN!");
                            }else{
                            	if(!acct.isVerified()){
                                	player.sendMessage(ChatColor.RED + "[BANK] Please verify pin before continuing.");
                                	return;
                                }else{
	                                dao.deposit(bank, dao.getAccount(bank, player.getName()), amount);
	                                player.sendMessage(ChatColor.AQUA + "[BANK] " + "You deposited " + ChatColor.GOLD + amount
	                                        + ChatColor.AQUA + " tregs into your account");
	                            }
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "[BANK] " + "You do not have enough money to deposit that much.");
                        }
                    }catch(NumberFormatException e){
                        player.sendMessage(ChatColor.RED + "[BANK] " + args[1] + " is not a number");
                    }
                }
                return;
            }
            
            if(args[0].equalsIgnoreCase("balance")){
            	if(!acct.isVerified()){
                	player.sendMessage(ChatColor.RED + "[BANK] Please verify pin before continuing.");
                	return;
                }
                player.sendMessage(ChatColor.AQUA + "[BANK] " + "Your account balance is " + ChatColor.GOLD +
                        dao.getAccount(bank, player.getName()).getBalance() + ChatColor.AQUA + " tregs");
                return;
            }
            
            if(args[0].equalsIgnoreCase("account")){
            	if(args.length == 1){
            		player.sendMessage(ChatColor.GREEN + "[BANK] + Your account number is " + ChatColor.GOLD + acct.getAccountNumber());
            	}else if(args.length == 2){
                    int i = 0;
                    try{
                        i = Integer.parseInt(args[1]);
                    }catch(NumberFormatException e){
                        i = dao.getAccount(bank, player.getName()).getAccountNumber();
                    }
                    accounts.put(player, dao.getAccount(bank, i));
                    player.sendMessage(ChatColor.GREEN + "[BANK] " + "Switched to account " + i);
                    return;
                }
            }
            
            if(args[0].equalsIgnoreCase("pin")){
            	if(args[1].equalsIgnoreCase("change")){
            		if(!acct.isVerified()){
            			player.sendMessage(ChatColor.RED + "[BANK] Account must be verified before changing pin");
            			return;
            		}
            		String s = args[2];
            		if(s.length() > 4){
            			player.sendMessage(ChatColor.RED + "[BANK] Pin must be 4 digits long");
            		}else{
            			dao.setPin(acct, s);
            			player.sendMessage(ChatColor.GREEN + "[BANK] Changed pin to " + ChatColor.GOLD + s);
            		}
            	}else{
            		if(acct.isVerified()){
            			player.sendMessage(ChatColor.GREEN + "[BANK] No need, the account is verified");
            		}
            		int i;
            		try{
            			i = Integer.parseInt(args[1]);
            			if(i == Integer.parseInt(acct.getPin())){
            				accounts.get(player).setVerified(true);
            			}else{
            				player.sendMessage(ChatColor.RED + "[BANK] Incorrect pin");
            			}
            		}catch(NumberFormatException e){
            			player.sendMessage(ChatColor.RED + "[BANK] " + args[1] + " is not a number");
            		}
            	}
            }
            
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if(event.getClickedBlock().getType() != Material.IRON_BLOCK) return; //Needs to be changed?
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        ZoneWorld world = new ZoneWorld(player.getWorld());
        Point p =  new Point(player.getLocation().getBlockX(), player
                        .getLocation().getBlockY());
        if (world.findLot(p) == null)return;
        Lot lot = world.findLot(p);
        if (lot.hasFlag(Lot.Flags.BANK)) {
            if(player.getChatState() == ChatState.BANK){
                player.setChatState(ChatState.CHAT);
            }else{
                player.setChatState(ChatState.BANK);
                player.sendMessage(ChatColor.GREEN + "[BANK] " + "You are now banking, type \"help\" for help");
            }
        }
    }

}
