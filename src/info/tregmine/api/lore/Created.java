package info.tregmine.api.lore;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Created {
    MINED(ChatColor.GREEN + "MINED"), SPAWNED(ChatColor.RED + "SPAWNED"), FILLED(
            ChatColor.RED + "FILLED"), PURCHASED(ChatColor.AQUA + "PURCHASED"), UNKOWN(
            "UNKNOWN"), CREATIVE(ChatColor.YELLOW + "CREATIVE");

    private String colorString = null;

    private Created(String text)
    {
        this.colorString = text;
    }

    public String toColorString()
    {
        return colorString;
    }

    public static Created valueOf(ItemStack stack)
    {
        if (stack == null) {
            return Created.UNKOWN;
        }

        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null || lore.get(0) == null) {
            return Created.UNKOWN;
        }

        if (lore.get(0).matches(Created.CREATIVE.colorString)) {
            return Created.CREATIVE;
        }

        if (lore.get(0).matches(Created.FILLED.colorString)) {
            return Created.FILLED;
        }

        if (lore.get(0).matches(Created.SPAWNED.colorString)) {
            return Created.SPAWNED;
        }

        if (lore.get(0).matches(Created.MINED.colorString)) {
            return Created.MINED;
        }

        return Created.UNKOWN;
    }

}
