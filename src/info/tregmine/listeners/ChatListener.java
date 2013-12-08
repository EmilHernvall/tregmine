package info.tregmine.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import info.tregmine.Tregmine;
import info.tregmine.WebServer;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.ILogDAO;
import info.tregmine.database.IPlayerDAO;

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
        if (sender.getChatState() != TregminePlayer.ChatState.CHAT) {
            return;
        }

        String channel = sender.getChatChannel();

        for (TregminePlayer to : plugin.getOnlinePlayers()) {
            if (to.getChatState() == TregminePlayer.ChatState.SETUP) {
                continue;
            }
            
            boolean ignored;
            try (IContext ctx = plugin.createContext()) {
                IPlayerDAO playerDAO = ctx.getPlayerDAO();
                ignored = playerDAO.doesIgnore(to, sender);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            if (sender.getRank().canNotBeIgnored()) ignored = false;
            if (ignored == true) continue;

            ChatColor txtColor = ChatColor.WHITE;
            if (sender.equals(to)) {
                txtColor = ChatColor.GRAY;
            }

            String text = event.getMessage();
            for (TregminePlayer online : plugin.getOnlinePlayers()) {
                if (text.contains(online.getName()) && !online.hasFlag(TregminePlayer.Flags.INVISIBLE)){
                    text = text.replaceAll(online.getName(), online.getChatName() + txtColor);
                }
            }

            List<String> player_keywords;
            try (IContext ctx = plugin.createContext()) {
                IPlayerDAO playerDAO = ctx.getPlayerDAO();
                player_keywords = playerDAO.getKeywords(to);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }

            if (player_keywords.size() > 0 && player_keywords != null) {
                for( String keyword : player_keywords ){
                    if (text.toLowerCase().contains(keyword.toLowerCase())) {
                        text = text.replaceAll(keyword, ChatColor.AQUA + keyword + txtColor);
                    }
                }
            }

            if (sender.getChatChannel().equalsIgnoreCase(to.getChatChannel()) ||
                    to.hasFlag(TregminePlayer.Flags.CHANNEL_VIEW)) {
                if ("GLOBAL".equalsIgnoreCase(sender.getChatChannel())) {
                    to.sendMessage("<" + sender.getChatName()
                            + ChatColor.WHITE + "> " + txtColor + text);
                }
                else {
                    to.sendMessage(channel + " <" + sender.getChatName()
                            + ChatColor.WHITE + "> " + txtColor + text);
                }
            }

            if (text.contains(to.getName()) &&
                "GLOBAL".equalsIgnoreCase(sender.getChatChannel()) &&
                !("GLOBAL".equalsIgnoreCase(to.getChatChannel()))) {

                to.sendMessage(ChatColor.BLUE +
                    "You were mentioned in GLOBAL by " + sender.getNameColor() +
                    sender.getChatName());
            }
        }

        Tregmine.LOGGER.info(channel + " <" + sender.getName() + "> " +
                             event.getMessage());

        try (IContext ctx = plugin.createContext()) {
            ILogDAO logDAO = ctx.getLogDAO();
            logDAO.insertChatMessage(sender, channel, event.getMessage());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        event.setCancelled(true);

        WebServer server = plugin.getWebServer();
        server.sendChatMessage(new WebServer.ChatMessage(sender,
                                                         channel,
                                                         event.getMessage()));
    }
}
