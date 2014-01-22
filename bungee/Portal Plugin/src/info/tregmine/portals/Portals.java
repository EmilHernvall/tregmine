package info.tregmine.portals;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import info.tregmine.portals.commands.PortalCommand;
import info.tregmine.portals.listeners.PortalListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Portals extends JavaPlugin implements Listener, PluginMessageListener {

	@Override
	public void onEnable() {
		PluginManager pluginMgm = Bukkit.getServer().getPluginManager();
		pluginMgm.registerEvents(new PortalListener(this), this);

		getCommand("portal").setExecutor(new PortalCommand(this));

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
	}

	public static String serverName;
	public static String[] serverList;
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		try{
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
			String subchannel = in.readUTF();
			if (subchannel.equals("GetServers")) {
				serverName = in.readUTF();
				serverList = serverName.split(", ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
