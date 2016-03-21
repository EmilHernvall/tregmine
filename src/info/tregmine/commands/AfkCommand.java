package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;

public class AfkCommand extends AbstractCommand{

	public AfkCommand(Tregmine tregmine) {
		super(tregmine, "afk");
	}
	
	@Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
		if(args.length != 0){
			player.sendMessage(RED + "This command does not need arguments.");
			
			return true;
		}
		if(player.isAfk()){
			//Player is afk, wake them up
			player.setAfk(false);
			player.setTemporaryChatName(player.getNameColor() + player.getRealName());
			Bukkit.broadcastMessage(ITALIC + player.getChatName() + RESET + GREEN + " is no longer afk.");
		}else{
			//Player is not afk, make them afk and announce it.
			player.setAfk(true);
			Bukkit.broadcastMessage(ITALIC + player.getChatName() + RESET + BLUE + " is now afk.");
			String oldname = player.getChatName();
			//This makes /who show that the player is AFK.
			player.setTemporaryChatName(GRAY + "[AFK] " + RESET + oldname);
		}
        return true;
    }

}
