package info.tregmine.tools;

import info.tregmine.Tregmine;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ToolCraft implements Listener
{
    @SuppressWarnings("unused")
    private Tregmine plugin;

    public ToolCraft(Tregmine instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void CraftTool(PrepareItemCraftEvent event){
        if (event.isRepair()) return;
        
        List<ItemStack> items = new ArrayList<ItemStack>();
        boolean success = false;
        for(ItemStack a : event.getInventory().getContents()) {
            if(a.getType().equals(Material.AIR)) continue;
            items.add(a);
        } // Get an easier to handle list.

        if (items.size() != 3) return; // If not two items in inventory then Stop

        if (items.contains(ToolsRegistry.LumberAxe())) {
            for (ItemStack i : items){
                if(ToolsRegistry.LumberAxeAllowed.contains(i.getType())) {
                    ItemMeta iMeta = i.getItemMeta();
                    
                    List<String> lore = new ArrayList<String>();
                    lore.add(ToolsRegistry.LumberAxeLoreTag);
                    lore.add(ToolsRegistry.durabilityLoreTag);
                    iMeta.setLore(lore);
                    
                    i.setItemMeta(iMeta);
                    event.getInventory().setResult(i);
                    success = true;
                }
            }
        } else if (items.contains(ToolsRegistry.VeinMiner())){
            for (ItemStack i : items){
                if(ToolsRegistry.VeinMinerAllowed.contains(i.getType())) {
                    ItemMeta iMeta = i.getItemMeta();
                    
                    List<String> lore = new ArrayList<String>();
                    lore.add(ToolsRegistry.VeinMinerLoreTag);
                    lore.add(ToolsRegistry.durabilityLoreTag);
                    iMeta.setLore(lore);
                    
                    i.setItemMeta(iMeta);
                    event.getInventory().setResult(i);
                    success = true;
                }
            }
        } else if (items.contains(ToolsRegistry.GravityGun())){
            for (ItemStack i : items){
                if(ToolsRegistry.GravityGunAllowed.contains(i.getType())) {
                    ItemMeta iMeta = i.getItemMeta();
                    
                    List<String> lore = new ArrayList<String>();
                    lore.add(ToolsRegistry.GravityGunLoreTag);
                    lore.add(ToolsRegistry.durabilityLoreTag);
                    iMeta.setLore(lore);
                    
                    i.setItemMeta(iMeta);
                    event.getInventory().setResult(i);
                    success = true;
                }
            }
        } else if (items.contains(ToolsRegistry.AreaOfEffect())){
            for (ItemStack i : items){
                if(ToolsRegistry.AreaOfEffectAllowed.contains(i.getType())) {
                    ItemMeta iMeta = i.getItemMeta();
                    
                    List<String> lore = new ArrayList<String>();
                    lore.add(ToolsRegistry.AreaOfEffectLoreTag);
                    lore.add(ToolsRegistry.durabilityLoreTag);
                    iMeta.setLore(lore);
                    
                    i.setItemMeta(iMeta);
                    event.getInventory().setResult(i);
                    success = true;
                }
            }
        } else {
            return;
        }
        
        if (success == false) {
            event.getInventory().setResult(null);
        }
    }
    
}
