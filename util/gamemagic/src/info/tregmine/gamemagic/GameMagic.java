package info.tregmine.gamemagic;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.zip.CRC32;

import org.bukkit.FireworkEffect;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.inventory.Inventory;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import info.tregmine.database.ConnectionPool;

public class GameMagic extends JavaPlugin implements Listener
{
    private Map<Integer, String> portalLookup;

    public GameMagic()
    {
        portalLookup = new HashMap<Integer, String>();
    }

    @Override
    public void onEnable()
    {
        PluginManager pluginMgm = getServer().getPluginManager();
        pluginMgm.registerEvents(this, this);

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
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this,
            new Runnable() {
                public void run() {
                    World world = GameMagic.this.getServer().getWorld("world");
                    Location loc = world.getSpawnLocation();

                    FireworksFactory factory = new FireworksFactory();
                    factory.addColor(Color.BLUE);
                    factory.addColor(Color.YELLOW);
                    factory.addType(FireworkEffect.Type.STAR);
                    factory.shot(loc);
                }
            }, 100L, 200L);
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
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                player.sendMessage(ChatColor.RED + "You are carrying too much " +
                                   "for the portal's magic to work.");
                return;
            }
        }

        World world = loc.getWorld();
        Chunk chunk = world.getChunkAt(loc);
        world.loadChunk(chunk);
        if (world.isChunkLoaded(chunk)) {
            player.teleport(loc);
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
}
