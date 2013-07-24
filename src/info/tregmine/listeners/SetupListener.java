package info.tregmine.listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.PlayerReport;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerDAO;
import info.tregmine.database.DBPlayerReportDAO;

public class SetupListener implements Listener
{
    public static class KickTask implements Runnable
    {
        private TregminePlayer player;
        private String welcomeDateStr;

        public KickTask(TregminePlayer player, String welcomeDateStr)
        {
            this.player = player;
            this.welcomeDateStr = welcomeDateStr;
        }

        @Override
        public void run()
        {
            player.kickPlayer("Younger than 13. Welcome back on " +
                    welcomeDateStr + "!");
        }
    }

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static long AGE_LIMIT = 13L * 60L * 60L * 24L * 365L * 1000L;

    private Tregmine plugin;

    public SetupListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getChatState() != TregminePlayer.ChatState.SETUP) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getChatState() != TregminePlayer.ChatState.SETUP) {
            return;
        }

        player.sendMessage(ChatColor.BLUE + "Welcome to Tregmine!");
        player.sendMessage(ChatColor.BLUE + "Use the chat to enter your date of " +
                "birth to continue. For example: 1986-11-10");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getChatState() != TregminePlayer.ChatState.SETUP) {
            return;
        }

        event.setCancelled(true);

        String text = event.getMessage();
        String[] dateSplit = text.split("-");
        if (dateSplit.length != 3) {
            player.sendMessage(ChatColor.RED + "Use the following format: " +
                               "YYYY-MM-DD. For example: 1986-11-10");
            return;
        }

        Date enteredDate;
        try {
            enteredDate = FORMAT.parse(text);
        } catch (ParseException e) {
            player.sendMessage(ChatColor.RED + "Use the following format: " +
                               "YYYY-MM-DD. For example: 1986-11-10");
            return;
        }

        Date currentDate = new Date();

        long age = currentDate.getTime() - enteredDate.getTime();
        //age /= 60L * 60L * 24L * 365L;

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            long diff = AGE_LIMIT - age;
            if (diff < 0) {
                player.sendMessage(ChatColor.BLUE + "Welcome to Tregmine!");
                player.setChatState(TregminePlayer.ChatState.CHAT);
                player.setSetup(true);

                DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
                playerDAO.updatePlayerPermissions(player);
            }
            else {
                Date welcomeDate = new Date(currentDate.getTime() + diff);
                String welcomeDateStr = FORMAT.format(welcomeDate);

                player.sendMessage(ChatColor.BLUE + "Unfortunately Tregmine has an " +
                        "age limit of 13 years and older. Because of that you have " +
                        "been automatically banned until you are old enough to play here.");
                player.sendMessage(ChatColor.BLUE + "In one minute you will be " +
                        "automatically kicked.");
                player.sendMessage(ChatColor.BLUE + "Welcome back on " + welcomeDateStr + "!");

                Server server = plugin.getServer();
                BukkitScheduler scheduler = server.getScheduler();
                scheduler.scheduleSyncRepeatingTask(plugin,
                        new KickTask(player, welcomeDateStr), 0, 20);

                String message = "Banned until " + welcomeDateStr + " due to age limit.";

                PlayerReport report = new PlayerReport();
                report.setSubjectId(player.getId());
                report.setIssuerId(0);
                report.setAction(PlayerReport.Action.BAN);
                report.setMessage(message);
                report.setValidUntil(welcomeDate);

                DBPlayerReportDAO reportDAO = new DBPlayerReportDAO(conn);
                reportDAO.insertReport(report);
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
}
