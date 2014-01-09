package info.tregmine.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class ToolCraftRegistry
{
    /*
     * Need to register every recipe of token + tool for the ToolCraft to recognise
     * ItemMeta doesn't matter at this stage of the game
     */
    public static void RegisterRecipes(Server server) {
        /*
         * Lumberaxes: Axe's
         * Veinminer: Pickaxe's
         * Area of Effect: Shovel's and Pickaxe's
         * Gravity : Diamond hoe
         */
        List<Material> items = new ArrayList<Material>();
        
        items.add(Material.DIAMOND_PICKAXE);
        items.add(Material.DIAMOND_AXE);
        items.add(Material.DIAMOND_SPADE);
        items.add(Material.DIAMOND_HOE);
        
        items.add(Material.GOLD_PICKAXE);
        items.add(Material.GOLD_AXE);
        items.add(Material.GOLD_SPADE);
        
        items.add(Material.IRON_PICKAXE);
        items.add(Material.IRON_AXE);
        items.add(Material.IRON_SPADE);
        
        items.add(Material.STONE_PICKAXE);
        items.add(Material.STONE_AXE);
        items.add(Material.STONE_SPADE);
        
        items.add(Material.WOOD_PICKAXE);
        items.add(Material.WOOD_AXE);
        items.add(Material.WOOD_SPADE);
        
        for (Material i : items) {
            ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.PAPER));
            recipe.addIngredient(Material.PAPER);
            recipe.addIngredient(i);
            server.addRecipe(recipe);
        }
        
    }
}
