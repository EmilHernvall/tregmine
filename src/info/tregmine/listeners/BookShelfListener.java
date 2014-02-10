/*
 * Copyright (c) 2013, Robert Catron
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    This product includes software developed by the <organization>.
 * 4. Neither the name of the <organization> nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY <COPYRIGHT HOLDER> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package info.tregmine.listeners;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BookMeta;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.*;
import info.tregmine.database.IInventoryDAO.ChangeType;
import info.tregmine.database.IInventoryDAO.InventoryType;

public class BookShelfListener implements Listener
{
    private Tregmine plugin;
    private Map<TregminePlayer, Inventory> openInventories;
    private Map<Location, ItemStack[]> inventories;
    private Map<Inventory, Location> locations;

    public BookShelfListener(Tregmine plugin)
    {
        this.plugin = plugin;
        openInventories = new HashMap<>();
        locations = new HashMap<>();
        inventories = new HashMap<>();
    }

    @EventHandler
    public void bookshelfOpen(PlayerInteractEvent event)
    {
        if(event.isCancelled() || event.getPlayer().getItemInHand().getType().equals(Material.BONE)){
            return;
        }
        
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Block block = event.getClickedBlock();
        
        if (block.getType().equals(Material.BOOKSHELF)) {
            return;
        }
        
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        Location loc = block.getLocation();
        
        try (IContext ctx = plugin.createContext()) {
            IInventoryDAO dao = ctx.getInventoryDAO();
            int id = dao.getInventoryId(loc);
            
            if(id == -1){
                id = dao.insertInventory(player, loc, InventoryType.BLOCK);
            }

            Inventory inv = plugin.getServer().createInventory(null, 9, "Bookshelf");
            inv.setContents(dao.getStacks(id, inv.getSize()));
            player.openInventory(inv);

            openInventories.put(player, inv);
            locations.put(inv, loc);
            inventories.put(loc, inv.getContents());
            
            event.setCancelled(true);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event)
    {
        TregminePlayer player = plugin.getPlayer((Player)event.getPlayer());

        Inventory inv = openInventories.get(player);

        if (inv == null) {
            return;
        }

        Location loc = locations.get(inv);
        Tregmine.LOGGER.info(player.getName() + " closed inventory: " +
                "x=" + loc.getBlockX() + " " +
                "y=" + loc.getBlockY() + " " +
                "z=" + loc.getBlockZ());
        
        ItemStack[] stacks = inv.getContents();
        ItemStack[] oldContents = inventories.get(loc);
        ItemStack[] currentContents = stacks;

        assert oldContents.length == currentContents.length;

        try (IContext ctx = plugin.createContext()) {
            IInventoryDAO dao = ctx.getInventoryDAO();
            int id = dao.getInventoryId(loc);
            dao.insertStacks(id, stacks);

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
                        dao.insertChangeLog(player, id, i, a, ChangeType.REMOVE);
                    }

                    // Added
                    if (b != null) {
                        dao.insertChangeLog(player, id, i, b, ChangeType.ADD);
                    }
                }
            }

            openInventories.remove(player);
            locations.remove(inv);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onSignBook(PlayerEditBookEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        
        if (event.isSigning()) {
            BookMeta meta = event.getNewBookMeta();
            meta.setAuthor(player.getChatName());
            event.setNewBookMeta(meta);
        }
    }
}
