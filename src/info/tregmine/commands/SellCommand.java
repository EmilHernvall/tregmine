package info.tregmine.commands;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
import org.bukkit.event.player.PlayerChatEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;
import info.tregmine.database.ITradeDAO;
import info.tregmine.database.IItemDAO;

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

        int bid = 0;
        try (IContext ctx = tregmine.createContext()) {
            IItemDAO itemDAO = ctx.getItemDAO();

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
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(YELLOW + "[Sell] The Federal Reserve of Tregmine " +
                "bids " + bid + " tregs. Type \"accept\" to sell, \"change\" " +
                "to modify your offering, and \"quit\" to abort.");
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event)
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

            try (IContext ctx = tregmine.createContext()) {
                IItemDAO itemDAO = ctx.getItemDAO();

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

                IWalletDAO walletDAO = ctx.getWalletDAO();

                walletDAO.add(player, bid);
                walletDAO.insertTransaction(0, player.getId(), bid);

                ITradeDAO tradeDAO = ctx.getTradeDAO();
                int tradeId = tradeDAO.insertTrade(player.getId(),
                                                   0,
                                                   bid);
                tradeDAO.insertStacks(tradeId, contents);

                player.sendMessage(YELLOW + "[Sell] " + bid
                        + " tregs was " + "added to your wallet!");
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }

            // Finalize
            player.setChatState(TregminePlayer.ChatState.CHAT);
            inventories.remove(player);
        }
    }
}
