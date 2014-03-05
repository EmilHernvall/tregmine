package info.tregmine.tools;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class LumberListener implements Listener
{
    private Tregmine plugin;

    public LumberListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @SuppressWarnings({ "deprecation", "static-access" })
    @EventHandler
    public void Lumber(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        if (!block.getType().equals(Material.LOG) &&
            !block.getType().equals(Material.LOG_2)) {

           return;
       }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        if (!player.getRank().canUseTools()) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }
        if (player.getItemInHand() == null) {
            return;
        }

        if (player.getItemInHand().getItemMeta() == null) {
            return;
        }

        List<String> lore = player.getItemInHand().getItemMeta().getLore();
        if (lore == null || lore.isEmpty()) {
            return;
        }
        if (!lore.get(0).equals(ToolsRegistry.LumberAxeLoreTag)) {
            return;
        }

        boolean stop = false;

        List<Block> connectedBlocks = new ArrayList<>();
        int size = connectedBlocks.size();

        connectedBlocks.add(block);

        do {
            size = connectedBlocks.size();
            if (size > (64*4)) {
                stop = true;
                continue;
            }
            connectedBlocks = anyConnectedBlocks(block, connectedBlocks);
            if (connectedBlocks.size() == size) stop = true;
        } while (stop == false);

        ItemStack itemm = player.getItemInHand();
        int blocksBroken = 0;
        boolean running = true;

        for (Block b : connectedBlocks) {
            if (!player.hasBlockPermission(b.getLocation(), false)) {
                continue;
            }

            List<String> durability_lore = player.getItemInHand().getItemMeta().getLore();

            String[] durability = durability_lore.get(1).split("/");

            if (Integer.parseInt(durability[0]) == 0) { // Leave the player with 1 durability point
                if (running == true){
                    player.sendMessage(ChatColor.RED + "Out of durability, Stopping...");
                    running = false;
                }
                continue;
            }

            lore.remove(1);
            lore.add(Integer.parseInt(durability[0]) - 1 + "/1000");

            ItemMeta meta = itemm.getItemMeta();
            meta.setLore(lore);

            itemm.setItemMeta(meta);

            byte data = 0;
            if(b.getData() == (byte) 0 ||
               b.getData() == (byte) 4 ||
               b.getData() == (byte) 8 ||
               b.getData() == (byte) 12){
                data = 0;
            } else if (b.getData() == (byte) 1 ||
                       b.getData() == (byte) 5 ||
                       b.getData() == (byte) 9 ||
                       b.getData() == (byte) 13){
                data = 1;
            } else if (b.getData() == (byte) 2 ||
                       b.getData() == (byte) 6 ||
                       b.getData() == (byte) 10 ||
                       b.getData() == (byte) 14){
                data = 2;
            } else if (b.getData() == (byte) 3 ||
                       b.getData() == (byte) 7 ||
                       b.getData() == (byte) 11 ||
                       b.getData() == (byte) 15){
                data = 3;
            } else {
                data = 0;
            }

            ItemStack item = new ItemStack(b.getType(), 1, data);
            b.getWorld().dropItemNaturally(player.getLocation(), item);
            b.setType(Material.AIR);
            blocksBroken++;
        }

        if (blocksBroken > 0) {
            player.sendMessage("You lumber axed a tree containing " + blocksBroken + " logs!");
            plugin.LOGGER.info(player.getRealName() + " broke " + blocksBroken + " using LumberAxe!");
        }
    }

    public List<Block> anyConnectedBlocks(Block block, List<Block> cBlock)
    {
        Material material = block.getType();

        List<Block> knownBlocks = new ArrayList<>();
        for(Block b : cBlock){
            knownBlocks.add(b);
        }

        for(Block b : knownBlocks){
            World world = b.getWorld();

            Block one = world.getBlockAt(b.getLocation().add(new Vector(0, 1, 0))); // Up
            Block two = world.getBlockAt(b.getLocation().subtract(new Vector(0, 1, 0))); // Down

            Block three = world.getBlockAt(b.getLocation().add(new Vector(1, 0, 0))); // Left
            Block four = world.getBlockAt(b.getLocation().subtract(new Vector(1, 0, 0))); // Right

            Block five = world.getBlockAt(b.getLocation().add(new Vector(0, 0, 1))); // Infront
            Block six = world.getBlockAt(b.getLocation().subtract(new Vector(0, 0, 1))); // Behind

            // Middle diagonals
            Block seven = world.getBlockAt(b.getLocation().subtract(new Vector(1, 0, 1)));
            Block eight = world.getBlockAt(b.getLocation().add(new Vector(1, 0, 1)));
            Block nine = world.getBlockAt(b.getLocation().subtract(new Vector(0, 0, 1)).add(new Vector(1, 0, 0)));
            Block ten = world.getBlockAt(b.getLocation().subtract(new Vector(1, 0, 0)).add(new Vector(0, 0, 1)));

            // Top Diagonals
            Block eleven = world.getBlockAt(b.getLocation().subtract(new Vector(1, 0, 1)).add(new Vector(0, 1, 0)));
            Block twelve = world.getBlockAt(b.getLocation().add(new Vector(1, 1, 1)));
            Block thirteen = world.getBlockAt(b.getLocation().subtract(new Vector(0, 0, 1)).add(new Vector(1, 1, 0)));
            Block fourteen = world.getBlockAt(b.getLocation().subtract(new Vector(1, 0, 0)).add(new Vector(0, 1, 1)));

            // Middle diagonals
            Block fifthteen = world.getBlockAt(b.getLocation().subtract(new Vector(1, 1, 1)));
            Block sixteen = world.getBlockAt(b.getLocation().add(new Vector(1, 0, 1)).subtract(new Vector(0, 1, 0)));
            Block eighteen = world.getBlockAt(b.getLocation().subtract(new Vector(0, 1, 1)).add(new Vector(1, 0, 0)));
            Block nineteen = world.getBlockAt(b.getLocation().subtract(new Vector(1, 1, 0)).add(new Vector(0, 0, 1)));

            if (one.getType().equals(material) && !cBlock.contains(one)) cBlock.add(one);
            if (two.getType().equals(material) && !cBlock.contains(two)) cBlock.add(two);
            if (three.getType().equals(material) && !cBlock.contains(three)) cBlock.add(three);
            if (four.getType().equals(material) && !cBlock.contains(four)) cBlock.add(four);
            if (five.getType().equals(material) && !cBlock.contains(five)) cBlock.add(five);
            if (six.getType().equals(material) && !cBlock.contains(six)) cBlock.add(six);

            if (seven.getType().equals(material) && !cBlock.contains(seven)) cBlock.add(seven);
            if (eight.getType().equals(material) && !cBlock.contains(eight)) cBlock.add(eight);
            if (nine.getType().equals(material) && !cBlock.contains(nine)) cBlock.add(nine);
            if (ten.getType().equals(material) && !cBlock.contains(ten)) cBlock.add(ten);

            if (eleven.getType().equals(material) && !cBlock.contains(eleven)) cBlock.add(eleven);
            if (twelve.getType().equals(material) && !cBlock.contains(twelve)) cBlock.add(twelve);
            if (thirteen.getType().equals(material) && !cBlock.contains(thirteen)) cBlock.add(thirteen);
            if (fourteen.getType().equals(material) && !cBlock.contains(fourteen)) cBlock.add(fourteen);

            if (fifthteen.getType().equals(material) && !cBlock.contains(fifthteen)) cBlock.add(fifthteen);
            if (sixteen.getType().equals(material) && !cBlock.contains(sixteen)) cBlock.add(sixteen);
            if (eighteen.getType().equals(material) && !cBlock.contains(eighteen)) cBlock.add(eighteen);
            if (nineteen.getType().equals(material) && !cBlock.contains(nineteen)) cBlock.add(nineteen);
        }

        return cBlock;
    }
}
