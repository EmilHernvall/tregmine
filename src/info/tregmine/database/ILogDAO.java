package info.tregmine.database;

import java.util.Date;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import info.tregmine.api.TregminePlayer;

public interface ILogDAO
{
    public void insertLogin(TregminePlayer player,
                            boolean logout,
                            int onlinePlayers) throws DAOException;

    public void insertChatMessage(TregminePlayer player,
                                  String channel,
                                  String message) throws DAOException;

    public void insertOreLog(TregminePlayer player,
                             Location loc,
                             int material) throws DAOException;

    public void insertGiveLog(TregminePlayer sender,
                              TregminePlayer recipient,
                              ItemStack stack) throws DAOException;

    public void insertWarpLog(TregminePlayer player,
                              int warpId) throws DAOException;

    public Date getLastSeen(TregminePlayer player) throws DAOException;

    public Set<String> getAliases(TregminePlayer player) throws DAOException;
}
