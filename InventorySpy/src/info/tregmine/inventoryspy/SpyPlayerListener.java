package info.tregmine.inventoryspy;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
//import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;


public class SpyPlayerListener implements Listener {
	private final Main plugin;

	public SpyPlayerListener(Main instance) {
		this.plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
		event.getPlayer().getInventory().clear();
	}

//	@EventHandler
//	public void onItemSpawn(ItemSpawnEvent event) {
//		if (event.getEntity().getItemStack().getType() == Material.SUGAR_CANE) {
//			event.setCancelled(true);
//		}
//	}

	/*
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		List<Entity> entitys = event.getWorld().getEntities();

		if (plugin.getServer().getPlayer("einand") != null) {
			plugin.getServer().getPlayer("einand").sendMessage("::" + event.getClass().toString());
		}


		for (Entity entity : entitys) {


			//			Entity entity = event.getEntity();
			if (entity instanceof ItemStack) {
				ItemStack itemstack = (ItemStack) entity;
				plugin.getServer().broadcastMessage("::" + itemstack.getType().toString());
			}
		}

	}
*/


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK  || event.getAction() == Action.LEFT_CLICK_BLOCK)) {


			if(event.getClickedBlock().getState() instanceof Chest) {
				Chest chest = (Chest) event.getClickedBlock().getState();
				Inventory invent = 	chest.getBlockInventory();
				Location loc = event.getClickedBlock().getLocation();


				for (int i = 0; i < invent.getSize(); i++) {
					if (invent.getItem(i) != null) {
						this.plugin.log.info("CHEST: " + 
								"(" + loc.getBlockX() + "," + loc.getBlockY() + "," +  loc.getBlockZ() + ")" + 
								"(" + i + ")" + 
								"(" + event.getPlayer().getName() + ") " 
								+ invent.getItem(i).getType().toString() +  
								"(" + invent.getItem(i).getType().getId() + ")" + 
								":" + invent.getItem(i).getData().toString() + " :: " + 
								invent.getItem(i).getAmount());
					}
				}

			}


			if (event.getClickedBlock().getType() == Material.CHEST && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
				//				event.getPlayer().sendMessage("error");
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem (PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}

		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(event.getPlayer().getName());
		this.plugin.whoDropedItem.put(event.getItemDrop().hashCode(), tregminePlayer.getName());


		if(event.getPlayer().getName().matches("einand")){
			event.getItemDrop().setMetadata("einand", new FixedMetadataValue(plugin, true));
		}
	}

	@EventHandler
	public void onPlayerPickupItem (PlayerPickupItemEvent event){
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}

		if (event.getItem().getItemStack().getType() == Material.MOB_SPAWNER) {
			event.setCancelled(true);
			return;
		}


		if (this.plugin.whoDropedItem.containsKey(event.getItem().hashCode())) {

			if (event.isCancelled()) {
				return;
			}

			String from = this.plugin.whoDropedItem.get(event.getItem().hashCode());

			if (from != null && !event.getPlayer().getName().matches(from)) {

				info.tregmine.api.TregminePlayer tregminePlayerFrom = this.plugin.tregmine.tregminePlayer.get(from);
				info.tregmine.api.TregminePlayer tregminePlayerTo = this.plugin.tregmine.tregminePlayer.get(event.getPlayer().getName());

				this.plugin.log.info (event.getItem().getItemStack().getAmount() + ":" + event.getItem().getItemStack().getType().toString() + " " + tregminePlayerFrom.getChatName() + " ==> " + tregminePlayerTo.getChatName() );
				if	 (!tregminePlayerFrom.getMetaBoolean("invis") && ! tregminePlayerTo.getMetaBoolean("invis")) {
					event.getPlayer().sendMessage(ChatColor.YELLOW + "You got " + event.getItem().getItemStack().getAmount() + " " + event.getItem().getItemStack().getType().toString().toLowerCase() + ChatColor.YELLOW + " from " + tregminePlayerFrom.getChatName() );
					this.plugin.getServer().getPlayer(from).sendMessage(ChatColor.YELLOW +  "You gave " + event.getItem().getItemStack().getAmount() + " " + event.getItem().getItemStack().getType().toString().toLowerCase() +  " to " + tregminePlayerTo.getChatName()  );
				}
			}

			if (event.getItem().hasMetadata("einand")) {
				event.getPlayer().sendMessage(ChatColor.AQUA + "This item have been touched by einand");
			}

			//			event.getItem().setMetadata("einand", new FixedMetadataValue(plugin, ""));

			this.plugin.whoDropedItem.put(event.getItem().hashCode(), null);
		}
	}

}
