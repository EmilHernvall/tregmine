package info.tregmine.ServerJump;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.minecraft.util.org.apache.commons.io.output.ByteArrayOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServerJump extends JavaPlugin implements Listener {

	private ServerJump plugin;

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		Bukkit.broadcastMessage("");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(sender instanceof Player){

			if ((commandLabel.equalsIgnoreCase("hub")) || (commandLabel.equalsIgnoreCase("lobby"))) {
				final Player player = (Player)sender;
				if (args.length == 0) {
					sendPlayer(player);
				}
			}
		}
		return false;
	}

	//Sends the player to the hub.
	public void sendPlayer(Player p) {
		p.sendMessage(ChatColor.GREEN + "Welcome to the " + ChatColor.GOLD + "Tregmine Network" + ChatColor.GREEN + " Lobby!");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF("Hub");
		p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
}
