package info.tregmine.bank;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.bank.Account;
import info.tregmine.api.bank.Bank;
import info.tregmine.api.bank.Banker;
import info.tregmine.api.bank.Interfaces;
import info.tregmine.commands.AbstractCommand;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import static org.bukkit.ChatColor.*;

public class BankCommand extends AbstractCommand implements Listener
{
    private Tregmine plugin;

    // Prices and other configuration variables
    private int creationCost = 1000000; // Price to enable a zone to have a bank with one banker.
    private int bankerCost = 100000; // Price for additional bankers.
    private int outpostCost = 100000; // Price for an outpost (ATM) bank.

    public BankCommand(Tregmine tregmine)
    {
        super(tregmine, "bank");
        this.plugin = tregmine;
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String args[])
    {
        if        (args.length == 2 && "internalCommand".equalsIgnoreCase(args[0]) && "make".equalsIgnoreCase(args[1])) {
            return command_makeBanker(player);
        } else if (args.length == 2 && "internalCommand".equalsIgnoreCase(args[0]) && "quit".equalsIgnoreCase(args[1])) {
            return command_quitBank(player);
        } else if (args.length == 2 && "internalCommand".equalsIgnoreCase(args[0]) && "create".equalsIgnoreCase(args[1])) {
            return command_createAccount(player);
        } else if (args.length == 2 && "internalCommand".equalsIgnoreCase(args[0]) && "register".equalsIgnoreCase(args[1])) {
            return command_makeBank(player);
        } else if (args.length >= 2 && "deposit".equalsIgnoreCase(args[0])) {
            return command_depositMoney(player, args);
        } else if (args.length >= 2 && "withdraw".equalsIgnoreCase(args[0])) {
            return command_withdrawMoney(player, args);
        } else if (args.length >= 2 && "change_pin".equalsIgnoreCase(args[0])) {
            return command_changePin(player, args);
        } else if (args.length == 2 && "change_acc".equalsIgnoreCase(args[0])) {
            return command_changeAccount(player, args);
        } else if (args.length == 2 && "verify".equalsIgnoreCase(args[0])) {
            return command_verifyAccount(player, args);
        } else if (args.length == 1 && "help".equalsIgnoreCase(args[0])) {
            return command_viewCommands(player);
        } else {
            player.sendMessage(AQUA + "Use /bank help - To receive all the command help!");
            return true;
        }
    }

    private boolean command_makeBanker(TregminePlayer player)
    {
        if (!player.getRank().canBuildBanks()) {
            player.sendMessage(RED + "Only administrators can add bankers!");
            return true;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());
        Zone zone = world.findZone(player.getLocation());

        if (zone == null) {
            player.sendMessage(RED + "You can not make a bank in the wilderness!");
            return true;
        }

        try (IContext ctx = plugin.createContext()) {

            IBankDAO bankDAO = ctx.getBankDAO();
            IWalletDAO walletDAO = ctx.getWalletDAO();
            Interfaces interfaces = new Interfaces();

            Bank bank = bankDAO.getBank(zone.getId());

            // If bank doesn't exist
            if (bank == null) {
                interfaces.bank_misc_register(player, zone);
                return true;
            }

            if (!walletDAO.take(player, bankerCost)) {
                player.sendMessage(RED + "You do not have sufficient money to create a banker, " + bankerCost + " tregs!");
                return true;
            }

            Banker banker = new Banker(plugin, player.getLocation(), bank);
            player.sendMessage(GREEN + "New banker added at: " + getLocationString(banker.getLocation()));

            bankDAO.addBanker(banker);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private boolean command_makeBank(TregminePlayer player)
    {
        if (!player.getRank().canBuildBanks()) {
            player.sendMessage(RED + "Only administrators can add bankers!");
            return true;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());
        Zone zone = world.findZone(player.getLocation());

        if (zone == null) {
            player.sendMessage(RED + "You can not make a bank in the wilderness!");
            return true;
        }

        try (IContext ctx = plugin.createContext()) {

            IBankDAO bankDAO = ctx.getBankDAO();
            IWalletDAO walletDAO = ctx.getWalletDAO();
            Bank bank = bankDAO.getBank(zone.getId());

            // If bank exists
            if (bank != null) {
                player.sendMessage(RED + "This zone is already a bank! You don't need to re-register it...");
                return true;
            }

            if (!walletDAO.take(player, creationCost)) {
                player.sendMessage(RED + "You do not have sufficient money to create a bank, " + bankerCost + " tregs!");
                return true;
            }

            bank = new Bank(zone.getId());
            bankDAO.createBank(bank);

            player.sendMessage(GREEN + "Bank has been successfully made in the zone: " + zone.getName() + "!");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private boolean command_quitBank(TregminePlayer player)
    {
        if (plugin.getBankersInUse().containsKey(player)) {
            player.setChatState(TregminePlayer.ChatState.CHAT);

            plugin.getBankersInUse().remove(player);
            plugin.getAccountsInUse().remove(player);

            player.setVillagerTimer(0);

            // TODO: Fetch current banker and have custom chat
            player.sendMessage(AQUA + "You are no longer talking to the banker!");
        } else {
            player.sendMessage(RED + "You were not talking to a banker...");
        }

        return true;
    }

    private boolean command_createAccount(TregminePlayer player)
    {
        if (plugin.getBankersInUse().containsKey(player)) {

            try (IContext ctx = plugin.createContext()) {

                IBankDAO bankDAO = ctx.getBankDAO();
                ZoneWorld world = plugin.getWorld(player.getWorld());
                Zone zone = world.findZone(player.getLocation());
                Bank bank = bankDAO.getBank(zone.getId());

                Account account = bankDAO.getAccountByPlayer(bank, player.getId());

                if (account != null) {
                    player.sendMessage(RED + "You already have an account!");
                    return true;
                }

                // If they have gotten this far, then we have determined that they don't
                // have an account. So let's remove the null account, and make a new one

                plugin.getAccountsInUse().remove(player);

                account = new Account();
                account.setBank(bank);
                account.setPlayerId(player.getId());
                account.setVerified(true);

                bankDAO.createAccount(account, player.getId());

                player.sendMessage(GREEN + "Successfully created an account!");
                player.sendMessage(AQUA + "Your pin is: " + account.getPin() + "! Keep it safe...");

                player.setVillagerTimer(plugin.getBankTimeoutCounter());

            } catch (DAOException e) {
                throw new RuntimeException(e);
            }

        } else {
            player.sendMessage(RED + "You were not talking to a banker...");
        }

        return true;
    }

    private boolean command_viewCommands(TregminePlayer player)
    {
        Interfaces interfaces = new Interfaces();
        interfaces.bank_banker_commands(player);

        return true;
    }

    private boolean command_withdrawMoney(TregminePlayer player, String[] args)
    {
        if (plugin.getBankersInUse().containsKey(player)) {
            if (args.length != 3) {
                try {
                    long withdrawAmount = Long.parseLong(args[1]);

                    player.setVillagerTimer(plugin.getBankTimeoutCounter());

                    try (IContext ctx = plugin.createContext()) {

                        IBankDAO bankDAO = ctx.getBankDAO();
                        IWalletDAO walletDAO = ctx.getWalletDAO();
                        Account account;

                        ZoneWorld world = plugin.getWorld(player.getWorld());
                        Zone zone = world.findZone(player.getLocation());
                        Bank bank = bankDAO.getBank(zone.getId());

                        if (!(plugin.getAccountsInUse().containsKey(player))) {
                            player.sendMessage(RED + "An error occured... Your account settings have not been found.");
                            return true;
                        }

                        account = plugin.getAccountsInUse().get(player);

                        if (account == null) {
                            player.sendMessage(RED + "An error occured... You don't haven account!");
                            return true;
                        }

                        if (!(account.isVerified())) {
                            player.sendMessage(RED + "Please verify your account first!");
                            player.sendMessage(AQUA + "Do this with /bank verify <pin number>");
                            return true;
                        }

                        if (bankDAO.withdraw(bank, account, player.getId(), withdrawAmount)) {
                            walletDAO.add(player, withdrawAmount);

                            account.setBalance(account.getBalance() - withdrawAmount);

                            player.sendMessage(GREEN + "You deposited " + withdrawAmount + " tregs!");
                            player.sendMessage(DARK_GREEN + "Wallet balance: " + walletDAO.balance(player) + " tregs.");
                            player.sendMessage(DARK_GREEN + "Bank balance: " + account.getBalance() + " tregs.");
                        } else {
                            player.sendMessage(RED + "You do not have that much money!");
                        }

                    } catch (DAOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(RED + "Incorrect value: " + args[1]);
                }
            } else {
                player.sendMessage(RED + "Wrong syntax: /bank withdraw <amount>");
            }
        } else {
            player.sendMessage(RED + "You were not talking to a banker...");
        }

        return true;
    }

    private boolean command_depositMoney(TregminePlayer player, String[] args)
    {
        if (plugin.getBankersInUse().containsKey(player)) {
            if (args.length != 3) {
                try {
                    long depositAmount = Long.parseLong(args[1]);

                    player.setVillagerTimer(plugin.getBankTimeoutCounter());

                    try (IContext ctx = plugin.createContext()) {

                        IBankDAO bankDAO = ctx.getBankDAO();
                        IWalletDAO walletDAO = ctx.getWalletDAO();
                        Account account;

                        ZoneWorld world = plugin.getWorld(player.getWorld());
                        Zone zone = world.findZone(player.getLocation());
                        Bank bank = bankDAO.getBank(zone.getId());

                        if (!(plugin.getAccountsInUse().containsKey(player))) {
                            player.sendMessage(RED + "An error occured... Your account settings have not been found.");
                            return true;
                        }

                        account = plugin.getAccountsInUse().get(player);

                        if (account == null) {
                            player.sendMessage(RED + "An error occured... You don't haven account!");
                            return true;
                        }

                        if (!(account.isVerified())) {
                            player.sendMessage(RED + "Please verify your account first!");
                            player.sendMessage(AQUA + "Do this with /bank verify <pin number>");
                            return true;
                        }

                        if (walletDAO.take(player, depositAmount)) {
                            bankDAO.deposit(bank, account, player.getId(), depositAmount);

                            account.setBalance(account.getBalance() + depositAmount);

                            player.sendMessage(GREEN + "You deposited " + depositAmount + " tregs!");
                            player.sendMessage(DARK_GREEN + "Wallet balance: " + walletDAO.balance(player) + " tregs.");
                            player.sendMessage(DARK_GREEN + "Bank balance: " + account.getBalance() + " tregs.");
                        } else {
                            player.sendMessage(RED + "You do not have that much money!");
                        }
                    } catch (DAOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(RED + "Incorrect value: " + args[1]);
                }
            } else {
                player.sendMessage(RED + "Wrong syntax: /bank deposit <amount>");
            }
        } else {
            player.sendMessage(RED + "You were not talking to a banker...");
        }

        return true;
    }

    private boolean command_changePin(TregminePlayer player, String[] args)
    {
        if (plugin.getBankersInUse().containsKey(player)) {
            if (args.length != 3) {
                try {
                    // These few lines are pretty redundant, but is the only real way I thought off
                    // it converts them to longs to see if they are numbers, if not throw the exception
                    // and then convert them back to string, as that's what we store our pins as
                    long one = Long.parseLong(args[1]);
                    long two = Long.parseLong(args[2]);

                    String oldPin = String.valueOf(one);
                    String newPin = String.valueOf(two);

                    player.setVillagerTimer(plugin.getBankTimeoutCounter());

                    try (IContext ctx = plugin.createContext()) {

                        IBankDAO bankDAO = ctx.getBankDAO();
                        Account account;

                        if (!(plugin.getAccountsInUse().containsKey(player))) {
                            player.sendMessage(RED + "An error occured... Your account settings have not been found.");
                            return true;
                        }

                        account = plugin.getAccountsInUse().get(player);

                        if (account == null) {
                            player.sendMessage(RED + "An error occured... You don't haven account!");
                            return true;
                        }

                        // No need to check if account is verified, as they need the old pin to be able to change the pin

                        if (!(oldPin.equalsIgnoreCase(account.getPin()))) {
                            player.sendMessage(RED + "Your old pin is incorrect!");
                            return true;
                        }

                        if (newPin.length() != 4) {
                            player.sendMessage(RED + "Your new pin must be atleast 4 characters!");
                            return true;
                        }

                        bankDAO.setPin(account, newPin);
                        account.setPin(newPin);
                        account.setVerified(false);

                        player.sendMessage(GREEN + "You have changed your pin to " + newPin + "!");
                    } catch (DAOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(RED + "Incorrect value: " + args[1] + " or " + args[2]);
                }
            } else {
                player.sendMessage(RED + "Wrong syntax: /bank change_pin <old pin> <new pin>");
            }
        } else {
            player.sendMessage(RED + "You were not talking to a banker...");
        }

        return true;
    }

    private boolean command_changeAccount(TregminePlayer player, String[] args)
    {
        if (plugin.getBankersInUse().containsKey(player)) {

            player.setVillagerTimer(plugin.getBankTimeoutCounter());

            try (IContext ctx = plugin.createContext()) {

                IBankDAO bankDAO = ctx.getBankDAO();
                Account account;

                ZoneWorld world = plugin.getWorld(player.getWorld());
                Zone zone = world.findZone(player.getLocation());
                Bank bank = bankDAO.getBank(zone.getId());

                int accountNumber;

                try {
                    accountNumber = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    player.sendMessage(RED + "An invalid parameter was passed: " + args[1] + "!");
                    return true;
                }

                account = bankDAO.getAccount(bank, accountNumber);

                if (account == null) {
                    player.sendMessage(RED + "An error occured... This account doens't exist!");
                    return true;
                }

                if (!(plugin.getAccountsInUse().containsKey(player))) {
                    player.sendMessage(RED + "Please talk to a banker before changing accounts!");
                    return true;
                }

                plugin.getAccountsInUse().remove(player);

                account.setVerified(false);

                plugin.getAccountsInUse().put(player, account);

                player.sendMessage(GREEN + "Success! You have changed to account: " + account.getId());
                player.sendMessage(AQUA + "Please verify the account pin with: /bank verify <pin number>");
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        } else {
            player.sendMessage(RED + "You were not talking to a banker...");
        }

        return true;
    }

    private boolean command_verifyAccount(TregminePlayer player, String[] args)
    {
        if (plugin.getBankersInUse().containsKey(player)) {

            player.setVillagerTimer(plugin.getBankTimeoutCounter());

            try (IContext ctx = plugin.createContext()) {

                IBankDAO bankDAO = ctx.getBankDAO();
                Account account;

                ZoneWorld world = plugin.getWorld(player.getWorld());
                Zone zone = world.findZone(player.getLocation());
                Bank bank = bankDAO.getBank(zone.getId());

                if (!(plugin.getAccountsInUse().containsKey(player))) {
                    player.sendMessage(RED + "An error occured... Your account settings have not been found.");
                    return true;
                }

                account = plugin.getAccountsInUse().get(player);

                if (account == null) {
                    player.sendMessage(RED + "An error occured... You don't have an account!");
                    return true;
                }

                if (account.isVerified()) {
                    player.sendMessage(AQUA + "You are already verified!");
                    return true;
                }

                if (!(args[1].equalsIgnoreCase(account.getPin()))) {
                    player.sendMessage(RED + "You entered the incorrect pin!");
                    plugin.getLogger().info(player.getRealName() + " used the incorrect pin to access account " + account.getId());
                    return true;
                }

                account.setVerified(true);
                player.sendMessage(GREEN + "Your account has been verified!");
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        } else {
            player.sendMessage(RED + "You were not talking to a banker...");
        }

        return true;
    }

    private String getLocationString(Location loc)
    {
        return "X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ() + ".";
    }
}
