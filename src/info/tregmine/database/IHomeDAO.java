package info.tregmine.database;

import org.bukkit.Location;
import org.bukkit.Server;

import info.tregmine.api.TregminePlayer;

public interface IHomeDAO
{
    public void insertHome(TregminePlayer player, Location loc) throws DAOException;
    public Location getHome(TregminePlayer player) throws DAOException;
    public Location getHome(int playerId, Server server) throws DAOException;
}
