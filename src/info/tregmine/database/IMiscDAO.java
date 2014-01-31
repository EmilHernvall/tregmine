package info.tregmine.database;

import java.util.List;

import org.bukkit.Location;

public interface IMiscDAO
{
    public List<String> loadInsults() throws DAOException;
    public List<String> loadQuitMessages() throws DAOException;
    
    /*
     * I don't know where else to put this
     */
    public boolean blocksWereChanged(Location start, int radius) throws DAOException;
}
