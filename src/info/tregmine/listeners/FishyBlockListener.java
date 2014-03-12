package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.FishyBlock;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.*;
import info.tregmine.database.IFishyBlockDAO.TransactionType;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Lot;
import info.tregmine.zones.ZoneWorld;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.*;
import org.bukkit.scheduler.BukkitScheduler;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

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

        event.setCancelled(true);

        Map<Location, FishyBlock> fishyBlocks = plugin.getFishyBlocks();

        String text = event.getMessage().trim();
        String[] textSplit = text.split(" ");

        player.sendMessage(ChatColor.YELLOW + "[FISHY] " +
                ChatColor.WHITE + "<" +
                player.getChatName() +
                ChatColor.WHITE + "> " + text);

        if (player.getChatState() == TregminePlayer.ChatState.FISHY_SETUP) {

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
                    player.setChatState(TregminePlayer.ChatState.CHAT);

                    // Create info sign
                    World world = player.getWorld();
                    updateSign(world, newFishyBlock);

                    try (IContext ctx = plugin.createContext()) {
                        IFishyBlockDAO fishyBlockDAO = ctx.getFishyBlockDAO();
                        fishyBlockDAO.insert(newFishyBlock);
                    } catch (DAOException e) {
                        throw new RuntimeException(e);
                    }
                }
                catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED +
                            "Please enter a positive number.");
                    return;
                }
            }
        }
        else if (player.getChatState() == TregminePlayer.ChatState.FISHY_WITHDRAW) {
            FishyBlock fishyBlock = player.getCurrentFishyBlock();

            if ("changecost".equalsIgnoreCase(textSplit[0])) {
                if (textSplit.length != 2) {
                    player.sendMessage(ChatColor.RED + "Type \"changecost x\", with " +
                            "x being the cost in tregs of the item.");
                    return;
                }

                int cost = 0;
                try {
                    cost = Integer.parseInt(textSplit[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Type \"changecost x\", with " +
                            "x being the cost in tregs of the item.");
                    return;
                }

                if (cost <= 0) {
                    player.sendMessage(ChatColor.RED + "Type \"changecost x\", with " +
                            "x being the cost in tregs of the item.");
                    return;
                }

                int oldCost = fishyBlock.getCost();
                fishyBlock.setCost(cost);

                player.sendMessage(ChatColor.GREEN + "Cost changed to " +
                        cost + " tregs.");

                player.setChatState(TregminePlayer.ChatState.CHAT);
                player.setCurrentFishyBlock(null);

                updateSign(player.getWorld(), fishyBlock);

                try (IContext ctx = plugin.createContext()) {
                    IFishyBlockDAO fishyBlockDAO = ctx.getFishyBlockDAO();
                    fishyBlockDAO.update(fishyBlock);
                    fishyBlockDAO.insertCostChange(fishyBlock, oldCost);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if ("withdraw".equalsIgnoreCase(textSplit[0])) {
                if (textSplit.length != 2) {
                    player.sendMessage(ChatColor.RED + "Type \"withdraw x\", with " +
                            "x being the number of items you wish to withdraw.");
                    return;
                }

                int num = 0;
                if ("all".equalsIgnoreCase(textSplit[1])) {
                    int available = fishyBlock.getAvailableInventory();
                    num = available;
                } else {
                    try {
                        num = Integer.parseInt(textSplit[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Type \"withdraw x\", with " +
                                "x being the number of items you wish to withdraw.");
                        return;
                    }
                }

                if (num <= 0) {
                    player.sendMessage(ChatColor.RED + "Type \"withdraw x\", with " +
                            "x being the number of items you wish to withdraw.");
                    return;
                }

                int available = fishyBlock.getAvailableInventory();
                if (num > available) {
                    player.sendMessage(ChatColor.RED + "There are only " +
                            available + "items available.");
                    return;
                }

                int added = transferToInventory(fishyBlock, player, num);

                if (num != added) {
                    player.sendMessage(ChatColor.GREEN + "Your inventory is full. " +
                        "Could only withdraw " + added + " items.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "" +
                        added + " items withdrawn successfully.");
                }

                player.setChatState(TregminePlayer.ChatState.CHAT);
                player.setCurrentFishyBlock(null);

                updateSign(player.getWorld(), fishyBlock);

                try (IContext ctx = plugin.createContext()) {
                    IFishyBlockDAO fishyBlockDAO = ctx.getFishyBlockDAO();
                    fishyBlockDAO.update(fishyBlock);
                    fishyBlockDAO.insertTransaction(fishyBlock,
                                                    player,
                                                    TransactionType.WITHDRAW,
                                                    added);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if ("quit".equalsIgnoreCase(textSplit[0])) {
                player.sendMessage(ChatColor.GREEN +
                    "Quitting without action.");
                player.setChatState(TregminePlayer.ChatState.CHAT);
                player.setCurrentFishyBlock(null);
            }
            else {
                player.sendMessage(ChatColor.RED +
                                   "Type withdraw, changecost or quit.");
            }
        }
        else if (player.getChatState() == TregminePlayer.ChatState.FISHY_BUY) {
            FishyBlock fishyBlock = player.getCurrentFishyBlock();

            if ("buy".equalsIgnoreCase(textSplit[0])) {
                if (textSplit.length != 2) {
                    player.sendMessage(ChatColor.RED + "Type \"buy x\", with " +
                            "x being the number of items you wish to biy.");
                    return;
                }

                int num = 0;
                try {
                    num = Integer.parseInt(textSplit[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Type \"buy x\", with " +
                            "x being the number of items you wish to biy.");
                    return;
                }

                if (num <= 0) {
                    player.sendMessage(ChatColor.RED + "Type \"buy x\", with " +
                            "x being the number of items you wish to buy.");
                    return;
                }

                int available = fishyBlock.getAvailableInventory();
                if (num > available) {
                    player.sendMessage(ChatColor.RED + "There are only " +
                            available + "items available.");
                    return;
                }

                player.setFishyBuyCount(num);

                int cost = num * fishyBlock.getCost();

                player.sendMessage(ChatColor.YELLOW + "Do you wish to buy " +
                    num + " items for a total cost of " + cost + " tregs?");
                player.sendMessage(ChatColor.YELLOW + "Type \"accept\" to confirm " +
                    "or quit to exit.");
            }
            else if ("accept".equalsIgnoreCase(textSplit[0])) {
                if (player.getFishyBuyCount() == 0) {
                    player.sendMessage(ChatColor.RED + "Please specify how many " +
                            "items you wish to buy using \"buy x\".");
                    return;
                }

                int num = player.getFishyBuyCount();

                // Check availability again to make sure, in case someone else
                // is buying at the same time
                int available = fishyBlock.getAvailableInventory();
                if (num > available) {
                    player.sendMessage(ChatColor.RED + "There are only " +
                            available + "items available.");
                    return;
                }

                int cost = num * fishyBlock.getCost();
                try (IContext dbCtx = plugin.createContext()) {
                    IWalletDAO walletDAO = dbCtx.getWalletDAO();

                    long balance = walletDAO.balance(player);
                    if (balance < cost) {
                        player.sendMessage(ChatColor.RED + "You do not have " +
                            "enough money to complete your purchase.");
                        return;
                    }

                    int added = transferToInventory(fishyBlock, player, num);

                    if (num != added) {
                        player.sendMessage(ChatColor.GREEN + "Your inventory is full. " +
                            "Could only buy " + added + " items.");
                        cost = added * fishyBlock.getCost();
                    } else {
                        player.sendMessage(ChatColor.GREEN + "" +
                            added + " items added to your inventory.");
                    }

                    TregminePlayer seller =
                        plugin.getPlayerOffline(fishyBlock.getPlayerId());

                    if (walletDAO.take(player, cost)) {
                        walletDAO.add(seller, cost);
                        walletDAO.insertTransaction(player.getId(), seller.getId(),
                                cost);

                        player.sendMessage(ChatColor.GREEN + "" + cost
                                + " tregs was taken from your wallet!");

                        IFishyBlockDAO fishyBlockDAO = dbCtx.getFishyBlockDAO();
                        fishyBlockDAO.update(fishyBlock);
                        fishyBlockDAO.insertTransaction(fishyBlock,
                                                        player,
                                                        TransactionType.BUY,
                                                        added);

                    }
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }

                player.setCurrentFishyBlock(null);
                player.setFishyBuyCount(0);
                player.setChatState(TregminePlayer.ChatState.CHAT);

                updateSign(player.getWorld(), fishyBlock);
            }
            else if ("quit".equalsIgnoreCase(textSplit[0])) {
                player.sendMessage(ChatColor.GREEN +
                    "Quitting without buying.");
                player.setChatState(TregminePlayer.ChatState.CHAT);
                player.setCurrentFishyBlock(null);
            }
            else {
                player.sendMessage(ChatColor.RED + "Type buy or quit.");
            }

        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Map<Location, FishyBlock> fishyBlocks = plugin.getFishyBlocks();

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getChatState() != TregminePlayer.ChatState.CHAT) {
            return;
        }

        Block block = event.getClickedBlock();
        BlockFace face = event.getBlockFace();
        Location loc = block.getLocation();

        ItemStack heldItem = player.getItemInHand();

        // Whenever someone clicks on a fishy block that's already setup
        if (fishyBlocks.containsKey(loc)) {

            if (player.getGameMode() == GameMode.CREATIVE) {
                player.sendMessage(ChatColor.RED + "Cannot use fishy blocks " +
                        "whilst in creative mode.");
                event.setCancelled(true);
                return;
            }

            if (player.getChatState() == TregminePlayer.ChatState.FISHY_WITHDRAW ||
                player.getChatState() == TregminePlayer.ChatState.FISHY_BUY) {

                event.setCancelled(true);
                return;
            }

            FishyBlock fishyBlock = fishyBlocks.get(loc);

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
                if (fishyBlock.hasStoredEnchantments()) {
                    player.sendMessage(ChatColor.YELLOW +
                            "With the following STORED enchants:");
                } else {
                    player.sendMessage(ChatColor.YELLOW +
                            "With the following enchants:");
                }
                try (IContext dbCtx = plugin.createContext()) {
                    IEnchantmentDAO enchantDAO = dbCtx.getEnchantmentDAO();
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        Enchantment enchant = entry.getKey();
                        Integer level = entry.getValue();
                        String enchantName = enchantDAO.localize(enchant.getName());
                        player.sendMessage("- " + enchantName +
                                " Level: " + level.toString());
                    }
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }
            }

            // Find the lot of which the fishyblock is in
            // Won't return null as a fishyblock has to be in a lot
            ZoneWorld world = plugin.getWorld(block.getWorld());
            Lot lot = world.findLot(new Point(loc.getBlockX(), loc.getBlockZ()));

            // Player owns this fishy block, and should either enter withdraw
            // mode or add items to this fishy block
            if (fishyBlock.getPlayerId() == player.getId() ||
                (lot.isOwner(player) && lot.hasFlag(Lot.Flags.FISHY_SHARE))) {

                // Check if the held item equals the type of the fishy block
                MaterialData fishyMaterial = fishyBlock.getMaterial();
                if (fishyMaterial == null) {
                    return;
                }

                MaterialData heldMaterial = heldItem.getData();

                boolean match = false;
                boolean all = false;
                if (fishyMaterial.equals(heldMaterial)) {
                    match = true;

                    if (player.isSneaking()) {
                        all = true;
                    }

                    if (fishyBlock.hasStoredEnchantments()) {
                        EnchantmentStorageMeta storageMeta = getStorageMeta(heldItem);
                        if (storageMeta != null) {
                            match = compareEnchants(fishyBlock.getEnchantments(),
                                                    storageMeta.getStoredEnchants());
                        }
                        else if (fishyBlock.getEnchantments().size() > 0) {
                            match = false;
                        }
                    } else {
                        match = compareEnchants(fishyBlock.getEnchantments(),
                                                heldItem.getEnchantments());
                    }
                }

                // Add to block inventory
                if (match) {
                    Material type = heldMaterial.getItemType();
                    if (type.getMaxDurability() != 0 && heldMaterial.getData() != 0) {
                        player.sendMessage(ChatColor.RED + "You cannot add " +
                                "damaged items.");
                        return;
                    }

                    int allAmount = 0;
                    boolean massEnchant = false;
                    if (all) {
                        for (ItemStack i : player.getInventory().getContents()) {
                            boolean allow = true;
                            if (i == null) {
                                continue; // Get rid of NPE
                            }
                            if (i.getType().getMaxDurability() != 0 && i.getData().getData() != 0) {
                                continue; // Ignore damaged items
                            }
                            if (!fishyMaterial.equals(i.getData())) {
                                continue; // Ignore items that do not match
                            }
                            if (fishyBlock.hasStoredEnchantments()) {
                                if (massEnchant == false) {
                                    player.sendMessage(ChatColor.RED +
                                            "Mass Submition only works with non enchanted items and blocks.");
                                    massEnchant = true;
                                }
                                continue;
                            }
                            if (i.getEnchantments().size() > 0) {
                                continue;
                            }
                            if (!allow) {
                                continue;
                            }
                            allAmount += i.getAmount();
                            player.getInventory().remove(i);
                        }
                    } else {
                        allAmount = heldItem.getAmount();
                        player.setItemInHand(null);
                    }

                    fishyBlock.addAvailableInventory(allAmount);
                    player.sendMessage(ChatColor.GREEN + "" +
                            allAmount + " items added to fishy block.");

                    updateSign(player.getWorld(), fishyBlock);

                    try (IContext ctx = plugin.createContext()) {
                        IFishyBlockDAO fishyBlockDAO = ctx.getFishyBlockDAO();
                        fishyBlockDAO.update(fishyBlock);
                        fishyBlockDAO.insertTransaction(fishyBlock,
                                                        player,
                                                        TransactionType.DEPOSIT,
                                                        allAmount);
                    } catch (DAOException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Enter withdrawal mode
                else {
                    player.sendMessage(ChatColor.YELLOW + "There are " +
                        fishyBlock.getAvailableInventory() + " items available. ");
                    player.sendMessage(ChatColor.YELLOW +
                        "Type \"withdraw x\" to withdraw items to your inventory.");
                    player.sendMessage(ChatColor.YELLOW +
                        "Type \"changecost x\" to change the cost of the items " +
                        "sold by this block.");
                    player.sendMessage(ChatColor.YELLOW +
                        "Type \"quit\" to exit without doing anything.");

                    player.setChatState(TregminePlayer.ChatState.FISHY_WITHDRAW);
                    player.setCurrentFishyBlock(fishyBlock);
                }
            }

            // This is somebody else, and the should enter buy mode
            else {
                event.setCancelled(true);

                player.sendMessage(ChatColor.YELLOW + "Each block is " +
                    fishyBlock.getCost() + " tregs. Type \"buy x\" to buy, " +
                    "with x being the number of items you want to buy. Type " +
                    "\"quit\" to exit without buying anything.");

                player.setChatState(TregminePlayer.ChatState.FISHY_BUY);
                player.setCurrentFishyBlock(fishyBlock);
            }
        }
        else if (block.getType() == Material.OBSIDIAN) {

            FishyBlock newFishyBlock = player.getNewFishyBlock();

            // We're creating a new fishy block
            if (heldItem.getType() == Material.RAW_FISH && newFishyBlock == null) {

                if (player.getGameMode() == GameMode.CREATIVE) {
                    player.sendMessage(ChatColor.RED + "Cannot use fishy blocks " +
                            "whilst in creative mode.");
                    event.setCancelled(true);
                    return;
                }

                if (face.getModY() != 0) {
                    player.sendMessage(ChatColor.RED + "Click on the sides of " +
                            "the block to set up a fishy block.");
                    return;
                }

                event.setCancelled(true);

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
                else if (!blockLot.isOwner(player)) {
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
                        "and left click on this block again.");

                heldItem.setAmount(heldItem.getAmount()-1);
                player.setItemInHand(heldItem);
            }
            // This is when the player sets the type of the fishy block
            else if (newFishyBlock != null &&
                     loc.equals(newFishyBlock.getBlockLocation())) {

                if (player.getGameMode() == GameMode.CREATIVE) {
                    player.sendMessage(ChatColor.RED + "Cannot use fishy blocks " +
                            "whilst in creative mode.");
                    event.setCancelled(true);
                    return;
                }

                event.setCancelled(true);

                MaterialData material = heldItem.getData();
                Material type = material.getItemType();
                // This is an item with limited durability that has been
                // damaged
                if (type.getMaxDurability() != 0 && material.getData() != 0) {
                    player.sendMessage(ChatColor.RED + "You cannot sell " +
                            "damaged items.");
                    return;
                }

                Map<Enchantment, Integer> enchantments = heldItem.getEnchantments();
                if (enchantments == null || enchantments.size() == 0) {
                    if (heldItem.hasItemMeta()) {
                        EnchantmentStorageMeta enchantMeta =
                            getStorageMeta(heldItem);
                        if (enchantMeta != null) {
                            enchantments = enchantMeta.getStoredEnchants();
                            newFishyBlock.setStoredEnchantments(true);
                        }
                    }
                }

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
                    if (newFishyBlock.hasStoredEnchantments()) {
                        player.sendMessage(ChatColor.GREEN +
                                "With the following STORED enchants:");
                    } else {
                        player.sendMessage(ChatColor.GREEN +
                                "With the following enchants:");
                    }
                    try (IContext dbCtx = plugin.createContext()) {
                        IEnchantmentDAO enchantDAO = dbCtx.getEnchantmentDAO();
                        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                            Enchantment enchant = entry.getKey();
                            Integer level = entry.getValue();
                            String enchantName = enchantDAO.localize(enchant.getName());
                            player.sendMessage("- " + enchantName +
                                    " Level: " + level.toString());
                        }
                    } catch (DAOException e) {
                        throw new RuntimeException(e);
                    }
                }

                player.sendMessage(ChatColor.YELLOW +
                        "How much should one item cost?");
                return;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        Block block = event.getBlock();
        Location loc = block.getLocation();

        Map<Location, FishyBlock> fishyBlocks = plugin.getFishyBlocks();
        if (!fishyBlocks.containsKey(loc)) {
            return;
        }

        FishyBlock fishyBlock = fishyBlocks.get(loc);
        if (fishyBlock.getPlayerId() != player.getId()) {
            event.setCancelled(true);
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(ChatColor.RED + "Cannot use fishy blocks " +
                    "whilst in creative mode.");
            event.setCancelled(true);
            return;
        }

        Location blockLoc = fishyBlock.getBlockLocation();
        Location signLoc = fishyBlock.getSignLocation();

        if (signLoc.equals(loc)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot delete the sign " +
                    "for a fishy block. Delete the obisidan to remove it.");
            return;
        }
        else if (blockLoc.equals(loc)) {
            player.sendMessage(ChatColor.GREEN + "Fishy block deleted.");

            try (IContext ctx = plugin.createContext()) {
                IFishyBlockDAO fishyBlockDAO = ctx.getFishyBlockDAO();
                fishyBlockDAO.delete(fishyBlock);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }

            fishyBlocks.remove(blockLoc);
            fishyBlocks.remove(signLoc);

            World world = player.getWorld();
            Block signBlock = world.getBlockAt(fishyBlock.getSignLocation());
            signBlock.setType(Material.AIR);

            FishyBlock currentFishyBlock = player.getCurrentFishyBlock();
            if (currentFishyBlock.getId() == fishyBlock.getId()) {
                player.setCurrentFishyBlock(null);
                player.setChatState(TregminePlayer.ChatState.CHAT);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void updateSign(final World world, final FishyBlock fishyBlock)
    {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTask(plugin,
            new Runnable() {
                @Override
                public void run() {
                    Location blockLoc = fishyBlock.getBlockLocation();
                    Location signLoc = fishyBlock.getSignLocation();

                    // force obsidian
                    world.getBlockAt(blockLoc).setType(Material.OBSIDIAN);

                    Block signBlock = world.getBlockAt(signLoc);
                    Block mainBlock = world.getBlockAt(blockLoc);
                    BlockFace facing = mainBlock.getFace(signBlock);

                    signBlock.setType(Material.WALL_SIGN);
                    switch (facing) {
                        case WEST:
                            signBlock.setData((byte)0x04);
                            break;
                        case EAST:
                            signBlock.setData((byte)0x05);
                            break;
                        case NORTH:
                            signBlock.setData((byte)0x02);
                            break;
                        case SOUTH:
                            signBlock.setData((byte)0x03);
                            break;
                        default:
                            break;
                    }
                    //signBlock.getState().setData(new MaterialData(Material.WALL_SIGN));

                    TregminePlayer player = plugin.getPlayerOffline(fishyBlock.getPlayerId());
                    MaterialData material = fishyBlock.getMaterial();

                    Sign sign = (Sign)signBlock.getState();
                    sign.setLine(0, player.getChatName());
                    if (material.getData() != 0) {
                        sign.setLine(1, material.toString());
                    } else {
                        sign.setLine(1, material.getItemType().toString());
                    }
                    sign.setLine(2, fishyBlock.getCost() + " tregs");
                    sign.setLine(3, fishyBlock.getAvailableInventory() + " available");
                    sign.update();
                }
            });
    }

    private int transferToInventory(FishyBlock fishyBlock,
                                    TregminePlayer player,
                                    int num)
    {
        MaterialData type = fishyBlock.getMaterial();
        Material material = type.getItemType();

        int stacks = num / material.getMaxStackSize();
        Inventory inventory = player.getInventory();
        int added = 0;
        boolean full = false;
        for (int i = 0; i < stacks; i++) {
            ItemStack stack = type.toItemStack(material.getMaxStackSize());
            addEnchants(stack, fishyBlock);

            HashMap<Integer, ItemStack> notAdded = inventory.addItem(stack);

            added += material.getMaxStackSize();
            if (notAdded.size() > 0) {
                full = true;
                for (ItemStack partialStack : notAdded.values()) {
                    added -= partialStack.getAmount();
                }
            }
        }

        if (!full) {
            int rem = num % material.getMaxStackSize();
            if (rem != 0) {
                ItemStack stack = type.toItemStack(rem);
                addEnchants(stack, fishyBlock);
                inventory.addItem(stack);
                added += rem;
            }
        }

        fishyBlock.removeAvailableInventory(added);

        return added;
    }

    private void addEnchants(ItemStack stack, FishyBlock fishyBlock)
    {
        Map<Enchantment, Integer> enchants = fishyBlock.getEnchantments();
        if (fishyBlock.hasStoredEnchantments()) {
            EnchantmentStorageMeta enchantMeta = getStorageMeta(stack);
            for (Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
                enchantMeta.addStoredEnchant(enchant.getKey(),
                                             enchant.getValue(),
                                             false);
            }
            stack.setItemMeta(enchantMeta);
        } else {
            stack.addEnchantments(fishyBlock.getEnchantments());
        }
    }

    private EnchantmentStorageMeta getStorageMeta(ItemStack stack)
    {
        ItemMeta meta = stack.getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            return (EnchantmentStorageMeta)meta;
        }

        return null;
    }

    private boolean compareEnchants(Map<Enchantment, Integer> fst,
                                    Map<Enchantment, Integer> snd)
    {
        boolean match = true;
        if (fst.size() == snd.size()) {
            for (Enchantment ench : fst.keySet()) {
                Integer a = fst.get(ench);
                Integer b = snd.get(ench);
                if (a != b) {
                    match = false;
                    break;
                }
            }
        } else {
            match = false;
        }

        return match;
    }
}
