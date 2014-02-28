package info.tregmine.quest;

import info.tregmine.api.TregminePlayer;

public abstract class Objective
{
    
    public static enum ObjectiveType
    {
        KILL_MOB,
        KILL_PLAYER,
        COLLECT_ITEM,
        BREAK_BLOCK,
        GO_TO_LOCATION,
        ANSWER_RIDDLE,
        SOLVE_PUZZLE;
        
        public static ObjectiveType fromString(String s)
        {
            for(ObjectiveType type: values())
            {
                if(type.name().equalsIgnoreCase(s)){
                    return type;
                }
            }
            return null;
        }
    }
    
    private final ObjectiveType objectiveType;
    private final TregminePlayer objectiveHolder;
    
    public Objective(ObjectiveType type, TregminePlayer player)
    {
        this.objectiveType = type;
        this.objectiveHolder = player;
    }

    public ObjectiveType getObjectiveType()
    {
        return objectiveType;
    }

    public TregminePlayer getObjectiveHolder()
    {
        return objectiveHolder;
    }
    
    public abstract String toString();
    
}
