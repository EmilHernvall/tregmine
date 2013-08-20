package info.tregmine.database.db;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.PlayerReport;
import info.tregmine.database.IPlayerReportDAO;
import info.tregmine.database.DAOException;

public class DBPlayerReportDAO implements IPlayerReportDAO
{
    private Connection conn;

    public DBPlayerReportDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public List<PlayerReport> getReportsBySubject(TregminePlayer player)
    throws DAOException
    {
        String sql = "SELECT * FROM player_report ";
        sql += "WHERE subject_id = ? ";
        sql += "ORDER BY report_timestamp DESC";

        List<PlayerReport> reports = new ArrayList<PlayerReport>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    PlayerReport report = new PlayerReport();
                    report.setId(rs.getInt("report_id"));
                    report.setSubjectId(rs.getInt("subject_id"));
                    report.setIssuerId(rs.getInt("issuer_id"));
                    report.setAction(PlayerReport.Action.fromString(rs
                            .getString("report_action")));
                    report.setMessage(rs.getString("report_message"));
                    report.setTimestamp(new Date(
                            rs.getInt("report_timestamp") * 1000l));

                    int validUntil = rs.getInt("report_validuntil");
                    if (validUntil != 0) {
                        report.setValidUntil(new Date(validUntil * 1000l));
                    }

                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return reports;
    }

    @Override
    public void insertReport(PlayerReport report) throws DAOException
    {
        String sql = "INSERT INTO player_report (subject_id, issuer_id, " +
            "report_action, report_message, report_timestamp, report_validuntil) ";
        sql += "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, report.getSubjectId());
            stmt.setInt(2, report.getIssuerId());
            PlayerReport.Action action = report.getAction();
            stmt.setString(3, action.toString());
            stmt.setString(4, report.getMessage());
            Date timestamp = report.getTimestamp();
            stmt.setLong(5, timestamp.getTime() / 1000l);
            Date validUntil = report.getValidUntil();
            stmt.setLong(6, validUntil != null ? validUntil.getTime() / 1000l
                    : 0);
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            try (ResultSet rs = stmt.getResultSet()) {
                if (rs.next()) {
                    report.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

}
