package info.tregmine.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

import info.tregmine.api.TregminePlayer;
import info.tregmine.Tregmine;

public class CompassListener implements Listener
{
    public enum CompassMode {
        OnTop, Precision;
    }

    private Tregmine plugin;
    private CompassMode mode = CompassMode.Precision;

    public CompassListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event)
    {
        TregminePlayer tregminePlayer = plugin.getPlayer(event.getPlayer());

        if (tregminePlayer.getRank().canUseCompass() &&
            event.getAnimationType() == PlayerAnimationType.ARM_SWING &&
            event.getPlayer().getItemInHand().getType() == Material.COMPASS) {

            Player player = event.getPlayer();
            Block target = player.getTargetBlock(null, 300);
            Block b1 = player.getWorld().getBlockAt(
                            new Location(player.getWorld(), target.getX(),
                                    target.getY() + 1, target.getZ()));
            Block b2 = player.getWorld().getBlockAt(
                            new Location(player.getWorld(), target.getX(),
                                    target.getY() + 2, target.getZ()));

            if (mode == CompassMode.OnTop) {
                int top = player.getWorld().getHighestBlockYAt(
                                target.getLocation());
                Location loc = new Location(player.getWorld(),
                                target.getX() + 0.5, top, target.getZ() + 0.5,
                                player.getLocation().getYaw(),
                                player.getLocation().getPitch());
                player.teleport(loc);
            }

            if (mode == CompassMode.Precision) {
                if ((b1.getType() == Material.AIR &&
                     (b2.getType() == Material.AIR ||
                      b2 .getType() == Material.TORCH)) ||
                    target.getY() == 127) {

                    Location loc = new Location(player.getWorld(),
                                                target.getX() + 0.5,
                                                target.getY() + 1,
                                                target.getZ() + 0.5,
                                                player.getLocation().getYaw(),
                                                player.getLocation().getPitch());
                    player.teleport(loc);
                }
                else {
                    player.sendMessage(ChatColor.RED
                            + "I think its a stupid idea to teleport in to a wall");
                }
            }
        }
    }
}
