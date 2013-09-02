package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

import info.tregmine.api.TregminePlayer;
import info.tregmine.database.IMentorLogDAO;
import static info.tregmine.database.IMentorLogDAO.MentoringEvent;
import info.tregmine.database.DAOException;

public class DBMentorLogDAO implements IMentorLogDAO
{
    private Connection conn;

    public DBMentorLogDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public void insertMentorLog(TregminePlayer student,
                                TregminePlayer mentor)
    throws DAOException
    {
        String sql = "INSERT INTO mentorlog (student_id, mentor_id, " +
            "mentorlog_resumed, mentorlog_startedtime, mentorlog_completedtime, " +
            "mentorlog_cancelledtime, mentorlog_status) ";
        sql += "VALUES (?, ?, 0, unix_timestamp(), 0, 0, 'started')";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, student.getId());
            stmt.setInt(2, mentor.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void updateMentorLogEvent(int mentorLogId,
                                     MentoringEvent event)
    throws DAOException
    {
        String sql = "UPDATE mentorlog SET mentorlog_status = ?, " +
            "mentorlog_completedtime = ?, mentorlog_cancelledtime = ? ";
        sql += "WHERE mentorlog_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.toString());
            if (event == MentoringEvent.COMPLETED) {
                stmt.setLong(2, new Date().getTime()/1000l);
                stmt.setLong(3, 0);
            } else if (event == MentoringEvent.CANCELLED) {
                stmt.setLong(2, 0);
                stmt.setLong(3, new Date().getTime()/1000l);
            } else {
                stmt.setLong(2, 0);
                stmt.setLong(3, 0);
            }
            stmt.setInt(4, mentorLogId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void updateMentorLogResume(int mentorLogId)
    throws DAOException
    {
        String sql = "UPDATE mentorlog SET mentorlog_status = 'started', " +
            "mentorlog_resumed = mentorlog_resumed + 1 ";
        sql += "WHERE mentorlog_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mentorLogId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void updateMentorLogChannel(int mentorLogId, String channel)
    throws DAOException
    {
        String sql = "UPDATE mentorlog SET mentorlog_channel = ? ";
        sql += "WHERE mentorlog_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, channel);
            stmt.setInt(2, mentorLogId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public int getMentorLogId(TregminePlayer student,
                              TregminePlayer mentor)
    throws DAOException
    {
        String sql = "SELECT * FROM mentorlog ";
        sql += "WHERE student_id = ? AND mentor_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, student.getId());
            stmt.setInt(2, mentor.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return 0;
                }

                return rs.getInt("mentorlog_id");
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
