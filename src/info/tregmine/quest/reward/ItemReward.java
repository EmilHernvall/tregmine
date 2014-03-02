package info.tregmine.quest.reward;

import org.bukkit.inventory.ItemStack;

import info.tregmine.quest.Reward;

public class ItemReward extends Reward
{
    private ItemStack reward;

    public ItemReward(ItemStack stack)
    {
        super(RewardType.ITEM);
        
        this.reward = stack;
    }
    
    public ItemStack getItemReward()
    {
        return reward;
    }
    
    public void setItemReward(ItemStack stack)
    {
        reward = stack;
    }

}
