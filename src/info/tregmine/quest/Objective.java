package info.tregmine.quest;

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
    
    public Objective(ObjectiveType type)
    {
        this.objectiveType = type;
    }

    public ObjectiveType getObjectiveType()
    {
        return objectiveType;
    }

    public abstract String toString();
    
}
