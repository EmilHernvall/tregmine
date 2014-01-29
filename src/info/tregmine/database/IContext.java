package info.tregmine.database;

public interface IContext extends AutoCloseable
{
    public void close();

    public IBlessedBlockDAO getBlessedBlockDAO();
    public IBankDAO getBankDAO();
    public IFishyBlockDAO getFishyBlockDAO();
    public IHomeDAO getHomeDAO();
    public IInventoryDAO getInventoryDAO();
    public IItemDAO getItemDAO();
    public ILogDAO getLogDAO();
    public IMentorLogDAO getMentorLogDAO();
    public IMotdDAO getMotdDAO();
    public IPlayerDAO getPlayerDAO();
    public IPlayerReportDAO getPlayerReportDAO();
    public ITradeDAO getTradeDAO();
    public IWalletDAO getWalletDAO();
    public IWarpDAO getWarpDAO();
    public IZonesDAO getZonesDAO();
    public IEnchantmentDAO getEnchantmentDAO();
    public IMiscDAO getMiscDAO();
}
