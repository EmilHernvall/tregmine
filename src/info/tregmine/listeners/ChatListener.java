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
            	if (text.contains("#r") || text.contains("#R")) {
            		text = text.replaceAll("#R", ChatColor.RESET + "");
                    text = text.replaceAll("#r", ChatColor.RESET + "");
                }
            	if (text.contains("#0")) {

                    text = text.replaceAll("#0", ChatColor.BLACK + "");
                }
                if (text.contains("#1")) {

                    text = text.replaceAll("#1", ChatColor.DARK_BLUE + "");
                }
                if (text.contains("#2")) {

                    text = text.replaceAll("#2", ChatColor.DARK_GREEN + "");
                }
                if (text.contains("#3")) {

                    text = text.replaceAll("#3", ChatColor.DARK_AQUA + "");
                }
                if (text.contains("#4")) {

                    text = text.replaceAll("#4", ChatColor.DARK_RED + "");
                }
                if (text.contains("#5")) {

                    text = text.replaceAll("#5", ChatColor.DARK_PURPLE + "");
                }
                if (text.contains("#6")) {

                    text = text.replaceAll("#6", ChatColor.GOLD + "");
                }
                if (text.contains("#7")) {

                    text = text.replaceAll("#7", ChatColor.GRAY + "");
                }
                if (text.contains("#8")) {

                    text = text.replaceAll("#8", ChatColor.DARK_GRAY + "");
                }
                if (text.contains("#9")) {

                    text = text.replaceAll("#9", ChatColor.BLUE + "");
                }
                if (text.contains("#a") || text.contains("#A")) {
                	text = text.replaceAll("#A", ChatColor.GREEN + "");
                    text = text.replaceAll("#a", ChatColor.GREEN + "");
                }
                if (text.contains("#b") || text.contains("#B")) {
                    text = text.replaceAll("#B", ChatColor.AQUA + "");
                    text = text.replaceAll("#b", ChatColor.AQUA + "");
                }
                if (text.contains("#c") || text.contains("#C")) {
                    text = text.replaceAll("#C", ChatColor.RED + "");
                    text = text.replaceAll("#c", ChatColor.RED + "");
                }
                if (text.contains("#d") || text.contains("#D")) {
                    text = text.replaceAll("#D", ChatColor.LIGHT_PURPLE + "");
                    text = text.replaceAll("#d", ChatColor.LIGHT_PURPLE + "");
                }
                if (text.contains("#e") || text.contains("#E")) {
                    text = text.replaceAll("#E", ChatColor.YELLOW + "");
                    text = text.replaceAll("#e", ChatColor.YELLOW + "");
                }
                if (text.contains("#f") || text.contains("#F")) {
                    text = text.replaceAll("#F", ChatColor.WHITE + "");
                    text = text.replaceAll("#f", ChatColor.WHITE + "");
                }
                if (text.contains("#k") || text.contains("#K")) {
                    text = text.replaceAll("#K", ChatColor.MAGIC + "");
                    text = text.replaceAll("#k", ChatColor.MAGIC + "");
                }
                if (text.contains("#l") || text.contains("#L")) {
                    text = text.replaceAll("#L", ChatColor.BOLD + "");
                    text = text.replaceAll("#l", ChatColor.BOLD + "");
                }
                if (text.contains("#m") || text.contains("#M")) {
                    text = text.replaceAll("#M", ChatColor.STRIKETHROUGH + "");
                    text = text.replaceAll("#m", ChatColor.STRIKETHROUGH + "");
                }
                if (text.contains("#n") || text.contains("#N")) {
                    text = text.replaceAll("#N", ChatColor.UNDERLINE + "");
                    text = text.replaceAll("#n", ChatColor.UNDERLINE + "");
                }
                if (text.contains("#o") || text.contains("#O")) {
                    text = text.replaceAll("#O", ChatColor.ITALIC + "");
                    text = text.replaceAll("#o", ChatColor.ITALIC + "");
                }
                }else{
                	sender.sendMessage(ChatColor.RED + "You are not allowed to use chat colors!");
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
            //ILogDAO logDAO = ctx.getLogDAO();
            //logDAO.insertChatMessage(sender, channel, event.getMessage());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        //event.setCancelled(true);

        WebServer server = plugin.getWebServer();
        server.executeChatAction(new WebServer.ChatMessage(sender, channel, event.getMessage()));
    }
}
