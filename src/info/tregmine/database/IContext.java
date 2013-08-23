package info.tregmine.database;

public interface IContext extends AutoCloseable
{
    public void close();

    public IHomeDAO getHomeDAO();
    public IInventoryDAO getInventoryDAO();
    public IItemDAO getItemDAO();
    public ILogDAO getLogDAO();
    public IMotdDAO getMotdDAO();
    public IPlayerDAO getPlayerDAO();
    public IPlayerReportDAO getPlayerReportDAO();
    public ITradeDAO getTradeDAO();
    public IWalletDAO getWalletDAO();
    public IWarpDAO getWarpDAO();
    public IZonesDAO getZonesDAO();
}
