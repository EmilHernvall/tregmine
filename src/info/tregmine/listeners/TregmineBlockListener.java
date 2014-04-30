package info.tregmine.listeners;

import java.util.Set;
import java.util.EnumSet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.ILogDAO;

public class TregmineBlockListener implements Listener
{
    private Set<Material> loggedMaterials = EnumSet.of(Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.GOLD_ORE,
            Material.LAPIS_ORE,
            Material.QUARTZ_ORE,
            Material.REDSTONE_ORE,
            Material.MOB_SPAWNER);

    private Tregmine plugin;

    public TregmineBlockListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (event.getBlock().getType().equals(Material.SPONGE)) {
            if (!player.getRank().canPlaceBannedBlocks()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        Block block = event.getBlock();
        Material material = block.getType();
        if (loggedMaterials.contains(material)) {
            try (IContext ctx = plugin.createContext()) {
                ILogDAO logDAO = ctx.getLogDAO();
                logDAO.insertOreLog(player, block.getLocation(), material.getId());
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }
        if (event.getBlock().getType().equals(Material.SPONGE)) {
            if (!player.getRank().canBreakBannedBlocks()) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
