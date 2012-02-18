package info.tregmine.invis;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class InvisPlayerListener implements Listener {
  private final Invis plugin;

  public InvisPlayerListener(Invis instance)
  {
    this.plugin = instance;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    this.plugin.updateInvisible(player);
    
	if (event.getPlayer().getName().matches("einand")) {
    	this.plugin.vanishCommand(event.getPlayer());
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    this.plugin.invisible.remove(player);
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent event)
  {
    if (event.isCancelled()) return;
    this.plugin.updateInvisibleOnTimer();
  }
}