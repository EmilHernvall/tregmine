package info.tregmine.gamemagic;

import info.tregmine.api.TregminePlayer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpongeCouponListener implements Listener {

    private final GameMagic plugin;

    public SpongeCouponListener(GameMagic instance){
        this.plugin = instance;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){

        TregminePlayer player = plugin.tregmine.getPlayer(event.getPlayer());

        if (event.getPlayer().getItemInHand().getType().equals(Material.PAPER) && event.getBlock().getType().equals(Material.DIRT)) {
            ItemStack paper = player.getItemInHand();
            ItemMeta meta = paper.getItemMeta();
            if(meta.hasDisplayName()){
                if(meta.getDisplayName().equals(ChatColor.GREEN + "DIRT -> SPONGE Coupon")){
                    event.getBlock().setType(Material.SPONGE);
                    paper.setAmount(paper.getAmount() - 1);
                    if(paper.getAmount() <= 0) {
                        event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
