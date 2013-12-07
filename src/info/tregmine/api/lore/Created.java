package info.tregmine.api.lore;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Created
{
    MINED(ChatColor.GREEN + "MINED"),
    SPAWNED(ChatColor.RED + "SPAWNED"),
    FILLED(ChatColor.RED + "FILLED"),
    PURCHASED(ChatColor.AQUA + "PURCHASED"),
    UNKOWN("UNKNOWN"),
    CREATIVE(ChatColor.YELLOW + "CREATIVE");

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

        if (lore == null || lore.size() == 0) {
            return Created.UNKOWN;
        }

        String s = lore.get(0);
        if (s.matches(Created.CREATIVE.colorString)) {
            return Created.CREATIVE;
        } else if (s.matches(Created.FILLED.colorString)) {
            return Created.FILLED;
        } else if (s.matches(Created.SPAWNED.colorString)) {
            return Created.SPAWNED;
        } else if (s.matches(Created.MINED.colorString)) {
            return Created.MINED;
        }

        return Created.UNKOWN;
    }

}
