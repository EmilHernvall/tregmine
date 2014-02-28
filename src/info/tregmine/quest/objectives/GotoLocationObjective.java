package info.tregmine.quest.objectives;

import org.bukkit.Location;

import info.tregmine.api.TregminePlayer;
import info.tregmine.quest.Objective;

public class GotoLocationObjective extends Objective
{
    private final Location location;
    
    public GotoLocationObjective(TregminePlayer holder, Location loc)
    {
        super(ObjectiveType.GO_TO_LOCATION, holder);
        
        this.location = loc;
    }

    public Location getLocation()
    {
        return location;
    }

    @Override
    public String toString()
    {
        return String.format("Go to location: %s", getLocation());
    }
    
    
}
