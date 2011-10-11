package info.tregmine.boxfill;

import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
//import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import info.tregmine.Tregmine; 
import info.tregmine.api.TregminePlayer;

public class BoxFill extends JavaPlugin {
	
	private static final Material[] disallowedMaterials = {
			Material.APPLE,
			Material.ARROW,
			Material.BED,
			Material.BED_BLOCK,
			Material.BOAT,
			Material.BONE,
			Material.BOOK,
			Material.BOW,
			Material.BOWL,
			Material.BREAD,
			Material.BROWN_MUSHROOM,
			Material.BUCKET,
			Material.BURNING_FURNACE,
			Material.CACTUS,
			Material.CAKE,
			Material.CHEST,
			Material.IRON_DOOR,	
			Material.IRON_DOOR_BLOCK,
			Material.MUSHROOM_SOUP,
			Material.RED_MUSHROOM,
			Material.YELLOW_FLOWER,
			Material.RED_ROSE,
			Material.SPONGE,
			Material.SAPLING,
			Material.BEDROCK,
			Material.CACTUS,
			Material.CLAY,
			Material.CLAY_BRICK,
			Material.COAL_ORE,
			Material.DIAMOND_ORE,
			Material.GOLD_ORE,
			Material.IRON_ORE,
			Material.LAPIS_ORE,
			Material.REDSTONE_ORE
		};
	
	private static int MAX_FILL_SIZE = (10 * 16) * (10 * 16) * 128; 
	
	// static initializer
	{
		Arrays.sort(disallowedMaterials);
	}
	
	public final Logger log = Logger.getLogger("Minecraft");
	public Tregmine tregmine = null;
	public final BoxFillBlockListener boxfillblockListener = new BoxFillBlockListener(this);
	
	//private List<BlockState> undo = null;
	private History undoHistory;
	private History copyHistory;

	public void onEnable() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if (this.tregmine == null) {
			if (test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, boxfillblockListener, Priority.Highest, this);
		
		undoHistory = new History();
		copyHistory = new History();
	}

	public void onDisable() {
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player player = null;

		if (sender instanceof Player) {
			player = (Player)sender;
		} else {
			return false;
		}

		TregminePlayer tregminePlayer = tregmine.tregminePlayer.get(player.getName());
		BukkitScheduler scheduler = getServer().getScheduler();

		if(commandName.equals("blockhere") && tregminePlayer.isAdmin()) {
			Block block = player.getWorld().getBlockAt(player.getLocation());
			block.setType(Material.DIRT);
			return true;
		}
		
		if(commandName.equals("timemachine") && tregminePlayer.isAdmin()) {
			Block b1 = tregminePlayer.getBlock("b1");
			Block b2 = tregminePlayer.getBlock("b2");

			TimeMachineFiller filler = new TimeMachineFiller(b1, b2, 10000, args[0]);
			filler.setScheduleState(scheduler, 
					scheduler.scheduleSyncRepeatingTask(this, filler, 0, 30));
			
			return true;
		}

		if(commandName.equals("timerestore") && tregminePlayer.isAdmin()) {
			Block b1 = tregminePlayer.getBlock("b1");
			Block b2 = tregminePlayer.getBlock("b2");

			TimeRestoreFiller filler = new TimeRestoreFiller(b1, b2, 10000, args[0]);
			filler.setScheduleState(scheduler, 
					scheduler.scheduleSyncRepeatingTask(this, filler, 0, 30));
			
			return true;
		}

		if(commandName.equals("crestore") && tregminePlayer.isAdmin()) {
			Block b1 = tregminePlayer.getBlock("b1");

			player.getWorld().regenerateChunk(b1.getChunk().getX(), b1.getChunk().getZ());
			return true;
		}

		if((commandName.equals("fill") || commandName.equals("testfill")) && ( tregminePlayer.isAdmin() || tregminePlayer.getMetaBoolean("builder"))) {
			
			// undo
			if (args.length > 0 && "undo".equals(args[0])) {
				World world = player.getWorld();
				SavedBlocks blocks = undoHistory.get(player);
				if (blocks == null) {
					return true;
				}
				
				Undo undo = new Undo(world, blocks, 100000);
				undo.setScheduleState(scheduler, scheduler.scheduleSyncRepeatingTask(this, undo, 0, 1));
				
				player.sendMessage(ChatColor.DARK_AQUA + "Undo in progress.");
				
				return true;
			}
			
			Block b1 = tregminePlayer.getBlock("b1");
			
			if (args.length > 0 && "paste".equals(args[0])) {
				if (b1 == null) {
					player.sendMessage(ChatColor.DARK_AQUA + "Specify the point where you want to paste.");
					return true;
				}
				
				double theta = args.length > 1 ? Double.parseDouble(args[1])*Math.PI/180.0 : 0.0; 
				player.sendMessage(ChatColor.DARK_AQUA + "Rotating " + theta + " radians.");
				
				World world = player.getWorld();
				SavedBlocks blocks = copyHistory.get(player);
				if (blocks == null) {
					return true;
				}
				
				Paster paster = new Paster(undoHistory, player, world, b1, blocks, theta, 100000);
				paster.setScheduleState(scheduler, scheduler.scheduleSyncRepeatingTask(this, paster, 0, 1));
				
				player.sendMessage(ChatColor.DARK_AQUA + "Paste in progress.");
				
				return true;
			}
			
			Block b2 = tregminePlayer.getBlock("b2");
			
			if(b1 == null || b2 == null) {
				player.sendMessage(ChatColor.DARK_AQUA + "You need to select two corners!");
				return true;
			}
			
			// execute a copy
			AbstractFiller filler = null;
			if (args.length > 0 && "copy".equals(args[0])) {
				filler = new Copy(copyHistory, player, b1, b2, 100000);
				player.sendMessage(ChatColor.DARK_AQUA + "Copied selected area.");
			}
			
			// otherwise, try regular fills
			else {
				MaterialData mat = parseMaterial(args[0]);
				if (mat != null && Arrays.binarySearch(disallowedMaterials, mat.getItemType()) > 0) {
					player.sendMessage(ChatColor.RED + "Disabled!");
					return true;
				}
				
				MaterialData toMat = args.length > 1 ? parseMaterial(args[1]) : null;
				if (toMat != null && Arrays.binarySearch(disallowedMaterials, toMat.getItemType()) > 0) {
					player.sendMessage(ChatColor.RED + "Disabled!");
					return true;
				}
				
				// regular fills
				if (mat != null && toMat == null) {
					player.sendMessage("You filled with " + ChatColor.DARK_AQUA  + mat.toString() + "("+ mat.getItemTypeId() + ")");
					
					if (commandName.equals("fill")) {
						filler = new Filler(undoHistory, player, b1, b2, mat, 100000);
						this.log.info("[FILL] " + player.getName() + " filled [" + b1.getLocation().getBlockX() + "," + 
								b1.getLocation().getBlockZ() + "," + b1.getLocation().getBlockY() + "] - [" + 
								b2.getLocation().getBlockX() + "," + b2.getLocation().getBlockZ() + "," + 
								b2.getLocation().getBlockY() + "]  with " + mat.toString() + " " + mat.getItemTypeId() );
					}
					
					if (commandName.equals("testfill")) {
						filler = new TestFiller(player, b1, b2, mat, 100000);						
					}
				}
	
				// replacers
				if (mat != null && toMat != null) {
					player.sendMessage("You replaced " + ChatColor.DARK_AQUA + mat.toString() + "("+ mat.getItemTypeId() + ")" + 
							ChatColor.BLUE + "with" + ChatColor.DARK_AQUA + toMat.toString() + "("+ toMat.getItemTypeId() + ")" );
					
					if (commandName.equals("fill")) {
						filler = new Replacer(undoHistory, player, b1, b2, mat, toMat, 100000);
					}
					
					if (commandName.equals("testfill")) {
						filler = new TestReplacer(player, b1, b2, mat, toMat, 100000);
					}
					
					this.log.info("[FILL] " + player.getName() + " replaced with "  + toMat.toString() + " " +  toMat.getItemTypeId() + 
							"[" + b1.getLocation().getBlockX() + "," + b1.getLocation().getBlockZ() + "," + b1.getLocation().getBlockY() + 
							"] - [" + b2.getLocation().getBlockX() + "," + b2.getLocation().getBlockZ() + "," + b2.getLocation().getBlockY() + 
							"] with " + mat.toString() + " " + mat.getItemTypeId() );
				}
			}
			
			if (filler.getTotalVolume() > MAX_FILL_SIZE) {
				player.sendMessage(ChatColor.DARK_AQUA + "Selected area is too big (" + 
						filler.getTotalVolume() + ")!");
				return true;
			}
			
			// execute action
			if (filler != null) {
				player.sendMessage(ChatColor.DARK_AQUA + "Total volume is " + filler.getTotalVolume() + ".");
				filler.setScheduleState(scheduler, scheduler.scheduleSyncRepeatingTask(this, filler, 0, 1));
			}

			return true;
		}
		
		return false;
	}
	
	private MaterialData parseMaterial(String str)
	{
		Material material;
		MaterialData data;

		try {
			byte subType = 0;
			if (str.matches("^[0-9]+$")) {
				material = Material.getMaterial(Integer.parseInt(str));
			} else if (str.matches("^[0-9]+:[0-9]+$")) {
				String[] segmentedInput = str.split(":");
				
				int materialType = Integer.parseInt(segmentedInput[0]);
				subType = Byte.parseByte(segmentedInput[1]);
				
				material = Material.getMaterial(materialType);
			} else if (str.matches("^[A-Za-z_]+:[0-9]+$")) {
				String[] segmentedInput = str.split(":");
				
				material = Material.getMaterial(segmentedInput[0].toUpperCase());
				subType = Byte.parseByte(segmentedInput[1]);
			} else {
				material = Material.getMaterial(str.toUpperCase());
			}
			
			if (material == null) {
				return null;
			}
			
			data = new MaterialData(material, subType);
			
			return data;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
