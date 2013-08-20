package info.tregmine.database;

public class DAOException extends Exception
{
    private String query = null;

    public DAOException(String message, String query)
    {
        super(message);

        this.query = query;
    }

    public DAOException(String query, Throwable e)
    {
        super(e);

        this.query = query;
    }

    public DAOException(Throwable e)
    {
        super(e);
    }

    public String getQuery() { return query; }
}
