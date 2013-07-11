package info.tregmine.commands;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBWalletDAO;
import info.tregmine.database.DBTradeDAO;

public class TradeCommand extends AbstractCommand implements Listener
{
    private enum TradeState {
        ITEM_SELECT, BID, CONSIDER_BID;
    };

    private static class TradeContext
    {
        Inventory inventory;
        TregminePlayer firstPlayer;
        TregminePlayer secondPlayer;
        TradeState state;
        int bid;
    }

    private Map<TregminePlayer, TradeContext> activeTrades;

    public TradeCommand(Tregmine tregmine)
    {
        super(tregmine, "trade");

        activeTrades = new HashMap<TregminePlayer, TradeContext>();

        PluginManager pluginMgm = tregmine.getServer().getPluginManager();
        pluginMgm.registerEvents(this, tregmine);
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }
        if (player.isTrading()) {
            player.sendMessage(RED + "A trade is already in progress!");
            return true;
        }

        Server server = tregmine.getServer();
        String pattern = args[0];

        List<TregminePlayer> candidates = tregmine.matchPlayer(pattern);
        if (candidates.size() != 1) {
            // TODO: error message
            return true;
        }

        TregminePlayer target = candidates.get(0);
        if (target.isTrading()) {
            player.sendMessage(RED + target.getName() + "is in a trade!");
            return true;
        }

        Inventory inv = server.createInventory(null, InventoryType.CHEST);
        player.openInventory(inv);

        TradeContext ctx = new TradeContext();
        ctx.inventory = inv;
        ctx.firstPlayer = player;
        ctx.secondPlayer = target;
        ctx.state = TradeState.ITEM_SELECT;

        activeTrades.put(player, ctx);
        activeTrades.put(target, ctx);

        player.setIsTrading(true);
        target.setIsTrading(true);

        player.sendMessage(YELLOW + "[Trade] You are now trading with "
                + target.getChatName() + YELLOW + ". What do you want "
                + "to offer?");

        target.sendMessage(YELLOW + "[Trade] You are now in a trade with "
                + player.getChatName() + YELLOW + ". To exit, type \"quit\".");

        return true;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        TregminePlayer player = tregmine.getPlayer((Player) event.getPlayer());
        TradeContext ctx = activeTrades.get(player);
        if (ctx == null) {
            return;
        }

        TregminePlayer target = ctx.secondPlayer;

        target.sendMessage(YELLOW + "[Trade] " + player.getChatName() + " "
                + YELLOW + " is offering: ");
        player.sendMessage("[Trade] You are offering: ");

        ItemStack[] contents = ctx.inventory.getContents();
        for (ItemStack stack : contents) {
            if (stack == null) {
                continue;
            }
            Material material = stack.getType();
            int amount = stack.getAmount();
            target.sendMessage(YELLOW + "[Trade] " + amount + " "
                    + material.toString());
            player.sendMessage(YELLOW + "[Trade] " + amount + " "
                    + material.toString());
        }

        target.sendMessage(YELLOW
                + "[Trade] To make a bid, type \"bid <tregs>\". "
                + "For example: bid 1000");
        player.sendMessage(YELLOW + "[Trade] Waiting for "
                + target.getChatName() + YELLOW
                + " to make a bid. Type \"change\" to modify " + "your offer.");

        ctx.state = TradeState.BID;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        TregminePlayer player = tregmine.getPlayer(event.getPlayer());
        if (!player.isTrading()) {
            return;
        }

        event.setCancelled(true);

        TradeContext ctx = activeTrades.get(player);
        if (ctx == null) {
            return;
        }

        TregminePlayer first = ctx.firstPlayer;
        TregminePlayer second = ctx.secondPlayer;

        String text = event.getMessage();
        String[] args = text.split(" ");

        Tregmine.LOGGER.info("state: " + ctx.state);
        Tregmine.LOGGER.info("message: " + text);

        if ("quit".equals(args[0]) && args.length == 1) {
            first.setIsTrading(false);
            second.setIsTrading(false);
            activeTrades.remove(first);
            activeTrades.remove(second);
            if (ctx.state == TradeState.ITEM_SELECT) {
                first.closeInventory();
            }

            // Restore contents to player inventory
            ItemStack[] contents = ctx.inventory.getContents();
            Inventory firstInv = first.getInventory();
            for (ItemStack stack : contents) {
                if (stack == null) {
                    continue;
                }
                firstInv.addItem(stack);
            }

            first.sendMessage(YELLOW + "[Trade] Trade ended.");
            second.sendMessage(YELLOW + "[Trade] Trade ended.");
        }
        else if ("bid".equals(args[0]) && args.length == 2) {
            if (second != player) {
                player.sendMessage(RED + "[Trade] You can't make a bid!");
                return;
            }
            if (ctx.state != TradeState.BID) {
                player.sendMessage(RED
                        + "[Trade] You can't make a bid right now.");
                return;
            }

            int amount = 0;
            try {
                amount = Integer.parseInt(args[1]);
                if (amount < 0) {
                    player.sendMessage(RED
                            + "[Trade] You have to bid an integer "
                            + "number of tregs.");
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(RED + "[Trade] You have to bid an integer "
                        + "number of tregs.");
                return;
            }

            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();
                DBWalletDAO walletDAO = new DBWalletDAO(conn);

                long balance = walletDAO.balance(second);
                if (amount > balance) {
                    player.sendMessage(RED + "[Trade] You only have " + balance
                            + " tregs!");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                }
            }

            first.sendMessage(YELLOW + "[Trade] " + second.getChatName()
                    + " bid " + amount + " tregs. Type \"accept\" to "
                    + "proceed with the trade. Type \"change\" to modify "
                    + "your offer. Type \"quit\" to stop trading.");
            second.sendMessage(YELLOW + "[Trade] You bid " + amount + "tregs.");

            ctx.bid = amount;
            ctx.state = TradeState.CONSIDER_BID;
        }
        else if ("change".equals(args[0]) && args.length == 1) {
            if (first != player) {
                player.sendMessage(RED + "[Trade] You can't change the offer!");
                return;
            }

            player.openInventory(ctx.inventory);
            ctx.state = TradeState.ITEM_SELECT;
        }
        else if ("accept".equals(args[0]) && args.length == 1) {
            if (first != player) {
                player.sendMessage(RED + "[Trade] You can't accept an offer!");
                return;
            }

            ItemStack[] contents = ctx.inventory.getContents();

            // Withdraw ctx.bid tregs from second players wallet
            // Add ctx.bid tregs from first players wallet
            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();
                DBWalletDAO walletDAO = new DBWalletDAO(conn);
                DBTradeDAO tradeDAO = new DBTradeDAO(conn);

                if (walletDAO.take(second, ctx.bid)) {
                    walletDAO.add(first, ctx.bid);
                    walletDAO.insertTransaction(second.getId(), first.getId(),
                            ctx.bid);

                    int tradeId =
                            tradeDAO.insertTrade(first.getId(), second.getId(),
                                    ctx.bid);
                    tradeDAO.insertStacks(tradeId, contents);

                    first.sendMessage(YELLOW + "[Trade] " + ctx.bid
                            + " tregs was " + "added to your wallet!");
                    second.sendMessage(YELLOW + "[Trade] " + ctx.bid
                            + " tregs was " + "withdrawn to your wallet!");
                }
                else {
                    first.sendMessage(RED + "[Trade] Failed to withdraw "
                            + ctx.bid + " from the wallet of "
                            + second.getChatName() + "!");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                }
            }

            // Move items to second players inventory
            Inventory secondInv = second.getInventory();
            for (ItemStack stack : contents) {
                if (stack == null) {
                    continue;
                }
                secondInv.addItem(stack);
            }

            // Finalize
            first.setIsTrading(false);
            second.setIsTrading(false);
            activeTrades.remove(first);
            activeTrades.remove(second);
        }
        else {
            first.sendMessage(YELLOW + "[Trade] " + WHITE + "<"
                    + player.getChatName() + WHITE + "> " + text);

            second.sendMessage(YELLOW + "[Trade] " + WHITE + "<"
                    + player.getChatName() + WHITE + "> " + text);
        }
    }
}
