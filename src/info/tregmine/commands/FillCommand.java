package info.tregmine.commands;

import java.util.Arrays;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.boxfill.AbstractFiller;
import info.tregmine.boxfill.Filler;
import info.tregmine.boxfill.Replacer;
import info.tregmine.boxfill.TestFiller;
import info.tregmine.boxfill.TestReplacer;
import info.tregmine.boxfill.Paster;
import info.tregmine.boxfill.Copy;
import info.tregmine.boxfill.SavedBlocks;
import info.tregmine.boxfill.Undo;
import info.tregmine.boxfill.History;

public class FillCommand extends AbstractCommand
{
    private static final Material[] disallowedMaterials = { Material.APPLE,
            Material.ARROW, Material.BED, Material.BED_BLOCK, Material.BOAT,
            Material.BONE, Material.BOOK, Material.BOW, Material.BOWL,
            Material.BREAD, Material.BROWN_MUSHROOM, Material.BUCKET,
            Material.BURNING_FURNACE, Material.CACTUS, Material.CAKE,
            Material.CHEST, Material.IRON_DOOR, Material.IRON_DOOR_BLOCK,
            Material.MUSHROOM_SOUP, Material.RED_MUSHROOM,
            Material.YELLOW_FLOWER, Material.RED_ROSE, Material.SPONGE,
            Material.SAPLING, Material.CACTUS, Material.CLAY,
            Material.CLAY_BRICK, Material.COAL_ORE, Material.DIAMOND_ORE,
            Material.GOLD_ORE, Material.IRON_ORE, Material.LAPIS_ORE,
            Material.SAPLING, Material.REDSTONE_ORE, Material.MONSTER_EGG,
            Material.MONSTER_EGGS };

    private static int MAX_FILL_SIZE = (10 * 16) * (10 * 16) * 128;

    // static initializer
    {
        Arrays.sort(disallowedMaterials);
    }

    private History undoHistory;
    private History copyHistory;

    public FillCommand(Tregmine tregmine, String command)
    {
        super(tregmine, command);

        undoHistory = new History();
        copyHistory = new History();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
    	if(args.length == 0){
    		player.sendMessage(RED + "Please specify the material");
    		return true;
    	}
        if (!player.getRank().canFill()) {
        	player.nopermsMessage(false, "/fill");
            return true;
        }

        Server server = tregmine.getServer();
        BukkitScheduler scheduler = server.getScheduler();

        // undo
        if (args.length > 0 && "undo".equals(args[0])) {
            World world = player.getWorld();
            SavedBlocks blocks = undoHistory.get(player);
            if (blocks == null) {
                return true;
            }

            Undo undo = new Undo(world, blocks, 100000);
            undo.setScheduleState(scheduler,
                    scheduler.scheduleSyncRepeatingTask(tregmine, undo, 0, 1));

            player.sendMessage(DARK_AQUA + "Undo in progress.");

            return true;
        }

        Block b1 = player.getFillBlock1();

        if (args.length > 0 && "paste".equals(args[0])) {
            if (b1 == null) {
                player.sendMessage(DARK_AQUA
                        + "Specify the point where you want to paste.");
                return true;
            }

            double theta =
                    args.length > 1 ? Double.parseDouble(args[1]) * Math.PI
                            / 180.0 : 0.0;
            player.sendMessage(DARK_AQUA + "Rotating " + theta + " radians.");

            World world = player.getWorld();
            SavedBlocks blocks = copyHistory.get(player);
            if (blocks == null) {
                return true;
            }

            Paster paster =
                    new Paster(undoHistory, player, world, b1, blocks, theta,
                            100000);
            paster.setScheduleState(scheduler,
                    scheduler.scheduleSyncRepeatingTask(tregmine, paster, 0, 1));

            player.sendMessage(DARK_AQUA + "Paste in progress.");

            return true;
        }

        Block b2 = player.getFillBlock2();

        if (b1 == null || b2 == null) {
            player.sendMessage(DARK_AQUA + "You need to select two corners!");
            return true;
        }

        // execute a copy
        AbstractFiller filler = null;
        if (args.length > 0 && "copy".equals(args[0])) {
            filler = new Copy(tregmine, copyHistory, player, b1, b2, 100000);
            player.sendMessage(DARK_AQUA + "Copied selected area.");
        }

        // otherwise, try regular fills
        else {
            MaterialData mat = parseMaterial(args[0]);
            MaterialData toMat =
                    args.length > 1 ? parseMaterial(args[1]) : null;

            // regular fills
            if (mat != null && toMat == null) {

                if (!player.isOp() &&
						!mat.toItemStack().getType().isBlock()) {
                    player.sendMessage(RED + "Disabled!");
                    return true;
                }

                player.sendMessage("You filled with " + DARK_AQUA
                        + mat.toString() + "(" + mat.getItemTypeId() + ")");

                if (command.equals("fill")) {
                    filler =
                            new Filler(tregmine, undoHistory, player, b1, b2, mat, 100000);
                    LOGGER.info("[FILL] " + player.getName() + " filled ["
                            + b1.getLocation().getBlockX() + ","
                            + b1.getLocation().getBlockZ() + ","
                            + b1.getLocation().getBlockY() + "] - ["
                            + b2.getLocation().getBlockX() + ","
                            + b2.getLocation().getBlockZ() + ","
                            + b2.getLocation().getBlockY() + "]  with "
                            + mat.toString() + " " + mat.getItemTypeId());
                }

                if (command.equals("testfill")) {
                    filler = new TestFiller(tregmine, player, b1, b2, mat, 100000);
                }
            }

            // replacers
            if (mat != null && toMat != null) {

				if (!player.isOp() &&
						!mat.toItemStack().getType().isBlock()) {
                    player.sendMessage(RED + "Disabled!");
                    return true;

                }

                player.sendMessage("You replaced " + DARK_AQUA + mat.toString()
                        + "(" + mat.getItemTypeId() + ")" + BLUE + "with"
                        + DARK_AQUA + toMat.toString() + "("
                        + toMat.getItemTypeId() + ")");

                if (command.equals("fill")) {
                    filler = new Replacer(tregmine, undoHistory, player, b1, b2, mat,
                                    toMat, 100000);
                }

                if (command.equals("testfill")) {
                    filler = new TestReplacer(tregmine, player, b1, b2, mat, toMat, 100000);
                }

                LOGGER.info("[FILL] " + player.getName() + " replaced with "
                        + toMat.toString() + " " + toMat.getItemTypeId() + "["
                        + b1.getLocation().getBlockX() + ","
                        + b1.getLocation().getBlockZ() + ","
                        + b1.getLocation().getBlockY() + "] - ["
                        + b2.getLocation().getBlockX() + ","
                        + b2.getLocation().getBlockZ() + ","
                        + b2.getLocation().getBlockY() + "] with "
                        + mat.toString() + " " + mat.getItemTypeId());
            }
        }

        if (filler == null) {
            player.sendMessage(RED + "Invalid command!");
            return false;
        }

        if (filler.getTotalVolume() > MAX_FILL_SIZE) {
            player.sendMessage(DARK_AQUA + "Selected area is too big ("
                    + filler.getTotalVolume() + ")!");
            return true;
        }

        // execute action
        if (filler != null) {
            player.sendMessage(DARK_AQUA + "Total volume is "
                    + filler.getTotalVolume() + ".");
            filler.setScheduleState(scheduler,
                    scheduler.scheduleSyncRepeatingTask(tregmine, filler, 0, 1));
        }

        return true;
    }

    private MaterialData parseMaterial(String str)
    {
        Material material;
        MaterialData data;

        try {
            byte subType = 0;
            if (str.matches("^[0-9]+$")) {
                material = Material.getMaterial(Integer.parseInt(str));
            }
            else if (str.matches("^[0-9]+:[0-9]+$")) {
                String[] segmentedInput = str.split(":");

                int materialType = Integer.parseInt(segmentedInput[0]);
                subType = Byte.parseByte(segmentedInput[1]);

                material = Material.getMaterial(materialType);
            }
            else if (str.matches("^[A-Za-z_]+:[0-9]+$")) {
                String[] segmentedInput = str.split(":");

                material =
                        Material.getMaterial(segmentedInput[0].toUpperCase());
                subType = Byte.parseByte(segmentedInput[1]);
            }
            else {
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
