package info.tregmine.bank;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.MathUtil;
import info.tregmine.events.PlayerMoveBlockEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static org.bukkit.ChatColor.RED;

public class BankerTimeoutListener implements Listener {

    private Tregmine plugin;

    public BankerTimeoutListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void villagerDistance(PlayerMoveBlockEvent event)
    {
        TregminePlayer player = event.getPlayer();

        if (!(plugin.getBankersInUse().containsKey(player))) {
            return;
        }

        if (!(MathUtil.calcDistance2d(player.getLocation(), plugin.getBankersInUse().get(player).getLocation()) > 10)) {
            return;
        }

        plugin.getBankersInUse().remove(player);
        plugin.getAccountsInUse().remove(player);

        player.sendMessage(RED + "You are no longer talking to a banker! You moved too far away.");
        player.setChatState(TregminePlayer.ChatState.CHAT);
    }
}