package info.tregmine.quest.objectives;

import org.bukkit.entity.EntityType;

import info.tregmine.api.TregminePlayer;
import info.tregmine.quest.Objective;

public class KillMobObjective extends Objective
{
    private final EntityType type;
    private int amount;
    
    
    public KillMobObjective(TregminePlayer holder, EntityType type, int amount)
    {
        super(ObjectiveType.KILL_MOB, holder);
        
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
    
    public void remove(int x)
    {
        this.amount = amount - x < 0 ? 0 : amount - x;
    }


    @Override
    public String toString()
    {
        return String.format("Kill %d %ss", getAmount(), getEntityType().name().toLowerCase().replace('_', ' '));
    }
}
