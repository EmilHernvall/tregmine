package info.tregmine.vote;

import info.tregmine.Tregmine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.vexsoftware.votifier.model.Vote;
 
public class voter implements Listener {

	private final Tregmine plugin;
	
	public voter(Tregmine instance) {
		plugin = instance;
		plugin.getServer();
	}
	
	
    @EventHandler(priority=EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
		Vote vote = event.getVote();
		
		this.plugin.hasVoted.add(vote.getUsername());
//		System.out.println("VOTE: " + vote.getUsername());
	}

}