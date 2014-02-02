package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class ChannelCommand extends AbstractCommand
{
    public ChannelCommand(Tregmine tregmine)
    {
        super(tregmine, "channel");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        String oldChannel = player.getChatChannel();
        
        switch(args.length) {
            case 0:
                player.sendMessage(ChatColor.RED + "Incorrect Syntax: /channel <channel name>");
                return true;
            case 1:
                String newChannel = args[0];
                
                if (oldChannel.equalsIgnoreCase(newChannel)) {
                    return true;
                }

                player.setChatChannel(newChannel);
                player.sendMessage(YELLOW + "You are now talking in channel " + newChannel + ".");
                player.sendMessage(YELLOW + "Write /channel global to switch to the global chat.");

                if (player.hasFlag(TregminePlayer.Flags.INVISIBLE)) {
                    return true;
                }

                sendChannelMessage(newChannel, player.getChatName() + ChatColor.YELLOW + 
                        " has left channel " + oldChannel);
                sendChannelMessage(oldChannel, player.getChatName() + ChatColor.YELLOW +
                        " has joined channel " + newChannel);
                return true;
            case 2:
                if (!player.getRank().canChannelView()) {
                    return true;
                }
                
                String channel = args[0];
                String message = args[1];
                
                sendChannelMessage(channel, "<" + player.getChatName() + "> " + message);
                return true;
        }
        return false;
    }
    
    private void sendChannelMessage(String channel, String message)
    {
        for (TregminePlayer player : tregmine.getOnlinePlayers()) {
            if (channel.equalsIgnoreCase(player.getChatChannel())) {
                player.sendMessage(message);
            }
        }
    }
}
