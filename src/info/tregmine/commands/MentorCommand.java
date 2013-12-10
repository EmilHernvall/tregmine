package info.tregmine.commands;

import java.util.Queue;

import static org.bukkit.ChatColor.*;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Rank;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerDAO;
import info.tregmine.database.IMentorLogDAO;

public class MentorCommand extends AbstractCommand
{
    public MentorCommand(Tregmine tregmine)
    {
        super(tregmine, "mentor");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        String action = "queue";
        if (args.length > 0) {
            action = args[0];
        }

        if ("queue".equalsIgnoreCase(action)) {
            if (!player.canMentor()) {
                player.sendMessage(RED + "You have not been granted mentoring abilities.");
                return true;
            }

            if (player.getStudent() != null) {
                player.sendMessage(RED + "You can only mentor one " +
                        "student at any given time.");
                return true;
            }

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
            if (player.getRank() == Rank.TOURIST) {
                TregminePlayer mentor = player.getMentor();

                try (IContext ctx = tregmine.createContext()) {
                    IMentorLogDAO mentorLogDAO = ctx.getMentorLogDAO();
                    int mentorLogId = mentorLogDAO.getMentorLogId(player, mentor);
                    mentorLogDAO.updateMentorLogEvent(mentorLogId,
                            IMentorLogDAO.MentoringEvent.CANCELLED);
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }

                player.setMentor(null);
                mentor.setStudent(null);

                mentor.sendMessage(player.getChatName() + RED + " cancelled " +
                        "mentoring with you.");
                player.sendMessage(GREEN + "Mentoring cancelled. Attempting to " +
                        "find you a new mentor.");

                findMentor(tregmine, player);
            } else {
                Queue<TregminePlayer> mentors = tregmine.getMentorQueue();
                if (!mentors.contains(player)) {
                    player.sendMessage(RED + "You are not part of the mentor queue. " +
                            "If you have already been assigned a student, you cannot " +
                            "abort the mentoring.");
                    return true;
                }
                mentors.remove(player);

                player.sendMessage(GREEN + "You are no longer part of the mentor queue.");
            }
        }
        else if ("complete".equalsIgnoreCase(action)) {
            if (!player.canMentor()) {
                player.sendMessage(RED + "You have not been granted mentoring abilities.");
                return true;
            }

            TregminePlayer student = player.getStudent();
            if (student == null) {
                player.sendMessage(RED + "You are not mentoring anyone right now.");
                return true;
            }

            int timeRemaining = Math.max(60*15 - student.getPlayTime()
                                               - student.getTimeOnline(), 0);
            if (timeRemaining > 0) {
                player.sendMessage(RED + student.getChatName() + RED + " has " +
                        timeRemaining + " seconds of mentoring left.");
                return true;
            }

            player.sendMessage(GREEN + "Mentoring of " + student.getChatName() +
                    GREEN + " has now finished!");
            player.giveExp(100);

            student.sendMessage(GREEN + "Congratulations! You have now achieved " +
                    "settler status. We hope you'll enjoy your stay on Tregmine!");

            Tregmine.LOGGER.info("[MENTOR] " + student.getChatName() + " was " +
                                 "promoted to settler by " + player.getChatName() +
                                 ".");

            try (IContext ctx = tregmine.createContext()) {
                student.setRank(Rank.SETTLER);
                student.setMentor(null);
                player.setStudent(null);

                IPlayerDAO playerDAO = ctx.getPlayerDAO();
                playerDAO.updatePlayer(student);
                playerDAO.updatePlayerInfo(student);

                IMentorLogDAO mentorLogDAO = ctx.getMentorLogDAO();
                int mentorLogId = mentorLogDAO.getMentorLogId(student, player);
                mentorLogDAO.updateMentorLogEvent(mentorLogId,
                        IMentorLogDAO.MentoringEvent.COMPLETED);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            return false;
        }

        return true;
    }

    public static void findMentor(Tregmine plugin, TregminePlayer student)
    {
        Queue<TregminePlayer> mentors = plugin.getMentorQueue();
        TregminePlayer mentor = mentors.poll();
        if (mentor != null) {
            startMentoring(plugin, student, mentor);
        } else {
            student.sendMessage(YELLOW + "You will now be assigned " +
                "a mentor to show you around, as soon as one becomes available.");

            Queue<TregminePlayer> students = plugin.getStudentQueue();
            students.offer(student);

            for (TregminePlayer p : plugin.getOnlinePlayers()) {
                if (!p.canMentor()) {
                    continue;
                }

                p.sendMessage(student.getChatName() +
                    YELLOW + " needs a mentor! Type /mentor to " +
                    "offer your services!");
            }
        }
    }

    public static void startMentoring(Tregmine tregmine,
                                      TregminePlayer student,
                                      TregminePlayer mentor)
    {
        student.setMentor(mentor);
        mentor.setStudent(student);

        try (IContext ctx = tregmine.createContext()) {
            IMentorLogDAO mentorLogDAO = ctx.getMentorLogDAO();
            int mentorLogId = mentorLogDAO.getMentorLogId(student, mentor);
            Tregmine.LOGGER.info("Mentor log id: " + mentorLogId);
            if (mentorLogId == 0) {
                mentorLogDAO.insertMentorLog(student, mentor);
            } else {
                mentorLogDAO.updateMentorLogResume(mentorLogId);
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        Tregmine.LOGGER.info("[MENTOR] " + mentor.getChatName() + " is " +
                "mentoring " + student.getChatName());

        // Instructions for students
        student.sendMessage(mentor.getChatName() + GREEN +
                " has been assigned as your mentor!");
        student.sendMessage(YELLOW + "He or she will show you " +
                "around, answer any questions, and help you find a place " +
                "to build.");
        student.sendMessage(YELLOW + "If your mentor turns out to be unhelpful, " +
                "type " + RED + "/mentor cancel" + YELLOW + " to stop and wait " +
                "for a new mentor to become available.");

        // Instructions for mentor
        mentor.sendMessage(GREEN + "You have been assigned to " +
                "mentor " + student.getChatName() + GREEN + ".");
        mentor.sendMessage(YELLOW + "Please do this: ");
        mentor.sendMessage(YELLOW + "1. Explain basic rules (" + RED +
                "Do not force your student to read the rules, or take a test " +
                YELLOW + ")");
        mentor.sendMessage(YELLOW + "2. Demonstrate basic commands");
        mentor.sendMessage(YELLOW + "3. Show him or her around");
        mentor.sendMessage(YELLOW + "4. Help him or her to find a lot " +
                "and start building. If you own a zone, you may sell " +
                "a lot, but keep in mind that it might be a good idea " +
                "to let other players make offers too. Your students will " +
                "also be able to build anywhere as long as they are within a " +
                "50 block radius of you.");
        mentor.sendMessage(YELLOW + "Scamming new players will not be  "+
                "tolerated.");
        mentor.sendMessage(YELLOW + "Mentoring takes at least 15 minutes, and " +
                "after that time has passed you can upgrade the tourist to " +
                "settler rank by doing " + GREEN + "/mentor complete" +
                YELLOW + ".");
        mentor.sendMessage(YELLOW + "Please start by teleporting to " +
                student.getChatName() + YELLOW + ", or by summoning him or her!");
    }
}
