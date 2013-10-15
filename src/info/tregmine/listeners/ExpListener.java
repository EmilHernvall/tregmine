package info.tregmine.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import info.tregmine.Tregmine;

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
        TregminePlayer TGPlayer = this.plugin.getPlayer(event.getPlayer());
        if(TGPlayer.getRank.canBottleXP == false){
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
        if(level > 30){
            event.getPlayer().sendMessage(ChatColor.AQUA + "XP in a bottle is capped at 30 levels per bottle!");
            return;
        }
        TregminePlayer TGPlayer = this.plugin.getPlayer(event.getPlayer());
        if(TGPlayer.getRank.canBottleXP == false){
            return;
        }
        
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
        event.setCancelled(true);
        Vector expBottle = event.getPlayer().getEyeLocation().getDirection().multiply(2);
        ExperienceOrb expThrow = (ExperienceOrb)event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation().add(expBottle.getX(), expBottle.getY(), expBottle.getZ()), ExperienceOrb.class);
        expThrow.setVelocity(event.getPlayer().getEyeLocation().getDirection());
        
        int experience = Integer.parseInt(splitLore[2]);
        event.getPlayer().setLevel(event.getPlayer().getLevel()+experience);
        ItemStack remove = event.getPlayer().getItemInHand();
        if(remove.getAmount() == 1){
            event.getPlayer().getInventory().remove(remove);
        }else{
            remove.setAmount(remove.getAmount()-1);
        }
    }

}
