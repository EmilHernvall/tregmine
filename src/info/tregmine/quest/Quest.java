package info.tregmine.quest;

import info.tregmine.quest.Objective.ObjectiveType;

import java.util.List;

import com.google.common.collect.Lists;

public class Quest
{
    
    public static enum Difficulty{
        NEWBIE,
        EASY,
        NORMAL,
        ANNOYING,
        HARD,
        MASTER,
        GRANDMASTER;
        
        public static Difficulty byName(String string)
        {
            for(Difficulty d: values())
            {
                if(d.name().equalsIgnoreCase(string)){
                    return d;
                }
            }
            return null;
        }
        
        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
        
    };
    
    private final List<Objective> objectives;
    private final List<Objective> completedObjectives;
    private final List<Reward> rewards;
    
    private final String name;
    private final Difficulty difficulty;
    
    /*
     * private CustomReward reward;
     * 
     * public CustomReward getReward()
     * {
     *    return reward; //you get it
     * }
     * 
     */
    
    public Quest(String name, Difficulty difficulty)
    {
        this.name = name;
        this.difficulty = difficulty;
        this.objectives = Lists.newArrayList();
        this.completedObjectives = Lists.newArrayList();
        this.rewards = Lists.newArrayList();
    }

    public String getName()
    {
        return name;
    }

    public List<Objective> getObjectives()
    {
        return objectives;
    }
    
    public List<? extends Objective> getObjetives(ObjectiveType type)
    {
        List<Objective> objectives = Lists.newArrayList();
        for(Objective obj: getObjectives())
        {
            if(obj.getObjectiveType() != type){
                continue;
            }
            objectives.add(obj);
        }
        return objectives;
    }
    
    public List<Objective> getCompletedObjectives()
    {
        return this.completedObjectives;
    }
    
    public Quest addObjective(Objective o)
    {
        this.objectives.add(o);
        
        return this;
    }
    
    public Quest completeObjective(Objective o)
    {
        this.objectives.remove(o);
        this.completedObjectives.add(o);
        
        return this;
    }
    
    public Quest addReward(Reward reward)
    {
        this.rewards.add(reward);
        
        return this;
    }
    
    public List<Reward> getRewards()
    {
        return this.rewards;
    }
    
    public Difficulty getDifficulty()
    {
        return difficulty;
    }
    
    public boolean isStarted()
    {
        return (this.getObjectives().size() != 0 && this.getCompletedObjectives().size() != 0);
    }
    
    public boolean isFinished()
    {
        return (getObjectives().size() == 0 && this.getCompletedObjectives().size() != 0);
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
        return String.format("Quest{name=%s, Difficulty=%s}", name, difficulty);
    }

}
