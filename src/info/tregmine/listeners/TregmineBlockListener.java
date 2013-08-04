package info.tregmine.listeners;

import java.util.Set;
import java.util.EnumSet;
import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBLogDAO;

public class TregmineBlockListener implements Listener
{
    private Set<Material> loggedMaterials = EnumSet.of(Material.DIAMOND_ORE,
                                                       Material.EMERALD_ORE,
                                                       Material.GOLD_ORE,
                                                       Material.LAPIS_ORE,
                                                       Material.QUARTZ_ORE,
                                                       Material.REDSTONE_ORE);

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
            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();

                DBLogDAO logDAO = new DBLogDAO(conn);
                logDAO.insertOreLog(player, block.getLocation(), material.getId());
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
    }
}
