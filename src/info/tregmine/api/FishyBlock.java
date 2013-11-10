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
    private int cost = 0;
    private int availableInventory = 0;
    private Location blockLoc = null;
    private Location signLoc = null;
    private boolean storedEnchants = false;

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

    public int getCost() { return cost; }
    public void setCost(int v) { this.cost = v; }

    public int getAvailableInventory() { return availableInventory; }
    public void setAvailableInventory(int v) { this.availableInventory = v; }
    public void addAvailableInventory(int v) { this.availableInventory += v; }
    public void removeAvailableInventory(int v) { this.availableInventory -= v; }

    public Location getBlockLocation() { return blockLoc; }
    public void setBlockLocation(Location v) { this.blockLoc = v; }

    public Location getSignLocation() { return signLoc; }
    public void setSignLocation(Location v) { this.signLoc = v; }

    public void setStoredEnchantments(boolean v) { this.storedEnchants = v; }
    public boolean hasStoredEnchantments() { return storedEnchants; }
}
