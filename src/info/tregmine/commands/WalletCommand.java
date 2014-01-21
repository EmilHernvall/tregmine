package info.tregmine.commands;

import java.util.List;
import java.text.NumberFormat;

import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.MathUtil;

public class WalletCommand extends AbstractCommand
{
    private final static NumberFormat FORMAT = NumberFormat.getNumberInstance();

    public WalletCommand(Tregmine tregmine)
    {
        super(tregmine, "wallet");
    }

    private boolean balance(TregminePlayer player)
    {
        try (IContext ctx = tregmine.createContext()) {
            IWalletDAO walletDAO = ctx.getWalletDAO();

            long balance = walletDAO.balance(player);
            if (balance >= 0) {
                player.sendMessage("You have " + GOLD + FORMAT.format(balance)
                        + WHITE + " Tregs.");
            }
            else {
                player.sendMessage(RED + "An error occured.");
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private boolean tell(TregminePlayer player, String name)
    {
        TregminePlayer target = tregmine.getPlayer(name);
        if (target == null) {
            player.sendMessage(RED + "Usage: /wallet tell <player>");
            return true;
        }

        try (IContext ctx = tregmine.createContext()) {
            IWalletDAO walletDAO = ctx.getWalletDAO();

            long balance = walletDAO.balance(player);
            if (balance >= 0) {
                Server server = tregmine.getServer();
                target.sendMessage(player.getChatName() + AQUA +
                    " has " + GOLD + FORMAT.format(balance) + AQUA +
                    " Tregs.");
                player.sendMessage(" You have " + GOLD +
                    FORMAT.format(balance) + AQUA + " Tregs.");
            }
            else {
                player.sendMessage(RED + "An error occured.");
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private boolean donate(TregminePlayer player, TregminePlayer target,
            int amount)
    {
        if (MathUtil.calcDistance2d(player.getLocation(), target.getLocation()) > 5) {
            if (player.canSee(target.getDelegate())) {
                player.sendMessage(RED
                        + target.getName()
                        + " is to far away for a wallet transaction, please move closer");
            }
            return true;
        }

        try (IContext ctx = tregmine.createContext()) {
            IWalletDAO walletDAO = ctx.getWalletDAO();

            if (walletDAO.take(player, amount)) {
                walletDAO.add(target, amount);
                walletDAO.insertTransaction(player.getId(), target.getId(),
                        amount);

                player.sendMessage(AQUA + "You donated to "
                        + target.getChatName() + " " + GOLD
                        + FORMAT.format(amount) + AQUA + " Tregs.");
                target.sendMessage(AQUA + "You received " + GOLD
                        + FORMAT.format(amount) + AQUA + " Tregs from a "
                        + "secret admirer.");
                LOGGER.info(amount + ":TREG_DONATED " + player.getName() + "("
                        + walletDAO.balance(player) + ")" + " => "
                        + target.getName() + "(" + walletDAO.balance(target)
                        + ")");
            }
            else {
                player.sendMessage(RED + "You cant give more then you have!");
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private boolean give(TregminePlayer player, TregminePlayer target,
            int amount)
    {
        if (MathUtil.calcDistance2d(player.getLocation(), target.getLocation()) > 5) {
            if (player.canSee(target.getDelegate())) {
                player.sendMessage(RED
                        + target.getName()
                        + " is to far away for a wallet transaction, please move closer");
            }
            return true;
        }

        try (IContext ctx = tregmine.createContext()) {
            IWalletDAO walletDAO = ctx.getWalletDAO();

            if (walletDAO.take(player, amount)) {
                walletDAO.add(target, amount);
                walletDAO.insertTransaction(player.getId(), target.getId(),
                        amount);

                player.sendMessage(AQUA + "You gave " + target.getChatName()
                        + " " + GOLD + FORMAT.format(amount) + AQUA + " Tregs.");
                target.sendMessage(AQUA + "You received " + GOLD
                        + FORMAT.format(amount) + AQUA + " Tregs from "
                        + player.getChatName() + ".");
                LOGGER.info(amount + ":TREG " + player.getName() + "("
                        + walletDAO.balance(player) + ")" + " => "
                        + target.getName() + "(" + walletDAO.balance(target)
                        + ")");
            }
            else {
                player.sendMessage(RED + "You cant give more then you have!");
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length == 0) {
            player.sendMessage(RED + "Incorrect usage! Try:");
            player.sendMessage(AQUA + "/wallet tell <player>");
            player.sendMessage(AQUA + "/wallet balance");
            player.sendMessage(AQUA + "/wallet donate <player> <amount>");
            player.sendMessage(AQUA + "/wallet give <player> <amount>");
            return true;
        }

        String cmd = args[0];

        // inform people that syntax has changed
        if ("tell".equalsIgnoreCase(cmd) && args.length == 1) {
            player.sendMessage(RED + "Usage: /wallet tell <player>");
            return true;
        }
        // new version with player parameter
        else if ("tell".equalsIgnoreCase(cmd) && args.length == 2) {
            return tell(player, args[1]);
        }
        else if ("balance".equalsIgnoreCase(cmd)) {
            return balance(player);
        }
        else if ("donate".equalsIgnoreCase(cmd) && args.length == 3) {
            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                return true;
            }

            List<TregminePlayer> candidates = tregmine.matchPlayer(args[1]);
            if (candidates.size() != 1) {
                player.sendMessage(RED + "Unknown Player: " + args[1]);
                return true;
            }

            TregminePlayer target = candidates.get(0);
            
            // Sneaky ;)
            if (target.hasFlag(TregminePlayer.Flags.INVISIBLE)) {
                player.sendMessage(RED + "Unknown Player: " + args[1]);
                return true;
            }
            return donate(player, target, amount);
        }
        else if ("give".equalsIgnoreCase(cmd) && args.length == 3) {
            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                return true;
            }

            List<TregminePlayer> candidates = tregmine.matchPlayer(args[1]);
            if (candidates.size() != 1) {
                player.sendMessage(RED + "Unknown Player: " + args[1]);
                return true;
            }
            
            TregminePlayer target = candidates.get(0);
            
            if (target.hasFlag(TregminePlayer.Flags.INVISIBLE)) {
                player.sendMessage(RED + "Unknown Player: " + args[1]);
                return true;
            }

            return give(player, target, amount);
        }

        return false;
    }
}
