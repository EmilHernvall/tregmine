package info.tregmine.commands;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.Rank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.TregminePlayer.GuardianState;

public class UnderCoverBossCommand extends AbstractCommand{
	private Tregmine plugin;
	private String request;
	public UnderCoverBossCommand(Tregmine tregmine){
		super(tregmine, "undercoverboss");
		this.plugin = tregmine;
	}
	public boolean handlePlayer(TregminePlayer player, String[] args){
		return true;
	}
}
