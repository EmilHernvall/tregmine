package info.tregmine.gamemagic;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBWalletDAO;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * 
 * @author Joe Notaro (notaro1997)
 *
 */

public class ButtonListener implements Listener{

	private final Tregmine plugin;
	
	public ButtonListener(Tregmine instance){
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void Buttons(PlayerInteractEvent event){
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			TregminePlayer player = this.plugin.getPlayer(event.getPlayer());
			NumberFormat format = NumberFormat.getNumberInstance();
			int x = block.getX();
			int y = block.getY();
			int z = block.getZ();
			Connection connection = null;
			try {
				connection = ConnectionPool.getConnection();
				DBWalletDAO wallet = new DBWalletDAO(connection);
				long balance = wallet.balance(player);
				long difference = 15000 - balance;
				if (balance >= 0){

					if(block.getType().equals(Material.STONE_BUTTON)){
						// ---------- Bitchslap button ----------
						if(x == 599 && y == 20 && z == -96){                     
							if(wallet.take(player, 15000)){
								plugin.getServer().broadcastMessage(ChatColor.YELLOW + "You got a bitchslap from " + player.getName() + "!");
								player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
							}else{
								player.sendMessage(ChatColor.RED + "You need " + format.format(difference) + " more Tregs for this button. (15000)");
							}
						}

						// ---------- Hug button ----------
						if(x == 599 && y == 20 && z == -99){
							if(wallet.take(player, 15000)){
								if(player.getName().equals("Camrenn")){
									plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "You just got hugged by princess Camrenn!");
									plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "(Be happy, but not too happy)");
								}else{
									plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "You got a hug from " + player.getName() + "!");
									player.sendMessage(ChatColor.GREEN + "15,000 Tregs were removed from your wallet.");
								}
							}else{
								player.sendMessage(ChatColor.RED + "You need " + format.format(difference) + " more Tregs for this button. (15000)");
							}
						}

						// ---------- Heaven and Hell button ----------
						if(x == 599 && y == 20 && z == -108){
							if(Material.DIRT.equals(player.getWorld().getBlockAt(600, 18, -108).getType())){

								player.getWorld().getBlockAt(600, 18, -108).setType(Material.LAPIS_BLOCK);
								
								for(int y1 = 20; y1 < 34; y1++){
									for(int z1 = -102; z1 < -106; z1++){
										player.getWorld().getBlockAt(614, y1, z1).setType(Material.AIR);
										player.getWorld().getBlockAt(614, y1, z1).setType(Material.WATER);
									}
								}
/*
								player.getWorld().getBlockAt(614, 33, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 32, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 31, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 30, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 29, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 28, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 27, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 26, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 25, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 24, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 23, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 22, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 21, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 20, -105).setType(Material.AIR);

								player.getWorld().getBlockAt(614, 33, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 32, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 31, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 30, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 29, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 28, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 27, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 26, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 25, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 24, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 23, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 22, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 21, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 20, -104).setType(Material.AIR);

								player.getWorld().getBlockAt(614, 33, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 32, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 31, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 30, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 29, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 28, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 27, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 26, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 25, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 24, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 23, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 22, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 21, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 20, -103).setType(Material.AIR);

								player.getWorld().getBlockAt(614, 33, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 32, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 31, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 30, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 29, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 28, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 27, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 26, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 25, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 24, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 23, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 22, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 21, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 20, -102).setType(Material.AIR);

								player.getWorld().getBlockAt(614, 33, -105).setType(Material.WATER);
								player.getWorld().getBlockAt(614, 33, -104).setType(Material.WATER);
								player.getWorld().getBlockAt(614, 33, -103).setType(Material.WATER);
								player.getWorld().getBlockAt(614, 33, -102).setType(Material.WATER);
*/
							} else if(Material.LAPIS_BLOCK.equals(player.getWorld().getBlockAt(600, 18, -108).getType())){

								player.getWorld().getBlockAt(600, 18, -108).setType(Material.DIRT);
								
								for(int y1 = 20; y1 < 34; y1++){
									for(int z1 = -102; z1 < -106; z1++){
										player.getWorld().getBlockAt(614, y1, z1).setType(Material.AIR);
										player.getWorld().getBlockAt(614, y1, z1).setType(Material.LAVA);
									}
								}
								
/*
								player.getWorld().getBlockAt(614, 33, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 32, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 31, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 30, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 29, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 28, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 27, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 26, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 25, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 24, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 23, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 22, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 21, -105).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 20, -105).setType(Material.AIR);

								player.getWorld().getBlockAt(614, 33, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 32, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 31, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 30, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 29, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 28, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 27, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 26, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 25, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 24, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 23, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 22, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 21, -104).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 20, -104).setType(Material.AIR);

								player.getWorld().getBlockAt(614, 33, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 32, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 31, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 30, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 29, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 28, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 27, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 26, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 25, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 24, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 23, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 22, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 21, -103).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 20, -103).setType(Material.AIR);

								player.getWorld().getBlockAt(614, 33, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 32, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 31, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 30, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 29, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 28, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 27, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 26, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 25, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 24, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 23, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 22, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 21, -102).setType(Material.AIR);
								player.getWorld().getBlockAt(614, 20, -102).setType(Material.AIR);

								player.getWorld().getBlockAt(614, 33, -105).setType(Material.LAVA);
								player.getWorld().getBlockAt(614, 33, -104).setType(Material.LAVA);
								player.getWorld().getBlockAt(614, 33, -103).setType(Material.LAVA);
								player.getWorld().getBlockAt(614, 33, -102).setType(Material.LAVA);
*/
							}
						}
					}

				}else{
					player.sendMessage(ChatColor.RED + "You don't seem to have a wallet, ask an admin for assistance.");
				}

			} catch (SQLException error) {
				throw new RuntimeException(error);
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
					}
				}
			}
		}
	}
}
