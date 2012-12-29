package info.tregmine.basiccommands;


import info.tregmine.api.TregminePlayer;
import info.tregmine.currency.Wallet;

//import net.minecraft.server.v1_4_6.Item;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
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
			player.sendMessage(ChatColor.AQUA + FireWork.colorToString(c) + " added");
		}


	}

	public void fadeFirework(TregminePlayer player, Color c, int button, Block block) {

		if (info.tregmine.api.math.Checksum.block(block) == button) {
			if (!this.plugin.firework.containsKey(player.getName())) {
				this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
			}

			info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());

			FireWork.fadeTo(c);
			player.sendMessage(ChatColor.AQUA + "Changed fade color to " + FireWork.colorToString(c));
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

			this.fadeFirework(player, Color.WHITE, 		327843995, 		block);
			this.fadeFirework(player, Color.AQUA, 		100155864, 		block);
			this.fadeFirework(player, Color.PURPLE, 	-96357641, 		block);
			this.fadeFirework(player, Color.BLUE, 		-292871146, 	block);
			this.fadeFirework(player, Color.FUCHSIA, 	689696379, 		block);
			this.fadeFirework(player, Color.BLACK, 		-1078925166,	block);
			this.fadeFirework(player, Color.ORANGE, 	524357500, 		block);
			this.fadeFirework(player, Color.YELLOW, 	-561837485, 	block);
			this.fadeFirework(player, Color.LIME, 		-2069389294, 	block);
			this.fadeFirework(player, Color.GRAY, 		493182874, 		block);
			this.fadeFirework(player, Color.SILVER, 	296669369, 		block);
			this.fadeFirework(player, Color.GREEN, 		-685898156, 	block);			
			this.fadeFirework(player, Color.RED, 		-882411661, 	block);
			this.fadeFirework(player, Color.MAROON, 	-489384651, 	block);
			this.fadeFirework(player, Color.TEAL, 		1804221178, 	block);
			this.fadeFirework(player, Color.NAVY, 		1607707673, 	block);
			this.fadeFirework(player, Color.OLIVE, 		-983194309, 	block);

			// Reset colors -565613610
			if (info.tregmine.api.math.Checksum.block(block) == -565613610) {
				this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				player.sendMessage(ChatColor.AQUA + "You have now rested everyting and need to start from scratch");
			}


			// duration 1 button
			if (info.tregmine.api.math.Checksum.block(block) == -1938184705) {
				if (!this.plugin.firework.containsKey(player.getName())) {
					this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				}

				info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());
				FireWork.duration(1);
				player.sendMessage(ChatColor.AQUA + "Changed duration to 1");
			}

			// duration 2 button
			if (info.tregmine.api.math.Checksum.block(block) == -430632896) {
				if (!this.plugin.firework.containsKey(player.getName())) {
					this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				}

				info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());
				FireWork.duration(2);
				player.sendMessage(ChatColor.AQUA + "Changed duration to 2");
			}
			
			// duration 3 button
			if (info.tregmine.api.math.Checksum.block(block) == 1076918913) {
				if (!this.plugin.firework.containsKey(player.getName())) {
					this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				}

				info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());
				FireWork.duration(3);
				player.sendMessage(ChatColor.AQUA + "Changed duration to 3");
			}
			
			// Large ball effect button
			if (info.tregmine.api.math.Checksum.block(block) == 367026419) {
				if (!this.plugin.firework.containsKey(player.getName())) {
					this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				}

				info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());
				FireWork.addType(FireworkEffect.Type.BALL_LARGE);
				player.sendMessage(ChatColor.AQUA + "Changed to huge ball effect");
			}

			if (info.tregmine.api.math.Checksum.block(block) == 956566934) {
				if (!this.plugin.firework.containsKey(player.getName())) {
					this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				}

				info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());
				FireWork.addType(FireworkEffect.Type.BURST);
				player.sendMessage(ChatColor.AQUA + "Changed to burst effect");
			}


			if (info.tregmine.api.math.Checksum.block(block) == 563539924) {
				if (!this.plugin.firework.containsKey(player.getName())) {
					this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				}

				info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());
				FireWork.addType(FireworkEffect.Type.STAR);
				player.sendMessage(ChatColor.AQUA + "Changed to star effect");
			}


			if (info.tregmine.api.math.Checksum.block(block) == 760053429) {
				if (!this.plugin.firework.containsKey(player.getName())) {
					this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				}

				info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());
				FireWork.addType(FireworkEffect.Type.CREEPER);
				player.sendMessage(ChatColor.AQUA + "Changed to creeper effect");
			}


			if (info.tregmine.api.math.Checksum.block(block) == 656425969) {
				if (!this.plugin.firework.containsKey(player.getName())) {
					this.plugin.firework.put(player.getName(), new info.tregmine.api.firework.createFirwork());
				}

				info.tregmine.api.firework.createFirwork FireWork = this.plugin.firework.get(player.getName());


				Wallet wallet = new Wallet(player.getName());
				if ( wallet.take(2000) ) {
					PlayerInventory inv = player.getInventory();
					inv.addItem(FireWork.getAsStack(5));
					player.sendMessage(ChatColor.AQUA + "you got 5 fireworks and " + ChatColor.GOLD + "2000" + ChatColor.AQUA + " Tregs was taken from you");
					this.plugin.log.info(player.getName() + ":2000");
					
				} else {
					player.sendMessage("I'm sorry but you can't afford the 2000 tregs");
				}


			}

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
