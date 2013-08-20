package info.tregmine.database;

import java.util.Date;
import java.util.List;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.PlayerReport;

public interface IPlayerReportDAO
{
    public List<PlayerReport> getReportsBySubject(TregminePlayer player)
    throws DAOException;

    public void insertReport(PlayerReport report) throws DAOException;
}
