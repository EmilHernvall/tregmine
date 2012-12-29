package info.tregmine.basiccommands;


import info.tregmine.api.TregminePlayer;

//import net.minecraft.server.v1_4_6.Item;

import org.bukkit.ChatColor;
import org.bukkit.Color;
//import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
//import org.bukkit.entity.Firework;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Firework;
//import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
//import org.bukkit.event.entity.EntityChangeBlockEvent;
//import org.bukkit.event.hanging.HangingEvent;
//import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
//import org.bukkit.event.player.PlayerListener;
//import org.bukkit.inventory.meta.FireworkMeta;

public class BasicCommandsBlock implements Listener {
	private final BasicCommands plugin;

	public BasicCommandsBlock(BasicCommands instance) {
		this.plugin = instance;
		plugin.getServer();
	}

	
	public void colorFirework(TregminePlayer player, Color c, int button, Block block) {
		
		if (info.tregmine.api.math.Checksum.block(block) == button) {
			if (!this.plugin.firework.containsKey(player.getName())) {
				this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
			}
			
			info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());
			
			FireWork.addColor(c);
			player.sendMessage(FireWork.colorToString(c) + " added");
			
			PlayerInventory inv = player.getInventory();
			inv.addItem(FireWork.getAsStack(5));
		}

	
	}
	
	
	
	@EventHandler 
	public void fireWorkButton(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			TregminePlayer player = this.plugin.tregmine.getPlayer(event.getPlayer());
			Block block = event.getClickedBlock();

			this.colorFirework(player, Color.WHITE, 	-1845477288, 	block);
			this.colorFirework(player, Color.AQUA, 		-337925479, 	block);
			this.colorFirework(player, Color.PURPLE, 	1169626330, 	block);
			this.colorFirework(player, Color.BLUE, 		-1617789157, 	block);
			this.colorFirework(player, Color.FUCHSIA, 	-1541627631, 	block);
			this.colorFirework(player, Color.BLACK, 	38377012, 		block);
			this.colorFirework(player, Color.ORANGE, 	1938955831, 	block);
			this.colorFirework(player, Color.YELLOW, 	-1934654641, 	block);
			this.colorFirework(player, Color.LIME, 		-1738141136, 	block);
			this.colorFirework(player, Color.GRAY, 		-1345114126, 	block);
			this.colorFirework(player, Color.SILVER, 	-1148600621, 	block);
			this.colorFirework(player, Color.GREEN, 	-952087116, 	block);			
			this.colorFirework(player, Color.RED, 		-755573611, 	block);
			this.colorFirework(player, Color.MAROON, 	-1469174797, 	block);
			this.colorFirework(player, Color.TEAL, 		1967285645, 	block);
			this.colorFirework(player, Color.NAVY, 		-2131168146, 	block);
			this.colorFirework(player, Color.OLIVE, 	-2038282101, 	block);

			//			-1845477288 WHITE 		| silver *
			//			-337925479 CYAN 		| Aqua *
			//			1169626330 PURPLE 		| Purple *
			//			-1617789157 BLUE 		| BLUE*
			//			-1469174797 BROWN 		| MAROON
			//			38377012 BLACK			| BLACK
			//			1938955831 ORANGE		| ORANGE
			//			1967285645 MAGENTA		| TEAL
			//			-2131168146 LIGHT BLUE	| NAVY
			//			-1934654641 YELLOW *	| YELLOW
			//			-1738141136 GREEN 		| LIME *
			//			-1541627631 PINK 		| FUCHSIA *
			//			-1345114126 DARK GREY	| GRAY *
			//			-1148600621 GREY		| SILVER *
			//			-952087116 DARK GREEN 	| GREEN
			//			-755573611 RED 			| RED
			//			-2038282101	OLIVE		| OLIVE



			// Reset colors -565613610
			if (info.tregmine.api.math.Checksum.block(block) == -565613610) {
//					this.plugin.fireWorkEffect.put(player.getName(), null);
//					this.plugin.fireWorkMeta.put(player.getName(), null);
//					this.plugin.fireWork.put(player.getName(), null);
//					this.plugin.property.put(player.getName(), null);
					player.sendMessage(ChatColor.AQUA + "You have now rested everyting and need to start from scratch");
			}



			if (info.tregmine.api.math.Checksum.block(block) == 656425969) {
				player.sendMessage("you got 5 fireworks");
//				PlayerInventory inv = player.getInventory();
//				inv.addItem(this.plugin.fireWork.get(player.getName()));

			}
			//			Firework meta = (Firework) item.getItemMeta();




			//			Location loc = player.getLocation();
			//			Firework f1 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
			//			FireworkMeta meta = f1.getFireworkMeta();
			//			meta.setPower(1);
			//			meta.addEffect(this.plugin.fireWorkEffect.get(player.getName()).build());
			//			f1.setFireworkMeta(meta);
			//			f1.getFireworkMeta().setDisplayName("test");

		}

	}



	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			Location loc = block.getLocation();

			if (player.getItemInHand().getType() == Material.BOOK) {

				player.sendMessage(ChatColor.DARK_AQUA + "Type: " + ChatColor.AQUA + block.getType().toString() + " ("+ ChatColor.BLUE + block.getType().getId() + ChatColor.DARK_AQUA + ")" );
				player.sendMessage(ChatColor.DARK_AQUA +"Data: " + ChatColor.AQUA + (int) block.getData() );
				player.sendMessage(
						ChatColor.RED +"X" +
								ChatColor.WHITE + ", " +
								ChatColor.GREEN + "Y" +
								ChatColor.WHITE + ", " +
								ChatColor.BLUE + "Z" +
								ChatColor.WHITE + ": " +
								ChatColor.RED + loc.getBlockX() +
								ChatColor.WHITE + ", " +
								ChatColor.GREEN + loc.getBlockY() +
								ChatColor.WHITE + ", " +
								ChatColor.BLUE + loc.getBlockZ()
						);

				try {
					player.sendMessage(ChatColor.DARK_AQUA +"Biome: " + ChatColor.AQUA + block.getBiome().toString() );
				} catch (Exception e) {
					player.sendMessage(ChatColor.DARK_AQUA +"Biome: " + ChatColor.AQUA + "NULL" );
				}


				//				player.sendMessage(ChatColor.DARK_AQUA +"Light: " + ChatColor.AQUA + (int) block.getFace(BlockFace.UP).getLightLevel() );
				player.sendMessage(ChatColor.DARK_AQUA +"Hash2d: " + ChatColor.AQUA + info.tregmine.api.math.Checksum.flat(block) );
				player.sendMessage(ChatColor.DARK_AQUA +"Hash3d: " + ChatColor.AQUA + info.tregmine.api.math.Checksum.block(block) );
				plugin.log.info("BlockHash2d: " +  info.tregmine.api.math.Checksum.flat(block) );
				plugin.log.info("BlockHash3d: " +  info.tregmine.api.math.Checksum.block(block) );
				plugin.log.info("POS: " +  loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
			}
		}
	}    
}
