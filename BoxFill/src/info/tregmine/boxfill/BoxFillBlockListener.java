package info.tregmine.boxfill;

import org.bukkit.Material;
import org.bukkit.block.Block;
//import org.bukkit.event.block.BlockListener;
//import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class BoxFillBlockListener extends PlayerListener {
	private final BoxFill plugin;

	public BoxFillBlockListener(BoxFill boxfill) {
		this.plugin = boxfill;
		plugin.getServer();
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			info.tregmine.api.TregminePlayer tregminePlayer = this.plugin.tregmine.tregminePlayer.get(event.getPlayer().getName());

			boolean isAdmin = tregminePlayer.isAdmin();
			boolean isBuilder =  tregminePlayer.getMetaBoolean("builder");

			if (isAdmin || isBuilder) {
				if (event.getPlayer().getItemInHand().getType() == Material.WOOD_SPADE) {
					Block block = event.getClickedBlock();
					int count;

					try {
						count = tregminePlayer.getMetaInt("bcf");
					} catch (Exception  e) {
						count = 0;
					}

					if (count == 0) {
						tregminePlayer.setBlock("b1", block);
						tregminePlayer.setBlock("b2", null);
						event.getPlayer().sendMessage("First block set");
						tregminePlayer.setMetaInt("bcf", 1);
					}

					if (count == 1) {
						tregminePlayer.setBlock("b2", block);
						event.getPlayer().sendMessage("Second block set");
						tregminePlayer.setMetaInt("bcf", 0);
					}
				}
			}
		}
	}    
}
