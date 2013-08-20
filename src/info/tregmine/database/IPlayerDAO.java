package info.tregmine.database;

import org.bukkit.entity.Player;

import info.tregmine.api.TregminePlayer;

public interface IPlayerDAO
{
    public TregminePlayer getPlayer(int id) throws DAOException;
    public TregminePlayer getPlayer(Player player) throws DAOException;
    public TregminePlayer getPlayer(String name) throws DAOException;
    public TregminePlayer getPlayer(String name, Player wrap) throws DAOException;

    public TregminePlayer createPlayer(Player wrap) throws DAOException;
    public void updatePlayerKeyword(TregminePlayer player) throws DAOException;
    public void updatePlayTime(TregminePlayer player) throws DAOException;
    public void updatePlayerInfo(TregminePlayer player) throws DAOException;
    public void updatePlayer(TregminePlayer player) throws DAOException;
}
