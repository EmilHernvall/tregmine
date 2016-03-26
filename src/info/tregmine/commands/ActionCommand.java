package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.Rank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

public class ActionCommand extends AbstractCommand
{
    public ActionCommand(Tregmine tregmine)
    {
        super(tregmine, "action");
    }

    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(args[0]);
        for (int i = 1; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length == 0) {
            return false;
        }

        Server server = player.getServer();
        String channel = player.getChatChannel();
        String msg = argsToMessage(args);
        if(player.getRank() != Rank.RESIDENT && player.getRank() != Rank.SETTLER && player.getRank() != Rank.TOURIST && player.getRank() != Rank.UNVERIFIED){
        	if (msg.contains("#r") || msg.contains("#R")) {
        		msg = msg.replaceAll("#R", ChatColor.RESET + "");
                msg = msg.replaceAll("#r", ChatColor.RESET + "");
            }
        	if (msg.contains("#0")) {

                msg = msg.replaceAll("#0", ChatColor.BLACK + "");
            }
            if (msg.contains("#1")) {

                msg = msg.replaceAll("#1", ChatColor.DARK_BLUE + "");
            }
            if (msg.contains("#2")) {

                msg = msg.replaceAll("#2", ChatColor.DARK_GREEN + "");
            }
            if (msg.contains("#3")) {

                msg = msg.replaceAll("#3", ChatColor.DARK_AQUA + "");
            }
            if (msg.contains("#4")) {

                msg = msg.replaceAll("#4", ChatColor.DARK_RED + "");
            }
            if (msg.contains("#5")) {

                msg = msg.replaceAll("#5", ChatColor.DARK_PURPLE + "");
            }
            if (msg.contains("#6")) {

                msg = msg.replaceAll("#6", ChatColor.GOLD + "");
            }
            if (msg.contains("#7")) {

                msg = msg.replaceAll("#7", ChatColor.GRAY + "");
            }
            if (msg.contains("#8")) {

                msg = msg.replaceAll("#8", ChatColor.DARK_GRAY + "");
            }
            if (msg.contains("#9")) {

                msg = msg.replaceAll("#9", ChatColor.BLUE + "");
            }
            if (msg.contains("#a") || msg.contains("#A")) {
            	msg = msg.replaceAll("#A", ChatColor.GREEN + "");
                msg = msg.replaceAll("#a", ChatColor.GREEN + "");
            }
            if (msg.contains("#b") || msg.contains("#B")) {
                msg = msg.replaceAll("#B", ChatColor.AQUA + "");
                msg = msg.replaceAll("#b", ChatColor.AQUA + "");
            }
            if (msg.contains("#c") || msg.contains("#C")) {
                msg = msg.replaceAll("#C", ChatColor.RED + "");
                msg = msg.replaceAll("#c", ChatColor.RED + "");
            }
            if (msg.contains("#d") || msg.contains("#D")) {
                msg = msg.replaceAll("#D", ChatColor.LIGHT_PURPLE + "");
                msg = msg.replaceAll("#d", ChatColor.LIGHT_PURPLE + "");
            }
            if (msg.contains("#e") || msg.contains("#E")) {
                msg = msg.replaceAll("#E", ChatColor.YELLOW + "");
                msg = msg.replaceAll("#e", ChatColor.YELLOW + "");
            }
            if (msg.contains("#f") || msg.contains("#F")) {
                msg = msg.replaceAll("#F", ChatColor.WHITE + "");
                msg = msg.replaceAll("#f", ChatColor.WHITE + "");
            }
            if (msg.contains("#k") || msg.contains("#K")) {
                msg = msg.replaceAll("#K", ChatColor.MAGIC + "");
                msg = msg.replaceAll("#k", ChatColor.MAGIC + "");
            }
            if (msg.contains("#l") || msg.contains("#L")) {
                msg = msg.replaceAll("#L", ChatColor.BOLD + "");
                msg = msg.replaceAll("#l", ChatColor.BOLD + "");
            }
            if (msg.contains("#m") || msg.contains("#M")) {
                msg = msg.replaceAll("#M", ChatColor.STRIKETHROUGH + "");
                msg = msg.replaceAll("#m", ChatColor.STRIKETHROUGH + "");
            }
            if (msg.contains("#n") || msg.contains("#N")) {
                msg = msg.replaceAll("#N", ChatColor.UNDERLINE + "");
                msg = msg.replaceAll("#n", ChatColor.UNDERLINE + "");
            }
            if (msg.contains("#o") || msg.contains("#O")) {
                msg = msg.replaceAll("#O", ChatColor.ITALIC + "");
                msg = msg.replaceAll("#o", ChatColor.ITALIC + "");
            }
            }else{
            	player.sendMessage(ChatColor.RED + "You are not allowed to use chat colors!");
            }

        Collection<? extends Player> players = server.getOnlinePlayers();
        for (Player tp : players) {
            TregminePlayer to = tregmine.getPlayer(tp);
            if (!channel.equals(to.getChatChannel())) {
                continue;
            }
            
            boolean ignored;
            try (IContext ctx = tregmine.createContext()) {
                IPlayerDAO playerDAO = ctx.getPlayerDAO();
                ignored = playerDAO.doesIgnore(to, player);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
            if (player.getRank().canNotBeIgnored()) ignored = false;
            if (ignored == true) continue;

            to.sendMessage("* " + player.getChatName() + " " + WHITE + msg);
        }

        return true;
    }
}
