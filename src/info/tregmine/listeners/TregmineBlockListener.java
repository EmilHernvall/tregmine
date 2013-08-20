package info.tregmine.listeners;

import java.util.Set;
import java.util.EnumSet;
import java.sql.Connection;
import java.sql.SQLException;

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
                                                       Material.REDSTONE_ORE);

    private Tregmine plugin;

    public TregmineBlockListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onUseElevator(PlayerInteractEvent event) 
    {
        Player player = event.getPlayer();
        if(event.getClickedBlock().getType().equals(Material.STONE_BUTTON)) 
        {
            Location loc = event.getPlayer().getLocation();
            Block standOn = event.getPlayer().getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ());

            if(Material.SPONGE.equals(standOn.getType())) 
            {
                Block signBlock = event.getPlayer().getWorld().getBlockAt(event.getClickedBlock().getLocation().getBlockX(), event.getClickedBlock().getLocation().getBlockY()+1, event.getClickedBlock().getLocation().getBlockZ());

                if(signBlock.getState() instanceof Sign) 
                {
                    Sign sign = (Sign) signBlock.getState();

                    if (sign.getLine(0).contains("up")) {

                        sign.setLine(0, ChatColor.DARK_RED + "Elevator");
                        sign.setLine(2, ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "UP" + ChatColor.GOLD + "]");
                        sign.update(true);
                        
                        player.sendMessage(ChatColor.GREEN + "Elevator Setup!");

                    } 

                    else if (sign.getLine(0).equals(ChatColor.DARK_RED + "Elevator") 
                            && sign.getLine(2).equals(ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "UP" + ChatColor.GOLD + "]")) {

                        int i = standOn.getLocation().getBlockY();

                        while (i < 255) 
                        {
                            i++;
                            Block sponge = event.getPlayer().getWorld().getBlockAt(standOn.getLocation().getBlockX(),  i, standOn.getLocation().getBlockZ());

                            if (sponge.getType().equals(Material.SPONGE)) 
                            {
                                i=256;
                                Location tp = sponge.getLocation();
                                tp.setY(tp.getBlockY() + 1.5);
                                tp.setZ(tp.getBlockZ() + 0.5);
                                tp.setX(tp.getBlockX() + 0.5);
                                tp.setPitch(player.getLocation().getPitch());
                                tp.setYaw(player.getLocation().getYaw());

                                player.teleport(tp);

                            }
                        };
                        player.sendMessage(ChatColor.AQUA + "Going up");
                    }

                    // sign.setLine(0, ChatColor.DARK_PURPLE + "Elevator");
                    // sign.setLine(2, ChatColor.GOLD + "[ " + ChatColor.DARK_GRAY + "UP" + ChatColor.GOLD + " ]");

                    if (sign.getLine(0).contains("down")) {

                        sign.setLine(0, ChatColor.DARK_RED + "Elevator");
                        sign.setLine(2, ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "DOWN" + ChatColor.GOLD + "]");
                        sign.update(true);
                        
                        player.sendMessage(ChatColor.GREEN + "Elevator Setup!");

                    } 

                    else if (sign.getLine(0).equals(ChatColor.DARK_RED + "Elevator") 
                            && sign.getLine(2).equals(ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "DOWN" + ChatColor.GOLD + "]")) {


                        int i = standOn.getLocation().getBlockY();

                        while (i > 0) {
                            i--;
                            Block sponge = event.getPlayer().getWorld().getBlockAt(standOn.getLocation().getBlockX(),  i, standOn.getLocation().getBlockZ());

                            if (sponge.getType().equals(Material.SPONGE)) {
                                i=0;
                                Location tp = sponge.getLocation();
                                tp.setY(tp.getBlockY() + 1.5);
                                tp.setZ(tp.getBlockZ() + 0.5);
                                tp.setX(tp.getBlockX() + 0.5);
                                tp.setPitch(player.getLocation().getPitch());
                                tp.setYaw(player.getLocation().getYaw());

                                player.teleport(tp);

                            }
                        };
                        player.sendMessage(ChatColor.AQUA +"Going down");
                    }
                }
            }
        }
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
