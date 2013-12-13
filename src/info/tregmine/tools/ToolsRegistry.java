package info.tregmine.tools;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ToolsRegistry
{
    
    public final static String durabilityLoreTag = "1000/1000";
    
    /**
     * Creates and returns an ItemStack for the token which will create a Lumberaxe
     * @return ItemStack
     */
    public static ItemStack LumberAxe() {
        ItemStack Lumberaxe = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = Lumberaxe.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Token: Lumberaxe!");
        
        List<String> lores = new ArrayList<String>();
        lores.add(ChatColor.AQUA + "Shapeless craft with a tool of your choice!");
        lores.add(ChatColor.GOLD + "Axe only");
        
        meta.setLore(lores);
        
        Lumberaxe.setItemMeta(meta);
        return Lumberaxe;
    }
    public final static String LumberAxeLoreTag = ChatColor.GREEN + "[SpecialTool] Lumberaxe!";
    public final static Set<Material> LumberAxeAllowed =
            EnumSet.of(Material.WOOD_AXE,
                       Material.STONE_AXE,
                       Material.IRON_AXE,
                       Material.GOLD_AXE,
                       Material.DIAMOND_AXE);
    
    /**
     * Creates and returns an ItemStack for the token which will create a vein mining tool
     * @return ItemStack
     */
    public static ItemStack VeinMiner() {
        ItemStack Veinminer = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = Veinminer.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Token: Vein Miner!");
        
        List<String> lores = new ArrayList<String>();
        lores.add(ChatColor.AQUA + "Shapeless craft with a tool of your choice!");
        lores.add(ChatColor.GOLD + "Pickaxe only");
        
        meta.setLore(lores);
        
        Veinminer.setItemMeta(meta);
        return Veinminer;
    }
    public final static String VeinMinerLoreTag = ChatColor.GREEN + "[SpecialTool] Vein miner!";
    public final static Set<Material> VeinMinerAllowed =
            EnumSet.of(Material.WOOD_PICKAXE,
                       Material.STONE_PICKAXE,
                       Material.IRON_PICKAXE,
                       Material.GOLD_PICKAXE,
                       Material.DIAMOND_PICKAXE);
    
    /**
     * Creates and returns an ItemStack for the token which will create an GravityGun tool
     * @return ItemStack
     */
    public static ItemStack GravityGun() {
        ItemStack Aoe = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = Aoe.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Token: GravityGun!");
        
        List<String> lores = new ArrayList<String>();
        lores.add(ChatColor.AQUA + "Shapeless craft with a diamond hoe!");
        lores.add(ChatColor.GOLD + "Diamond Hoe");
        
        meta.setLore(lores);
        
        Aoe.setItemMeta(meta);
        return Aoe;
    }
    public final static String GravityGunLoreTag = ChatColor.GREEN + "[SpecialTool] Gravity Gun!";
    public final static Set<Material> GravityGunAllowed =
            EnumSet.of(Material.DIAMOND_HOE);
    
    /**
     * Creates and returns an ItemStack for the token which will create an Area of Effect tool
     * @return ItemStack
     */
    public static ItemStack AreaOfEffect() {
        ItemStack Aoe = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = Aoe.getItemMeta();
        
        meta.setDisplayName(ChatColor.GREEN + "Token: Area of Effect!");
        
        List<String> lores = new ArrayList<String>();
        lores.add(ChatColor.AQUA + "Shapeless craft with a tool of your choice!");
        lores.add(ChatColor.GOLD + "Pickaxe / Shovel");
        
        meta.setLore(lores);
        
        Aoe.setItemMeta(meta);
        return Aoe;
    }
    public final static String AreaOfEffectLoreTag = ChatColor.GREEN + "[SpecialTool] Area of effect! (3x3x1)";
    public final static Set<Material> AreaOfEffectAllowed =
            EnumSet.of(Material.WOOD_PICKAXE,
                       Material.STONE_PICKAXE,
                       Material.IRON_PICKAXE,
                       Material.GOLD_PICKAXE,
                       Material.DIAMOND_PICKAXE,
                       Material.WOOD_SPADE,
                       Material.STONE_SPADE,
                       Material.IRON_SPADE,
                       Material.GOLD_SPADE,
                       Material.DIAMOND_SPADE);
}
