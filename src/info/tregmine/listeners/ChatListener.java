package info.tregmine.listeners;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBLogDAO;

public class ChatListener implements Listener
{
    private Tregmine plugin;

    public ChatListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        TregminePlayer sender = plugin.getPlayer(event.getPlayer());
        if (sender.isTrading()) {
            return;
        }

        String channel = sender.getChatChannel();

        String text = event.getMessage();

        if (text == null) {
            text = event.getMessage();
        }

        Player[] players = plugin.getServer().getOnlinePlayers();
        for (Player player : players) {
            TregminePlayer to = plugin.getPlayer(player);

            ChatColor txtColor = ChatColor.WHITE;
            if (sender.equals(to)) {
                txtColor = ChatColor.GRAY;
            }

            if (sender.getChatChannel().equals(to.getChatChannel())) {
                if ("GLOBAL".equalsIgnoreCase(sender.getChatChannel())) {
                    player.sendMessage("<" + sender.getChatName()
                            + ChatColor.WHITE + "> " + txtColor + text);
                }
                else {
                    player.sendMessage(channel + " <" + sender.getChatName()
                            + ChatColor.WHITE + "> " + txtColor + text);
                }
            }
        }

        Tregmine.LOGGER.info(channel + " <" + sender.getName() + "> " + text);

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBLogDAO logDAO = new DBLogDAO(conn);
            logDAO.insertChatMessage(sender, channel, text);
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
    }
}
