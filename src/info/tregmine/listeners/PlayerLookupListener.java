package info.tregmine.listeners;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import info.tregmine.Tregmine;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;
import info.tregmine.database.ILogDAO;
import info.tregmine.api.TregminePlayer;

public class PlayerLookupListener implements Listener
{
    private Tregmine plugin;

    public PlayerLookupListener(Tregmine instance)
    {
        plugin = instance;
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

        if (!player.hasFlag(TregminePlayer.Flags.HIDDEN_LOCATION)) {
            if (player.getCountry() != null) {
                plugin.getServer().broadcastMessage(
                    ChatColor.DARK_AQUA + "Welcome " + player.getChatName() +
                    ChatColor.DARK_AQUA + " from " + player.getCountry() + "!");
            } else {
                plugin.getServer().broadcastMessage(
                    ChatColor.DARK_AQUA + "Welcome " + player.getChatName());
            }
        }

        if ("95.141.47.226".equals(player.getIp())) {
            return;
        }

        String aliasList = null;
        try (IContext ctx = plugin.createContext()) {
            ILogDAO logDAO = ctx.getLogDAO();
            Set<String> aliases = logDAO.getAliases(player);

            StringBuilder buffer = new StringBuilder();
            String delim = "";
            for (String name : aliases) {
                buffer.append(delim);
                buffer.append(name);
                delim = ", ";
            }

            aliasList = buffer.toString();

            if (aliases.size() > 1) {
                Tregmine.LOGGER.info("Aliases: " + aliasList);

                for (TregminePlayer current : plugin.getOnlinePlayers()) {
                    if (!current.getRank().canSeeAliases()) {
                        continue;
                    }
                    if (player.hasFlag(TregminePlayer.Flags.HIDDEN_LOCATION)) {
                        continue;
                    }
                    current.sendMessage(ChatColor.YELLOW
                            + "This player have also used names: " + aliasList);
                }
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}
