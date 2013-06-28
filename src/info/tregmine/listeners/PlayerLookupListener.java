package info.tregmine.listeners;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import info.tregmine.Tregmine;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBWalletDAO;
import info.tregmine.database.DBPlayerDAO;
import info.tregmine.api.TregminePlayer;

public class PlayerLookupListener implements  Listener
{
    private static class ScoreboardRunnable implements Runnable
    {
        private TregminePlayer player;

        public ScoreboardRunnable(TregminePlayer player)
        {
            this.player = player;
        }

        @Override
        public void run()
        {
            if (!player.isOnline()) {
                return;
            }

            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();

            Objective objective = board.registerNewObjective("1", "2");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("" + ChatColor.DARK_RED + "" +
                    ChatColor.BOLD + "Welcome to Tregmine!");

            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();

                DBWalletDAO walletDAO = new DBWalletDAO(conn);

                // Get a fake offline player
                Score score = objective.getScore(Bukkit.getOfflinePlayer(
                            ChatColor.BLACK + "Your Balance:"));
                score.setScore((int)walletDAO.balance(player.getName()));
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally {
                if (conn != null) {
                    try { conn.close(); } catch (SQLException e) {}
                }
            }

            player.setScoreboard(board);
        }
    }

    private Tregmine plugin;
    private LookupService cl = null;

    public PlayerLookupListener(Tregmine instance)
    {
        plugin = instance;
        try {
            cl = new LookupService("GeoIPCity.dat", LookupService.GEOIP_MEMORY_CACHE );
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
                Tregmine.LOGGER.info(player.getName() + ": " + l1.countryName + ", " + l1.city + ", " + ip + ", " + l1.postalCode + ", " + l1.region + ", " + host);
                player.setCountryName(l1.countryName);
                player.setCity(l1.city);
                player.setPostalCode(l1.postalCode);
                player.setRegion(l1.region);

                if (!event.getPlayer().isOp() || !player.hasHiddenLocation()) {
                    plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Welcome! " + player.getChatName() + ChatColor.DARK_AQUA + " from " +l1.countryName);
                    player.sendMessage(ChatColor.DARK_AQUA + l1.city + " - " + l1.postalCode);
                }
            }
        }

        // save settings
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayerInfo(player);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) {}
            }
        }

        if (player.isDonator() ||
            player.isAdmin() ||
            player.isGuardian() ||
            player.isBuilder()) {

            player.sendMessage("You are allowed to fly");
            player.setAllowFlight(true);
        } else {
            player.sendMessage("no-z-cheat");
            player.sendMessage("You are NOT allowed to fly");
            player.setAllowFlight(false);
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String aliasList = null;
        try {
            conn = ConnectionPool.getConnection();

            stmt = conn.prepareStatement("SELECT user.player FROM user, " +
                    "user_settings WHERE user.uid=user_settings.id AND " +
                    "user_settings.value=? ORDER BY time DESC LIMIT 5");
            stmt.setString(1, ip);
            stmt.execute();

            rs = stmt.getResultSet();

            StringBuilder buffer = new StringBuilder();
            String delim = "";
            while (rs.next()) {
                buffer.append(delim);
                buffer.append(rs.getString("player"));
                delim = ", ";
            }

            aliasList = buffer.toString();
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

        if (aliasList == null) {
            Tregmine.LOGGER.info("Aliases: " + aliasList);

            Player[] players = plugin.getServer().getOnlinePlayers();
            for (Player p : players) {
                TregminePlayer current = plugin.getPlayer(p);

                if (current.isAdmin() || current.isGuardian()) {
                    if (!current.hasHiddenLocation()) {
                        current.sendMessage(ChatColor.YELLOW +
                                "This player have also used names: " + aliasList);
                    }
                }
            }
        }

        if (player.isBuilder()) {
            player.setGameMode(GameMode.CREATIVE);
        } else if (!player.isOp()) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        Server server = plugin.getServer();
        BukkitScheduler scheduler = server.getScheduler();

        Runnable runnable = new ScoreboardRunnable(player);
        runnable.run();

        //400 = 20 seconds. 1 second = 20 ticks, 20*20=400
        scheduler.scheduleSyncDelayedTask(plugin,
                                          runnable,
                                          400);
    }
}
