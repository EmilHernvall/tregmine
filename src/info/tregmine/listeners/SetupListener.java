package info.tregmine.listeners;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Queue;

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
import info.tregmine.commands.MentorCommand;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Rank;
import info.tregmine.api.PlayerReport;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;
import info.tregmine.database.IPlayerReportDAO;

public class SetupListener implements Listener
{
    private Tregmine plugin;

    public SetupListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player == null) {
            event.getPlayer().kickPlayer("Something went wrong");
            Tregmine.LOGGER.info(event.getPlayer().getName() + " was not found " +
                    "in players map.");
            return;
        }

        if (player.getChatState() != TregminePlayer.ChatState.SETUP) {
            return;
        }

        Tregmine.LOGGER.info("[SETUP] " + player.getChatName() + " is a new player!");

        player.sendMessage(ChatColor.YELLOW + "Welcome to Tregmine!");
        player.sendMessage(ChatColor.YELLOW + "This is an age restricted server. " +
                "Please confirm that you are 13 years or older by typing \"yes\". " +
                "If you are younger than 13, please leave this server, or " +
                "type \"no\" to quit.");
        player.sendMessage(ChatColor.YELLOW + "You will not be able to talk " +
                "to other players until you've verified your age.");
        player.sendMessage(ChatColor.RED + "Are you 13 years or older?");
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
        player.sendMessage(text);

        Tregmine.LOGGER.info("[SETUP] <" + player.getChatName() + "> " + text);

        try (IContext ctx = plugin.createContext()) {
            Server server = plugin.getServer();
            if ("yes".equalsIgnoreCase(text)) {
                player.sendMessage(ChatColor.GREEN + "You have now joined Tregmine " +
                        "and can talk with other players! Say Hi! :)");
                player.setChatState(TregminePlayer.ChatState.CHAT);
                player.setRank(Rank.TOURIST);

                IPlayerDAO playerDAO = ctx.getPlayerDAO();
                playerDAO.updatePlayer(player);

                Tregmine.LOGGER.info("[SETUP] " + player.getChatName() +
                        " joined the server.");

                server.broadcastMessage(ChatColor.GREEN + "Welcome to Tregmine, " +
                        player.getChatName() + ChatColor.GREEN + "!");

                MentorCommand.findMentor(plugin, player);
            }
            else if ("no".equalsIgnoreCase(text)) {
                player.sendMessage(ChatColor.YELLOW + "Unfortunately Tregmine has an " +
                        "age limit of 13 years and older. Please come back after " +
                        "your 13th birthday! :)");

                player.setChatState(TregminePlayer.ChatState.CHAT);
                player.setFlag(TregminePlayer.Flags.CHILD);
                player.setRank(Rank.TOURIST);

                IPlayerDAO playerDAO = ctx.getPlayerDAO();
                playerDAO.updatePlayer(player);

                Tregmine.LOGGER.info("[SETUP] " + player.getChatName() +
                        " has been marked as a child.");
            }
            else {
                player.sendMessage(ChatColor.RED + "Please say \"yes\" or \"no\". " +
                        "You will not be able to talk to other players until you do.");
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}
