package info.tregmine.commands;

import java.util.List;
import java.util.ArrayList;

import static org.bukkit.ChatColor.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.lore.Created;

public class ItemCommand extends AbstractCommand
{
    public ItemCommand(Tregmine tregmine)
    {
        super(tregmine, "item");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length == 0) {
            return false;
        }
        if (!player.getRank().canSpawnItems()) {
            return false;
        }

        String param = args[0].toUpperCase();

        int materialId;
        try {
            materialId = Integer.parseInt(param);
        } catch (NumberFormatException e) {
            try {
                Material material = Material.getMaterial(param);
                materialId = material.getId();
            } catch (NullPointerException ne) {
                player.sendMessage(DARK_AQUA
                        + "/item <id|name> <amount> <data>.");
                return true;
            }
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            amount = 1;
        } catch (NumberFormatException e) {
            amount = 1;
        }

        int data;
        try {
            data = Integer.parseInt(args[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            data = 0;
        } catch (NumberFormatException e) {
            data = 0;
        }

        ItemStack item = new ItemStack(materialId, amount, (byte) data);
        if (item.getType() == Material.MONSTER_EGG || item.getType() == Material.NAME_TAG) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add(Created.SPAWNED.toColorString());
        lore.add(WHITE + "by: " + player.getName());
        lore.add(WHITE + "Value: " + MAGIC + "0000" + RESET + WHITE + " Treg");
        meta.setLore(lore);
        item.setItemMeta(meta);

        PlayerInventory inv = player.getInventory();
        inv.addItem(item);

        Material material = Material.getMaterial(materialId);
        String materialName = material.toString();
        player.sendMessage("You received " + amount + " of " + DARK_AQUA
                + materialName.toLowerCase() + ".");
        LOGGER.info(player.getName() + " SPAWNED " + amount + ":"
                + materialName);

        return true;
    }
}
