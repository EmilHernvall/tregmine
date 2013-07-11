package info.tregmine.listeners;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import info.tregmine.Tregmine;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerDAO;
import info.tregmine.api.TregminePlayer;

public class PlayerLookupListener implements Listener
{
    private Tregmine plugin;
    private LookupService cl = null;

    public PlayerLookupListener(Tregmine instance)
    {
        plugin = instance;
        try {
            cl =
                    new LookupService("GeoIPCity.dat",
                            LookupService.GEOIP_MEMORY_CACHE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        InetSocketAddress sock = player.getAddress();
        String ip = sock.getAddress().getHostAddress();
        String host = sock.getAddress().getCanonicalHostName();
        player.setHostName(host);
        player.setIp(ip);

        if (cl != null) {
            Location l1 = cl.getLocation(ip);
            if (l1 != null) {
                Tregmine.LOGGER.info(player.getName() + ": " + l1.countryName
                        + ", " + l1.city + ", " + ip + ", " + l1.postalCode
                        + ", " + l1.region + ", " + host);
                player.setCountryName(l1.countryName);
                player.setCity(l1.city);
                player.setPostalCode(l1.postalCode);
                player.setRegion(l1.region);

                if (!event.getPlayer().isOp() || !player.hasHiddenLocation()) {
                    plugin.getServer().broadcastMessage(
                            ChatColor.DARK_AQUA + "Welcome! "
                                    + player.getChatName()
                                    + ChatColor.DARK_AQUA + " from "
                                    + l1.countryName);
                    player.sendMessage(ChatColor.DARK_AQUA + l1.city + " - "
                            + l1.postalCode);
                }
            }
        }

        // save settings
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayerInfo(player);
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

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String aliasList = null;
        try {
            conn = ConnectionPool.getConnection();

            stmt =
                    conn.prepareStatement("SELECT player_name FROM player "
                            + "INNER JOIN player_property USING (player_id) "
                            + "WHERE property_key = 'ip' AND property_value = ? "
                            + "ORDER BY player_created DESC LIMIT 5");
            stmt.setString(1, ip);
            stmt.execute();

            rs = stmt.getResultSet();

            StringBuilder buffer = new StringBuilder();
            String delim = "";
            while (rs.next()) {
                buffer.append(delim);
                buffer.append(rs.getString("player_name"));
                delim = ", ";
            }

            aliasList = buffer.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if (aliasList == null) {
            Tregmine.LOGGER.info("Aliases: " + aliasList);

            Player[] players = plugin.getServer().getOnlinePlayers();
            for (Player p : players) {
                TregminePlayer current = plugin.getPlayer(p);

                if (current.isAdmin() || current.isGuardian()) {
                    if (!current.hasHiddenLocation()) {
                        current.sendMessage(ChatColor.YELLOW
                                + "This player have also used names: "
                                + aliasList);
                    }
                }
            }
        }
    }
}
