package info.tregmine.death;

//import java.util.Random;

import info.tregmine.Tregmine;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class DeathEntity implements Listener  {
	private final Tregmine plugin;

	public DeathEntity(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	
	@EventHandler
	public void onEntityDeath (EntityDeathEvent event) {
		 if (event instanceof PlayerDeathEvent) {
			 Player player = (Player) event.getEntity();
             PlayerDeathEvent e = (PlayerDeathEvent) event;
             
             
 			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
 			SkullMeta meta = (SkullMeta) item.getItemMeta();
 			meta.setOwner(player.getName());
 			meta.setDisplayName(ChatColor.GRAY + player.getName());
 			item.setItemMeta(meta);
            player.getWorld().dropItemNaturally(player.getLocation(), item);
             
             e.setDeathMessage(ChatColor.DARK_GRAY + "DIED - " + player.getName() + " " + Insult.random());
         }
	}

	@EventHandler
	public void onEntityExplode (EntityExplodeEvent event) {
		if(event.getEntity() instanceof Player) {
		}
	}

	@EventHandler
	public void onEntityDamage (EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
		}
	}
}