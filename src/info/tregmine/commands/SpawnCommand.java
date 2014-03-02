package info.tregmine.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Horse;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.RED;

public class SpawnCommand extends AbstractCommand
{
	private static class SpawnTask implements Runnable
	{
		private TregminePlayer player;
		private Location loc;
		private Location oldLocation;

		public SpawnTask(TregminePlayer player, Location loc)
		{
			this.player = player;
			this.loc = loc;
			this.oldLocation = player.getLocation();
		}

		@Override
		public void run()
		{
			if (oldLocation != player.getLocation()) {
				player.sendMessage(ChatColor.RED + "Teleportation stopped! You moved...");
				return;
			}
			player.teleportWithHorse(loc);
		}
	}

	private Tregmine plugin;
    public SpawnCommand(Tregmine tregmine)
    {
        super(tregmine, "spawn");
		this.plugin = tregmine;
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
		if (	player.getWorld().getName().equalsIgnoreCase(tregmine.getRulelessWorld().getName()) ||
				player.getWorld().getName().equalsIgnoreCase(tregmine.getRulelessEnd().getName()) ||
				player.getWorld().getName().equalsIgnoreCase(tregmine.getRulelessNether().getName()) &&
						!player.getRank().canBypassWorld()) {

			player.sendMessage(ChatColor.RED + "Teleporting to spawn... Please wait 30 seconds.");
			player.sendMessage(ChatColor.DARK_BLUE + "Oh, and don't move! Moving will stop the teleportation.");
			BukkitScheduler scheduler = plugin.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(tregmine, new SpawnTask(player, player.getWorld().getSpawnLocation()), 20*30);
			return true;
		}
        player.teleportWithHorse(player.getWorld().getSpawnLocation());
        return true;
    }
}
