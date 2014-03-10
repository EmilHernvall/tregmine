package info.tregmine.bank;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.bank.Banker;
import org.bukkit.ChatColor;

public class BankerTimeoutRunnable implements Runnable {

    private Tregmine plugin;

    public BankerTimeoutRunnable(Tregmine plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (TregminePlayer player : plugin.getOnlinePlayers()) {
            if (player.getVillagerTime() > 0) {
                player.setVillagerTimer(player.getVillagerTime() - 1);

                if (player.getVillagerTime() == 0 && plugin.getBankersInUse().containsKey(player)) {
                    Banker banker = plugin.getBankersInUse().get(player);

                    player.sendMessage(ChatColor.DARK_GRAY + ChatColor.stripColor(banker.getVillager().getCustomName()) +
                            ChatColor.GRAY + ": " + ChatColor.RED + "Your time has expired with me!");

                    plugin.getBankersInUse().remove(player);
                    plugin.getAccountsInUse().remove(player);

                    player.setChatState(TregminePlayer.ChatState.CHAT);

                    // TODO: (Nearyby players) Message: "Banker <name> just became available!"
                }
            }
        }
    }
}