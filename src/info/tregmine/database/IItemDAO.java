package info.tregmine.database;

public interface IItemDAO
{
    public int getItemValue(int itemId, byte itemData) throws DAOException;
}
