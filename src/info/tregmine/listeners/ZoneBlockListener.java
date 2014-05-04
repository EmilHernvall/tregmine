package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.*;
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

		if (	location.getWorld().getName().equalsIgnoreCase(plugin.getRulelessWorld().getName()) ||
				location.getWorld().getName().equalsIgnoreCase(plugin.getRulelessEnd().getName()) ||
				location.getWorld().getName().equalsIgnoreCase(plugin.getRulelessNether().getName())) {
			if (player.hasBlockPermission(location, false)) return;
		} else {
			if (player.hasBlockPermission(location, true)) return;
		}

        /*if (!player.getRank().canBuild() &&
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
        }*/

        event.setCancelled(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 5, 2));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        Location location = event.getBlock().getLocation();

		if (	location.getWorld().getName().equalsIgnoreCase(plugin.getRulelessWorld().getName()) ||
				location.getWorld().getName().equalsIgnoreCase(plugin.getRulelessEnd().getName()) ||
				location.getWorld().getName().equalsIgnoreCase(plugin.getRulelessNether().getName())) {
			if (player.hasBlockPermission(location, false)) return;
		} else {
			if (player.hasBlockPermission(location, true)) return;
		}

        /*if (!player.getRank().canBuild() &&
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
        }*/

        event.setCancelled(true);
    }

    // Stops trampling of crops
    @EventHandler
    public void onBlockTrample(PlayerInteractEvent event)
    {
        // Check it's a physical event (by moving)
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        // Check it's farmland (otherwise it would stop pressure plates and such)
        if (event.getClickedBlock() == null || !event.getClickedBlock().getType().equals(Material.DIRT)) {
            return;
        }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        Location location = event.getClickedBlock().getLocation();

        // Check for block permission
        if (!player.hasBlockPermission(location, true)) {
            event.setCancelled(true);
        }
    }

    // Keeps liquids in the zone/lot they were originally placed in.
    @EventHandler
    public void onLiquid(BlockFromToEvent event)
    {
        ZoneWorld world = plugin.getWorld(event.getBlock().getWorld());

        Zone zone = world.findZone(event.getBlock().getLocation());
        Zone zoneTo = world.findZone(event.getToBlock().getLocation());

        // If liquid flows into a zone from wilderness
        if (zone == null &&
                zoneTo != null) {
            event.setCancelled(true);
            return;
        }

        // If liquid flows from a zone into another zone
        if (zone != null &&
                zoneTo != null &&
                !zone.getName().equalsIgnoreCase(zoneTo.getName())) {
            event.setCancelled(true);
            return;
        }

        Lot lot = world.findLot(event.getBlock().getLocation());
        Lot lotTo = world.findLot(event.getToBlock().getLocation());

        // If liquid flows from a lot into a lot
        if (lot != null &&
                lotTo != null &&
                !lot.getName().equalsIgnoreCase(lotTo.getName())) {
            event.setCancelled(true);
            return;
        }

        // If liquid flows from a zone into a lot
        if (lot == null &&
                lotTo != null) {
            event.setCancelled(true);
            return;
        }

        // If liquid flows from a lot into a zone
        if (lot != null &&
                lotTo == null) {
            event.setCancelled(true);
            return;
        }
    }
}
