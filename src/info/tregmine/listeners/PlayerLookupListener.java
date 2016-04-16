package info.tregmine.listeners;

import java.util.Set;
import java.util.Date;

import info.tregmine.Tregmine;
import info.tregmine.api.PlayerReport;
import info.tregmine.api.PlayerReport.Action;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.ILogDAO;
import info.tregmine.database.IPlayerReportDAO;
import java.text.SimpleDateFormat;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
            event.getPlayer().kickPlayer(ChatColor.RED + "Something went wrong");
            Tregmine.LOGGER.info(event.getPlayer().getName() + " was not found " +
                    "in players map.");
            return;
        }

        try (IContext ctx = plugin.createContext()) {
            IPlayerReportDAO report = ctx.getPlayerReportDAO();
            List<PlayerReport> list = report.getReportsBySubject(player);
            for (PlayerReport i : list) {
                if (i.getAction() != Action.HARDWARN &&
                    i.getAction() != Action.SOFTWARN) {
                    continue;
                }
                Date validUntil = i.getValidUntil();
                if (validUntil == null) {
                    continue;
                }
                if (validUntil.getTime() < System.currentTimeMillis()) {
                    continue;
                }

                SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yy hh:mm:ss a");
                player.sendMessage(ChatColor.RED +
                        "[" + i.getAction() + "]" +
                        i.getMessage() + " - Valid until: " +
                        dfm.format(i.getTimestamp()));
                break;
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (!player.hasFlag(TregminePlayer.Flags.HIDDEN_ANNOUNCEMENT)) {
            if (player.hasFlag(TregminePlayer.Flags.INVISIBLE)) {
                for (TregminePlayer to : plugin.getOnlinePlayers()) {
                    if (to.getRank().canSeeHiddenInfo()) {
                        if (player.getCountry() != null) {
                            to.sendMessage(
                            		"Welcome " + player.getChatName() + " from " + player.getCountry() + "!");
                            to.sendMessage(player.getChatName() + ChatColor.DARK_AQUA + " is invisible!");
                        } else {
                            to.sendMessage(ChatColor.DARK_AQUA + "Welcome " + player.getChatName());
                            to.sendMessage(player.getChatName() + ChatColor.DARK_AQUA + " is invisible!");
                        }
                    }
                }
            } else {
                if (player.getCountry() != null && !player.hasFlag(TregminePlayer.Flags.HIDDEN_LOCATION)) {
                    plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Welcome " + player.getChatName() + ChatColor.DARK_AQUA + " from " + player.getCountry() + "!");
                } else {
                    plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "Welcome " + player.getChatName());
                }
            }
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
                    if (player.hasFlag(TregminePlayer.Flags.INVISIBLE) ||
                            player.hasFlag(TregminePlayer.Flags.HIDDEN_LOCATION)){
                        continue;
                    }
                }
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}
