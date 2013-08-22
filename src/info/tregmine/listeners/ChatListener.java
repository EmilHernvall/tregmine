package info.tregmine.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import info.tregmine.Tregmine;
import info.tregmine.ChatHandler;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.ILogDAO;

public class ChatListener implements Listener
{
    private Tregmine plugin;

    public ChatListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event)
    {
        TregminePlayer sender = plugin.getPlayer(event.getPlayer());
        if (sender.getChatState() != TregminePlayer.ChatState.CHAT) {
            return;
        }

        String channel = sender.getChatChannel();

        String text = event.getMessage();

        for (TregminePlayer to : plugin.getOnlinePlayers()) {
            if (to.getChatState() == TregminePlayer.ChatState.SETUP) {
                continue;
            }

            ChatColor txtColor = ChatColor.WHITE;
            if (sender.equals(to)) {
                txtColor = ChatColor.GRAY;
            }

            if (sender.getChatChannel().equalsIgnoreCase(to.getChatChannel())) {
                if ("GLOBAL".equalsIgnoreCase(sender.getChatChannel())) {
                    to.sendMessage("<" + sender.getChatName()
                            + ChatColor.WHITE + "> " + txtColor + text);
                }
                else {
                    to.sendMessage(channel + " <" + sender.getChatName()
                            + ChatColor.WHITE + "> " + txtColor + text);
                }
            }
        }

        Tregmine.LOGGER.info(channel + " <" + sender.getName() + "> " + text);

        try (IContext ctx = plugin.createContext()) {
            ILogDAO logDAO = ctx.getLogDAO();
            logDAO.insertChatMessage(sender, channel, text);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        event.setCancelled(true);

        plugin.getServer()
              .getPluginManager()
              .callEvent(new ChatHandler.MinecraftChatEvent(sender,
                                                            channel,
                                                            text));
    }
}
