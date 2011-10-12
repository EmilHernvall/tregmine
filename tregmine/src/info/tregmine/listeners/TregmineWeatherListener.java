package info.tregmine.listeners;

import info.tregmine.Tregmine;

import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

public class TregmineWeatherListener extends WeatherListener {
	private final Tregmine plugin;
	public TregmineWeatherListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	
	public void	onWeatherChange(WeatherChangeEvent event)  {
//		if (event.toWeatherState()) {
//			event.setCancelled(true);
//		}
	}
}
