package info.tregmine.quest;

import java.util.List;

import com.google.common.collect.Lists;

public class Quest
{
    private final List<Objective> objectives;
    private final String name;
    
    /*
     * private CustomReward reward;
     * 
     * public CustomReward getReward()
     * {
     *    return reward; //you get it
     * }
     * 
     */
    
    public Quest(String name)
    {
        this.name = name;
        this.objectives = Lists.newArrayList();
    }

    public String getName()
    {
        return name;
    }

    public List<Objective> getObjectives()
    {
        return objectives;
    }
    
    public Quest addObjective(Objective o)
    {
        this.objectives.add(o);
        
        return this;
    }
    
    public int hashCode()
    {
        return name.hashCode();
    }
    
    public boolean equals(Object o)
    {
        return (o instanceof Quest) && (((Quest)o).getName().equalsIgnoreCase(name));
    }
    
    public String toString()
    {
        return String.format("Quest{name=%s, Objectives=%s}", name, objectives.toString());
    }
}
