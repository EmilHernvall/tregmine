package info.tregmine.quest.reward;

import info.tregmine.quest.Reward;

public class MonateryReward extends Reward
{
    private long amount;

    public MonateryReward(long amount)
    {
        super(RewardType.MONEY);
        
        this.amount = amount;
    }
    
    public long getAmount()
    {
        return amount;
    }
    
    public void setAmount(long amount)
    {
        this.amount = amount;
    }
}
