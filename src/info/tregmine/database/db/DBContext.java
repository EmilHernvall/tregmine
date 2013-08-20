package info.tregmine.database.db;

import java.io.*;
import java.util.*;
import java.sql.*;

import info.tregmine.database.*;

import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;

public class DBContext implements IContext
{
    private Connection conn;

    public DBContext(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public void close()
    {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { }
        }
    }

    @Override
    public IHomeDAO getHomeDAO()
    {
        return new DBHomeDAO(conn);
    }

    @Override
    public IInventoryDAO getInventoryDAO()
    {
        return new DBInventoryDAO(conn);
    }

    @Override
    public IItemDAO getItemDAO()
    {
        return new DBItemDAO(conn);
    }

    @Override
    public ILogDAO getLogDAO()
    {
        return new DBLogDAO(conn);
    }

    @Override
    public IMotdDAO getMotdDAO()
    {
        return new DBMotdDAO(conn);
    }

    @Override
    public IPlayerDAO getPlayerDAO()
    {
        return new DBPlayerDAO(conn);
    }

    @Override
    public IPlayerReportDAO getPlayerReportDAO()
    {
        return new DBPlayerReportDAO(conn);
    }

    @Override
    public ITradeDAO getTradeDAO()
    {
        return new DBTradeDAO(conn);
    }

    @Override
    public IWalletDAO getWalletDAO()
    {
        return new DBWalletDAO(conn);
    }

    @Override
    public IWarpDAO getWarpDAO()
    {
        return new DBWarpDAO(conn);
    }

    @Override
    public IZonesDAO getZonesDAO()
    {
        return new DBZonesDAO(conn);
    }
}
