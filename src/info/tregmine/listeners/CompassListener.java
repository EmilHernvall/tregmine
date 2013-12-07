package info.tregmine.listeners;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.ItemStack;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.TargetBlock;
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
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) {
            return;
        }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        ItemStack heldItem = player.getItemInHand();
        if (heldItem.getType() != Material.COMPASS) {
            return;
        }

        World world = player.getWorld();

        if (player.getRank().canUseEnhancedCompass()) {

            float pitch = event.getPlayer().getLocation().getPitch();
            float yaw = event.getPlayer().getLocation().getYaw();

            TargetBlock targetCalc = new TargetBlock(event.getPlayer());
            Block target = targetCalc.getTargetBlock();

            if (target != null) {

                for (int i=0; i<100; i++) {

                    int landingType = world.getBlockAt(target.getX(),
                                                       target.getY() + i,
                                                       target.getZ()).getTypeId();

                    int landingAbove = world.getBlockAt(target.getX(),
                                                        target.getY() + i + 1,
                                                        target.getZ()).getTypeId();

                    if (landingType == 0 && landingAbove == 0) {
                        Location loc = target.getLocation();

                        loc.setX(loc.getX() + .5);
                        loc.setZ(loc.getZ() + .5);
                        loc.setY(loc.getY() + i);
                        loc.setPitch(pitch);
                        loc.setYaw(yaw);
                        if (loc.getY() < 255) {
                            player.teleport(loc);
                        }
                        break;
                    }
                }
            }
        }
        else if (player.getRank().canUseCompass()) {

            Block target = player.getDelegate().getTargetBlock(null, 300);

            Block b1 = world.getBlockAt(new Location(player.getWorld(),
                                                     target.getX(),
                                                     target.getY() + 1,
                                                     target.getZ()));

            Block b2 = world.getBlockAt(new Location(player.getWorld(),
                                                     target.getX(),
                                                     target.getY() + 2,
                                                     target.getZ()));

            if (mode == CompassMode.OnTop) {
                int top = world.getHighestBlockYAt(target.getLocation());
                Location loc = new Location(player.getWorld(),
                                target.getX() + 0.5, top, target.getZ() + 0.5,
                                player.getLocation().getYaw(),
                                player.getLocation().getPitch());
                player.teleport(loc);
            }
            else if (mode == CompassMode.Precision) {
                if ((b1.getType() == Material.AIR &&
                    (b2.getType() == Material.AIR ||
                     b2.getType() == Material.TORCH)) ||
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
                    player.sendMessage(ChatColor.RED +
                            "I think its a stupid idea to teleport in to a wall");
                }
            }
        }
    }
}
