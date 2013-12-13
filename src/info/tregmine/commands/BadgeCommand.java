package info.tregmine.commands;

import info.tregmine.Tregmine;
import info.tregmine.api.Badge;
import info.tregmine.api.TregminePlayer;
import java.util.Map;
import org.bukkit.ChatColor;

public class BadgeCommand extends AbstractCommand
{
    public BadgeCommand(Tregmine tregmine)
    {
        super(tregmine, "badge");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String args[])
    {
        if (args.length == 1 && "list".equalsIgnoreCase(args[0])) {

            Map<Badge, Integer> badges = player.getBadges();
            if(badges.isEmpty()){
                player.sendMessage(ChatColor.AQUA +
                        "You currently have no badges!");
                return true;
            }

            for(Map.Entry<Badge, Integer> badge : badges.entrySet()){
                player.sendMessage(ChatColor.AQUA + badge.getKey().name() +
                        " - Level " + badge.getValue());
            }

        } else if (args.length == 2 && "list".equalsIgnoreCase(args[0])) {

            if (!player.getRank().canViewPlayersBadge()) {
                return true;
            }

            TregminePlayer target = tregmine.getPlayerOffline(args[0]);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "Could not find player: "
                        + ChatColor.YELLOW + args[0]);
                return true;
            }
            Map<Badge, Integer> badges = target.getBadges();
            if(badges.isEmpty()){
                player.sendMessage(target.getChatName() + ChatColor.AQUA +
                        " currently has no badges!");
                return true;
            }

            for(Map.Entry<Badge, Integer> badge : badges.entrySet()){
                player.sendMessage(ChatColor.AQUA + badge.getKey().name() +
                        " - Level " + badge.getValue());
            }

        }
        return true;
    }
}
