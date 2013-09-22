package info.tregmine.spleef.listeners;

import info.tregmine.spleef.Arena;
import info.tregmine.spleef.ArenaManager;
import info.tregmine.spleef.SettingsManager;
import info.tregmine.spleef.Spleef;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FallFromArena implements Listener {

    private final Spleef plugin;

    public FallFromArena(Spleef instance){
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
    public void onPlayerFallFromArena(PlayerMoveEvent e) {

        Player p = e.getPlayer(); 
        Location pLoc = p.getLocation();
        Block b = pLoc.getBlock();

        if (ArenaManager.getInstance().getArena(p) == null) return; 
        
        ConfigurationSection conf = SettingsManager.getInstance().get(getTeam(e.getPlayer()) + "");

        this.min = getLocation(conf.getConfigurationSection("minArenaLoc"));
        this.max = getLocation(conf.getConfigurationSection("maxArenaLoc"));

        int spleefMinX = min.getBlockX();
        int spleefMaxX = max.getBlockX();
        int spleefMinZ = min.getBlockZ();
        int spleefMaxZ = max.getBlockZ();
        int spleefSnow = min.getBlockY() - 1;

        Arena aNum = ArenaManager.getInstance().getArena(p);

        if(aNum.isStarted()){
            if (b.getLocation().getBlockX() >= spleefMinX && b.getLocation().getBlockX() <= spleefMaxX){
                if (b.getLocation().getBlockZ() >= spleefMinZ && b.getLocation().getBlockZ() <= spleefMaxZ){
                    if (b.getLocation().getBlockY() == spleefSnow){    
                        ArenaManager.getInstance().getArena(p).clearScore(p);
                        ArenaManager.getInstance().getArena(p).removePlayer(p, true);
                    }
                }
            }
        }
    }
}
