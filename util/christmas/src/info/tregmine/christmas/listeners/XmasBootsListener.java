package info.tregmine.christmas.listeners;

import java.util.HashSet;
import java.util.Set;

import info.tregmine.christmas.ChristmasMain;
import info.tregmine.christmas.Fireworks;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class XmasBootsListener implements Listener {

	private ChristmasMain plugin;

	public XmasBootsListener(ChristmasMain instance)
	{
		this.plugin = instance;
	}

	public static Set<String> bootwearers = new HashSet<String>();

	@EventHandler
	public void onMoveWearingBoots(PlayerMoveEvent e) {

		Player p = e.getPlayer();

		if(p.getInventory().getBoots() == null){
			return;
		}

		if(p.getInventory().getBoots().getItemMeta() == null){
			return;
		}
		

		if(p.getInventory().getBoots().getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(p.getInventory().getBoots().getItemMeta().getDisplayName().contains("§4§k||§aChristmas Boots§4§k||")){
			bootwearers.add(p.getName());
			p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.GOLD_BLOCK, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK, 1);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 5));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000, 5));
		}
	}

	@EventHandler
	public void onMoveNotWearingBoots(PlayerMoveEvent e) {

		Player p = e.getPlayer();

		if(p.getInventory().getBoots() == null ||
				p.getInventory().getBoots().getItemMeta() == null ||
				p.getInventory().getBoots().getItemMeta().getDisplayName() == null || 
				!p.getInventory().getBoots().getItemMeta().getDisplayName().contains("§aChristmas Boots")){
			if(bootwearers.contains(p.getName())){
				p.removePotionEffect(PotionEffectType.SPEED);
				p.removePotionEffect(PotionEffectType.JUMP);
				bootwearers.remove(p.getName());
			}
		}
	}
	
	@EventHandler
	public void onBootwearerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(bootwearers.contains(p.getName())){
			p.removePotionEffect(PotionEffectType.SPEED);
			p.removePotionEffect(PotionEffectType.JUMP);
			bootwearers.remove(p.getName());
		}
	}
}