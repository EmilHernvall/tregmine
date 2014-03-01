package info.tregmine.quest.objectives;

import org.bukkit.Location;

import info.tregmine.quest.Objective;

public class GotoLocationObjective extends Objective
{
    private final Location location;
    
    public GotoLocationObjective(Location loc)
    {
        super(ObjectiveType.GO_TO_LOCATION);
        
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
