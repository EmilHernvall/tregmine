package info.tregmine.surprise;


import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;



public class SuprisePlayerListener extends PlayerListener {
	private final Main plugin;

	public SuprisePlayerListener(Main instance) {
		this.plugin = instance;
		plugin.getServer();
	}


	public void onInventoryOpen (PlayerInventoryEvent event) {
	}

	public void onPlayerDropItem (PlayerDropItemEvent event) {
		int x = event.getItemDrop().getLocation().getBlockX();
		int z = event.getItemDrop().getLocation().getBlockZ();
		int y = event.getItemDrop().getLocation().getBlockY();
		
	    Random ran = new Random( System.currentTimeMillis() );
		int  rand = ran.nextInt(85)+1;
	    Material mat = Material.getMaterial(rand);

	 
	    
	    if (mat == null) {
	    	event.getPlayer().sendMessage("Nothing was given at this time");
	    	return;
	    }
	    
		if (event.getPlayer().getName().matches("einand")) {
			event.getPlayer().sendMessage("X: 	" + x);
			event.getPlayer().sendMessage("Z: 	" + z);
			event.getPlayer().sendMessage("Y: 	" + y);
			event.getPlayer().sendMessage(mat.name().toString());
		}

		
		if(
			(x == 890 && y == 61 && z == -166) ||
			(x == 890 && y == 61 && z == -165) ||
			(x == 890 && y == 60 && z == -165) ||
			(x == 890 && y == 59 && z == -165) ||
			(x == 890 && y == 60 && z == -167) ||
			(x == 889 && y == 60 && z == -166) ||
			(x == 890 && y == 60 && z == -166) ||
			(x == 891 && y == 60 && z == -167) ||
			(x == 891 && y == 60 && z == -166) ||
			(x == 889 && y == 60 && z == -166) ||
			(x == 889 && y == 60 && z == -166)
			) {


		    
		    if (mat != null) {
				ItemStack stack = new ItemStack(mat, event.getItemDrop().getItemStack().getAmount());
				event.getItemDrop().setItemStack(stack);
		    }
		    
		}
		
	}

	public void onPlayerPickupItem (PlayerPickupItemEvent event){
	}

}
