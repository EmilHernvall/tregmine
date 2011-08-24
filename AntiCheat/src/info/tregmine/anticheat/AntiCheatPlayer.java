package info.tregmine.anticheat;

import org.bukkit.event.player.PlayerListener;

public class AntiCheatPlayer extends PlayerListener {
	private final AntiCheat plugin;

	public AntiCheatPlayer(AntiCheat instance) {
		this.plugin = instance;
		plugin.getServer();
	}
}
