package info.tregmine.blocklog;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.CRC32;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import info.tregmine.database.ConnectionPool;

public class BlockLog extends JavaPlugin
{
    private static class BlockLogListener implements Listener
    {
        public BlockLogListener()
        {
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
            Player player = event.getPlayer();
            ItemStack inHand = player.getItemInHand();
            if (inHand.getTypeId() != Material.PAPER.getId()) {
                return;
            }
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            Location loc = event.getClickedBlock().getLocation();
            long checksum = locationChecksum(loc);

            //String timezone = tregminePlayer.getTimezone();
            String world = loc.getWorld().getName();

            SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
            //dfm.setTimeZone(TimeZone.getTimeZone(timezone));

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                conn = ConnectionPool.getConnection();

                String sql = "SELECT * FROM stats_blocks ";
                sql += "WHERE checksum = ? AND world = ? ";
                sql += "ORDER BY time DESC LIMIT 5";

                stmt = conn.prepareStatement(sql);
                stmt.setLong(1, checksum);
                stmt.setString(2, world);
                stmt.execute();

                rs = stmt.getResultSet();
                while (rs.next()) {
                    Date date = new Date(rs.getLong("time"));
                    long blockid = rs.getLong("blockid");
                    String playerName = rs.getString("player");
                    boolean placed = rs.getBoolean("status");

                    Material material = Material.getMaterial((int)blockid);
                    String materialName = material.name().toLowerCase();

                    String dateStr = dfm.format(date);

                    if (placed) {
                        player.sendMessage(ChatColor.DARK_AQUA + materialName +
                                " placed by " + playerName + " " + dateStr);
                    } else {
                        player.sendMessage(ChatColor.DARK_AQUA + materialName +
                                " delete by " + playerName + " " + dateStr);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (rs != null) {
                    try { rs.close(); } catch (SQLException e) {}
                }
                if (stmt != null) {
                    try { stmt.close(); } catch (SQLException e) {}
                }
                if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
            }
        }

        private void logBlock(Player player, Block block, boolean place)
        {
            Location loc = block.getLocation();
            long checksum = locationChecksum(loc);

            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = ConnectionPool.getConnection();

                String sql = "INSERT INTO stats_blocks (checksum, player, x, y, " +
                             "z, time, status, blockid, world) ";
                sql += "VALUES (?,?,?,?,?,?,?,?,?)";

                stmt = conn.prepareStatement(sql);
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (stmt != null) {
                    try { stmt.close(); } catch (SQLException e) {}
                }
                if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
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

    public BlockLog()
    {
    }

    @Override
    public void onEnable()
    {
        PluginManager pluginMgm = getServer().getPluginManager();
        pluginMgm.registerEvents(new BlockLogListener(), this);
    }
}
