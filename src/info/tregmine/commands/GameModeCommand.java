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

		if (	(player.getWorld().getName().equalsIgnoreCase(tregmine.getRulelessWorld().getName()) ||
				player.getWorld().getName().equalsIgnoreCase(tregmine.getRulelessEnd().getName()) ||
				player.getWorld().getName().equalsIgnoreCase(tregmine.getRulelessNether().getName())) &&
				player.getRank() == Rank.BUILDER) {

			player.sendMessage(RED + "Builders can not be in creative!" + DARK_RED + " Disabled.");
			player.setGameMode(GameMode.SURVIVAL);
			return false;
		}

        player.setGameMode(mode);
        player.sendMessage(YELLOW + "You are now in "
                + mode.toString().toLowerCase() + " mode.");

        if (player.getRank().canFly()) {
            player.setAllowFlight(true);
        }

        return true;
    }
}
