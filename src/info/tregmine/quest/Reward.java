package info.tregmine.quest;

/**
 * Represents a Reward from a quest
 * @author Robert Catron
 * @since 3/1/2014
 *
 */
public abstract class Reward
{
    
    public static enum RewardType
    {
        MONEY,
        ITEM,
        CUSTOM;
    }
    
    private final RewardType type;
    
    public Reward(RewardType type)
    {
        this.type = type;
    }
    
    /**
     * The type of reward
     * @return the type this reward is
     */
    public RewardType getType()
    {
        return type;
    }
    
}
