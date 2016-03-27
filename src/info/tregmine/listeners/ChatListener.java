package info.tregmine.listeners;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.RESET;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.*;

import info.tregmine.*;
import info.tregmine.api.Rank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.*;
import info.tregmine.events.TregmineChatEvent;

public class ChatListener implements Listener
{
    private Tregmine plugin;

    public ChatListener(Tregmine instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void onTregmineChat(TregmineChatEvent event)
    {
        TregminePlayer sender = event.getPlayer();
        String channel = sender.getChatChannel();
        if(event.getMessage().contains("%cancel%")){
        	event.setCancelled(true);
        	return;
        }
        if(sender.isAfk()){
        	sender.setAfk(false);
        }

        try (IContext ctx = plugin.createContext()) {
            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            for (TregminePlayer to : plugin.getOnlinePlayers()) {
                if (to.getChatState() == TregminePlayer.ChatState.SETUP) {
                    continue;
                }

                if (!sender.getRank().canNotBeIgnored()) {
                    if (playerDAO.doesIgnore(to, sender)) {
                        continue;
                    }
                }

                ChatColor txtColor = ChatColor.WHITE;
                if (sender.getRank() == Rank.JUNIOR_ADMIN || sender.getRank() == Rank.SENIOR_ADMIN){
                	txtColor = ChatColor.GRAY;
                }else if(sender.getRank() != Rank.JUNIOR_ADMIN && sender.getRank() != Rank.SENIOR_ADMIN){
                	if (sender.equals(to)) {
                    txtColor = ChatColor.GRAY;
                }}

                String text = event.getMessage();
                for (TregminePlayer online : plugin.getOnlinePlayers()) {
                    if (text.contains(online.getRealName()) &&
                        !online.hasFlag(TregminePlayer.Flags.INVISIBLE)) {

                        text = text.replaceAll(online.getRealName(),
                                               online.getChatName() + txtColor);
                    }
                }
                if(sender.getRank() != Rank.RESIDENT && sender.getRank() != Rank.SETTLER && sender.getRank() != Rank.TOURIST && sender.getRank() != Rank.UNVERIFIED){
            	text = ChatColor.translateAlternateColorCodes('#', text);
                }
                

                List<String> player_keywords = playerDAO.getKeywords(to);

                if (player_keywords.size() > 0 && player_keywords != null) {
                    for (String keyword : player_keywords) {
                        if (text.toLowerCase().contains(keyword.toLowerCase())) {
                            text = text.replaceAll(Pattern.quote(keyword),
                                    ChatColor.AQUA + keyword + txtColor);
                        }
                    }
                }

                String senderChan = sender.getChatChannel();
                String toChan = to.getChatChannel();
                if (senderChan.equalsIgnoreCase(toChan) ||
                    to.hasFlag(TregminePlayer.Flags.CHANNEL_VIEW)) {

                    if (event.isWebChat()) {
                        if ("GLOBAL".equalsIgnoreCase(senderChan)) {
                            to.sendMessage("(" + sender.getChatName()
                                    + ChatColor.WHITE + ") " + txtColor + text);
                        }
                        else {
                            to.sendMessage(channel + " (" + sender.getChatName()
                                    + ChatColor.WHITE + ") " + txtColor + text);
                        }
                    } else {
                        if ("GLOBAL".equalsIgnoreCase(senderChan)) {
                            to.sendMessage("<" + sender.getChatName()
                                    + ChatColor.WHITE + "> " + txtColor + text);
                        }
                        else {
                            to.sendMessage(channel + " <" + sender.getChatName()
                                    + ChatColor.WHITE + "> " + txtColor + text);
                        }
                    }
                }

                if (text.contains(to.getRealName()) &&
                    "GLOBAL".equalsIgnoreCase(senderChan) &&
                    !"GLOBAL".equalsIgnoreCase(toChan)) {

                    to.sendMessage(ChatColor.BLUE +
                        "You were mentioned in GLOBAL by " + sender.getNameColor() +
                        sender.getChatName());
                }
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (event.isWebChat()) {
            Tregmine.LOGGER.info(channel + " (" + sender.getRealName() + ") " + event.getMessage());
        } else {
            Tregmine.LOGGER.info(channel + " <" + sender.getRealName() + "> " + event.getMessage());
        }

        try (IContext ctx = plugin.createContext()) {
            ILogDAO logDAO = ctx.getLogDAO();
            logDAO.insertChatMessage(sender, channel, event.getMessage());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        //event.setCancelled(true);

        WebServer server = plugin.getWebServer();
        server.executeChatAction(new WebServer.ChatMessage(sender, channel, event.getMessage()));
    }
}
