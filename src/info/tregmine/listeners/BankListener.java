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
        if (player.getChatState() != ChatState.BANK) {
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());
        Point p =  new Point(player.getLocation().getBlockX(),
                             player.getLocation().getBlockZ());
        Lot lot = world.findLot(p);
        if (lot == null) {
            return;
        }

        if (!lot.hasFlag(Flags.BANK)) {
            return;
        }

        player.sendMessage(ChatColor.AQUA + "[BANK] " +
                ChatColor.WHITE + "<" + player.getChatName() +
                ChatColor.WHITE + "> " + ChatColor.AQUA + event.getMessage());
        event.setCancelled(true);

        String[] args = event.getMessage().split(" ");
        if ("help".equalsIgnoreCase(args[0])) {
            player.sendMessage(ChatColor.RED + "[BANK] "+
                    "Type \"exit\" to go back to chat");
            player.sendMessage(ChatColor.GREEN + "[BANK] " +
                    "Type \"withdraw x\" to withdraw x from your account");
            player.sendMessage(ChatColor.GREEN + "[BANK] " +
                    "Type \"deposit x\" to deposit x into your account, \n" +
                    "if you do not have an account, a new one will be created.");
            player.sendMessage(ChatColor.GREEN + "[BANK] " +
                    "Type \"balance\" to check your current balance");
            player.sendMessage(ChatColor.GREEN + "[BANK] " +
                    "Type \"account x\" to switch account number");
            player.sendMessage(ChatColor.GREEN + "[BANK] " +
                    "Type \"pin [change|x]\" to verify or change your pin");
            return;
        }
        else if ("exit".equalsIgnoreCase(args[0])) {
            player.setChatState(ChatState.CHAT);
            accounts.remove(player);
            return;
        }

        try (IContext ctx = plugin.createContext()) {
            IBankDAO dao = ctx.getBankDAO();
            IWalletDAO wDao = ctx.getWalletDAO();
            Bank bank = dao.getBank(lot.getId());
            Account acct;
            if (accounts.containsKey(player)) {
                acct = accounts.get(player);
                if (acct.isVerified()) {
                    acct = dao.getAccount(bank, acct.getAccountNumber());
                    acct.setVerified(true);
                }
                accounts.put(player, acct);
            } else {
                acct = dao.getAccountByPlayer(bank, player.getId());
                if (acct != null) {
                    acct.setVerified(true);
                    accounts.put(player, acct);
                }
            }

            if ("withdraw".equalsIgnoreCase(args[0])) {
                if (!acct.isVerified()) {
                    player.sendMessage(ChatColor.RED +
                            "[BANK] Please verify pin before continuing.");
                    return;
                }

                if (args.length == 2) {
                    try {
                        long amount = Long.parseLong(args[1]);
                        if (dao.withdraw(bank, acct, amount)) {
                            wDao.add(player, amount);

                            // update local object
                            acct = dao.getAccount(bank, acct.getAccountNumber());
                            acct.setVerified(true);
                            accounts.put(player, acct);

                            player.sendMessage(ChatColor.AQUA + "[BANK] " +
                                    "You withdrew " + ChatColor.GOLD +
                                    amount + ChatColor.AQUA + ChatColor.AQUA +
                                    " tregs from your bank.");
                            player.sendMessage(ChatColor.AQUA + "[BANK] " +
                                    "You now have " + ChatColor.GOLD +
                                    acct.getBalance() + " tregs in your bank");
                        } else {
                            player.sendMessage(ChatColor.RED + "[BANK] " +
                                    "You do not have that much money in your bank");
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "[BANK] " + args[1] +
                                " is not a number");
                    }
                }

                return;
            }
            else if ("deposit".equalsIgnoreCase(args[0])) {
                if (args.length != 2) {
                    return;
                }

                if (!acct.isVerified()) {
                    player.sendMessage(ChatColor.RED +
                            "[BANK] Please verify pin before continuing.");
                    return;
                }

                long amount;
                try {
                    amount = Long.parseLong(args[1]);
                } catch(NumberFormatException e){
                    player.sendMessage(ChatColor.RED + "[BANK] " + args[1] +
                            " is not a number");
                    return;
                }

                if (wDao.take(player, amount)) {
                    if (acct == null) {
                        acct = new Account();
                        acct.setBank(bank);
                        acct.setPlayerId(player.getId());
                        dao.createAccount(acct, player.getId(), amount);
                        accounts.put(player, acct);

                        player.sendMessage(ChatColor.AQUA + "[BANK] " +
                                "You created a new account with a " +
                                "balance of" + ChatColor.GOLD + amount +
                                ChatColor.AQUA + " tregs");
                        player.sendMessage(ChatColor.GREEN + "[BANK] " +
                                "Your account number is " +
                                ChatColor.GOLD + acct.getAccountNumber());
                        player.sendMessage(ChatColor.GREEN + "[BANK] " +
                                "Your pin number is " + ChatColor.GOLD +
                                acct.getPin() + ChatColor.GREEN +
                                " WRITE IT DOWN!");
                    } else {
                        dao.deposit(bank, acct, amount);

                        // update local object
                        acct = dao.getAccount(bank, acct.getAccountNumber());
                        acct.setVerified(true);
                        accounts.put(player, acct);

                        player.sendMessage(ChatColor.AQUA + "[BANK] " +
                                "You deposited " + ChatColor.GOLD +
                                amount + ChatColor.AQUA + " tregs " +
                                "into your account");
                    }
                } else{
                    player.sendMessage(ChatColor.RED + "[BANK] " +
                            "You do not have enough money to deposit that much.");
                }

                return;
            }
            else if ("balance".equalsIgnoreCase(args[0])) {
                if (!acct.isVerified()) {
                    player.sendMessage(ChatColor.RED + "[BANK] " +
                            "Please verify pin before continuing.");
                    return;
                }

                player.sendMessage(ChatColor.AQUA + "[BANK] " +
                        "Your account balance is " + ChatColor.GOLD +
                        acct.getBalance() +
                        ChatColor.AQUA + " tregs");

                return;
            }
            else if ("account".equalsIgnoreCase(args[0])) {
                if (args.length == 1) {
                    player.sendMessage(ChatColor.GREEN + "[BANK] + Your account " +
                            "number is " + ChatColor.GOLD + acct.getAccountNumber());
                    return;
                } else if (args.length == 2) {
                    int i = 0;
                    try {
                        i = Integer.parseInt(args[1]);
                    } catch(NumberFormatException e){
                        i = acct.getAccountNumber();
                    }

                    accounts.put(player, dao.getAccount(bank, i));
                    player.sendMessage(ChatColor.GREEN + "[BANK] " +
                            "Switched to account " + i);
                    return;
                }
            }
            else if ("pin".equalsIgnoreCase(args[0])) {
                if ("change".equalsIgnoreCase(args[1])) {
                    if (!acct.isVerified()){
                        player.sendMessage(ChatColor.RED + "[BANK] " +
                                "Account must be verified before changing pin");
                        return;
                    }

                    String s = args[2];
                    if (s.length() > 4) {
                        player.sendMessage(ChatColor.RED + "[BANK] " +
                                "Pin must be 4 digits long");
                    } else {
                        dao.setPin(acct, s);
                        player.sendMessage(ChatColor.GREEN + "[BANK] " +
                                "Changed pin to " + ChatColor.GOLD + s);
                    }
                } else {
                    if (acct.isVerified()) {
                        player.sendMessage(ChatColor.GREEN + "[BANK] " +
                                "No need, the account is verified");
                    }

                    int i;
                    try {
                        i = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "[BANK] " +
                                args[1] + " is not a number");
                        return;
                    }

                    if (i == Integer.parseInt(acct.getPin())) {
                        accounts.get(player).setVerified(true);
                    } else {
                        player.sendMessage(ChatColor.RED +
                                "[BANK] Incorrect pin");
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
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.IRON_BLOCK) {
            return;
        }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        ZoneWorld world = plugin.getWorld(player.getWorld());
        Point p =  new Point(player.getLocation().getBlockX(),
                             player.getLocation().getBlockZ());
        Lot lot = world.findLot(p);
        if (lot == null) {
            return;
        }

        if (lot.hasFlag(Lot.Flags.BANK)) {
            if (player.getChatState() == ChatState.BANK){
                player.setChatState(ChatState.CHAT);
            } else {
                player.setChatState(ChatState.BANK);
                player.sendMessage(ChatColor.GREEN + "[BANK] " +
                        "You are now banking, type \"help\" for help");
            }
        }
    }

}