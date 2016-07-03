package info.tregmine.database;

import java.util.Date;
import java.util.List;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.PlayerReport;
import info.tregmine.api.StaffNews;

public interface IStaffNewsDAO {
    public List<StaffNews> getStaffNews()
    throws DAOException;

    public void insertNews(StaffNews news) throws DAOException;
}
