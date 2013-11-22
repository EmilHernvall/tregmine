package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.Distance;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.entity.HumanEntity;
//import org.bukkit.entity.Player;

public class ZoneBlockListener implements Listener
{
    private Tregmine plugin;

    public ZoneBlockListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        Location location = event.getBlock().getLocation();
        
        if (!player.hasBlockPermission(location, true)) {
            event.setCancelled(true);
        }

        if (!player.getRank().canBuild() &&
                player.getMentor() != null) {
            
                TregminePlayer mentor = player.getMentor();
                Location a = player.getLocation();
                Location b = mentor.getLocation();
                
                if (Distance.calc2d(a, b) > 50) {
                    player.sendMessage(ChatColor.YELLOW + "You have to stay within " +
                            "a 50 block radius of your mentor in order to build.");
                    mentor.sendMessage(ChatColor.YELLOW + "Your student has to stay " +
                            "within a 50 block radius of you in order to build.");
                    event.setCancelled(true);
                    return;
                }
        } else {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        Location location = event.getBlock().getLocation();
        
        if (!player.hasBlockPermission(location, true)) {
            event.setCancelled(true);
        }

        if (!player.getRank().canBuild() &&
                player.getMentor() != null) {
            
                TregminePlayer mentor = player.getMentor();
                Location a = player.getLocation();
                Location b = mentor.getLocation();
                
                if (Distance.calc2d(a, b) > 50) {
                    player.sendMessage(ChatColor.YELLOW + "You have to stay within " +
                            "a 50 block radius of your mentor in order to build.");
                    mentor.sendMessage(ChatColor.YELLOW + "Your student has to stay " +
                            "within a 50 block radius of you in order to build.");
                    event.setCancelled(true);
                    return;
                }
        } else {
            event.setCancelled(true);
            return;
        }
    }
}
