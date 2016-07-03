package info.tregmine.gamemagic;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.MathUtil;
import info.tregmine.events.PlayerMoveBlockEvent;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.*;
import org.bukkit.util.Vector;

public class GameMagic extends JavaPlugin implements Listener
{
    private Map<Integer, String> portalLookup;

    public Tregmine tregmine = null;

    public GameMagic()
    {
        portalLookup = new HashMap<Integer, String>();
    }

    @Override
    public void onEnable()
    {
        PluginManager pluginMgm = getServer().getPluginManager();

        // Check for tregmine plugin
        if (tregmine == null) {
            Plugin mainPlugin = pluginMgm.getPlugin("tregmine");
            if (mainPlugin != null) {
                tregmine = (Tregmine)mainPlugin;
            } else {
                Tregmine.LOGGER.info(getDescription().getName() + " " +
                         getDescription().getVersion() +
                         " - could not find Tregmine");
                pluginMgm.disablePlugin(this);
                return;
            }
        }

        // Register events
        pluginMgm.registerEvents(this, this);
        pluginMgm.registerEvents(new Gates(this), this);
        pluginMgm.registerEvents(new ButtonListener(this), this);
        pluginMgm.registerEvents(new SpongeCouponListener(this), this);

        //WorldCreator elva = new WorldCreator("elva");
        //elva.environment(World.Environment.NORMAL);
        //elva.createWorld();

        //WorldCreator treton = new WorldCreator("treton");
        //treton.environment(World.Environment.NORMAL);
        //treton.createWorld();

        //WorldCreator einhome = new WorldCreator("einhome");
        //einhome.environment(World.Environment.NORMAL);
        //einhome.createWorld();

        // Portal in tower of einhome
        portalLookup.put(-1488547832, "world");

        // Portal in elva
        portalLookup.put(-1559526734, "world");
        portalLookup.put(-1349166371, "treton");
        portalLookup.put(1371197620, "citadel");

        // portals in world
        portalLookup.put(-973919203, "treton");
        portalLookup.put(-777405698, "treton");
        portalLookup.put(1259780606, "citadel");
        portalLookup.put(690186900, "elva");
        portalLookup.put(209068875, "einhome");

        // portals in TRETON
        portalLookup.put(45939467, "world");
        portalLookup.put(-1408237330, "citadel");
        portalLookup.put(559131756, "elva");

        // portals in CITADEL
        portalLookup.put(1609346891, "world");
        portalLookup.put(-449465967, "treton");
        portalLookup.put(1112623336, "elva");

        // Shoot fireworks at spawn
//        BukkitScheduler scheduler = getServer().getScheduler();
//        scheduler.scheduleSyncRepeatingTask(this,
//                new Runnable() {
//            public void run() {
//                World world = GameMagic.this.getServer().getWorld("world");
//                Location loc = world.getSpawnLocation().add(0.5, 0, 0.5);
//
//                FireworksFactory factory = new FireworksFactory();
//                factory.addColor(Color.BLUE);
//                factory.addColor(Color.YELLOW);
//                factory.addType(FireworkEffect.Type.STAR);
//                factory.shot(loc);
//            }
//        }, 100L, 200L);
    }

    public static int locationChecksum(Location loc)
    {
        int checksum = (loc.getBlockX() + "," +
                loc.getBlockZ() + "," +
                loc.getBlockY() + "," +
                loc.getWorld().getName()).hashCode();
        return checksum;
    }

    private void gotoWorld(Player player, Location loc)
    {
        World world = loc.getWorld();
        Chunk chunk = world.getChunkAt(loc);
        world.loadChunk(chunk);
        if (world.isChunkLoaded(chunk)) {
            tregmine.getPlayer(player).teleportWithHorse(loc);
            player.sendMessage(ChatColor.YELLOW + "Thanks for traveling with " +
                    "TregPort!");
        } else {
            player.sendMessage(ChatColor.RED + "The portal needs some " +
                    "preparation. Please try again!");
        }
    }

    @EventHandler
    public void buttons(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.LEFT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_AIR) {

            return;
        }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        int checksum = locationChecksum(block.getLocation());
        if (!portalLookup.containsKey(checksum)) {
            return;
        }

        String worldName = portalLookup.get(checksum);

        Location loc = getServer().getWorld(worldName).getSpawnLocation();

        gotoWorld(player, loc);
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        if ("alpha".equals(event.getPlayer().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        if (event.getBucket() == Material.LAVA_BUCKET) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        if ("alpha".equals(event.getPlayer().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if ("alpha".equals(event.getPlayer().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() == SpawnReason.SPAWNER_EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event)
    {
        Location l = event.getBlock().getLocation();
        Block fence =
                event.getBlock()
                .getWorld()
                .getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ());

        if (fence.getType() == Material.FENCE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        Location l = event.getBlock().getLocation();
        Block block =
                event.getBlock()
                .getWorld()
                .getBlockAt(l.getBlockX(), l.getBlockY() - 1, l.getBlockZ());

        if (block.getType() == Material.OBSIDIAN) {
            event.setCancelled(false);
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveBlockEvent event)
    {
//Deprecated.
    }

	private void movePlayerBack(TregminePlayer player, Location movingFrom, Location movingTo)
	{
		Vector a = new org.bukkit.util.Vector(movingFrom.getX(),
				movingFrom.getY(),
				movingFrom.getZ());

		Vector b = new org.bukkit.util.Vector(movingTo.getX(),
				movingTo.getY(),
				movingTo.getZ());

		Vector diff = b.subtract(a);
		diff = diff.multiply(-5);

		Vector newPosVector = a.add(diff);

		Location newPos = new Location(player.getWorld(),
				newPosVector.getX(),
				newPosVector.getY(),
				newPosVector.getZ());

		player.teleportWithHorse(newPos);
	}

    @EventHandler
    public void onUseElevator(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        if (block.getType().equals(Material.STONE_BUTTON)) {
            Location loc = player.getLocation();
            World world = player.getWorld();
            Block standOn = world.getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ());

            if (Material.SPONGE.equals(standOn.getType())) {
                Location bLoc = block.getLocation();
                Block signBlock = world.getBlockAt(bLoc.getBlockX(), bLoc.getBlockY()+1, bLoc.getBlockZ());

                if(signBlock.getState() instanceof Sign) {
                    Sign sign = (Sign) signBlock.getState();

                    if (sign.getLine(0).contains("up")) {

                        sign.setLine(0, ChatColor.DARK_RED + "Elevator");
                        sign.setLine(2, ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "UP" + ChatColor.GOLD + "]");
                        sign.update(true);

                        player.sendMessage(ChatColor.GREEN + "Elevator Setup!");

                    }

                    else if (sign.getLine(0).equals(ChatColor.DARK_RED + "Elevator")
                            && sign.getLine(2).equals(ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "UP" + ChatColor.GOLD + "]")) {

                        int i = standOn.getLocation().getBlockY();

                        while (i < 255) {
                            i++;
                            Block sponge = event.getPlayer().getWorld().getBlockAt(standOn.getLocation().getBlockX(),  i, standOn.getLocation().getBlockZ());

                            if (sponge.getType().equals(Material.SPONGE)) {
                                i=256;
                                Location tp = sponge.getLocation();
                                tp.setY(tp.getBlockY() + 1.5);
                                tp.setZ(tp.getBlockZ() + 0.5);
                                tp.setX(tp.getBlockX() + 0.5);
                                tp.setPitch(player.getLocation().getPitch());
                                tp.setYaw(player.getLocation().getYaw());

                                player.teleport(tp);

                            }
                        }
                        player.sendMessage(ChatColor.AQUA + "Going up");
                    }

                    // sign.setLine(0, ChatColor.DARK_PURPLE + "Elevator");
                    // sign.setLine(2, ChatColor.GOLD + "[ " + ChatColor.DARK_GRAY + "UP" + ChatColor.GOLD + " ]");

                    if (sign.getLine(0).contains("down")) {

                        sign.setLine(0, ChatColor.DARK_RED + "Elevator");
                        sign.setLine(2, ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "DOWN" + ChatColor.GOLD + "]");
                        sign.update(true);

                        player.sendMessage(ChatColor.GREEN + "Elevator Setup!");

                    }

                    else if (sign.getLine(0).equals(ChatColor.DARK_RED + "Elevator")
                            && sign.getLine(2).equals(ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "DOWN" + ChatColor.GOLD + "]")) {


                        int i = standOn.getLocation().getBlockY();

                        while (i > 0) {
                            i--;
                            Block sponge = event.getPlayer().getWorld().getBlockAt(standOn.getLocation().getBlockX(),  i, standOn.getLocation().getBlockZ());

                            if (sponge.getType().equals(Material.SPONGE)) {
                                i=0;
                                Location tp = sponge.getLocation();
                                tp.setY(tp.getBlockY() + 1.5);
                                tp.setZ(tp.getBlockZ() + 0.5);
                                tp.setX(tp.getBlockX() + 0.5);
                                tp.setPitch(player.getLocation().getPitch());
                                tp.setYaw(player.getLocation().getYaw());

                                player.teleport(tp);

                            }
                        }
                        player.sendMessage(ChatColor.AQUA +"Going down");
                    }
                }
            }
        }
    }
}
