package info.tregmine.events;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.RESET;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;

public class CallEventListener implements Listener
{
    private Tregmine plugin;

    public CallEventListener(Tregmine instance)
    {
        this.plugin = instance;
    }
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
    	if(event.getMessage().equalsIgnoreCase("/afk")){
    		return;
    	}else{
    	TregminePlayer sender = plugin.getPlayer(event.getPlayer().getPlayer());
    	if(sender.isAfk()){
    		sender.setAfk(false);
    	}
    	}
    }
    // Triggers when a player pings the server
	@EventHandler
	public void onServerListPing(ServerListPingEvent event){
		if(plugin.getLockdown()){
			event.setMotd(ChatColor.GOLD + plugin.getConfig().getString("general.servername") + ChatColor.RED + " is on lockdown.\n" + ChatColor.RED + "Only staff can join.");	
		}else{
		if(plugin.getConfig().getBoolean("general.motd.lineoneauto")){
		String extraText = "";
		String type = plugin.releaseType;
		if(type.contains("re")){
			extraText = ChatColor.GREEN + " Release";
		}else if(type.contains("be")){
			extraText = ChatColor.BOLD + " Beta";
		}else if(type.contains("te")){
			extraText = ChatColor.BLUE + " Test Release";
		}else if(type.contains("bl")){
			extraText = ChatColor.RED + " Bleeding Edge";
		}else if(type.contains("gm")){
			extraText = ChatColor.GOLD + " Golden Master";
		}else{
			extraText = ChatColor.DARK_RED + "" + ChatColor.BOLD + " YOU DID SOMETHING WRONG.";
		}
		event.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "Tregmine " + plugin.getDescription().getVersion() + extraText + "\n" + ChatColor.RESET + "" + ChatColor.translateAlternateColorCodes('#', plugin.getConfig().getString("general.motd.linetwo")));
		}else{
			event.setMotd(ChatColor.translateAlternateColorCodes('#', plugin.getConfig().getString("general.motd.lineone")) + "\n" + ChatColor.RESET + "" + ChatColor.translateAlternateColorCodes('#', plugin.getConfig().getString("general.motd.linetwo")));
			
		}
	}
}
    // Triggers when a player changes lot
    @EventHandler
    public void PlayerLotChangeEventListener(PlayerMoveEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        if (player == null) {
            event.getPlayer().kickPlayer("Something went wrong!");
            Tregmine.LOGGER.info(event.getPlayer().getName() + " was not found in players map (PlayerMoveEvent).");
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());

        Lot oldLot = world.findLot(event.getFrom());
        String oldLotName = null;

        Lot newLot = world.findLot(event.getTo());
        String newLotName = null;

        if (oldLot == null && newLot == null) {
            return;
        }

        if (oldLot == null) {
            oldLotName = "null";
        } else {
            oldLotName = oldLot.getName();
        }

        if (newLot == null) {
            newLotName = "null";
        } else {
            newLotName = newLot.getName();
        }

        if (oldLotName.equalsIgnoreCase(newLotName)) {
            return;
        }

        PlayerLotChangeEvent customEvent = new PlayerLotChangeEvent(event.getFrom(), event.getTo(), player, oldLot, newLot);
        plugin.getServer().getPluginManager().callEvent(customEvent);
    }

    // Triggers when the player changes zone
    @EventHandler
    public void PlayerZoneChangeEventListener(PlayerMoveEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        if (player == null) {
            event.getPlayer().kickPlayer("Something went wrong!");
            Tregmine.LOGGER.info(event.getPlayer().getName() + " was not found in players map (PlayerMoveEvent).");
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());

        Zone oldZone = world.findZone(event.getFrom());
        String oldZoneName = null;

        Zone newZone = world.findZone(event.getTo());
        String newZoneName = null;

        if (oldZone == null && newZone == null) {
            return;
        }

        if (oldZone == null) {
            oldZoneName = "null";
        } else {
            oldZoneName = oldZone.getName();
        }

        if (newZone == null) {
            newZoneName = "null";
        } else {
            newZoneName = newZone.getName();
        }

        if (oldZoneName.equalsIgnoreCase(newZoneName)) {
            return;
        }

        PlayerZoneChangeEvent customEvent = new PlayerZoneChangeEvent(event.getFrom(), event.getTo(), player, oldZone, newZone);
        plugin.getServer().getPluginManager().callEvent(customEvent);
    }

    // Triggers when a player moves position, not head.
    @EventHandler
    public void PlayerMoveBlockEventListener(PlayerMoveEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        if (player == null) {
            event.getPlayer().kickPlayer("Something went wrong!");
            Tregmine.LOGGER.info(event.getPlayer().getName() + " was not found in players map (PlayerMoveEvent).");
            return;
        }

        if (event.getFrom().getX() == event.getTo().getX() &&
                event.getFrom().getY() == event.getTo().getY() &&
                event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }

        PlayerMoveBlockEvent customEvent = new PlayerMoveBlockEvent(event.getFrom(), event.getTo(), player);
        plugin.getServer().getPluginManager().callEvent(customEvent);
    }

    // Triggers on a server chat event
    @EventHandler
    public void TregmineChatEventListener(AsyncPlayerChatEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getChatState() != TregminePlayer.ChatState.CHAT) {
            return;
        }

        if (player == null) {
            event.getPlayer().kickPlayer("Something went wrong!");
            Tregmine.LOGGER.info(event.getPlayer().getName() + " was not found in players map (PlayerMoveEvent).");
            return;
        }

        TregmineChatEvent customEvent = new TregmineChatEvent(player, event.getMessage(), player.getChatChannel(), false);
        plugin.getServer().getPluginManager().callEvent(customEvent);

        event.setCancelled(true);
    }
}
