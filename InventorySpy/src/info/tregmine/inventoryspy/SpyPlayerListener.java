package info.tregmine.inventoryspy;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;


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

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK  || event.getAction() == Action.LEFT_CLICK_BLOCK)) {

			if(event.getClickedBlock().getState() instanceof Chest) {
//				event.getPlayer().sendMessage("You clicked on a chest!");
				int check = info.tregmine.api.math.Checksum.block(event.getClickedBlock());
			}

			
			if (event.getClickedBlock().getType() == Material.CHEST && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
				event.getPlayer().sendMessage("error");
				event.setCancelled(true);
				return;
			}
		}
	}


	@EventHandler
	public void onInventoryOpen (PlayerInventoryEvent event) {
		event.getPlayer().sendMessage("INV");
		this.plugin.log.info(event.getInventory().getItem(0).getType().name());
	}

	@EventHandler
	public void onPlayerDropItem (PlayerDropItemEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}


		info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(event.getPlayer().getName());
		this.plugin.whoDropedItem.put(event.getItemDrop().hashCode(), tregminePlayer.getName());
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
			this.plugin.whoDropedItem.put(event.getItem().hashCode(), null);

		}


	}

}
