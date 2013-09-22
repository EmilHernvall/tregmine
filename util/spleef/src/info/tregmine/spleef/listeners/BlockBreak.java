package info.tregmine.spleef.listeners;

import info.tregmine.api.Rank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.quadtree.Point;
import info.tregmine.spleef.Arena;
import info.tregmine.spleef.ArenaManager;
import info.tregmine.spleef.SettingsManager;
import info.tregmine.spleef.Spleef;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockBreak implements Listener {

    private final Spleef plugin;

    public BlockBreak(Spleef instance){
        this.plugin = instance;
    }

    private Location min, max;

    private Location getLocation(ConfigurationSection path) {
        return new Location(
                Bukkit.getServer().getWorld(path.getString("world")),
                path.getDouble("x"),
                path.getDouble("y"),
                path.getDouble("z")
                );
    }

    public int getTeam(Player p) {
        Arena a = ArenaManager.getInstance().getArena(p);
        return a.getID();
    }

    @EventHandler
    public void BreakBlockArena(PlayerInteractEvent e)
    {   
        Block b = e.getClickedBlock();
        Player p = e.getPlayer();

        if (ArenaManager.getInstance().getArena(p) != null){
            ConfigurationSection conf = SettingsManager.getInstance().get(getTeam(e.getPlayer()) + "");

            this.min = getLocation(conf.getConfigurationSection("minArenaLoc"));
            this.max = getLocation(conf.getConfigurationSection("maxArenaLoc"));

            int spleefMinX = min.getBlockX();
            int spleefMaxX = max.getBlockX();
            int spleefMinZ = min.getBlockZ();
            int spleefMaxZ = max.getBlockZ();
            int spleefSnow = min.getBlockY();

            Arena aNum = ArenaManager.getInstance().getArena(p);

            if(aNum.isStarted()){
                if(p.getItemInHand().getType().equals(Material.DIAMOND_SPADE) 
                        && p.getItemInHand().getItemMeta().getDisplayName().equals("§3§lShovel")){
                    p.getItemInHand().setDurability((short) 0);
                    //This check is necessary
                    if (b == null){
                        return;
                    }
                    if (b.getLocation().getBlockX() >= spleefMinX && b.getLocation().getBlockX() <= spleefMaxX){
                        if (b.getLocation().getBlockZ() >= spleefMinZ && b.getLocation().getBlockZ() <= spleefMaxZ){
                            if (b.getLocation().getBlockY() == spleefSnow){    
                                e.setCancelled(true);
                                b.setType(Material.AIR);
                                b.getDrops().clear();
                                ArenaManager.getInstance().getArena(p).addBlockBreak(p);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void BreakAttempt(BlockBreakEvent e)
    {   
        Player p = e.getPlayer();
        TregminePlayer player = plugin.tregmine.getPlayer(e.getPlayer());

        if (ArenaManager.getInstance().getArena(p) != null){
            e.setCancelled(true);
        }
        else{

            if (player.getRank().canModifyZones()) {
                return;
            }
            else if (player.hasFlag(TregminePlayer.Flags.HARDWARNED)) {
                e.setCancelled(true);
                return;
            }

            ZoneWorld world = plugin.tregmine.getWorld(player.getWorld());

            Block block = e.getBlock();
            Location location = block.getLocation();
            Point pos = new Point(location.getBlockX(), location.getBlockZ());

            Zone currentZone = player.getCurrentZone();
            if (currentZone == null || !currentZone.contains(pos)) {
                currentZone = world.findZone(pos);
                player.setCurrentZone(currentZone);
            }

            if (currentZone != null) {
                Zone.Permission perm = currentZone.getUser(player);
                if (!currentZone.getDestroyDefault()) {
                    if (perm == null
                            || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
                        player.setFireTicks(0);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
