package info.tregmine.invis;

//import java.io.File;
//import java.io.IOException;
import info.tregmine.Tregmine;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
//import net.minecraft.server.EntityPlayer;
//import net.minecraft.server.NetServerHandler;
import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet29DestroyEntity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
//import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
//import org.bukkit.event.Event.Priority;
//import org.bukkit.event.Event.Type;
//import org.bukkit.plugin.Plugin;
//import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.util.config.Configuration;

public class Invis extends JavaPlugin
{
//  public static PermissionHandler Permissions = null;
  public int RANGE;
  public int TOTAL_REFRESHES;
  public int REFRESH_TIMER;
  private Timer timer = new Timer();
  public Tregmine tregmine = null;

public List<Player> invisible = new ArrayList<Player>();

  private final InvisPlayerListener playerListener = new InvisPlayerListener(this);
  private final Logger log = Logger.getLogger("Minecraft");

  public void onDisable()
  {
    this.timer.cancel();
  }

  public void onEnable()
  {
    
		Plugin test = this.getServer().getPluginManager().getPlugin("Tregmine");

		if(this.tregmine == null) {
			if(test != null) {
				this.tregmine = ((Tregmine)test);
			} else {
				log.info(this.getDescription().getName() + " " + this.getDescription().getVersion() + " - could not find Tregmine");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}


		//TODO: Not sure if this work
    this.RANGE = 512;           // getConfiguration().getInt("range", 512);
    this.TOTAL_REFRESHES = 10;  // getConfiguration().getInt("total_refreshes", 10);
    this.REFRESH_TIMER = 2;     // getConfiguration().getInt("refresh_timer", 2);

    PluginManager pm = getServer().getPluginManager();
    
    pm.registerEvent(Event.Type.PLAYER_JOIN, this.playerListener, Event.Priority.Monitor, this);
    pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerListener, Event.Priority.Monitor, this);
    pm.registerEvent(Event.Type.PLAYER_TELEPORT, this.playerListener, Event.Priority.Monitor, this);

    this.log.info(getDescription().getName() + " " + getDescription().getVersion() + " loaded.");

    this.timer.schedule(new UpdateInvisibleTimerTask(true), (int) 60000 * this.REFRESH_TIMER);
  }

  public boolean check(CommandSender sender, String permNode)
  {
	  
    if ((sender instanceof Player))
    {
        return sender.isOp();
    }
    
    return (sender instanceof ConsoleCommandSender);
  }

  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
  {
    if ((command.getName().equalsIgnoreCase("vanish")) || (command.getName().equalsIgnoreCase("poof")))
    {
      vanishCommand(sender);
      return true;
    }
    return false;
  }

  public void vanishCommand(CommandSender sender)
  {
    if (!check(sender, "vanish.vanish")) return;
    if ((sender instanceof Player))
    {
      Player player = (Player)sender;
      vanish(player);
      return;
    }
    sender.sendMessage("That doesn't work from here");
  }

  private void invisible(Player p1, Player p2)
  {
    invisible(p1, p2, false);
  }

  private void invisible(Player p1, Player p2, boolean force)
  {
    if ((!force) && (check(p2, "vanish.dont.hide"))) return;
    CraftPlayer hide = (CraftPlayer)p1;
    CraftPlayer hideFrom = (CraftPlayer)p2;
    hideFrom.getHandle().netServerHandler.sendPacket(new Packet29DestroyEntity(hide.getEntityId()));
  }

  private void uninvisible(Player p1, Player p2)
  {
    CraftPlayer unHide = (CraftPlayer)p1;
    CraftPlayer unHideFrom = (CraftPlayer)p2;
    unHideFrom.getHandle().netServerHandler.sendPacket(new Packet20NamedEntitySpawn(unHide.getHandle()));
  }

  public void vanish(Player player)
  {
    if (this.invisible.contains(player))
    {
      reappear(player);
      return;
    }
    
    this.invisible.add(player);
    Player[] playerList = getServer().getOnlinePlayers();
    for (Player p : playerList)
    {
      if ((getDistance(player, p) > this.RANGE) || (p.equals(player)))
        continue;
      invisible(player, p);
    }

    this.log.info(player.getName() + " disappeared.");
	info.tregmine.api.TregminePlayer tregP = this.tregmine.tregminePlayer.get(player.getName());
	tregP.setMetaString("invis", "true");
    player.sendMessage(ChatColor.YELLOW + "Like an flying elephant your turn yourself unseen");
  }

  public void reappear(Player player)
  {
    if (this.invisible.contains(player))
    {
      this.invisible.remove(player);

      updateInvisibleForPlayer(player, true);
      Player[] playerList = getServer().getOnlinePlayers();
      for (Player p : playerList)
      {
        if ((getDistance(player, p) >= this.RANGE) || (p.equals(player)))
          continue;
        uninvisible(player, p);
      }
  	info.tregmine.api.TregminePlayer tregP = this.tregmine.tregminePlayer.get(player.getName());
  	tregP.setMetaString("invis", "false");
    player.sendMessage(ChatColor.YELLOW + "Like a mad cow you spawned yourelf from nothing!");

      this.log.info(player.getName() + " reappeared.");
    }
  }

  public void reappearAll()
  {
    this.log.info("Everyone is going reappear.");
    for (Player InvisiblePlayer : this.invisible) {
      reappear(InvisiblePlayer);
    }
    this.invisible.clear();
  }

  public void updateInvisibleForPlayer(Player player)
  {
    updateInvisibleForPlayer(player, false);
  }

  public void updateInvisibleForPlayer(Player player, boolean force)
  {
    Player[] playerList = getServer().getOnlinePlayers();
    for (Player p : playerList)
    {
      if ((getDistance(player, p) > this.RANGE) || (p.equals(player)))
        continue;
      invisible(player, p, force);
    }
  }

  public void updateInvisibleForAll()
  {
    Player[] playerList = getServer().getOnlinePlayers();
    for (Player invisiblePlayer : this.invisible)
    {
      for (Player p : playerList)
      {
        if ((getDistance(invisiblePlayer, p) > this.RANGE) || (p.equals(invisiblePlayer)))
          continue;
        invisible(invisiblePlayer, p);
      }
    }
  }

  public void updateInvisibleForAll(boolean startTimer)
  {
    updateInvisibleForAll();
    if (startTimer)
    {
      this.timer.schedule(new UpdateInvisibleTimerTask(true), 60000 * this.REFRESH_TIMER);
    }
  }

  public void updateInvisible(Player player)
  {
    for (Player invisiblePlayer : this.invisible)
    {
      if ((getDistance(invisiblePlayer, player) > this.RANGE) || (player.equals(invisiblePlayer)))
        continue;
      invisible(invisiblePlayer, player);
    }
  }

  public double getDistance(Player player1, Player player2)
  {
    Location loc1 = player1.getLocation();
    Location loc2 = player1.getLocation();
    return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2.0D) + Math.pow(loc1.getY() - loc2.getY(), 2.0D) + Math.pow(loc1.getZ() - loc2.getZ(), 2.0D));
  }

  public void updateInvisibleOnTimer()
  {
    updateInvisibleForAll();
    Timer timer = new Timer();
    int i = 0;
    while (i < this.TOTAL_REFRESHES)
    {
      i++;
      timer.schedule(new UpdateInvisibleTimerTask(), i * 1000);
    }
  }

  public class UpdateInvisibleTimerTask extends TimerTask
  {
    private boolean startTimer = false;

    public UpdateInvisibleTimerTask()
    {
    }

    public UpdateInvisibleTimerTask(boolean startTimer)
    {
      this.startTimer = startTimer;
    }

    public void run()
    {
      Invis.this.updateInvisibleForAll(this.startTimer);
    }
  }
}