package info.tregmine.quest.objectives;

import org.bukkit.Material;

import info.tregmine.quest.Objective;

public class CollectItemObjective extends Objective
{
    
    private final Material itemType;
    private final int amount;
    
    public CollectItemObjective(Material itemType, int amount)
    {
        super(ObjectiveType.COLLECT_ITEM);
        
        this.itemType = itemType;
        this.amount = amount;
    }

    public Material getItemType()
    {
        return itemType;
    }

    public int getAmount()
    {
        return amount;
    }

    @Override
    public String toString()
    {
        return String.format("Collect %d items of %s", getAmount(), getItemType().name().toLowerCase().replace('_', ' '));
    }
    
}
