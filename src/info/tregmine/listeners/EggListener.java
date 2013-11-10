package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class EggListener implements Listener
{
    private Tregmine plugin;

    public EggListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onEggThrown(PlayerEggThrowEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getRank().canModifyZones()) {
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());
        if (world == null) {
            return;
        }

        Location location = player.getLocation();
        Point pos = new Point(location.getBlockX(), location.getBlockZ());

        Zone zone = world.findZone(pos);
        if (zone == null) {
            return;
        }

        Zone.Permission perm = zone.getUser(player);
        Lot lot = world.findLot(pos);

        if(zone != null && lot == null){ // If Zone but not Lot
            if(perm == Zone.Permission.Allowed ||
               perm == Zone.Permission.Maker ||
               perm == Zone.Permission.Owner){ // Checks if player is Allowed/Maker/Owner
                return;
            }
            if(zone.isPvp()){ // If pvp is on then let it fly... It's an awesome weapon
                return;
            }
            if(zone.getDestroyDefault()){ // If people can destroy, Then we will let them throw eggs.
                return;
            }
            // If still at this point then they are not allowed to throw eggs so BURN THEM ALIVE and stop the egg from hatching, I could've burnt the chicken? but thats harsh its only a chick!
            player.setFireTicks(50);
            event.setHatching(false);
            player.sendMessage(ChatColor.RED + "["
                    + zone.getName() + "] "
                    + "You are not allowed to throw eggs in "
                    + zone.getName() + ".");
            return;
        }else if(zone != null && lot != null){ // If lot in a zone
            if (perm == Zone.Permission.Owner && zone.isCommunist()) { // Zone owners can modify lots in communist zones
                return;
            }
            if (lot.isOwner(player)) { // Lot owners can throw eggs if they so please
                return;
            }
            // If still at this point then they are not allowed to throw eggs so BURN THEM ALIVE and stop the egg from hatching, I could've burnt the chicken? but thats harsh its only a chick!
            player.setFireTicks(50);
            event.setHatching(false);
            player.sendMessage(ChatColor.RED + "["
                    + zone.getName() + "] "
                    + "You are not allowed to throw eggs in "
                    + zone.getName() + ".");
            return;
        }else{ // No Zone (obviously No Lot) so just end.
            return;
        }
    }
}
