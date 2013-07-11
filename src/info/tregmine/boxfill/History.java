package info.tregmine.boxfill;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class History
{
    private Map<Player, SavedBlocks> currentState;

    public History()
    {
        currentState = new HashMap<Player, SavedBlocks>();
    }

    public void set(Player player, SavedBlocks blocks)
    {
        currentState.put(player, blocks);
    }

    public SavedBlocks get(Player player)
    {
        return currentState.get(player);
    }
}
