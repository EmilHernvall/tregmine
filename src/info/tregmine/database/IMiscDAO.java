package info.tregmine.database;

import java.util.List;

public interface IMiscDAO
{
    public List<String> loadInsults() throws DAOException;
    public List<String> loadQuitMessages() throws DAOException;
}
