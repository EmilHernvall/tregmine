package info.tregmine.listeners;

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
import info.tregmine.zones.ZoneWorld;
import info.tregmine.zones.Lot.Flags;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BankListener implements Listener
{

    private Tregmine plugin;

    public BankListener(Tregmine plugin)
    {
        this.plugin = plugin;
    }

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
            return;
        }
        
        
        
        if(args[0].equalsIgnoreCase("exit")){
            player.setChatState(ChatState.CHAT);
            return;
        }
        
        try(IContext ctx = plugin.createContext()){
            IBankDAO dao = ctx.getBankDAO();
            IWalletDAO wDao = ctx.getWalletDAO();
            Bank bank = dao.getBank(lot.getName());
            if(args[0].equalsIgnoreCase("withdraw")){
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
                                Account acct = new Account();
                                acct.setBank(bank);
                                acct.setPlayer(player.getName());
                                dao.createAccount(acct, player.getName(), amount);
                                player.sendMessage(ChatColor.AQUA + "[BANK] " + "You created a new account with a balance of"
                                        + ChatColor.GOLD + amount + ChatColor.AQUA + " tregs");
                            }else{
                                dao.deposit(bank, dao.getAccount(bank, player.getName()), amount);
                                player.sendMessage(ChatColor.AQUA + "[BANK] " + "You deposited " + ChatColor.GOLD + amount
                                        + ChatColor.AQUA + " tregs into your account");
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
                player.sendMessage(ChatColor.AQUA + "[BANK] " + "Your account balance is " + ChatColor.GOLD +
                        dao.getAccount(bank, player.getName()).getBalance() + ChatColor.AQUA + " tregs");
                return;
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
