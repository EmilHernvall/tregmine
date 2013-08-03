package info.tregmine.commands;

import java.util.List;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class InventoryCommand extends AbstractCommand
{
    public InventoryCommand(Tregmine tregmine)
    {
        super(tregmine, "inv");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canInspectInventories()) {
            return false;
        }
        if (args.length == 0) {
            return false;
        }

        List<TregminePlayer> candidates = tregmine.matchPlayer(args[0]);
        if (candidates.size() != 1) {
            // TODO: error message
            return false;
        }

        TregminePlayer candidate = candidates.get(0);
        player.openInventory(candidate.getInventory());

        return true;
    }
}
