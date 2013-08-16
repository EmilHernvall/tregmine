package info.tregmine.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
//import org.bukkit.entity.HumanEntity;
//import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.Distance;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Lot;
import info.tregmine.zones.ZoneWorld;
import info.tregmine.zones.Zone;

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
        if (player.getRank().canModifyZones()) {
            return;
        }
        else if (player.hasFlag(TregminePlayer.Flags.HARDWARNED)) {
            event.setCancelled(true);
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());

        Block block = event.getBlock();
        Location location = block.getLocation();
        Point pos = new Point(location.getBlockX(), location.getBlockZ());

        Zone currentZone = player.getCurrentZone();
        if (currentZone == null || !currentZone.contains(pos)) {
            currentZone = world.findZone(pos);
            player.setCurrentZone(currentZone);
        }

        if (currentZone != null) {
            Zone.Permission perm = currentZone.getUser(player.getName());

            Lot lot = world.findLot(pos);
            if (lot != null) {
                if (perm == Zone.Permission.Owner && currentZone.isCommunist()) {
                    // Zone owners can modify lots in communist zones
                }
                else if (lot.isOwner(player.getName())) {
                    // Lot owners can always modify lots
                }
                else {
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are not allowed to break blocks in lot "
                            + lot.getName() + ".");
                    event.setCancelled(true);
                    return;
                }
                return;
            }

            // if everyone is allowed to build in this zone...
            if (currentZone.getDestroyDefault()) {
                // ...the only people that can't build are those that are banned
                if (perm != null && perm == Zone.Permission.Banned) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are banned from " + currentZone.getName()
                            + ".");
                }
            }

            // if this zone has limited building privileges...
            else {
                // ...we only allow builders and owners to make changes.
                if (perm == null
                        || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
                    player.setFireTicks(50);
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are not allowed to break blocks in "
                            + currentZone.getName() + ".");
                }
            }
        }
        else if (!player.getRank().canBuild()) {

            if (player.getMentor() != null) {
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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getRank().canModifyZones()) {
            return;
        }
        else if (player.hasFlag(TregminePlayer.Flags.HARDWARNED)) {
            event.setCancelled(true);
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());

        Block block = event.getBlock();
        Location location = block.getLocation();
        Point pos = new Point(location.getBlockX(), location.getBlockZ());

        Zone currentZone = player.getCurrentZone();
        if (currentZone == null || !currentZone.contains(pos)) {
            currentZone = world.findZone(pos);
            player.setCurrentZone(currentZone);
        }

        if (currentZone != null) {
            Zone.Permission perm = currentZone.getUser(player.getName());

            Lot lot = world.findLot(pos);
            if (lot != null) {
                if (perm == Zone.Permission.Owner && currentZone.isCommunist()) {
                    // Zone owners can modify lots in communist zones
                }
                else if (lot.isOwner(player.getName())) {
                    // Lot owners can always modify lots
                }
                else {
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are not allowed to break blocks in lot "
                            + lot.getName() + ".");
                    event.setCancelled(true);
                    return;
                }

                // we should only get here if the event is allowed, in which
                // case we don't need
                // any more checks.
                return;
            }

            // if everyone is allowed to build in this zone...
            if (currentZone.getPlaceDefault()) {
                // ...the only people that can't build are those that are banned
                if (perm != null && perm == Zone.Permission.Banned) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are banned from " + currentZone.getName()
                            + ".");
                }
            }
            // if this zone has limited building privileges...
            else {
                // ...we only allow builders and owners to make changes.
                if (perm == null
                        || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
                    player.setFireTicks(50);
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are not allowed to place blocks in "
                            + currentZone.getName() + ".");
                }
            }
        }
        else if (!player.getRank().canBuild()) {

            if (player.getMentor() != null) {
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
}
