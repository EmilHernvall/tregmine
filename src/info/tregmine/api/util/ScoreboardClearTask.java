package info.tregmine.api.util;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

import info.tregmine.api.TregminePlayer;

public class ScoreboardClearTask implements Runnable
{
    private TregminePlayer player;

    private ScoreboardClearTask(TregminePlayer player)
    {
        this.player = player;
    }

    @Override
    public void run()
    {
        if (!player.isOnline()) {
            return;
        }

        try {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            player.setScoreboard(manager.getNewScoreboard());
        } catch (IllegalStateException e) {
            // We don't really care
        }
    }

    public static void start(Plugin plugin, TregminePlayer player)
    {
        Runnable runnable = new ScoreboardClearTask(player);

        Server server = Bukkit.getServer();
        BukkitScheduler scheduler = server.getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, runnable, 400);
    }
}
