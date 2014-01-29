package info.tregmine.database.db;

import info.tregmine.Tregmine;
import info.tregmine.database.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DBContext implements IContext
{
    private Connection conn;
    private Tregmine plugin;

    public DBContext(Connection conn, Tregmine instance)
    {
        this.conn = conn;
        this.plugin = instance;
    }

    public Connection getConnection()
    {
        return conn;
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
        return new DBPlayerDAO(conn, plugin);
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

    @Override
    public IMentorLogDAO getMentorLogDAO()
    {
        return new DBMentorLogDAO(conn);
    }

    @Override
    public IFishyBlockDAO getFishyBlockDAO()
    {
        return new DBFishyBlockDAO(conn);
    }

    @Override
    public IBlessedBlockDAO getBlessedBlockDAO()
    {
        return new DBBlessedBlockDAO(conn);
    }

    @Override
    public IEnchantmentDAO getEnchantmentDAO()
    {
        return new DBEnchantmentDAO(conn);
    }
    
    @Override
    public IBankDAO getBankDAO()
    {
        return new DBBankDAO(conn);
    }
    
    @Override
    public IMiscDAO getMiscDAO()
    {
        return new DBMiscDAO(conn);
    }
}
