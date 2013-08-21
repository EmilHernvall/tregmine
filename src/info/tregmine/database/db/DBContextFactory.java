package info.tregmine.database.db;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.apache.commons.dbcp.BasicDataSource;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import info.tregmine.database.DAOException;
import info.tregmine.database.IContextFactory;
import info.tregmine.database.IContext;

public class DBContextFactory implements IContextFactory
{
    private BasicDataSource ds;
    private Map<String, LoggingConnection.LogEntry> queryLog;

    public DBContextFactory(FileConfiguration config)
    {
        queryLog = new HashMap<>();

        String driver = config.getString("db.driver");
        if (driver == null) {
            driver = "com.mysql.jdbc.Driver";
        }

        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        }

        String user = config.getString("db.user");
        String password = config.getString("db.password");
        String url = config.getString("db.url");

        ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setMaxActive(5);
        ds.setMaxIdle(5);
        ds.setDefaultAutoCommit(true);
    }

    public Map<String, LoggingConnection.LogEntry> getLog()
    {
        return queryLog;
    }

    @Override
    public IContext createContext()
    throws DAOException
    {
        try {
            // It's the responsibility of the context to make sure that the
            // connection is correctly closed
            Connection conn = ds.getConnection();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET NAMES latin1");
            }

            return new DBContext(new LoggingConnection(conn, queryLog));
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
