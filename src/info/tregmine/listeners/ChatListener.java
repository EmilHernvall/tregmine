package info.tregmine.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

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
                if (sender.getChatChannel().matches("GLOBAL")) {
                    channel = "";
                }

                player.sendMessage(channel+"<" + sender.getChatName() + ChatColor.WHITE + "> " + txtColor + text);
            }
        }

        Tregmine.LOGGER.info(channel+"<" + sender.getName() + "> " + text);

        event.setCancelled(true);
    }
}
