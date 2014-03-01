package info.tregmine.quest.objectives;

import org.bukkit.Material;

import info.tregmine.quest.Objective;

public class BreakBlockObjective extends Objective
{
    private final Material itemType;
    private final int amount;
    
    public BreakBlockObjective(Material itemType, int amount)
    {
        super(ObjectiveType.BREAK_BLOCK);
        
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
        return String.format("Break %d blocks of %s", getAmount(), getItemType().name().toLowerCase().replace('_', ' '));
    }
}
