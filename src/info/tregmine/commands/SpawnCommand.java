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
		private final Location oldLocation;

		public SpawnTask(TregminePlayer player, Location loc, Location oldLocation)
		{
			this.player = player;
			this.loc = loc;
			this.oldLocation = oldLocation;
		}

		@Override
		public void run()
		{
			if (oldLocation.getBlockX() != player.getLocation().getBlockX() ||
			    oldLocation.getBlockY() != player.getLocation().getBlockY() ||
			    oldLocation.getBlockZ() != player.getLocation().getBlockZ()) {

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
        player.teleportWithHorse(player.getWorld().getSpawnLocation());
        return true;
    }
}
