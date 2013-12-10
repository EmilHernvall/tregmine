package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public class TabListener implements Listener//, TabCompleter
{
    private Tregmine plugin;

    public TabListener(Tregmine instance)
    {
        this.plugin = instance;
    }


    @EventHandler
    public void tabcomplete(PlayerChatTabCompleteEvent e) {

        TregminePlayer p = plugin.getPlayer(e.getPlayer());
        if (p.getRank().canGetTrueTab()) {
            return;
        }

        e.getTabCompletions().clear();
        String[] args = e.getChatMessage().split(" ");
        if (args.length == 0) {
            return;
        }

        List<String> nonOps = new ArrayList<String>();
        List<String> result = new ArrayList<String>();

        for (TregminePlayer player : plugin.getOnlinePlayers()) {
            if (!player.hasFlag(TregminePlayer.Flags.INVISIBLE)) {
                nonOps.add(player.getName());
            }
        }

        for (String name : nonOps) {
            if (name.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                result.add(name);
            }
        }

        e.getTabCompletions().addAll(result);
    }
}


