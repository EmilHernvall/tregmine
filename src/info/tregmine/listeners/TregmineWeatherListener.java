package info.tregmine.listeners;

import info.tregmine.Tregmine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class TregmineWeatherListener implements Listener {
	private final Tregmine plugin;
	public TregmineWeatherListener(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}

	
	@EventHandler
	public void	onWeatherChange(WeatherChangeEvent event)  {
//		if (event.toWeatherState()) {
//			event.setCancelled(true);
//		}
	}
}
