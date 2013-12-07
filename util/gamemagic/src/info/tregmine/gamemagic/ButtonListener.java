package info.tregmine.gamemagic;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * 
 * @author Joe Notaro (notaro1997)
 *
 */

public class ButtonListener implements Listener {

    private final GameMagic plugin;

    public ButtonListener(GameMagic instance){
        this.plugin = instance;
    }

    @EventHandler
    public void buttons(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock().getType().equals(Material.STONE_BUTTON)){
                Block block = event.getClickedBlock();
                final TregminePlayer player = plugin.tregmine.getPlayer(event.getPlayer());
                NumberFormat format = NumberFormat.getNumberInstance();
                int coordX = block.getX();
                int coordY = block.getY();
                int coordZ = block.getZ();
                try (IContext ctx = plugin.tregmine.createContext()) {
                    IWalletDAO wallet = ctx.getWalletDAO();
                    long balance = wallet.balance(player);
                    long difference = 15000 - balance;

                    // ---------- Bitchslap Button ----------
                    if(coordX == 599 && coordY == 20 && coordZ == -96){                     
                        if(wallet.take(player, 15000)){
                            plugin.getServer().broadcastMessage(ChatColor.YELLOW + "You got a bitchslap from " + player.getChatName());
                            player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
                        }else{
                            player.sendMessage(ChatColor.RED + "You need " + format.format(difference) + " more Tregs for this button. (15,000)");
                        }


                        // ---------- Hug Button ----------
                    } else if(coordX == 599 && coordY == 20 && coordZ == -99){
                        if(wallet.take(player, 15000)){
                            if(player.getName().equals("Camrenn")){
                                plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "You just got hugged by princess Camrenn!");
                                plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "(Be happy, but not too happy)");
                            }else{
                                plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "You got a hug from " + player.getChatName());
                                player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "You need " + format.format(difference) + " more Tregs for this button. (15,000)");
                        }

                        // ---------- Heaven And Hell Button ----------
                    } else if(coordX == 599 && coordY == 20 && coordZ == -108){
                        if(player.getWorld().getBlockAt(598, 17, -108).getType().equals(Material.STATIONARY_LAVA)){ 

                            player.getWorld().getBlockAt(598, 17, -108).setType(Material.STATIONARY_WATER);     

                            for(int y = 20; y < 34; y++){
                                for(int z = -102; z > -106; z--){
                                    player.getWorld().getBlockAt(614, y, z).setType(Material.AIR);
                                }
                            }

                            BukkitScheduler scheduler = plugin.getServer().getScheduler();
                            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run(){
                                    for(int y = 20; y < 34; y++){
                                        for(int z = -102; z > -106; z--){
                                            player.getWorld().getBlockAt(614, y, z).setTypeId(Material.WATER.getId(), false);
                                        }
                                    }
                                }
                            }, 1L);
                            
                        } else {

                            player.getWorld().getBlockAt(598, 17, -108).setType(Material.STATIONARY_LAVA);

                            for(int y = 20; y < 34; y++){
                                for(int z = -102; z > -106; z--){
                                    player.getWorld().getBlockAt(614, y, z).setType(Material.AIR);
                                }
                            } 

                            BukkitScheduler scheduler = plugin.getServer().getScheduler();
                            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run(){
                                    for(int y = 20; y < 34; y++){
                                        for(int z = -102; z > -106; z--){
                                            player.getWorld().getBlockAt(614, y, z).setTypeId(Material.LAVA.getId(), false);
                                        }
                                    }
                                }
                            }, 1L);

                        }
                     // ---------- Sponge Coupon Button ----------
                    } else if (coordX == 599 && coordY == 20 && coordZ == -102) {
                        if(wallet.take(player, 25000)){
                            ItemStack item = new ItemStack(Material.PAPER, 1);
                            PlayerInventory inventory = player.getInventory();
                            ItemMeta meta = item.getItemMeta();
                            List<String> lore = new ArrayList<String>();
                            lore.add(info.tregmine.api.lore.Created.PURCHASED.toColorString());
                            lore.add(ChatColor.WHITE + "By: " + player.getName());
                            lore.add(ChatColor.WHITE + "Value: 25,000 Tregs");
                            meta.setLore(lore);
                            meta.setDisplayName(ChatColor.GREEN + "DIRT -> SPONGE Coupon");
                            item.setItemMeta(meta);
                            inventory.addItem(item);
                            player.sendMessage(ChatColor.AQUA + "You received 1 coupon for 25,000 Tregs.");
                        }else{
                            player.sendMessage(ChatColor.RED + "You need at least 25,000 Tregs for this button!");
                        }
                    }
                } catch (DAOException error) {
                    throw new RuntimeException(error);
                }
            }
        }
    }
}


