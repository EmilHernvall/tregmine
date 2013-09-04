package info.tregmine.api;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.material.MaterialData;
import org.bukkit.enchantments.Enchantment;

public class FishyBlock
{
    private int id = 0;
    private int playerId = 0;
    private MaterialData material = null;
    private Map<Enchantment, Integer> enchantments = null;
    private int availableInventory = 0;
    private Location blockLoc = null;
    private Location signLoc = null;

    public FishyBlock()
    {
    }

    public int getId() { return id; }
    public void setId(int v) { this.id = v; }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int v) { this.playerId = v; }

    public MaterialData getMaterial() { return material; }
    public void setMaterial(MaterialData v) { this.material = v; }

    public Map<Enchantment, Integer> getEnchantments() { return enchantments; }
    public void setEnchantments(Map<Enchantment, Integer> v) { this.enchantments = v; }

    public int getAvailableInventory() { return availableInventory; }
    public void setAvailableInventory(int v) { this.availableInventory = v; }

    public Location getBlockLocation() { return blockLoc; }
    public void setBlockLocation(Location v) { this.blockLoc = v; }

    public Location getSignLocation() { return signLoc; }
    public void setSignLocation(Location v) { this.signLoc = v; }
}
