package info.tregmine.vote;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.vexsoftware.votifier.model.VotifierEvent;
import com.vexsoftware.votifier.model.Vote;
 
public class voter implements Listener {

    @EventHandler(priority=EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
		Vote vote = event.getVote();
		System.out.println("VOTE: " + vote.getUsername());
	}

}