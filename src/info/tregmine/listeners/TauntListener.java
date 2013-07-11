package info.tregmine.listeners;

//import java.util.Random;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
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

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(ChatColor.GRAY + player.getName());

        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.stripColor(e.getDeathMessage()));

        meta.setLore(lore);

        item.setItemMeta(meta);

        player.getWorld().dropItemNaturally(player.getLocation(), item);

        e.setDeathMessage(death);
    }
}
