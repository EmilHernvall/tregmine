package info.tregmine.boxfill;

import java.util.HashMap;
import java.util.Map;

import info.tregmine.api.TregminePlayer;

public class History
{
    private Map<TregminePlayer, SavedBlocks> currentState;

    public History()
    {
        currentState = new HashMap<TregminePlayer, SavedBlocks>();
    }

    public void set(TregminePlayer player, SavedBlocks blocks)
    {
        currentState.put(player, blocks);
    }

    public SavedBlocks get(TregminePlayer player)
    {
        return currentState.get(player);
    }
}
