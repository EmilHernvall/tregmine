package info.tregmine.basiccommands;


import info.tregmine.api.TregminePlayer;

//import net.minecraft.server.v1_4_6.Item;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
//import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.meta.FireworkMeta;

public class BasicCommandsBlock implements Listener {
	private final BasicCommands plugin;

	public BasicCommandsBlock(BasicCommands instance) {
		this.plugin = instance;
		plugin.getServer();
	}

	
	public void createFirework(TregminePlayer player, Color c) {

		String colorName = c.toString();
		
        if (c.equals(Color.WHITE)) {
        	colorName = "white";
        }
		
        if (c.equals(Color.BLACK)) {
        	colorName = "black";
        }

        if (c.equals(Color.BLUE)) {
        	colorName = "blue";
        }

        if (c.equals(Color.FUCHSIA)) {
        	colorName = "fuchsia";
        }
        
        if (c.equals(Color.GRAY)) {
        	colorName = "gray";
        }
        
        if (c.equals(Color.GREEN)) {
        	colorName = "green";
        }

        if (c.equals(Color.LIME)) {
        	colorName = "lime";
        }

        if (c.equals(Color.MAROON)) {
        	colorName = "maroon";
        }

        if (c.equals(Color.NAVY)) {
        	colorName = "navy";
        }

        if (c.equals(Color.OLIVE)) {
        	colorName = "olive";
        }

        if (c.equals(Color.ORANGE)) {
        	colorName = "orange";
        }

        if (c.equals(Color.PURPLE)) {
        	colorName = "purple";
        }

        if (c.equals(Color.AQUA)) {
        	colorName = "aqua";
        }

        
        if (c.equals(Color.RED)) {
        	colorName = "red";
        }
       
        if (c.equals(Color.SILVER)) {
        	colorName = "silver";
        }
       
        if (c.equals(Color.TEAL)) {
        	colorName = "teal";
        }
        
        if (c.equals(Color.YELLOW)) {
        	colorName = "yellow";
        }
       
        if (!this.plugin.fireWork.containsKey(player.getName())) {
			ItemStack item = new ItemStack(Material.FIREWORK, 5);
			FireworkMeta meta = (FireworkMeta) item.getItemMeta();
			meta.setDisplayName("Firework: ");
			this.plugin.fireWorkMeta.put(player.getName(), meta);
			this.plugin.fireWork.put(player.getName(), item);
		}

		if (!this.plugin.fireWorkEffect.containsKey(player.getName())) {
			this.plugin.fireWorkEffect.put(player.getName(), FireworkEffect.builder());
		}

		if (!this.plugin.property.containsKey(colorName)) {
			FireworkEffect.Builder effect = this.plugin.fireWorkEffect.get(player.getName());
//			FireworkMeta meta = this.plugin.fireWorkMeta.get(player.getName());
			ItemStack fireWork = this.plugin.fireWork.get(player.getName());
			
			effect.withColor(c);
			
			FireworkMeta meta = (FireworkMeta) fireWork.getItemMeta();
			meta.addEffect(effect.build());
			fireWork.setItemMeta(meta);
			
			this.plugin.fireWork.put(player.getName(), fireWork);
			
			player.sendMessage(ChatColor.AQUA + "You have now added " + colorName);
			this.plugin.property.put(colorName, true);

		} else {
			player.sendMessage(colorName + " already added");
		}
	}
	
	
	
	@EventHandler 
	public void fireWorkButton(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			TregminePlayer player = this.plugin.tregmine.getPlayer(event.getPlayer());
			Block block = event.getClickedBlock();

			if (info.tregmine.api.math.Checksum.block(block) == -1845477288) {
				info.tregmine.api.firework.createFirwork FireWork = new info.tregmine.api.firework.createFirwork();
				
				FireWork.addColor(Color.WHITE);

				PlayerInventory inv = player.getInventory();
				inv.addItem(FireWork.getAsStack(5));
				
				
				
//				this.createFirework(player, Color.WHITE);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -337925479) {
				this.createFirework(player, Color.AQUA);
			}

			if (info.tregmine.api.math.Checksum.block(block) == 1169626330) {
				this.createFirework(player, Color.PURPLE);
			}


			if (info.tregmine.api.math.Checksum.block(block) == -1617789157) {
				this.createFirework(player, Color.BLUE);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -1541627631) {
				this.createFirework(player, Color.FUCHSIA);
			}


			if (info.tregmine.api.math.Checksum.block(block) == 38377012) {
				this.createFirework(player, Color.BLACK);
			}

			if (info.tregmine.api.math.Checksum.block(block) == 1938955831) {
				this.createFirework(player, Color.ORANGE);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -1934654641) {
				this.createFirework(player, Color.YELLOW);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -1738141136) {
				this.createFirework(player, Color.LIME);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -1345114126) {
				this.createFirework(player, Color.GRAY);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -1148600621) {
				this.createFirework(player, Color.SILVER);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -952087116) {
				this.createFirework(player, Color.GREEN);
			}


			if (info.tregmine.api.math.Checksum.block(block) == -755573611) {
				this.createFirework(player, Color.RED);
			}


			if (info.tregmine.api.math.Checksum.block(block) == -1469174797) {
				this.createFirework(player, Color.MAROON);
			}
			
			if (info.tregmine.api.math.Checksum.block(block) == 1967285645) {
				this.createFirework(player, Color.TEAL);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -2131168146) {
				this.createFirework(player, Color.NAVY);
			}

			if (info.tregmine.api.math.Checksum.block(block) == -2038282101) {
				this.createFirework(player, Color.OLIVE);
			}

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
					this.plugin.fireWorkEffect.put(player.getName(), null);
					this.plugin.fireWorkMeta.put(player.getName(), null);
					this.plugin.fireWork.put(player.getName(), null);
					this.plugin.property.put(player.getName(), null);
					player.sendMessage(ChatColor.AQUA + "You have now rested everyting and need to start from scratch");
			}



			if (info.tregmine.api.math.Checksum.block(block) == 656425969) {
				player.sendMessage("you got 5 fireworks");
				PlayerInventory inv = player.getInventory();
				inv.addItem(this.plugin.fireWork.get(player.getName()));

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
