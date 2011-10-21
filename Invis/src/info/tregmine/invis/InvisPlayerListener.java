package info.tregmine.invis;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class InvisPlayerListener extends PlayerListener
{
  private final Invis plugin;

  public InvisPlayerListener(Invis instance)
  {
    this.plugin = instance;
  }

  public void onPlayerJoin(PlayerJoinEvent event)
  {
    Player player = event.getPlayer();
    this.plugin.updateInvisible(player);
    
	if (event.getPlayer().getName().matches("einand")) {
    	this.plugin.vanishCommand(event.getPlayer());
    }
  }

  public void onPlayerQuit(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    this.plugin.invisible.remove(player);
  }

  public void onPlayerTeleport(PlayerTeleportEvent event)
  {
    if (event.isCancelled()) return;
    this.plugin.updateInvisibleOnTimer();
  }
}