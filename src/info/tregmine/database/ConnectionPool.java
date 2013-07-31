package info.tregmine.database;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.apache.commons.dbcp.BasicDataSource;

public class ConnectionPool
{
    private static BasicDataSource ds;

    public static Connection getConnection() throws SQLException
    {
        if (ds == null) {
            String driver = "com.mysql.jdbc.Driver";
            try {
                Class.forName(driver).newInstance();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            }

            String user = null, password = null, url = null;
            try {
                Properties settings = new Properties();
                settings.load(new FileInputStream("mysql.cfg"));
                url = settings.getProperty("url");
                user = settings.getProperty("user");
                password = settings.getProperty("password");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ds = new BasicDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(password);
            ds.setMaxActive(5);
            ds.setMaxIdle(5);
            ds.setDefaultAutoCommit(true);
        }

        Connection conn = ds.getConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute("SET NAMES latin1");
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        return conn;
    }
}
