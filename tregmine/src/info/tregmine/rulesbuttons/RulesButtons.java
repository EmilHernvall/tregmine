package info.tregmine.rulesbuttons;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class RulesButtons implements Listener {

	private final Tregmine plugin;

	public RulesButtons(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	@EventHandler
	public void rulesButtons(PlayerInteractEvent event) {

		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) return;
		if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block block = event.getClickedBlock();
			int hash = info.tregmine.api.math.Checksum.block(block);
			TregminePlayer tregminePlayer = plugin.getPlayer(event.getPlayer());
			if(Material.STONE_BUTTON.equals(block.getType())){
				if(hash == 1877332415){
					// if(tregminePlayer.isTrusted()){
					Location testing = new Location(plugin.getServer().getWorld("world"), 518438, 35, -19339);
					tregminePlayer.teleport(testing);
					plugin.log.info(tregminePlayer.getName() + " :RULESTELEPORTBUTTON");
					// }
				}
			}
		}	
	}
}