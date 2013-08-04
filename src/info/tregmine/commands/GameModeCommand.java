package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.GameMode;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class GameModeCommand extends AbstractCommand
{
    private GameMode mode;

    public GameModeCommand(Tregmine tregmine, String name, GameMode mode)
    {
        super(tregmine, name);

        this.mode = mode;
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canUseCreative()) {
            return true;
        }

        player.setGameMode(mode);
        player.sendMessage(YELLOW + "You are now in "
                + mode.toString().toLowerCase() + " mode.");

        return true;
    }
}
