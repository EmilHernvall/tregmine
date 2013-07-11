package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
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
            to.sendMessage(AQUA + from.getName() + " teleported to you!");
            from.teleport(to.getLocation());
            from.setNoDamageTicks(200);
            PotionEffect ef =
                    new PotionEffect(PotionEffectType.BLINDNESS, 60, 100);
            from.addPotionEffect(ef);
        }
    }

    public TeleportCommand(Tregmine tregmine)
    {
        super(tregmine, "tp");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 1) {
            return false;
        }
        if (!player.isTrusted()) {
            return false;
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

        if (target.isInvisible()) {
            return true;
        }

        if (target.hasTeleportShield() && !player.isAdmin()) {
            player.sendMessage(RED + target.getName() + AQUA
                    + "'s teloptical deflector absorbed all motion. "
                    + "Teleportation failed.");
            target.sendMessage(player.getName() + AQUA
                    + "'s teleportation spell "
                    + "cannot bypass your sophisticated defenses.");
            return true;
        }

        World sourceWorld = player.getWorld();
        World targetWorld = target.getWorld();
        if (player.isAdmin()) {
            player.sendMessage(AQUA + "You started teleport to "
                    + target.getName() + AQUA + " in " + BLUE
                    + targetWorld.getName() + ".");

            scheduler.scheduleSyncDelayedTask(tregmine, new TeleportTask(
                    target, player), 20 * 0);
            return true;
        }

        if (player.isGuardian()) {
            player.sendMessage(AQUA + "You started teleport to "
                    + target.getName() + AQUA + " in " + BLUE
                    + targetWorld.getName() + ".");

            scheduler.scheduleSyncDelayedTask(tregmine, new TeleportTask(
                    target, player), 20 * 1);
            return true;
        }

        String targetWorldName = targetWorld.getName();
        String sourceWorldName = sourceWorld.getName();
        if (sourceWorldName.equalsIgnoreCase(targetWorldName)) {

            double distance =
                    Distance.calc2d(player.getLocation(), target.getLocation());
            if (player.isDonator() && distance < 10000) {
                player.sendMessage(AQUA + "You started teleport to "
                        + target.getName() + AQUA + " in " + BLUE
                        + targetWorld.getName() + ".");

                scheduler.scheduleSyncDelayedTask(tregmine, new TeleportTask(
                        target, player), 20 * 30);
            }
            else if (player.isTrusted() && distance < 100) {
                player.sendMessage(AQUA + "You started teleport to "
                        + target.getName() + AQUA + " in " + BLUE
                        + targetWorld.getName() + ".");

                scheduler.scheduleSyncDelayedTask(tregmine, new TeleportTask(
                        target, player), 20 * 30);
            }
            else {
                player.sendMessage(RED
                        + "Your teleportation spell is not strong "
                        + "enough for the longer distances.");
            }

        }
        else {
            player.sendMessage(RED + "The user is in another world called "
                    + BLUE + targetWorld.getName() + ".");
        }

        return true;
    }
}
