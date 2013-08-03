package info.tregmine.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import info.tregmine.api.TregminePlayer;
import info.tregmine.Tregmine;

public class BoxFillBlockListener implements Listener
{
    private Tregmine plugin;

    public BoxFillBlockListener(Tregmine tregmine)
    {
        this.plugin = tregmine;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (!player.getRank().canFill()) {
            return;
        }

        if (player.getItemInHand().getType() != Material.WOOD_SPADE) {
            return;
        }

        Block block = event.getClickedBlock();
        int count;

        try {
            count = player.getFillBlockCounter();
        } catch (Exception e) {
            count = 0;
        }

        if (count == 0) {
            player.setFillBlock1(block);
            player.setFillBlock2(null);
            event.getPlayer().sendMessage("First block set");
            player.setFillBlockCounter(1);
        }

        if (count == 1) {
            player.setFillBlock2(block);
            event.getPlayer().sendMessage("Second block set");
            player.setFillBlockCounter(0);
        }
    }
}
