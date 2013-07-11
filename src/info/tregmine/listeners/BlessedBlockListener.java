package info.tregmine.listeners;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBInventoryDAO;
import info.tregmine.database.DBWalletDAO;

public class BlessedBlockListener implements Listener
{
    private Tregmine plugin;
    private Set<Material> allowedMaterials;

    public BlessedBlockListener(Tregmine instance)
    {
        plugin = instance;

        allowedMaterials = new HashSet<Material>();
        allowedMaterials.add(Material.CHEST);
        allowedMaterials.add(Material.FURNACE);
        allowedMaterials.add(Material.BURNING_FURNACE);
        allowedMaterials.add(Material.WOOD_DOOR);
        allowedMaterials.add(Material.WOODEN_DOOR);
        allowedMaterials.add(Material.LEVER);
        allowedMaterials.add(Material.STONE_BUTTON);
        allowedMaterials.add(Material.STONE_PLATE);
        allowedMaterials.add(Material.WOOD_PLATE);
        allowedMaterials.add(Material.WORKBENCH);
        allowedMaterials.add(Material.SIGN_POST);
        allowedMaterials.add(Material.DIODE);
        allowedMaterials.add(Material.DIODE_BLOCK_OFF);
        allowedMaterials.add(Material.TRAP_DOOR);
        allowedMaterials.add(Material.DIODE_BLOCK_ON);
        allowedMaterials.add(Material.JUKEBOX);
        allowedMaterials.add(Material.SIGN);
        allowedMaterials.add(Material.FENCE_GATE);
        allowedMaterials.add(Material.DISPENSER);
        allowedMaterials.add(Material.WOOD_BUTTON);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Block block = event.getClickedBlock();
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && player.getItemInHand().getType() == Material.BONE
                && (player.isAdmin() || player.isGuardian())
                && allowedMaterials.contains(block.getType())) {

            int targetId = player.getBlessTarget();
            if (targetId == 0) {
                player.sendMessage(ChatColor.RED + "Use /bless [name] first!");
                return;
            }

            TregminePlayer target = plugin.getPlayerOffline(targetId);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Use /bless [name] first!");
                return;
            }

            if (!player.isAdmin()) {
                int amount;
                switch (block.getType()) {
                case CHEST:
                    amount = 25000;
                    break;
                case WOOD_DOOR:
                case WOODEN_DOOR:
                    amount = 2000;
                    break;
                default:
                    amount = 4000;
                    break;
                }

                Connection conn = null;
                try {
                    conn = ConnectionPool.getConnection();
                    DBWalletDAO walletDAO = new DBWalletDAO(conn);

                    if (walletDAO.take(player, amount)) {
                        player.sendMessage(ChatColor.LIGHT_PURPLE
                                + (amount + " tregs was taken from you"));
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "You need " + amount
                                + " tregs");
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
            }

            Location loc = block.getLocation();
            if (target.isOnline()) {
                target.sendMessage(ChatColor.AQUA
                        + "Your god blessed it in your name!");
            }
            player.sendMessage(ChatColor.AQUA + "You blessed for "
                    + target.getName() + ".");
            Tregmine.LOGGER.info(player.getName() + " Blessed a block " + loc
                    + " to " + target.getName() + ".");

            Map<Location, Integer> blessedBlocks = plugin.getBlessedBlocks();
            blessedBlocks.put(loc, targetId);

            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();

                DBInventoryDAO invDAO = new DBInventoryDAO(conn);
                invDAO.insertInventory(target, loc,
                        DBInventoryDAO.InventoryType.BLOCK);
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

            event.setCancelled(true);
            return;
        }

        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
                && allowedMaterials.contains(block.getType())) {

            Location loc = block.getLocation();

            Map<Location, Integer> blessedBlocks = plugin.getBlessedBlocks();
            if (blessedBlocks.containsKey(loc)) {
                int id = blessedBlocks.get(loc);
                TregminePlayer target = plugin.getPlayerOffline(id);
                if (id != player.getId()) {
                    if (player.isAdmin()) {
                        player.sendMessage(ChatColor.YELLOW + "Blessed to: "
                                + target.getName() + ".");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Blessed to: "
                                + target.getName() + ".");
                        event.setCancelled(true);
                    }
                }
                else {
                    player.sendMessage(ChatColor.AQUA + "Blessed to you.");
                    event.setCancelled(false);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Location loc = event.getBlock().getLocation();
        Player player = event.getPlayer();

        Map<Location, Integer> blessedBlocks = plugin.getBlessedBlocks();
        if (blessedBlocks.containsKey(loc)) {
            player.sendMessage(ChatColor.RED
                    + "You can't destroy a blessed item.");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Block block = event.getBlockPlaced();

        Map<Location, Integer> blessedBlocks = plugin.getBlessedBlocks();
        if (block.getType() == Material.CHEST) {
            Player player = event.getPlayer();

            Location loc = block.getLocation();
            Location loc1 = loc.add(new Vector(1, 0, 0));
            Location loc2 = loc.subtract(new Vector(1, 0, 0));
            Location loc3 = loc.add(new Vector(0, 0, 1));
            Location loc4 = loc.subtract(new Vector(0, 0, 1));

            if (blessedBlocks.containsKey(loc1)
                    || blessedBlocks.containsKey(loc2)
                    || blessedBlocks.containsKey(loc3)
                    || blessedBlocks.containsKey(loc4)) {

                player.sendMessage(ChatColor.RED
                        + "You can't place a chest next to one that is already blessed.");
                event.setCancelled(true);
                return;
            }
        }
        else if (block.getType() == Material.HOPPER) {
            TregminePlayer player = plugin.getPlayer(event.getPlayer());

            Location loc = block.getLocation();
            Location loc1 = loc.subtract(new Vector(0, 0, 1));

            if (blessedBlocks.containsKey(loc)
                    || blessedBlocks.containsKey(loc1)) {

                player.sendMessage(ChatColor.RED
                        + "You can't place a hopper under a blessed chest.");
                event.setCancelled(true);
            }
        }
    }
}
