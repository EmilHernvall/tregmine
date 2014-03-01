package info.tregmine.quest.objectives;

import org.bukkit.entity.EntityType;

import info.tregmine.quest.Objective;

public class KillMobObjective extends Objective
{
    private final EntityType type;
    private int amount;
    
    
    public KillMobObjective(EntityType type, int amount)
    {
        super(ObjectiveType.KILL_MOB);
        
        this.type = type;
        this.amount = amount;
    }


    public EntityType getEntityType()
    {
        return type;
    }
    
    public int getAmount()
    {
        return amount;
    }
    
    public int remove(int x)
    {
        this.amount = amount - x < 0 ? 0 : amount - x;
        
        return this.amount;
    }


    @Override
    public String toString()
    {
        return String.format("Kill %d %ss", getAmount(), getEntityType().name().toLowerCase().replace('_', ' '));
    }
}
