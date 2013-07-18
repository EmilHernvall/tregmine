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
import info.tregmine.database.DBItemDAO;

public class SellCommand extends AbstractCommand implements Listener
{
    private Map<TregminePlayer, Inventory> inventories;

    public SellCommand(Tregmine tregmine)
    {
        super(tregmine, "sell");

        inventories = new HashMap<TregminePlayer, Inventory>();

        PluginManager pluginMgm = tregmine.getServer().getPluginManager();
        pluginMgm.registerEvents(this, tregmine);
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (player.getChatState() != TregminePlayer.ChatState.CHAT) {
            player.sendMessage(RED + "A trade is already in progress!");
            return true;
        }

        Server server = tregmine.getServer();

        player.setChatState(TregminePlayer.ChatState.SELL);
        player.sendMessage(YELLOW + "[Sell] Welcome to the Federal Reserve " +
                " of Tregmine!");
        player.sendMessage(YELLOW + "[Sell] What do you want to sell?");

        Inventory inv = server.createInventory(null, InventoryType.CHEST);
        player.openInventory(inv);

        inventories.put(player, inv);

        return true;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        TregminePlayer player = tregmine.getPlayer((Player) event.getPlayer());

        Inventory inventory = inventories.get(player);
        if (inventory == null) {
            return;
        }

        player.sendMessage("[Sell] You are offering: ");

        Connection conn = null;
        int bid = 0;
        try {
            conn = ConnectionPool.getConnection();

            DBItemDAO itemDAO = new DBItemDAO(conn);

            ItemStack[] contents = inventory.getContents();
            for (ItemStack stack : contents) {
                if (stack == null) {
                    continue;
                }

                Material material = stack.getType();
                int amount = stack.getAmount();
                int value = itemDAO.getItemValue(material.getId());

                player.sendMessage(YELLOW + "[Sell] " + material.toString() +
                        ": " + amount + " * " + value + " = " + (amount*value) +
                        " tregs");

                bid += amount * value;
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

        player.sendMessage(YELLOW + "[Sell] The Federal Reserve of Tregmine " +
                "bids " + bid + " tregs. Type \"accept\" to sell, \"change\" " +
                "to modify your offering, and \"quit\" to abort.");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        TregminePlayer player = tregmine.getPlayer(event.getPlayer());
        if (player.getChatState() != TregminePlayer.ChatState.SELL) {
            return;
        }

        Inventory inventory = inventories.get(player);
        if (inventory == null) {
            player.setChatState(TregminePlayer.ChatState.CHAT);
            return;
        }

        event.setCancelled(true);

        String text = event.getMessage();

        if ("quit".equalsIgnoreCase(text)) {
            player.setChatState(TregminePlayer.ChatState.CHAT);
            inventories.remove(player);

            // Restore contents to player inventory
            ItemStack[] contents = inventory.getContents();
            Inventory playerInv = player.getInventory();
            for (ItemStack stack : contents) {
                if (stack == null) {
                    continue;
                }
                playerInv.addItem(stack);
            }

            player.sendMessage(YELLOW + "[Sell] Trade ended.");
        }
        else if ("change".equalsIgnoreCase(text)) {
            player.openInventory(inventory);
        }
        else if ("accept".equalsIgnoreCase(text)) {
            ItemStack[] contents = inventory.getContents();

            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();

                DBItemDAO itemDAO = new DBItemDAO(conn);

                // Recalculate cost
                int bid = 0;
                for (ItemStack stack : contents) {
                    if (stack == null) {
                        continue;
                    }

                    Material material = stack.getType();
                    int amount = stack.getAmount();
                    int value = itemDAO.getItemValue(material.getId());

                    bid += amount * value;
                }

                DBWalletDAO walletDAO = new DBWalletDAO(conn);
                DBTradeDAO tradeDAO = new DBTradeDAO(conn);

                walletDAO.add(player, bid);
                walletDAO.insertTransaction(0, player.getId(), bid);

                player.sendMessage(YELLOW + "[Sell] " + bid
                        + " tregs was " + "added to your wallet!");
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

            // Finalize
            player.setChatState(TregminePlayer.ChatState.CHAT);
            inventories.remove(player);
        }
    }
}
