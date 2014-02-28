package info.tregmine.quest.objectives;

import info.tregmine.api.TregminePlayer;
import info.tregmine.quest.Objective;

public class KillPlayerObjective extends Objective
{
    
    private final TregminePlayer target;
    
    public KillPlayerObjective(TregminePlayer holder, TregminePlayer target)
    {
        super(ObjectiveType.KILL_PLAYER, holder);
        
        this.target = target;
    }

    public TregminePlayer getTarget()
    {
        return target;
    }

    @Override
    public String toString()
    {
        return String.format("Kill %s", getTarget().getName());
    }
    
}
