package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.Notification;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.lore.Lore.Armor;
import info.tregmine.api.lore.Lore.Sword;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import static org.bukkit.Material.*;
import static org.bukkit.ChatColor.*;
import static org.bukkit.enchantments.Enchantment.*;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

public class RareDropListener implements Listener
{
    private Tregmine plugin;

    public RareDropListener(Tregmine plugin)
    {
        this.plugin = plugin;
    }

    private Random random = new Random();

    private class DropDelayTask extends BukkitRunnable
    {

        private TregminePlayer player;

        public DropDelayTask(TregminePlayer player)
        {
            this.player = player;
        }

        @Override
        public void run()
        {
            players.remove(player);
            this.cancel();
        }

    }

    /*
     * private Set<Material> swords = EnumSet.of(WOOD_SWORD, STONE_SWORD,
     * IRON_SWORD, GOLD_SWORD, DIAMOND_SWORD);
     */

    private static final List<TregminePlayer> players = Lists.newArrayList();

    private Set<EntityType> mobs = EnumSet.of(EntityType.CREEPER,
            EntityType.SKELETON, EntityType.ZOMBIE, EntityType.SPIDER,
            EntityType.ENDERMAN, EntityType.WITHER, EntityType.WITCH,
            EntityType.BAT, EntityType.GHAST, EntityType.MAGMA_CUBE,
            EntityType.PIG_ZOMBIE, EntityType.SLIME);

    private Material[] leather_armor = { LEATHER_HELMET, LEATHER_CHESTPLATE,
            LEATHER_LEGGINGS, LEATHER_BOOTS };

    private Material[] iron_armor = { IRON_HELMET, IRON_CHESTPLATE,
            IRON_LEGGINGS, IRON_BOOTS };

    private Material[] gold_armor = { GOLD_HELMET, GOLD_CHESTPLATE,
            GOLD_LEGGINGS, GOLD_BOOTS };

    private Material[] diamond_armor = { DIAMOND_HELMET, DIAMOND_CHESTPLATE,
            DIAMOND_LEGGINGS, DIAMOND_BOOTS };

    private Material[] getRandomArmorSet()
    {
        int i = random.nextInt(99);// 0-99 -> 100%
        if (i < 50) { // 50% (0-49)
            return leather_armor;
        }
        if (i >= 50 && i <= 74) { // 25% (50-74)
            return iron_armor;
        }
        if (i >= 75 && i <= 94) {// 20% (75 - 94)
            return gold_armor;
        }
        if (i >= 95 && i <= 99) {// 5% (95 - 99)
            return diamond_armor;
        }
        return leather_armor;
    }

    private Material getRandomArmor()
    {
        int i = random.nextInt(99);
        Material[] armor = getRandomArmorSet();

        if (i < 50) {
            return armor[0]; // returns helmet
        }
        if (i >= 50 && i <= 74) {
            return armor[3]; // returns boots
        }
        if (i >= 75 && i <= 94) {
            return armor[2]; // returns leggings
        }
        if (i >= 95 && i <= 99) {
            return armor[1]; // returns chest_plate
        }
        return armor[0];
    }

    private Material getRandomSword()
    {
        int i = random.nextInt(99);
        if (i < 50) { // 50%
            return WOOD_SWORD;
        }
        if (i >= 50 && i <= 74) { // 25%
            return STONE_SWORD;
        }
        if (i >= 75 && i <= 94) { // 20%
            return IRON_SWORD;
        }
        if (i >= 95 && i <= 99) { // 5%
            return DIAMOND_SWORD;
        }

        return WOOD_SWORD;
    }

    private Enchantment getRandomEnchantment(ItemStack stack)
    {
        if (!stack.getType().name().contains("SWORD")) {
            // Armor enchantments
            Enchantment[] ench =
                    { PROTECTION_FIRE, PROTECTION_FALL, PROTECTION_EXPLOSIONS,
                            WATER_WORKER, OXYGEN };
            int i = random.nextInt(99) + 1;
            if (i <= 40) {// 40
                return ench[0];
            }
            if (i > 40 && i <= 70) {// 30%
                return ench[1];
            }
            if (i > 70 && i <= 85) { // 15
                return ench[2];
            }
            if (i > 85 && i <= 95) { // 10
                return ench[3];
            }
            if (i > 95 && i <= 100) { // 5
                return ench[4];
            }
        }
        return null;
    }
    
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event)
    {
        if(!mobs.contains(event.getEntityType())) 
            return;
        if(event.getSpawnReason() != SpawnReason.SPAWNER)
            return;
        LivingEntity ent = event.getEntity();
        
        ent.setMetadata("spawned", new FixedMetadataValue(plugin, true));
    }

    @EventHandler
    public void onKill(EntityDeathEvent event)
    {
        // To make sure it's a valid mob
        if (!this.mobs.contains(event.getEntityType()))
            return;
        LivingEntity ent = event.getEntity();
        EntityDamageEvent cause = ent.getLastDamageCause();
        if (!(cause instanceof EntityDamageByEntityEvent))
            return;
        // these four lines make sure it was a player that killed the entity
        Entity damager = ((EntityDamageByEntityEvent) cause).getDamager();
        if (!(damager instanceof Player))
            return;
        
        
        if(ent.hasMetadata("spawned")){
            for(MetadataValue value: ent.getMetadata("spawned")){
                if(value.asBoolean()){
                    return;
                }
            }
        }

        TregminePlayer player = plugin.getPlayer((Player) damager);
        if (players.contains(player)) {
            return;
        }
        int i = random.nextInt(99) + 1;
        if (i == 35) {
            ent.getEquipment().setBootsDropChance(0.0F);
            ent.getEquipment().setLeggingsDropChance(0.0F);
            ent.getEquipment().setChestplateDropChance(0.0F);
            ent.getEquipment().setHelmetDropChance(0.0F);
            drop(player, event);
        }
    }

    public void drop(TregminePlayer player, EntityDeathEvent event)
    {
        Material mat = null;
        if (random.nextInt(99) >= 74) {
            mat = this.getRandomArmor(); // 25% armor
        }
        else {
            mat = this.getRandomSword(); // 75% sword
        }

        ItemStack stack = new ItemStack(mat, 1);
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = Lists.newArrayList();

        int itemLevel = random.nextInt(24) + 1; // Max level is 25

        lore.add(ChatColor.GOLD + "Level: " + ChatColor.WHITE + itemLevel);

        if ((random.nextInt(99) + 1) == 30) {
            int durability = random.nextInt(itemLevel * 8) + 1;
            if (durability > 200) {
                durability = 200;
            }
            lore.add(GOLD + "Bonus Durability: " + WHITE + durability);
            stack.setDurability((short) (stack.getDurability() + durability));
        }

        boolean armor = !mat.name().contains("SWORD");

        if (armor) {
            meta.setDisplayName(material(stack)
                    + Armor.getRandomByType(mat).toString());
            int armorBonus = random.nextInt(itemLevel) + 1;
            lore.add(ChatColor.GOLD + "Armor: " + ChatColor.WHITE + armorBonus);

            if ((random.nextInt(99) + 1) == 50) {
                Enchantment e = this.getRandomEnchantment(stack);
                boolean bool = e.canEnchantItem(stack);
                do {
                    e = getRandomEnchantment(stack);
                } while (!bool);
                stack.addEnchantment(e,
                        (e.getMaxLevel() == 1 ? 1 : e.getMaxLevel() - 1));
            }
        }
        else {
            int bonusDamage = random.nextInt(itemLevel * 2) + 1;
            meta.setDisplayName(material(stack) + Sword.getRandom().toString());
            lore.add(ChatColor.GOLD + "Damage: " + ChatColor.WHITE
                    + bonusDamage);
            if ((random.nextInt(49) + 1) == 30) { // 1 in 50 chance
                int critChance = random.nextInt(bonusDamage) + 1;
                lore.add(ChatColor.GOLD + "Critical Chance: " + ChatColor.WHITE
                        + critChance);
            }
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
        player.getInventory().addItem(stack);
        player.sendNotification(Notification.RARE_DROP,
                ChatColor.GOLD + "Congratulations, you've aquired an item from the rare drop table.");
        players.add(player);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                new DropDelayTask(player), 20 * 60 * 5L);
    }

    private String material(ItemStack stack)
    {
        String[] args = stack.getType().name().split("_");
        return ChatColor.AQUA + WordUtils.capitalizeFully(args[0]) + " ";
    }
}
