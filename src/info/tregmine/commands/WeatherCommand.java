package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class WeatherCommand extends AbstractCommand
{
    public WeatherCommand(Tregmine tregmine)
    {
        super(tregmine, "weather");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.getRank().canSetWeather()) {
            return true;
        }
        if (args.length != 1) {
            player.resetPlayerWeather();
            return true;
        }

        try {
            WeatherType type = WeatherType.valueOf(args[0].toUpperCase());
            player.setPlayerWeather(type);
            player.sendMessage(YELLOW + "Weather set to " + type);
        }
        catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }
}
