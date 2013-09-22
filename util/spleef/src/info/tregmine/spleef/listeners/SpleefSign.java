package info.tregmine.spleef.listeners;

import info.tregmine.spleef.MessageManager;
import info.tregmine.spleef.Spleef;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpleefSign implements Listener {


    private final Spleef plugin;

    public SpleefSign(Spleef instance){
        this.plugin = instance;
    }
    
    @EventHandler
    public void onSetupSign(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Block signBlock = e.getClickedBlock();
        if(signBlock.getState() instanceof Sign) {
            Sign sign = (Sign) signBlock.getState();

            if (sign.getLine(0).equals(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Spleef" + ChatColor.DARK_GRAY + "]")) {

                int id = -1;
                try { id = Integer.parseInt(sign.getLine(2)); }
                catch (Exception ex) {
                    MessageManager.getInstance().severe(e.getPlayer(), "'" + sign.getLine(2) + "' is not a valid number!");
                    signBlock.breakNaturally();
                    return;
                }
                e.getPlayer().performCommand("spleef join " + id);
            }
            
            else if (sign.getLine(0).contains("spleef")) {

                sign.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Spleef" + ChatColor.DARK_GRAY + "]");
                String st = sign.getLine(2).replaceAll("\\s","");
                sign.setLine(2, st);
                sign.update(true);

                player.sendMessage(ChatColor.GREEN + "Sign Setup!");

            }
        }
    }
}