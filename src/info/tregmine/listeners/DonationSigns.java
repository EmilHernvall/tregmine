package info.tregmine.listeners;

import java.text.NumberFormat;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DonationSigns implements Listener {

    private final Tregmine plugin;

    public DonationSigns(Tregmine tregmine){
        this.plugin = tregmine;
    }

    @EventHandler
    public void donate(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock().getType().equals(Material.STONE_BUTTON) 
                    || event.getClickedBlock().getType().equals(Material.WOOD_BUTTON)){              
                Block block = event.getClickedBlock();
                Location signLocation = block.getLocation();
                Block signBlock = event.getPlayer().getWorld().getBlockAt(signLocation.getBlockX(), signLocation.getBlockY() - 1, signLocation.getBlockZ()); 
                if(signBlock.getState() instanceof Sign){         
                    Sign sign = (Sign) signBlock.getState();                      
                    if(sign.getLine(0).contains("donate")){  
                        try (IContext ctx = plugin.createContext()) {
                            IWalletDAO wallet = ctx.getWalletDAO();
                                Integer amount = Integer.parseInt(sign.getLine(1).trim());
                                NumberFormat format = NumberFormat.getNumberInstance();
                                TregminePlayer player = plugin.getPlayer(event.getPlayer());
                                TregminePlayer receiver = plugin.getPlayerOffline(sign.getLine(3).trim());
                                if(receiver != null){
                                    if(wallet.take(player, amount)){
                                        wallet.add(receiver, amount);
                                        player.sendMessage(ChatColor.DARK_AQUA + "You donated " + 
                                                ChatColor.GOLD + format.format(amount) + " Tregs " 
                                                + ChatColor.DARK_AQUA + "to " + receiver.getRealName());
                                    }else{
                                        player.sendMessage(ChatColor.RED + "You dont have enough Tregs!");
                                    }
                                }else{
                                    player.sendMessage(ChatColor.RED + "The player on the sign does not exist!");
                                }
                        }catch(DAOException error){
                            throw new RuntimeException(error);
                        }
                    }
                }
            }
        }
    }
}
