package info.tregmine.listeners;

import java.text.NumberFormat;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;

import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DonationSigns implements Listener
{
    private Tregmine plugin;

    public DonationSigns(Tregmine tregmine)
    {
        this.plugin = tregmine;
    }

    @EventHandler
    public void donate(PlayerInteractEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        if (event.getAction() == Action.LEFT_CLICK_AIR ||
            event.getAction() == Action.RIGHT_CLICK_AIR) {

            return;
        }

        if (event.getAction() != Action.LEFT_CLICK_BLOCK &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK) {

            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (!clickedBlock.getType().equals(Material.STONE_BUTTON) &&
            !clickedBlock.getType().equals(Material.WOOD_BUTTON)) {

            return;
        }

        Location signLocation = clickedBlock.getLocation();
        World world = player.getWorld();
        Block signBlock = world.getBlockAt(signLocation.getBlockX(),
                                           signLocation.getBlockY() - 1,
                                           signLocation.getBlockZ());
        if (!(signBlock.getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign)signBlock.getState();
        if (!sign.getLine(0).contains("donate")) {
            return;
        }

        try (IContext ctx = plugin.createContext()) {
            IWalletDAO wallet = ctx.getWalletDAO();
            Integer amount = Integer.parseInt(sign.getLine(1).trim());
            NumberFormat format = NumberFormat.getNumberInstance();
            TregminePlayer receiver = plugin.getPlayerOffline(sign.getLine(3).trim());
            if (receiver == null) {
                player.sendMessage(ChatColor.RED +
                        "The player on the sign does not exist!");
                return;
            }

            if (wallet.take(player, amount)) {
                wallet.add(receiver, amount);
                player.sendMessage(ChatColor.DARK_AQUA + "You donated " +
                        ChatColor.GOLD + format.format(amount) + " Tregs "
                        + ChatColor.DARK_AQUA + "to " + receiver.getRealName());
            } else {
                player.sendMessage(ChatColor.RED + "You dont have enough Tregs!");
            }
        } catch (DAOException error) {
            throw new RuntimeException(error);
        } catch (NumberFormatException error) {
        }
    }
}
