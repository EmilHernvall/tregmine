package info.tregmine.database;

import org.bukkit.Server;
import org.bukkit.Location;

import info.tregmine.api.Warp;

public interface IWarpDAO
{
    public void insertWarp(String name, Location loc) throws DAOException;
    public Warp getWarp(String name, Server server) throws DAOException;
}
