package info.tregmine.database;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;

import info.tregmine.api.TregminePlayer;

public interface IHomeDAO
{
    public void insertHome(TregminePlayer player, String name, Location loc) throws DAOException;
    public Location getHome(TregminePlayer player) throws DAOException;
    public Location getHome(TregminePlayer player, String name) throws DAOException;
    public Location getHome(int playerId, String name, Server server) throws DAOException;
    public List<String> getHomeNames(int playerId) throws DAOException;
    public void deleteHome(int playerId, String name) throws DAOException;
}
