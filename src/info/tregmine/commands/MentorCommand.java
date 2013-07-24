package info.tregmine.commands;

import java.util.Queue;

import static org.bukkit.ChatColor.*;
import org.bukkit.entity.Player;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class MentorCommand extends AbstractCommand
{
    public MentorCommand(Tregmine tregmine)
    {
        super(tregmine, "mentor");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        // residents, donator, not warned players

        String action = "queue";
        if (args.length > 0) {
            action = args[0];
        }

        if ("queue".equalsIgnoreCase(action)) {
            Queue<TregminePlayer> students = tregmine.getStudentQueue();
            if (students.size() > 0) {
                TregminePlayer student = students.poll();
                // assign mentor
                return true;
            }

            Queue<TregminePlayer> mentors = tregmine.getMentorQueue();
            mentors.offer(player);
        }
        else if ("cancel".equalsIgnoreCase(action)) {
        }
        else {
            return false;
        }

        return true;
    }
}
