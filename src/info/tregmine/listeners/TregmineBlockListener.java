package info.tregmine.listeners;

//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;

import info.tregmine.Tregmine;
//import info.tregmine.api.TregminePlayer;
//import info.tregmine.api.TregminePlayer;
//import info.tregmine.database.ConnectionPool;

//import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
//import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.event.block.SignChangeEvent;
//import org.bukkit.inventory.meta.ItemMeta;

import info.tregmine.api.TregminePlayer;

public class TregmineBlockListener implements Listener
{
    private Tregmine plugin;

    public TregmineBlockListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        TregminePlayer tregminePlayer = plugin.getPlayer(event.getPlayer());

        if (!tregminePlayer.isTrusted()) {
            event.setCancelled(true);
        }

        if (tregminePlayer.isAdmin()) {
            event.setCancelled(false);
        }

        if (event.getBlock().getType().equals(Material.SPONGE)) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        TregminePlayer tregminePlayer = plugin.getPlayer(event.getPlayer());

        if (!tregminePlayer.isTrusted()) {
            event.setCancelled(true);
        }

        if (tregminePlayer.isAdmin()) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event)
    {
        Location l = event.getBlock().getLocation();
        Block  fence = event.getBlock().getWorld().getBlockAt(l.getBlockX(),l.getBlockY()-1, l.getBlockZ());

        if (fence.getType() == Material.FENCE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        event.setCancelled(true);

        Location loc = event.getBlock().getLocation();
        Block block = event.getBlock().getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ());

        if (block.getType() == Material.OBSIDIAN) {
            event.setCancelled(false);
        }
    }
}
