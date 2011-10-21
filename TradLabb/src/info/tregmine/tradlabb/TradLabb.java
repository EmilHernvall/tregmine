package info.tregmine.tradlabb;


import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 



public class TradLabb extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public boolean enable = true;

	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
			}
		}

		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				World world = getServer().getWorld("world");

				Block block2 = world.getBlockAt(565, 71, -171);
				Block signBlock = block2.getWorld().getBlockAt(block2.getLocation().getBlockX(),
						block2.getLocation().getBlockY(),
						block2.getLocation().getBlockZ());
				signBlock.setType(Material.SIGN_POST);

				BlockState state = signBlock.getState();

	    	    SimpleDateFormat ny = new SimpleDateFormat("hh:mm:ss a");
	    	    SimpleDateFormat swe = new SimpleDateFormat("HH:mm:ss (a)");
	    	    SimpleDateFormat la = new SimpleDateFormat("hh:mm:ss a");
	    		ny.setTimeZone(TimeZone.getTimeZone("America/New_York"));
	    		swe.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
	    		la.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
	
		        
				if (state instanceof Sign) {
					Sign signs = (Sign)state;
					long houer = signs.getWorld().getTime()/10 * 32 /60 /60;
					long min = signs.getWorld().getTime()/10 * 32 /60 %60;
					
					long houer2 = signs.getWorld().getTime()-12000/10 * 32 /60 /60;
					long min2 = signs.getWorld().getTime()-12000/10 * 32 /60 %60;
					signs.setLine(0, "");
					signs.setLine(1, "TIME: " + houer + ":" + min );
					signs.setLine(2, "TIME2: " + houer2 + ":" + min2 );
					signs.setLine(3, "" + signs.getWorld().getTime());
					signs.update();
				}
	    		
	    		
	    		
			}
		}, 20L, 36L);

	}

	public void onDisable(){
		this.enable = false;
	}
	public void onLoad() {
	}

}
