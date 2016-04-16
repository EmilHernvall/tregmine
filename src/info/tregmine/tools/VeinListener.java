package info.tregmine.tools;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class VeinListener implements Listener
{
    private Tregmine plugin;

    public VeinListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    
    @SuppressWarnings({ "deprecation", "static-access" })
    @EventHandler
    public void VeinMiner(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        if (!block.getType().equals(Material.GOLD_ORE) &&
            !block.getType().equals(Material.DIAMOND_ORE) &&
            !block.getType().equals(Material.IRON_ORE) &&
            !block.getType().equals(Material.REDSTONE_ORE) &&
            !block.getType().equals(Material.EMERALD_ORE) &&
            !block.getType().equals(Material.COAL_ORE) &&
            !block.getType().equals(Material.LAPIS_ORE)) {

            return;
        }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        if (!player.getRank().canUseTools()) {
            return;
        }
        if (player.getItemInHand() == null) {
            return;
        }

        ItemStack heldItem = player.getItemInHand();
        if (heldItem.getItemMeta() == null) {
            return;
        }
        if (heldItem.getItemMeta().getLore() == null) {
            return;
        }

        List<String> lore = heldItem.getItemMeta().getLore();
        if (lore.isEmpty()) {
            return;
        }
        if (!lore.get(0).equals(ToolsRegistry.VeinMinerLoreTag)) {
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
        Map<Enchantment, Integer> enchantments = itemm.getEnchantments();
        int blocksBroken = 0;
        boolean running = true;

        for(Block b : connectedBlocks){

            if (!player.hasBlockPermission(b.getLocation(), false)) continue;

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

            ItemStack item = null;

            if (enchantments.containsKey(Enchantment.SILK_TOUCH)) {

                if(b.getType().equals(Material.DIAMOND_ORE)) item = new ItemStack(Material.DIAMOND_ORE, 1, b.getData());
                if(b.getType().equals(Material.IRON_ORE)) item = new ItemStack(Material.IRON_ORE, 1, b.getData());
                if(b.getType().equals(Material.GOLD_ORE)) item = new ItemStack(Material.GOLD_ORE, 1, b.getData());
                if(b.getType().equals(Material.EMERALD_ORE)) item = new ItemStack(Material.EMERALD_ORE, 1, b.getData());
                if(b.getType().equals(Material.COAL_ORE)) item = new ItemStack(Material.COAL_ORE, 1, b.getData());
                if(b.getType().equals(Material.LAPIS_ORE)) item = new ItemStack(Material.LAPIS_ORE, 1, b.getData());
                if(b.getType().equals(Material.REDSTONE_ORE)) item = new ItemStack(Material.REDSTONE_ORE, 1, b.getData());

            } else if (enchantments.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {

                int level = itemm.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                int multiplyer = 0;

                Random rand = new Random();

                while (multiplyer == 0) {
                    if (level == 1) {
                        if (rand.nextInt(3) <= 0) multiplyer = 1;
                    } else if (level == 2) {
                        int random = rand.nextInt(3);
                        if (random <= 1) multiplyer = random;
                    } else if (level == 3) {
                        int random = rand.nextInt(3);
                        if (random <= 2) multiplyer = random;
                    }
                }

                if(b.getType().equals(Material.DIAMOND_ORE)) item = new ItemStack(Material.DIAMOND, (1 * multiplyer), b.getData());
                if(b.getType().equals(Material.IRON_ORE)) item = new ItemStack(Material.IRON_ORE, (1 * multiplyer), b.getData());
                if(b.getType().equals(Material.GOLD_ORE)) item = new ItemStack(Material.GOLD_ORE, (1 * multiplyer), b.getData());
                if(b.getType().equals(Material.EMERALD_ORE)) item = new ItemStack(Material.EMERALD, (1 * multiplyer), b.getData());
                if(b.getType().equals(Material.COAL_ORE)) item = new ItemStack(Material.COAL, (1 * multiplyer), b.getData());
                if(b.getType().equals(Material.LAPIS_ORE)) item = new ItemStack(Material.INK_SACK, (fortuneRandomiser(4, 8) * multiplyer), (byte) 4);
                if(b.getType().equals(Material.REDSTONE_ORE)) item = new ItemStack(Material.REDSTONE, (fortuneRandomiser(4, 5) * multiplyer), b.getData());

            } else {

                if(b.getType().equals(Material.DIAMOND_ORE)) item = new ItemStack(Material.DIAMOND, 1, b.getData());
                if(b.getType().equals(Material.IRON_ORE)) item = new ItemStack(Material.IRON_ORE, 1, b.getData());
                if(b.getType().equals(Material.GOLD_ORE)) item = new ItemStack(Material.GOLD_ORE, 1, b.getData());
                if(b.getType().equals(Material.EMERALD_ORE)) item = new ItemStack(Material.EMERALD, 1, b.getData());
                if(b.getType().equals(Material.COAL_ORE)) item = new ItemStack(Material.COAL, 1, b.getData());
                if(b.getType().equals(Material.LAPIS_ORE)) item = new ItemStack(Material.INK_SACK, fortuneRandomiser(4, 8), (byte) 4);
                if(b.getType().equals(Material.REDSTONE_ORE)) item = new ItemStack(Material.REDSTONE, fortuneRandomiser(4, 5), b.getData());

            }


            b.getWorld().dropItemNaturally(player.getLocation(), item);
            b.setType(Material.AIR);
            blocksBroken++;
        }

        if (blocksBroken > 0) {
            player.sendMessage("You vein mined an ore vein containing " + blocksBroken + " ores!");
            plugin.LOGGER.info(player.getRealName() + " broke " + blocksBroken + " using VeinMiner!");
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

            Block seven = world.getBlockAt(b.getLocation().subtract(new Vector(1, 0, 1)));
            Block eight = world.getBlockAt(b.getLocation().add(new Vector(1, 0, 1)));
            Block nine = world.getBlockAt(b.getLocation().subtract(new Vector(0, 0, 1)).add(new Vector(1, 0, 0)));
            Block ten = world.getBlockAt(b.getLocation().subtract(new Vector(1, 0, 0)).add(new Vector(0, 0, 1)));

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
        }

        return cBlock;
    }

    public int fortuneRandomiser(int low, int high)
    {
        int amount = 0;

        Random random = new Random();

        while(!(amount > low && amount < high)){
            amount = random.nextInt(high);
        }

        return amount;
    }
}
