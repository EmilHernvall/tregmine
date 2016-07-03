package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.InventoryAccess;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.lore.Created;
import info.tregmine.database.*;
import info.tregmine.database.IInventoryDAO.ChangeType;
import info.tregmine.database.IInventoryDAO.InventoryType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryListener implements Listener
{
    private Tregmine plugin;
    private Map<Location, ItemStack[]> openInventories;

    public InventoryListener(Tregmine instance)
    {
        this.plugin = instance;
        this.openInventories = new HashMap<>();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event)
    {
    	long start = System.currentTimeMillis();
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

        openInventories.put(loc, contents);

        try (IContext ctx = plugin.createContext()) {
            IInventoryDAO invDAO = ctx.getInventoryDAO();

            // Find inventory id, or create a new row if none exists
            int id = invDAO.getInventoryId(loc);
            if (id == -1) {
                id = invDAO.insertInventory(player, loc, InventoryType.BLOCK);
            } else {
                List<InventoryAccess> accessLog = invDAO.getAccessLog(id, 10);
                int others = 0;
                for (InventoryAccess access : accessLog) {
                    if (access.getPlayerId() != player.getId()) {
                        others++;
                    }
                }

                if (others > 0 && player.hasFlag(TregminePlayer.Flags.CHEST_LOG)) {
                    player.sendMessage(ChatColor.YELLOW + "Last accessed by:");
                    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
                    int i = 0;
                    for (InventoryAccess access : accessLog) {
                        if (access.getPlayerId() != player.getId()) {
                            if (i > 2) {
                                break;
                            }
                            TregminePlayer p = plugin.getPlayerOffline(access.getPlayerId());
                            player.sendMessage(p.getChatName() + ChatColor.YELLOW + " on " +
                                dfm.format(access.getTimestamp()) + ".");
                            i++;
                        }
                    }
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
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if(meta.hasLore()){
                	List<String> lore = meta.getLore();
                	List<String> newlore = new ArrayList<String>();
                	for(String a : lore){
                		newlore.add(a.replace("Ã", ""));
                	}
                	meta.setLore(newlore);
                    item.setItemMeta(meta);
                }
            }
        }
    	if(player.getGameMode() == GameMode.CREATIVE){
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<String>();
                lore.add(Created.CREATIVE.toColorString());
                TregminePlayer p = this.plugin.getPlayer(player.getName());
                lore.add(ChatColor.WHITE + "by: " + p.getChatName());
                lore.add(ChatColor.WHITE + "Value: " + ChatColor.MAGIC
                        + "0000" + ChatColor.RESET + ChatColor.WHITE
                        + " Treg");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    	}
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

        if (!openInventories.containsKey(loc)) {
            return;
        }
        

        ItemStack[] oldContents = openInventories.get(loc);
        ItemStack[] currentContents = inv.getContents();
        
        try (IContext ctx = plugin.createContext()) {
            IInventoryDAO invDAO = ctx.getInventoryDAO();

            // Find inventory id, or create a new row if none exists
            int id = invDAO.getInventoryId(loc);
            if (id == -1) {
                return;
            }
            
            // Store all changes
            long logChestTime = System.currentTimeMillis();
            for (int i = 0; i < oldContents.length; i++) {
                ItemStack a = oldContents[i];
                ItemStack b = currentContents[i];
                if (a == null && b == null) {
                    continue;
                }

                if (a == null || b == null || !a.equals(b)) {
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
            long totalTime = System.currentTimeMillis() - logChestTime;
            // Store contents
            long startPoint = System.currentTimeMillis();
    		try{
    		invDAO.insertStacks(id, currentContents);
    		}catch(DAOException e){
    			throw new RuntimeException(e);
    		}
            long endPoint = System.currentTimeMillis() - startPoint;
        }
        catch (DAOException e) {
            throw new RuntimeException(e);
        }

        openInventories.remove(loc);
    }
}
