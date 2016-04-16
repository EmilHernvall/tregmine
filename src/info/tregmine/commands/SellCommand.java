package info.tregmine.commands;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.YELLOW;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.tregmine.events.TregminePortalEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

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
    	if(this.tregmine.getConfig().getBoolean("general.economy.minefortregs")){
    		player.sendMessage(ChatColor.RED + "The server has decided to use the mining system to obtain tregs.");
    		return true;
    	}
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
                int value = itemDAO.getItemValue(material.getId(), stack.getData().getData());
                
                //Check if the item is illegal :)
                if(stack.hasItemMeta()){
                ItemMeta meta = stack.getItemMeta();
                List<String> lore = meta.getLore();
                if(lore.get(0).contains("CREATIVE") || lore.get(0).contains("SURVIVAL")){
                	player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You CANNOT sell illegal items. The value for the " + material.toString() + " has been set to 0.");
                	amount = 0;
                	value = 0;
                }
                }
                
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
                    int value = itemDAO.getItemValue(material.getId(), stack.getData().getData());

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

	@EventHandler
    public void sellPlayerDeath(PlayerDeathEvent event)
    {
        TregminePlayer player = tregmine.getPlayer(event.getEntity());
        if(!inventories.containsKey(player)) return;
        
        player.setChatState(TregminePlayer.ChatState.CHAT);
        inventories.remove(player);
		player.sendMessage(ChatColor.RED + "You just lost your /sell inventory! Now that was silly...");
    }

	@EventHandler
	public void sellPlayerPortal(TregminePortalEvent event)
	{
		TregminePlayer player = event.getPlayer();
		if(!inventories.containsKey(player)) return;

		player.setChatState(TregminePlayer.ChatState.CHAT);
		inventories.remove(player);
		player.sendMessage(ChatColor.RED + "You just lost your /sell inventory! Now that was silly...");
	}
}
