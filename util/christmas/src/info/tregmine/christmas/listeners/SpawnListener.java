package info.tregmine.christmas.listeners;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class SpawnListener implements Listener {

	public void setHelmet(LivingEntity e)
	{
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET,1);
		ItemMeta meta = helmet.getItemMeta();
		meta.setDisplayName("Christmas Hat");
		helmet.setItemMeta(meta);

		LeatherArmorMeta lmeta = (LeatherArmorMeta) helmet.getItemMeta();
		lmeta.setColor(Color.RED);
		helmet.setItemMeta(lmeta);

		EntityEquipment ee = e.getEquipment();
		ee.setHelmet(helmet);
	}

	public void setChest(LivingEntity e)
	{
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE,1);
		ItemMeta meta = chest.getItemMeta();
		meta.setDisplayName("Christmas Top");
		chest.setItemMeta(meta);

		LeatherArmorMeta lmeta = (LeatherArmorMeta) chest.getItemMeta();
		lmeta.setColor(Color.RED);
		chest.setItemMeta(lmeta);

		EntityEquipment ee = e.getEquipment();
		ee.setChestplate(chest);
	}

	public void setLegs(LivingEntity e)
	{
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS,1);
		ItemMeta meta = leggings.getItemMeta();
		meta.setDisplayName("Christmas Pants");
		leggings.setItemMeta(meta);

		LeatherArmorMeta lmeta = (LeatherArmorMeta) leggings.getItemMeta();
		lmeta.setColor(Color.WHITE);
		leggings.setItemMeta(lmeta);

		EntityEquipment ee = e.getEquipment();
		ee.setLeggings(leggings);
	}

	public void setBoots(LivingEntity e)
	{
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS,1);
		ItemMeta meta = boots.getItemMeta();
		meta.setDisplayName("Christmas Boots");
		boots.setItemMeta(meta);

		LeatherArmorMeta lmeta = (LeatherArmorMeta) boots.getItemMeta();
		lmeta.setColor(Color.RED);
		boots.setItemMeta(lmeta);

		EntityEquipment ee = e.getEquipment();
		ee.setBoots(boots);
	}

	public void setHand(LivingEntity e)
	{
		ItemStack hand = new ItemStack(Material.CAKE,1);
		ItemMeta meta = hand.getItemMeta();
		meta.setDisplayName("Christmas Pudding");
		hand.setItemMeta(meta);

		EntityEquipment ee = e.getEquipment();
		ee.setItemInHand(hand);
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		if (e.getEntity().getWorld().getName() != "Christmas"){
			return;
		}

		if(e.getEntityType() == EntityType.GIANT ||
				e.getEntityType() == EntityType.VILLAGER ||
				e.getEntityType() == EntityType.WITCH){
			return;
		}
		
		if (e.getEntityType() == EntityType.ZOMBIE || 
				e.getEntityType() == EntityType.SKELETON) {
			setHelmet(e.getEntity());
			setChest(e.getEntity());
			setLegs(e.getEntity());
			setBoots(e.getEntity());
			setHand(e.getEntity());
		}else if (e.getEntity().getCustomName() == null){
			e.setCancelled(true);
		}
	}
}
