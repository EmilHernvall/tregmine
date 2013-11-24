package info.tregmine.listeners;

//import java.util.Random;

import info.tregmine.Tregmine;
import info.tregmine.api.Insult;
import info.tregmine.api.TregminePlayer;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
//import org.bukkit.inventory.meta.ItemMeta;

public class TauntListener implements Listener
{
    private Tregmine plugin;

    public TauntListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        if (!(event instanceof PlayerDeathEvent)) {
            return;
        }

        Player player = (Player) event.getEntity();
        PlayerDeathEvent e = (PlayerDeathEvent) event;

        String death =
                ChatColor.DARK_GRAY + "DIED - " + player.getName() + " "
                        + Insult.random();
        
        EntityDamageEvent damage = player.getLastDamageCause();
        DamageCause cause = damage.getCause();
        
        boolean playerCause = false;
        if (damage.getEntity() instanceof Player) playerCause = true;
        
        Location location = player.getLocation();
        
        Point pos = new Point(location.getBlockX(), location.getBlockZ());
        ZoneWorld world = plugin.getWorld(player.getWorld());
        TregminePlayer player2 = plugin.getPlayer(player);
        
        Zone currentZone = player2.getCurrentZone();
        if (currentZone == null || !currentZone.contains(pos)) {
            currentZone = world.findZone(pos);
            player2.setCurrentZone(currentZone);
        }
        
        if (currentZone == null) {
            e.setDeathMessage(death);
            return;
        }
        
        if (cause == DamageCause.ENTITY_ATTACK &&
                currentZone.isPvp() &&
                playerCause == true) {
            
            World w = player.getWorld();
            Entity a = w.spawnEntity(player.getLocation(), EntityType.ZOMBIE);
            if(!(a instanceof Zombie)) return;
            
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(player.getName());
            meta.setDisplayName(ChatColor.GRAY + player.getName());
    
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.stripColor(e.getDeathMessage()));
            meta.setLore(lore);
    
            item.setItemMeta(meta);
            
            Zombie zomb = (Zombie) a;
            zomb.setCustomName(player.getDisplayName());
            zomb.setCustomNameVisible(true);
            
            EntityEquipment ee = zomb.getEquipment();
            ee.setHelmet(item);
            ee.setHelmetDropChance(1F);
            
            zomb.setCanPickupItems(false);
        }
        
        e.setDeathMessage(death);
    }
}
