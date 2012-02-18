package info.tregmine.chestbless;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import info.tregmine.Tregmine; 


public class Bless extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	
	public Map<Integer, String> chests = new HashMap<Integer, String>();
	
	
	@Override
	public void onEnable(){
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
		getServer().getPluginManager().registerEvents(new BlessBlock(this), this);
		getServer().getPluginManager().registerEvents(new BlessPlayer(this), this);
	}

	@Override
	public void onDisable(){
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();

		if(!(sender instanceof Player)) {
			return false;
		}

		Player player = (Player) sender;
		info.tregmine.api.TregminePlayer tregminePlayer = this.tregmine.tregminePlayer.get(player.getName());

		boolean isAdmin = tregminePlayer.isAdmin();
		boolean isGuardian = tregminePlayer.getMetaBoolean("mentor");
		
		if (commandName.matches("bless") && (isAdmin || isGuardian)) {
			try {
				String name = getServer().matchPlayer(args[0]).get(0).getName();
				info.tregmine.api.TregminePlayer toPlayer = this.tregmine.tregminePlayer.get(getServer().matchPlayer(args[0]).get(0).getName());
				player.sendMessage(ChatColor.AQUA + "You will bless following blocks to " + toPlayer.getChatName() + ".");
				tregminePlayer.setMetaString("chestbless", name);
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Something went wrong. Is the player not online?");
				e.printStackTrace();
			}
			return true;
		}

		return false;
	}

	@Override
	public void onLoad() {
		this.chests = info.tregmine.chestbless.Store.loadbless();
	}
}
