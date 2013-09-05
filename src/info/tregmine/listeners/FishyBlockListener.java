package info.tregmine.listeners;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import info.tregmine.quadtree.Point;

import info.tregmine.Tregmine;
import info.tregmine.api.FishyBlock;
import info.tregmine.api.TregminePlayer;
import info.tregmine.zones.Lot;
import info.tregmine.zones.ZoneWorld;

public class FishyBlockListener implements Listener
{
    private Tregmine plugin;

    public FishyBlockListener(Tregmine plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getChatState() != TregminePlayer.ChatState.FISHY_SETUP &&
            player.getChatState() != TregminePlayer.ChatState.FISHY_WITHDRAW &&
            player.getChatState() != TregminePlayer.ChatState.FISHY_BUY) {
            return;
        }

        Map<Location, FishyBlock> fishyBlocks = plugin.getFishyBlocks();

        String text = event.getMessage().trim();
        //String[] textSplit = text.split(" ");

        if (player.getChatState() != TregminePlayer.ChatState.FISHY_SETUP) {

            FishyBlock newFishyBlock = player.getNewFishyBlock();
            if (newFishyBlock == null) {
                player.setChatState(TregminePlayer.ChatState.CHAT);
                return;
            }

            // expect price
            if (newFishyBlock.getCost() == 0) {
                try {
                    int cost = Integer.parseInt(text);
                    if (cost <= 0) {
                        player.sendMessage(ChatColor.RED +
                                "Please enter a positive number.");
                        return;
                    }

                    newFishyBlock.setCost(cost);

                    player.sendMessage(ChatColor.GREEN +
                        "Cost set to " + cost + " tregs. Fishy block set up.");

                    fishyBlocks.put(newFishyBlock.getBlockLocation(), newFishyBlock);
                    fishyBlocks.put(newFishyBlock.getSignLocation(), newFishyBlock);

                    player.setNewFishyBlock(null);

                    // Create info sign
                    World world = player.getWorld();
                    updateSign(world, newFishyBlock);
                }
                catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED +
                            "Please enter a positive number.");
                    return;
                }
            }
        }
        else if (player.getChatState() != TregminePlayer.ChatState.FISHY_WITHDRAW) {
            // expect withdraw x or quit
        }
        else if (player.getChatState() != TregminePlayer.ChatState.FISHY_BUY) {
            // expect buy x or quit
            // expect accept
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Map<Location, FishyBlock> fishyBlocks = plugin.getFishyBlocks();

        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        Block block = event.getClickedBlock();
        BlockFace face = event.getBlockFace();
        Location loc = block.getLocation();

        ItemStack heldItem = player.getItemInHand();

        if (block.getType() == Material.OBSIDIAN) {

            FishyBlock newFishyBlock = player.getNewFishyBlock();

            // We're creating a new fishy block
            if (heldItem.getType() == Material.RAW_FISH && newFishyBlock == null) {

                Location signLoc = new Location(loc.getWorld(),
                                                loc.getX() + face.getModX(),
                                                loc.getY() + face.getModY(),
                                                loc.getZ() + face.getModZ());

                // Check if this is the players lot
                ZoneWorld zoneWorld = plugin.getWorld(player.getWorld());
                Point blockPos = new Point(loc.getBlockX(), loc.getBlockZ());
                Point signPos = new Point(signLoc.getBlockX(), signLoc.getBlockZ());

                Lot blockLot = zoneWorld.findLot(blockPos);
                Lot signLot = zoneWorld.findLot(signPos);

                // Make sure that both the block and the sign are in an lot
                if (blockLot == null && signLot == null) {
                    player.sendMessage(ChatColor.RED +
                        "Fishy blocks can only be created in lots.");
                    return;
                }
                // In case one of them is, but the other one isn't
                else if (blockLot == null || signLot == null) {
                    player.sendMessage(ChatColor.RED +
                            "Too close to the edge of the lot.");
                    return;
                }
                // Make sure it's the same lot
                else if (blockLot.getId() != signLot.getId()) {
                    player.sendMessage(ChatColor.RED +
                            "Too close to the edge of the lot.");
                    return;
                }
                // Since we've already verified that it's the same lot, we only
                // need to check the owner for one of the lots
                else if (!blockLot.isOwner(player.getName())) {
                    player.sendMessage(ChatColor.RED +
                            "You have to be owner of the lot to create fishy blocks.");
                    return;
                }

                // Check if this has already been fishyfied
                if (fishyBlocks.containsKey(loc) ||
                    fishyBlocks.containsKey(signLoc)) {

                    player.sendMessage(ChatColor.RED + "This block has already " +
                            "been fishyfied.");
                    return;
                }

                World world = block.getWorld();
                Block signBlock = world.getBlockAt(signLoc);
                if (signBlock.getType() != Material.AIR) {
                    player.sendMessage(ChatColor.RED + "There must be at least one " +
                            "empty block in front of the block being fishyfied.");
                    return;
                }

                FishyBlock fishyBlock = new FishyBlock();
                fishyBlock.setPlayerId(player.getId());
                fishyBlock.setBlockLocation(loc);
                fishyBlock.setSignLocation(signLoc);

                player.setNewFishyBlock(fishyBlock);

                player.sendMessage(ChatColor.GREEN + "You are creating a new " +
                        "fishy block, which can be used to sell items. Now " +
                        "select the item or material you want to sell, " +
                        "and right click on this block again.");

                fishyBlocks.put(loc, fishyBlock);
                fishyBlocks.put(signLoc, fishyBlock);
            }
            // This is when the player sets the type of the fishy block
            else if (newFishyBlock != null &&
                     loc.equals(newFishyBlock.getBlockLocation())) {

                MaterialData material = heldItem.getData();
                Map<Enchantment, Integer> enchantments = heldItem.getEnchantments();

                newFishyBlock.setMaterial(material);
                newFishyBlock.setEnchantments(enchantments);

                player.setChatState(TregminePlayer.ChatState.FISHY_SETUP);

                if (material.getData() != 0) {
                    player.sendMessage(ChatColor.GREEN +
                        "This fishy block will sell " +
                        material.getItemType().toString() + ":" +
                        material.getData() + ".");
                } else {
                    player.sendMessage(ChatColor.GREEN +
                        "This fishy block will sell " +
                        material.getItemType().toString() + ".");
                }

                if (enchantments.size() > 0) {
                    player.sendMessage(ChatColor.GREEN + "With the following enchants:");
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        Enchantment enchant = entry.getKey();
                        Integer level = entry.getValue();
                        player.sendMessage(enchant.toString() + " lvl " + level);
                    }
                }

                player.sendMessage(ChatColor.YELLOW +
                        "How much should one item cost?");
                return;
            }
        }

        // Whenever someone clicks on a fishy block that's already setup
        if (fishyBlocks.containsKey(loc)) {

            FishyBlock fishyBlock = fishyBlocks.get(loc);

            // Player owns this fishy block, and should either enter withdraw
            // mode or add items to this fishy block
            if (fishyBlock.getPlayerId() == player.getId()) {

                // Check if the held item equals the type of the fishy block
                MaterialData fishyMaterial = fishyBlock.getMaterial();
                MaterialData heldMaterial = heldItem.getData();
                boolean match = false;
                if (fishyMaterial.equals(heldMaterial)) {
                    match = true;

                    Map<Enchantment, Integer> fishyEnchants = fishyBlock.getEnchantments();
                    Map<Enchantment, Integer> heldEnchants = heldItem.getEnchantments();
                    if (fishyEnchants.size() == heldEnchants.size()) {
                        for (Enchantment ench : fishyEnchants.keySet()) {
                            Integer a = fishyEnchants.get(ench);
                            Integer b = heldEnchants.get(ench);
                            if (a != b) {
                                match = false;
                                break;
                            }
                        }
                    } else {
                        match = false;
                    }
                }

                // Add to block inventory
                if (match) {
                    fishyBlock.addAvailableInventory(heldItem.getAmount());
                    player.setItemInHand(null);

                    player.sendMessage(ChatColor.GREEN + "" +
                            heldItem.getAmount() + " items added to fishy block.");

                    updateSign(player.getWorld(), fishyBlock);
                }
                // Enter withdrawal mode
                else {
                    player.sendMessage(ChatColor.YELLOW + "There are " +
                        fishyBlock.getAvailableInventory() + " items available. ");
                    player.sendMessage(ChatColor.YELLOW +
                        "Type \"withdraw x\" to withdraw items to your inventory.");
                    player.sendMessage(ChatColor.YELLOW +
                        "Type \"quit\" to exit without doing anything.");

                    player.setChatState(TregminePlayer.ChatState.FISHY_WITHDRAW);
                    player.setCurrentFishyBlock(fishyBlock);
                }
            }

            // This is somebody else, and the should enter buy mode
            else {
                MaterialData material = fishyBlock.getMaterial();
                if (material.getData() != 0) {
                    player.sendMessage(ChatColor.YELLOW +
                        "You are now talking to a fishy block that is selling: " +
                        material.getItemType().toString() + ":" +
                        material.getData() + ".");
                } else {
                    player.sendMessage(ChatColor.YELLOW +
                        "You are now talking to a fishy block that is selling: " +
                        material.getItemType().toString() + ".");
                }

                Map<Enchantment, Integer> enchantments = fishyBlock.getEnchantments();
                if (enchantments.size() > 0) {
                    player.sendMessage(ChatColor.YELLOW +
                            "With the following enchants:");
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        Enchantment enchant = entry.getKey();
                        Integer level = entry.getValue();
                        player.sendMessage(ChatColor.YELLOW +
                                enchant.toString() + " lvl " + level);
                    }
                }

                player.sendMessage(ChatColor.YELLOW + "Each block is " +
                    fishyBlock.getCost() + " tregs. Type \"buy x\" to buy, " +
                    "with x being the number of items you want to buy. Type " +
                    "\"quit\" to exit without buying anything.");

                player.setChatState(TregminePlayer.ChatState.FISHY_BUY);
                player.setCurrentFishyBlock(fishyBlock);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        Block block = event.getBlock();
        if (block.getType() != Material.OBSIDIAN &&
            block.getType() != Material.SIGN) {

            return;
        }

        // look up location
    }

    private void updateSign(World world, FishyBlock fishyBlock)
    {
        Block signBlock = world.getBlockAt(fishyBlock.getSignLocation());
        signBlock.setType(Material.SIGN);

        TregminePlayer player = plugin.getPlayerOffline(fishyBlock.getPlayerId());

        Sign sign = (Sign)signBlock.getState();
        sign.setLine(0, player.getChatName());
        sign.setLine(1, fishyBlock.getMaterial().toString());
        sign.setLine(2, fishyBlock.getCost() + " tregs");
        sign.setLine(3, fishyBlock.getAvailableInventory() + " available");
    }
}
