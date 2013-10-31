package info.tregmine.listeners;

//import java.util.Random;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import info.tregmine.Tregmine;
import info.tregmine.api.Insult;

public class TauntListener implements Listener
{
    //private Tregmine plugin;

    public TauntListener(Tregmine instance)
    {
        //this.plugin = instance;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        
        if (!(event instanceof PlayerDeathEvent)) {
            return;
        }

        Player p = (Player) event.getEntity();
        PlayerDeathEvent e = (PlayerDeathEvent) event;

        String death =
                ChatColor.DARK_GRAY + "DIED - " + p.getName() + " "
                        + Insult.random();
        
        World w = p.getWorld();
        
        Entity a = w.spawnEntity(p.getLocation(), EntityType.ZOMBIE);
        if(!(a instanceof Zombie)) return;
        
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(p.getName());
        meta.setDisplayName(ChatColor.GRAY + p.getName());
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.stripColor(event.getDeathMessage()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        Zombie zomb = (Zombie) a;
        zomb.setCustomName(p.getDisplayName());
        zomb.setCustomNameVisible(true);
        
        EntityEquipment ee = zomb.getEquipment();
        ee.setHelmet(item);
        ee.setHelmetDropChance(1F);
        
        zomb.setCanPickupItems(false);
    }
}
