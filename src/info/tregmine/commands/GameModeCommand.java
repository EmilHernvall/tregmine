package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import info.tregmine.api.Rank;
import org.bukkit.GameMode;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class GameModeCommand extends AbstractCommand
{
    private GameMode mode;
	private Tregmine tregmine;

    public GameModeCommand(Tregmine tregmine, String name, GameMode mode)
    {
        super(tregmine, name);

        this.mode = mode;
		this.tregmine = tregmine;
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canUseCreative()) {
            return true;
        }
        String additional = "";
        player.setGameMode(mode);
        if(mode == GameMode.SURVIVAL && !player.isOp()){
        	player.getInventory().clear();
        	additional = "Your inventory has been cleared.";
        }
        player.sendMessage(YELLOW + "You are now in "
                + mode.toString().toLowerCase() + " mode. " + additional);

        if (player.getRank().canFly()) {
            player.setAllowFlight(true);
        }

        return true;
    }
}
