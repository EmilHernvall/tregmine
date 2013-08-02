package info.tregmine.commands;

import java.util.Queue;
import java.sql.Connection;
import java.sql.SQLException;

import static org.bukkit.ChatColor.*;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Rank;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBPlayerDAO;

public class MentorCommand extends AbstractCommand
{
    public static class UpgradeTask implements Runnable
    {
        private TregminePlayer mentor;
        private TregminePlayer student;

        public UpgradeTask(TregminePlayer mentor, TregminePlayer student)
        {
            this.student = student;
            this.mentor = mentor;
        }

        @Override
        public void run()
        {
            if (!mentor.isValid()) {
                return;
            }
            if (!student.isValid()) {
                return;
            }

            mentor.sendMessage(GREEN + "Mentoring of " + student.getChatName() +
                    GREEN + " has now finished!");
            mentor.giveExp(100);

            student.sendMessage(GREEN + "Congratulations! You have now achieved " +
                    "settler status. We hope you'll enjoy your stay on Tregmine!");

            Tregmine.LOGGER.info("[MENTOR] " + student.getChatName() + " completed " +
                                 "mentoring.");

            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();

                student.setRank(Rank.SETTLER);
                student.setTemporaryChatName(student.getNameColor()
                        + student.getName());

                DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
                playerDAO.updatePlayerInfo(student);
                playerDAO.updatePlayerPermissions(student);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                }
            }
        }
    }

    public MentorCommand(Tregmine tregmine)
    {
        super(tregmine, "mentor");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        // residents, donator, not warned players
        if (!player.getRank().canMentor()) {
            player.sendMessage(RED + "Only residents and above can mentor " +
                    "new players.");
            return true;
        }

        String action = "queue";
        if (args.length > 0) {
            action = args[0];
        }

        if ("queue".equalsIgnoreCase(action)) {
            Queue<TregminePlayer> students = tregmine.getStudentQueue();
            if (students.size() > 0) {
                TregminePlayer student = students.poll();
                startMentoring(tregmine, student, player);
                return true;
            }

            Queue<TregminePlayer> mentors = tregmine.getMentorQueue();
            mentors.offer(player);

            player.sendMessage(GREEN + "You are now part of the mentor queue. " +
                    "You are number " + mentors.size() + ". Type /mentor cancel " +
                    "to opt out.");
        }
        else if ("cancel".equalsIgnoreCase(action)) {
            Queue<TregminePlayer> mentors = tregmine.getMentorQueue();
            if (!mentors.contains(player)) {
                player.sendMessage(RED + "You are not part of the mentor queue. " +
                        "If you have already been assigned a student, you cannot " +
                        "about the mentoring.");
                return true;
            }
            mentors.remove(player);

            player.sendMessage(GREEN + "You are no longer part of the mentor queue.");
        }
        else {
            return false;
        }

        return true;
    }

    public static void startMentoring(Tregmine tregmine,
                                      TregminePlayer student,
                                      TregminePlayer mentor)
    {
        if (student.getMentorId() != mentor.getId()) {
            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();

                student.setMentorId(mentor.getId());

                DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
                playerDAO.updatePlayerInfo(student);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                }
            }

            Tregmine.LOGGER.info("[MENTOR] " + mentor.getChatName() + " is " +
                    "mentoring " + student.getChatName());

            // Instructiosn for students
            student.sendMessage(mentor.getChatName() + YELLOW +
                    " has been assigned as your mentor!");
            student.sendMessage(YELLOW + "He or she will show you " +
                    "around, answer any questions, and help you find a place " +
                    "to build.");

            // Instructions for mentor
            mentor.sendMessage(YELLOW + "You have been assigned to " +
                    "mentor " + student.getChatName() + BLUE + ".");
            mentor.sendMessage(YELLOW + "Please do this: ");
            mentor.sendMessage(YELLOW + "1. Explain basic rules");
            mentor.sendMessage(YELLOW + "2. Demonstrate basic commands");
            mentor.sendMessage(YELLOW + "3. Show him or her around");
            mentor.sendMessage(YELLOW + "4. Help him or her to find a lot " +
                    "and start building. If you own a zone, you may sell " +
                    "a lot, but keep in mind that it might be a good idea " +
                    "to let other players make offers too.");
            mentor.sendMessage(YELLOW + "Scamming new players will not be  "+
                    "tolerated.");
            mentor.sendMessage(YELLOW + "For the next fifteen minutes, your student " +
                    "will only be able to build in lots he or she owns. After " +
                    "that time has passed, the student will be automatically upgraded " +
                    "to settler status, and will be able to build everywhere.");
            mentor.sendMessage(YELLOW + "Please start by teleporting to " +
                    student.getChatName() + "!");
        } else {
            student.sendMessage(YELLOW + "Mentoring resuming.");
            mentor.sendMessage(YELLOW + "Mentoring resuming.");
        }

        int timeRemaining = Math.max(60*15 - student.getPlayTime(), 0);
        Tregmine.LOGGER.info("[MENTOR] " + student.getChatName() + " has " +
                            timeRemaining + " seconds of mentoring left.");

        UpgradeTask task = new UpgradeTask(mentor, student);
        if (timeRemaining > 0) {
            Server server = tregmine.getServer();
            BukkitScheduler scheduler = server.getScheduler();
            scheduler.scheduleSyncDelayedTask(tregmine, task, 20 * timeRemaining);
        } else {
            task.run();
        }
    }
}
