package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.entity.Horse;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Rank;
import info.tregmine.api.math.Distance;

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
            Horse horse = null;

            if ((from.getVehicle() != null) && (from.getVehicle() instanceof Horse)){
                horse = (Horse)from.getVehicle();
            }

            if (horse != null){
                horse.eject();
                horse.teleport(to.getLocation());
                from.teleport(to.getLocation());
                horse.setPassenger(from.getDelegate());
            } else {
                from.teleport(to.getLocation());
            }

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

        double distance = Distance.calc2d(player.getLocation(),
                                          target.getLocation());
        if (distance < rank.getTeleportDistanceLimit()) {
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
