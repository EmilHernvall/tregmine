package info.tregmine.listeners;

import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.FishyBlock;

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
        TregminePlayer sender = plugin.getPlayer(event.getPlayer());
        if (sender.getChatState() != TregminePlayer.ChatState.FISHY_SETUP &&
            sender.getChatState() != TregminePlayer.ChatState.FISHY_WITHDRAW &&
            sender.getChatState() != TregminePlayer.ChatState.FISHY_BUY) {
            return;
        }

        if (sender.getChatState() != TregminePlayer.ChatState.FISHY_SETUP) {

            // expect material
            // optional: expect data
            // optional: expect enchants

            // expect price

            // expect accept
        }
        else if (sender.getChatState() != TregminePlayer.ChatState.FISHY_WITHDRAW) {
            // expect withdraw x or quit
        }
        else if (sender.getChatState() != TregminePlayer.ChatState.FISHY_BUY) {
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

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        Block block = event.getClickedBlock();
        BlockFace face = event.getBlockFace();
        ItemStack heldItem = player.getItemInHand();
        if (heldItem.getType() == Material.RAW_FISH &&
            block.getType() == Material.OBSIDIAN) {

            // Check if this is the players lot

            // Check if this has already been fishyfied

            // We're creating a new fishy block
            Location loc = block.getLocation();
            Location signLoc = new Location(loc.getWorld(),
                                            loc.getX() + face.getModX(),
                                            loc.getY() + face.getModY(),
                                            loc.getZ() + face.getModZ());

            FishyBlock fishyBlock = new FishyBlock();
            fishyBlock.setPlayerId(player.getId());
            fishyBlock.setBlockLocation(loc);
            fishyBlock.setSignLocation(signLoc);

            player.setNewFishyBlock(fishyBlock);
            player.setChatState(TregminePlayer.ChatState.FISHY_SETUP);

            player.sendMessage(ChatColor.GREEN + "You are creating a new " +
                    "fishy block, which can be used to sell items. Please " +
                    "enter what item you would like to sell.");
        }
        else if (block.getType() == Material.SIGN) {
            // The player is interacting with the fishy block
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        Block block = event.getClickedBlock();
        if (block.getType() != Material.OBSIDIAN &&
            block.getType() != Material.SIGN) {

            return;
        }

        // look up location
    }
}
