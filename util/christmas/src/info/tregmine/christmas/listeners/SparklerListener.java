package info.tregmine.christmas.listeners;

import info.tregmine.christmas.Fireworks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class SparklerListener implements Listener {

	public static void giveSparkler(Player p)
	{
		ItemStack sparkler = new ItemStack(Material.STICK,1);
		ItemMeta meta = sparkler.getItemMeta();
		meta.setDisplayName("§5Christmas Sparkler");
		sparkler.setItemMeta(meta);

		p.getInventory().addItem(sparkler);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		Player p = e.getPlayer();

		if(p.getWorld().getName() != "Christmas"){
			return;
		}

		if(p.getItemInHand().getType() != Material.STICK){
			return;
		}
		if(p.getItemInHand().getItemMeta().getDisplayName() != null){
			if(p.getItemInHand().getItemMeta().getDisplayName().contains("Christmas Sparkler")){
				Fireworks.Fireworks(p);
			}
		}
	}
}
