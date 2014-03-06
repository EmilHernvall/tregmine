package info.tregmine.bank;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.bank.Bank;
import info.tregmine.api.bank.Banker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static org.bukkit.ChatColor.*;

public class BankerListener implements Listener {

    private Tregmine plugin;

    public BankerListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void bankerInteract(PlayerInteractEntityEvent event)
    {
        if (!(event.getRightClicked() instanceof Villager)) {
            return;
        }

        Villager villager = (Villager) event.getRightClicked();

        if (!(villager.hasMetadata("banker"))) return;

        // We've just established that it is a banker, we don't want people to open banker inventories
        // so we cancel the inventory.
        event.setCancelled(true);

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        Banker banker = null;

        boolean containsPlayer = false;

        // if contains player, but the interacted villager is not the villager they are talking to
        // then give them an error.
        if (plugin.getBankersInUse().containsKey(player)) {
            Banker b = plugin.getBankersInUse().get(player);
            if (!(b.isSame(villager))) {
                player.sendMessage(DARK_GRAY + ChatColor.stripColor(villager.getCustomName()) +
                        GRAY + ": " + RED + "You are already talking to " + ChatColor.stripColor(b.getVillager().getCustomName()));
                return;
            }
            containsPlayer = true;
            banker = b;
        }

        // If it doesn't contain the player already, check all the bankers and see
        if (!containsPlayer) {
            for (Banker b : plugin.getBankers().values()) {
                if (!(b.isSame(villager))) {
                    continue;
                }

                // If it gets to this stage, then 'b' is the banker alternative of the villager.
                if (plugin.getBankersInUse().containsValue(b)) {
                    player.sendMessage(DARK_GRAY + ChatColor.stripColor(villager.getCustomName()) +
                            GRAY + ": " + RED + "I'm a bit busy right now, Come back later please!");
                    return;
                }

                banker = b;
            }
        }

        // Just as a backup, incase something happens...
        if (banker == null) {
            player.sendMessage(RED + "Something went wrong... Please try again later.");
            return;
        }

        // If we are at this stage, then we have received a successful right click on a banker
        // and that the banker is not busy, and can talk to player.

        Bank bank = banker.getBank();

        // Setup the player
        player.setChatState(TregminePlayer.ChatState.BANK);
        player.setVillagerTimer(plugin.getBankTimeoutCounter());


    }
}
