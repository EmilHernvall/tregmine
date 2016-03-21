package info.tregmine.commands;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.RESET;

import java.util.List;

import org.bukkit.Bukkit;

import info.tregmine.Tregmine;
import info.tregmine.api.Rank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.TregminePlayer.Flags;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IMentorLogDAO;
import info.tregmine.database.IPlayerDAO;

public class SkipMentorCommand extends AbstractCommand{
	
	public SkipMentorCommand(Tregmine tregmine) {
		super(tregmine, "skipmentor");
	}
	
	public boolean handlePlayer(TregminePlayer player, String[] args){
		if(player.getRank() != Rank.SENIOR_ADMIN && player.getRank() != Rank.JUNIOR_ADMIN && player.getRank() != Rank.GUARDIAN){
			player.sendMessage(RED + "You are not authorized to perform that command. All online administrators have been notified.");
			return true;
		}
		if(args.length != 1){
			//Player didn't enter two arguments, terminate.
			player.sendMessage(RED + "You didn't enter the right arguments. '/skipmentor <player>'");
			return true;
		}
		//The checks have finished, perform the command
		String possibleuser = args[0];
		List<TregminePlayer> candidate = tregmine.matchPlayer(possibleuser);
		if(candidate.size() != 1){
			player.sendMessage(RED + "The player specified was not found. Please try again.");
			return true;
		}
		TregminePlayer user = candidate.get(0);
		if(user.hasFlag(Flags.HARDWARNED)){
			//Players with a hardwarn cannot be promoted using this command. They must be promoted manually.
			player.sendMessage(RED + "The player specified has been hardwarned and is not eligible for promotion.");
			return true;
		}
		//Any other errors have now been checked and dealt with. Promote the user.
		try (IContext ctx = tregmine.createContext()) {
            user.setRank(Rank.SETTLER);
            user.setMentor(null);

            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            playerDAO.updatePlayer(user);
            playerDAO.updatePlayerInfo(user);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
		player.sendMessage(GREEN + player.getChatName() + " has skipped mentoring.");
		return true;
	}

}
