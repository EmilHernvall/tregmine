package info.tregmine.blocklog;

import info.tregmine.Tregmine;
import info.tregmine.api.*;
import info.tregmine.database.*;
import info.tregmine.database.db.DBContext;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;
import java.util.zip.CRC32;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockLog extends JavaPlugin
{
    private static class BlockLogListener implements Listener
    {
        // For Anarchy
        private static final Material[] protectedBlocks = {
            Material.STONE, Material.DIRT, Material.GRASS, Material.SAND, Material.NETHERRACK, Material.ENDER_STONE
        };

        private IContextFactory ctxFactory;
        private Tregmine tregmine;
        private BlockLog plugin;
        private Map<TregminePlayer, Integer> timedOut = new HashMap<>();

        public BlockLogListener(Tregmine tregmine, BlockLog blocklog)
        {
            this.ctxFactory = tregmine.getContextFactory();
            this.tregmine = tregmine;
            this.plugin = blocklog;
        }

        private static long locationChecksum(Location loc)
        {
            CRC32 crc32 = new CRC32();
            String pos = loc.getX() + "," + loc.getY() + "," + loc.getZ();
            crc32.update(pos.getBytes());
            return crc32.getValue();
        }

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event)
        {
            final TregminePlayer player = tregmine.getPlayer(event.getPlayer());
            ItemStack inHand = player.getItemInHand();

            if (!inHand.getType().equals(Material.PAPER)) {
                return;
            }

            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            Location loc = event.getClickedBlock().getLocation();
            long checksum = locationChecksum(loc);
            String world = loc.getWorld().getName();

            if ((world.equalsIgnoreCase(tregmine.getRulelessWorld().getName()) ||
                 world.equalsIgnoreCase(tregmine.getRulelessNether().getName()) ||
                 world.equalsIgnoreCase(tregmine.getRulelessEnd().getName())) &&
                !player.getRank().canBypassWorld()) {

                Material blockType = event.getClickedBlock().getType();
                if (blockType == null) {
                    return;
                }

                boolean success = true;

                for (Material mat : protectedBlocks) {
                    if (blockType.equals(mat)) {
                        success = false;
                    }
                }

                if (!success) {
                    player.sendMessage(ChatColor.RED + "You can not paper this block!");
                    return;
                }

                if (timedOut != null && !timedOut.isEmpty()) {
                    for (Entry<TregminePlayer, Integer> p : timedOut.entrySet()) {
                        if (p.getKey().equals(player)) {
                            player.sendMessage(ChatColor.RED + "Your paper is timed out, Try again in " + p.getValue() + "!");
                            return;
                        }
                    }
                }


                Integer timeout = 5; // seconds
                timedOut.put(player, timeout);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Entry<TregminePlayer, Integer> p : timedOut.entrySet()) {
                            if (p.getValue() <= 0) {
                                timedOut.remove(player);
                                player.sendMessage(ChatColor.GREEN + "Safe to use paper again!");
                            }
                            p.setValue(p.getValue() - 1);
                        }
                    }
                }.runTaskTimer(plugin, 10L, 10L);

                if (inHand.getAmount() > 1) {
                    inHand.setAmount(inHand.getAmount() - 1);
                } else {
                    player.getInventory().remove(inHand);
                }
            }

            SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");

            try (IContext ctx = ctxFactory.createContext()) {
                Connection conn = ((DBContext) ctx).getConnection();

                String sql = "SELECT * FROM stats_blocks ";
                sql += "WHERE checksum = ? AND world = ? ";
                sql += "ORDER BY time DESC LIMIT 5";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, checksum);
                    stmt.setString(2, world);
                    stmt.execute();

                    try (ResultSet rs = stmt.getResultSet()) {
                        while (rs.next()) {
                            Date date = new Date(rs.getLong("time"));
                            long blockid = rs.getLong("blockid");
                            String playerName = rs.getString("player");
                            boolean placed = rs.getBoolean("status");

                            Material material = Material.getMaterial((int)blockid);
                            String materialName = material.name().toLowerCase();

                            String dateStr = dfm.format(date);

                            if (placed) {
                                player.sendMessage(
                                    ChatColor.DARK_AQUA + materialName +
                                    " placed by " + playerName + " " + dateStr);
                            } else {
                                player.sendMessage(
                                    ChatColor.DARK_AQUA + materialName +
                                    " delete by " + playerName + " " + dateStr);
                            }
                        }
                    }
                }
            } catch (DAOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private void logBlock(Player player, Block block, boolean place)
        {
            Location loc = block.getLocation();
            long checksum = locationChecksum(loc);

            try (IContext ctx = ctxFactory.createContext()) {
                Connection conn = ((DBContext)ctx).getConnection();

                String sql = "INSERT INTO stats_blocks (checksum, player, x, y, " +
                             "z, time, status, blockid, world) ";
                sql += "VALUES (?,?,?,?,?,?,?,?,?)";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, checksum);
                    stmt.setString(2, player.getName());
                    stmt.setDouble(3, loc.getX());
                    stmt.setDouble(4, loc.getY());
                    stmt.setDouble(5, loc.getZ());
                    stmt.setDouble(6, System.currentTimeMillis());
                    stmt.setBoolean(7, place);
                    stmt.setDouble(8, block.getTypeId());
                    stmt.setString(9, loc.getWorld().getName());
                    stmt.execute();
                }
            } catch (DAOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @EventHandler
        public void onBlockPlace(BlockPlaceEvent event)
        {
            Player player = event.getPlayer();
            Block block = event.getBlock();

            logBlock(player, block, true);
        }

        @EventHandler
        public void onBlockBreak(BlockBreakEvent event)
        {
            Player player = event.getPlayer();
            Block block = event.getBlock();

            logBlock(player, block, false);
        }
    }

    public Tregmine tregmine = null;

    public BlockLog()
    {
    }

    @Override
    public void onEnable()
    {
        PluginManager pluginMgm = getServer().getPluginManager();

        // Check for tregmine plugin
        if (tregmine == null) {
            Plugin mainPlugin = pluginMgm.getPlugin("tregmine");
            if (mainPlugin != null) {
                tregmine = (Tregmine)mainPlugin;
            } else {
                Tregmine.LOGGER.info(getDescription().getName() + " " +
                         getDescription().getVersion() +
                         " - could not find Tregmine");
                pluginMgm.disablePlugin(this);
                return;
            }
        }

        pluginMgm.registerEvents(new BlockLogListener(tregmine, this), this);
    }
}
