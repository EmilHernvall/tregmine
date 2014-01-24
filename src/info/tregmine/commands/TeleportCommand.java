package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import info.tregmine.Tregmine;
import info.tregmine.api.*;
import info.tregmine.api.math.MathUtil;
import info.tregmine.zones.Zone;

import java.util.List;

import org.bukkit.*;
import org.bukkit.entity.Horse;
import org.bukkit.potion.*;
import org.bukkit.scheduler.BukkitScheduler;

public class TeleportCommand extends AbstractCommand
{
    private static class TeleportTask implements Runnable
    {
        private TregminePlayer to;
        private TregminePlayer from;

        public TeleportTask(TregminePlayer to, TregminePlayer from)
        {
            this.to = to;
            this.from = from;
        }

        @Override
        public void run()
        {
            if (to.getCurrentZone() != null) {
                Zone toZone = to.getCurrentZone();
                Zone.Permission fromPermission = toZone.getUser(from);
                
                if (toZone.getEnterDefault()) {
                    if (fromPermission != null && fromPermission == Zone.Permission.Banned) { // Stop banned
                        from.sendMessage(RED + "You are banned from the zone " + to.getChatName() + RED + " is in!");
                        return;
                    }
                } else {
                    if (from.getRank().canModifyZones()) { // Allow Admins
                        
                    }else if (fromPermission == null) { // If doesn't have banned/allowed/maker/owner then stop as they are not allowed in
                        from.sendMessage(RED + "You are not allowed in the zone " + to.getChatName() + RED + " is in!");
                        return;
                    } else if (fromPermission == Zone.Permission.Banned) { // Stop banned, but let Allowed/Maker/Owner
                        from.sendMessage(RED + "You are banned from the zone " + to.getChatName() + RED + " is in!");
                        return;
                    }
                }
            }
            
            // Check position hasn't changed since task started.
            double distance = MathUtil.calcDistance2d(from.getLocation(), to.getLocation());
            
            if (distance > from.getRank().getTeleportDistanceLimit()) {
                from.sendMessage(RED + "Your teleportation spell is not strong enough for this long distance!");
                return;
            }

            from.teleportWithHorse(to.getLocation());
            from.setNoDamageTicks(200);
            
            if (!from.getRank().canDoHiddenTeleport()) {
                to.sendMessage(AQUA + from.getName() + " teleported to you!");
                PotionEffect ef =
                        new PotionEffect(PotionEffectType.BLINDNESS, 60, 100);
                from.addPotionEffect(ef);
            }
        }
    }

    public TeleportCommand(Tregmine tregmine)
    {
        super(tregmine, "tp");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        Rank rank = player.getRank();
        if (args.length != 1) {
            return false;
        }
        if (!rank.canTeleport()) {
            return true;
        }

        Server server = tregmine.getServer();
        BukkitScheduler scheduler = server.getScheduler();
        String name = args[0];

        List<TregminePlayer> candidates = tregmine.matchPlayer(name);
        if (candidates.size() != 1) {
            player.sendMessage(RED + "Can't find user.");
            return true;
        }

        TregminePlayer target = candidates.get(0);

        if (target.hasFlag(TregminePlayer.Flags.INVISIBLE)) {
            return true;
        }

        if (target.hasFlag(TregminePlayer.Flags.TPSHIELD) &&
            !player.getRank().canOverrideTeleportShield()) {
            player.sendMessage(RED + target.getName() + AQUA +
                    "'s teloptical deflector absorbed all motion. " +
                    "Teleportation failed.");
            target.sendMessage(player.getName() + AQUA +
                    "'s teleportation spell " +
                    "cannot bypass your sophisticated defenses.");
            return true;
        }

        World sourceWorld = player.getWorld();
        World targetWorld = target.getWorld();
        String targetWorldName = targetWorld.getName();
        String sourceWorldName = sourceWorld.getName();
        if (!sourceWorldName.equalsIgnoreCase(targetWorldName) &&
            !rank.canTeleportBetweenWorlds()) {
            player.sendMessage(RED + "The user is in another world called "
                    + BLUE + targetWorld.getName() + ".");
        }

        double distance = MathUtil.calcDistance2d(player.getLocation(),
                                          target.getLocation());
        if (distance <= rank.getTeleportDistanceLimit()) {
            player.sendMessage(AQUA + "You started teleport to " +
                    target.getName() + AQUA + " in " + BLUE +
                    targetWorld.getName() + ".");

            scheduler.scheduleSyncDelayedTask(
                    tregmine,
                    new TeleportTask(target, player),
                    rank.getTeleportTimeout());
        }
        else {
            player.sendMessage(RED
                    + "Your teleportation spell is not strong "
                    + "enough for the longer distances.");
        }

        return true;
    }
}
