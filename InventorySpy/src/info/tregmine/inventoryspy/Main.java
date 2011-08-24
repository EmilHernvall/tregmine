package info.tregmine.inventoryspy;


//import java.text.NumberFormat;
import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.InventoryPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import info.tregmine.Tregmine; 


public class Main extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
//	NumberFormat nf = NumberFormat.getNumberInstance();
	public SpyPlayerListener inventory = new SpyPlayerListener(this);
	public HashMap<Integer, String> whoDropedItem = new HashMap<Integer, String>();

	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		
		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
			
			  getServer().getPluginManager().registerEvent(Event.Type.INVENTORY_OPEN, inventory, Priority.Monitor, this);
			  getServer().getPluginManager().registerEvent(Event.Type.PLAYER_DROP_ITEM, inventory, Priority.Monitor, this);
			  getServer().getPluginManager().registerEvent(Event.Type.PLAYER_PICKUP_ITEM, inventory, Priority.Monitor, this);

		}
	}

	public void onDisable(){
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();

		if(!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(player.getName());

		
		boolean isAdmin = tregminePlayer.isAdmin();
		
		if (commandName.matches("inv") && args.length > 0  && isAdmin) {
			Player target = this.getServer().matchPlayer(args[0]).get(0);
			if (target != null) {
				EntityPlayer eh = ((CraftPlayer) player).getHandle();
				EntityHuman ehtarget = ((CraftPlayer) target).getHandle();
				InventoryPlayer ehtargetinv = ehtarget.inventory;
				eh.a(ehtargetinv); // Show to the user.
				player.sendMessage("tried to look in " + target.getName() +  " inventory");
			}
		}
		return true;
	}
	
	public void onLoad() {
	}

}