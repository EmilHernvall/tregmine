package info.tregmine.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import info.tregmine.Tregmine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ExpListener extends JavaPlugin implements Listener
{
    public static String itemName = "Tregmine XP Bottle";

    private Tregmine plugin;

    public ExpListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @Override
    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void xpFirstFill(PlayerInteractEvent event){
        if( event.getItem() == null ){
            return;
        }
        if( event.getItem().getType() != Material.GLASS_BOTTLE ){
            return;
        }
        if( (event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK) ){
            return;
        }
        if(event.getPlayer().getLevel() < 1){
            return;
        }

        // Creating the XPBottle
        List<String> lores = new ArrayList<String>();
        lores.add("XP Value: 1 Level");
        ItemStack XPBottle = new ItemStack(Material.EXP_BOTTLE, 1);
        ItemMeta XPMeta = XPBottle.getItemMeta();
        XPMeta.setLore(lores);
        XPMeta.setDisplayName(itemName);
        XPBottle.setItemMeta(XPMeta);

        HashMap<Integer, ItemStack> map = event.getPlayer().getInventory().addItem(XPBottle);
        if(map.containsValue(XPBottle)){
            return;
        }

        if( event.getItem() != null && event.getItem().getAmount() == 1){
            event.getPlayer().getInventory().remove(event.getItem());
        }else{
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
        event.getPlayer().setLevel(event.getPlayer().getLevel() - 1);
    }

    @EventHandler
    public void xpHigherFill(PlayerInteractEvent event){
        if( event.getItem() != null && event.getItem().getType() != Material.EXP_BOTTLE ){
            return;
        }
        if( event.getItem() != null && event.getItem().getItemMeta().getDisplayName() != itemName){
            return;
        }
        if( !(event.getAction()==Action.LEFT_CLICK_AIR) && !(event.getAction()==Action.LEFT_CLICK_BLOCK)){
            return;
        }
        if(event.getPlayer().getLevel() < 1){
            return;
        }
        String[] splitLore = null;
        try{
            splitLore = event.getItem().getItemMeta().getLore().toString().split(" ");
        }catch(NullPointerException e){
            return;
        }
        int level = Integer.parseInt(splitLore[2])+1;

        // Creating the XPBottle
        List<String> lores = new ArrayList<String>();
        lores.add("XP Value: "+ level +" Levels");
        ItemStack XPBottle = new ItemStack(Material.EXP_BOTTLE, 1);
        ItemMeta XPMeta = XPBottle.getItemMeta();
        XPMeta.setLore(lores);
        XPMeta.setDisplayName(itemName);
        XPBottle.setItemMeta(XPMeta);

        if(event.getItem().getAmount() == 1){
            event.getPlayer().getInventory().setItemInHand(XPBottle);
        }else{
            HashMap<Integer, ItemStack> map = event.getPlayer().getInventory().addItem(XPBottle);
            if(map.containsValue(XPBottle)){
                return;
            }
        }


        if(event.getItem().getAmount() == 1){
            event.getPlayer().getInventory().remove(event.getItem());
        }else{
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
        event.getPlayer().setLevel(event.getPlayer().getLevel() - 1);
    }

    @EventHandler
    public void xpEmpty(PlayerInteractEvent event){
        if( event.getItem() != null && event.getItem().getType() != Material.EXP_BOTTLE ){
            return;
        }
        if( event.getItem() != null && event.getItem().getItemMeta().getDisplayName() != itemName){
            return;
        }
        if( !(event.getAction()==Action.RIGHT_CLICK_AIR) && !(event.getAction()==Action.RIGHT_CLICK_BLOCK)){
            return;
        }
        String[] splitLore = null;
        try{
            splitLore = event.getItem().getItemMeta().getLore().toString().split(" ");
        }catch(NullPointerException e){
            return;
        }

        Vector expBottle = event.getPlayer().getEyeLocation().getDirection().multiply(2);
        ExperienceOrb expThrow = (ExperienceOrb)event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation().add(expBottle.getX(), expBottle.getY(), expBottle.getZ()), ExperienceOrb.class);
        expThrow.setVelocity(event.getPlayer().getEyeLocation().getDirection());

        int experience = Integer.parseInt(splitLore[2]);
        int start = event.getPlayer().getLevel();
        int current = start;
        int orbs = 0;
        for(int i = 0; i < experience; i++){
            if(current < 16){
                orbs = orbs + 17;
                current = current + 1;
            }else if( (current < 30) && (current > 15) ){
                orbs = orbs + (3 * current) - 28;
                current = current + 1;
            }else if(current > 29){
                orbs = orbs + (7 * current) - 148;
            }
        }
        expThrow.setExperience(orbs);
    }

    @EventHandler
    public void stopExp(ExpBottleEvent event){
        event.setExperience(0); // Stops MC's bottles
    }

}
