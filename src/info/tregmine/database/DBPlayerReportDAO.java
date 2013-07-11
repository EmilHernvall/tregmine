package info.tregmine.database;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.PlayerReport;

public class DBPlayerReportDAO
{
    private Connection conn;

    public DBPlayerReportDAO(Connection conn)
    {
        this.conn = conn;
    }

    public List<PlayerReport> getReportsBySubject(TregminePlayer player)
    {
        List<PlayerReport> reports = new ArrayList<PlayerReport>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM player_report ";
            sql += "WHERE subject_id = ? ";
            sql += "ORDER BY report_timestamp DESC";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player.getId());
            stmt.execute();

            rs = stmt.getResultSet();
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        return reports;
    }

    public void insertReport(PlayerReport report) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "INSERT INTO player_report (subject_id, issuer_id, "
                            + "report_action, report_message, report_timestamp, "
                            + "report_validuntil) ";
            sql += "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);

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

            rs = stmt.getResultSet();
            if (rs.next()) {
                report.setId(rs.getInt(1));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

}
