package info.tregmine.listeners;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.InventoryAccess;
import info.tregmine.api.lore.Created;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IInventoryDAO;
import static info.tregmine.database.IInventoryDAO.InventoryType;
import static info.tregmine.database.IInventoryDAO.ChangeType;

public class InventoryListener implements Listener
{
    private Tregmine plugin;
    private Map<InventoryHolder, ItemStack[]> openInventories;

    public InventoryListener(Tregmine instance)
    {
        this.plugin = instance;
        this.openInventories = new HashMap<>();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        TregminePlayer player = plugin.getPlayer((Player)event.getPlayer());

        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        Location loc = null;
        if (holder instanceof BlockState) {
            BlockState block = (BlockState)holder;
            loc = block.getLocation();
        }
        else if (holder instanceof DoubleChest) {
            DoubleChest block = (DoubleChest)holder;
            loc = block.getLocation();
        }
        else {
            return;
        }

        ItemStack[] contents = inv.getContents();
        ItemStack[] copy = new ItemStack[contents.length];
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                copy[i] = contents[i].clone();
            }
        }

        openInventories.put(holder, contents);

        try (IContext ctx = plugin.createContext()) {
            IInventoryDAO invDAO = ctx.getInventoryDAO();

            // Find inventory id, or create a new row if none exists
            int id = invDAO.getInventoryId(loc);
            if (id == -1) {
                id = invDAO.insertInventory(player, loc, InventoryType.BLOCK);
            } else {
                List<InventoryAccess> accessLog = invDAO.getAccessLog(id, 5);

                player.sendMessage(ChatColor.YELLOW + "Last accessed by:");
                SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
                for (InventoryAccess access : accessLog) {
                    TregminePlayer p = plugin.getPlayerOffline(access.getPlayerId());
                    player.sendMessage(p.getChatName() + ChatColor.YELLOW + " on " +
                        dfm.format(access.getTimestamp()) + ".");
                }
            }

            // Insert into access log
            invDAO.insertAccessLog(player, id);
        }
        catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        TregminePlayer player = plugin.getPlayer((Player)event.getPlayer());

        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        Location loc = null;
        if (holder instanceof BlockState) {
            BlockState block = (BlockState)holder;
            loc = block.getLocation();
        }
        else if (holder instanceof DoubleChest) {
            DoubleChest block = (DoubleChest)holder;
            loc = block.getLocation();
        }
        else {
            return;
        }

        if (!openInventories.containsKey(holder)) {
            Tregmine.LOGGER.info("Holder not found.");
            return;
        }

        Tregmine.LOGGER.info(player.getRealName() + " closed inventory: " +
                             "x=" + loc.getBlockX() + " " +
                             "y=" + loc.getBlockY() + " " +
                             "z=" + loc.getBlockZ());

        ItemStack[] oldContents = openInventories.get(holder);
        ItemStack[] currentContents = inv.getContents();

        assert oldContents.length == currentContents.length;

        try (IContext ctx = plugin.createContext()) {
            IInventoryDAO invDAO = ctx.getInventoryDAO();

            // Find inventory id, or create a new row if none exists
            int id = invDAO.getInventoryId(loc);
            if (id == -1) {
                Tregmine.LOGGER.warning("Inventory id " + id + " not found!");
                return;
            }

            // Store all changes
            for (int i = 0; i < oldContents.length; i++) {
                ItemStack a = oldContents[i];
                ItemStack b = currentContents[i];
                if (a == null && b == null) {
                    continue;
                }

                if (a == null || b == null || !a.equals(b)) {
                    Tregmine.LOGGER.info("Slot " + i + " changed. Was " +
                        a + " and is " + b);

                    // Removed
                    if (a != null) {
                        invDAO.insertChangeLog(player, id, i, a, ChangeType.REMOVE);
                    }

                    // Added
                    if (b != null) {
                        invDAO.insertChangeLog(player, id, i, b, ChangeType.ADD);
                    }
                }
            }

            // Store contents
            invDAO.insertStacks(id, currentContents);
        }
        catch (DAOException e) {
            throw new RuntimeException(e);
        }

        openInventories.remove(holder);

        /*Player player = (Player) event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<String>();
                    lore.add(Created.CREATIVE.toColorString());
                    TregminePlayer p = this.plugin.getPlayer(player);
                    lore.add(ChatColor.WHITE + "by: " + p.getChatName());
                    lore.add(ChatColor.WHITE + "Value: " + ChatColor.MAGIC
                            + "0000" + ChatColor.RESET + ChatColor.WHITE
                            + " Treg");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            }
        }*/
    }

    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent event)
    {
        Tregmine.LOGGER.info("InventoryCreative");
        Tregmine.LOGGER.info(event.getInventory().getHolder().toString());
    }
}
