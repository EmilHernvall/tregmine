package info.tregmine.database.db;

import java.util.*;
import java.util.concurrent.*;
import java.sql.*;

public class LoggingConnection implements Connection
{
    public static class LogEntry
    {
        public String sql;
        public int invocationCount;
        public double avgTime;
        public double maxTime;
    }

    private Connection delegate;

    private Map<String, LogEntry> log;

    public LoggingConnection(Connection delegate)
    {
        this.delegate = delegate;
        this.log = new HashMap<String, LogEntry>();
    }

    public LoggingConnection(Connection delegate, Map<String, LogEntry> log)
    {
        this.delegate = delegate;
        this.log = log;
    }

    public Map<String, LogEntry> getLog()
    {
        return log;
    }

    public synchronized void updateEntry(String sql, long execTime)
    {
        LogEntry logEntry;
        if (!log.containsKey(sql)) {
            logEntry = new LogEntry();
            logEntry.sql = sql;
            logEntry.invocationCount = 0;
            logEntry.avgTime = 0;
            logEntry.maxTime = 0;
            log.put(sql, logEntry);
        } else {
            logEntry = log.get(sql);
        }

        logEntry.avgTime = (logEntry.invocationCount * logEntry.avgTime + execTime) / (logEntry.invocationCount + 1);
        logEntry.maxTime = Math.max(logEntry.maxTime, execTime);
        logEntry.invocationCount++;
    }

    @Override
    public void abort(Executor executor)
    throws SQLException
    {
        delegate.abort(executor);
    }

    @Override
    public void clearWarnings()
    throws SQLException
    {
        delegate.clearWarnings();
    }

    @Override
    public void close()
    throws SQLException
    {
        delegate.close();
    }

    @Override
    public void commit()
    throws SQLException
    {
        delegate.commit();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
    throws SQLException
    {
        return delegate.createArrayOf(typeName, elements);
    }

    @Override
    public Blob createBlob()
    throws SQLException
    {
        return delegate.createBlob();
    }

    @Override
    public Clob createClob()
    throws SQLException
    {
        return delegate.createClob();
    }

    @Override
    public NClob createNClob()
    throws SQLException
    {
        return delegate.createNClob();
    }

    @Override
    public SQLXML createSQLXML()
    throws SQLException
    {
        return delegate.createSQLXML();
    }

    @Override
    public Statement createStatement()
    throws SQLException
    {
        return delegate.createStatement();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
    throws SQLException
    {
        return delegate.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    throws SQLException
    {
        return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
    throws SQLException
    {
        return delegate.createStruct(typeName, attributes);
    }

    @Override
    public boolean getAutoCommit()
    throws SQLException
    {
        return delegate.getAutoCommit();
    }

    @Override
    public String getCatalog()
    throws SQLException
    {
        return delegate.getCatalog();
    }

    @Override
    public Properties getClientInfo()
    throws SQLException
    {
        return delegate.getClientInfo();
    }

    @Override
    public String getClientInfo(String name)
    throws SQLException
    {
        return delegate.getClientInfo(name);
    }

    @Override
    public int getHoldability()
    throws SQLException
    {
        return delegate.getHoldability();
    }

    @Override
    public DatabaseMetaData getMetaData()
    throws SQLException
    {
        return delegate.getMetaData();
    }

    @Override
    public int getTransactionIsolation()
    throws SQLException
    {
        return delegate.getTransactionIsolation();
    }

    @Override
    public Map<String,Class<?>> getTypeMap()
    throws SQLException
    {
        return delegate.getTypeMap();
    }

    @Override
    public SQLWarning getWarnings()
    throws SQLException
    {
        return delegate.getWarnings();
    }

    @Override
    public boolean isClosed()
    throws SQLException
    {
        return delegate.isClosed();
    }

    @Override
    public boolean isReadOnly()
    throws SQLException
    {
        return delegate.isReadOnly();
    }

    @Override
    public boolean isValid(int timeout)
    throws SQLException
    {
        return delegate.isValid(timeout);
    }

    @Override
    public String nativeSQL(String sql)
    throws SQLException
    {
        return delegate.nativeSQL(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql)
    throws SQLException
    {
        return delegate.prepareCall(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
    throws SQLException
    {
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    throws SQLException
    {
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql)
    throws SQLException
    {
        return new LoggingPreparedStatement(delegate.prepareStatement(sql), sql, this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
    throws SQLException
    {
        return new LoggingPreparedStatement(delegate.prepareStatement(sql, autoGeneratedKeys), sql, this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
    throws SQLException
    {
        return new LoggingPreparedStatement(delegate.prepareStatement(sql, columnIndexes), sql, this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
    throws SQLException
    {
        return new LoggingPreparedStatement(delegate.prepareStatement(sql, resultSetType, resultSetConcurrency), sql, this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    throws SQLException
    {
        return new LoggingPreparedStatement(delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), sql, this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
    throws SQLException
    {
        return new LoggingPreparedStatement(delegate.prepareStatement(sql, columnNames), sql, this);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint)
    throws SQLException
    {
        delegate.releaseSavepoint(savepoint);
    }

    @Override
    public void rollback()
    throws SQLException
    {
        delegate.rollback();
    }

    @Override
    public void rollback(Savepoint savepoint)
    throws SQLException
    {
        delegate.rollback(savepoint);
    }

    @Override
    public void setAutoCommit(boolean autoCommit)
    throws SQLException
    {
        delegate.setAutoCommit(autoCommit);
    }

    @Override
    public void setCatalog(String catalog)
    throws SQLException
    {
        delegate.setCatalog(catalog);
    }

    @Override
    public void setClientInfo(Properties properties)
    throws SQLClientInfoException
    {
        delegate.setClientInfo(properties);
    }

    @Override
    public void setClientInfo(String name, String value)
    throws SQLClientInfoException
    {
        delegate.setClientInfo(name, value);
    }

    @Override
    public void setHoldability(int holdability)
    throws SQLException
    {
        delegate.setHoldability(holdability);
    }

    @Override
    public void setReadOnly(boolean readOnly)
    throws SQLException
    {
        delegate.setReadOnly(readOnly);
    }

    @Override
    public Savepoint setSavepoint()
    throws SQLException
    {
        return delegate.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name)
    throws SQLException
    {
        return delegate.setSavepoint(name);
    }

    @Override
    public void setTransactionIsolation(int level)
    throws SQLException
    {
        delegate.setTransactionIsolation(level);
    }

    @Override
    public void setTypeMap(Map<String,Class<?>> map)
    throws SQLException
    {
        delegate.setTypeMap(map);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds)
    throws SQLException
    {
        delegate.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout()
    throws SQLException
    {
        return delegate.getNetworkTimeout();
    }

    @Override
    public String getSchema()
    throws SQLException
    {
        return delegate.getSchema();
    }

    @Override
    public void setSchema(String v)
    throws SQLException
    {
        delegate.setSchema(v);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)
    {
        return iface.isInstance(delegate);
    }

    @Override
    public <T> T unwrap(Class<T> iface)
    {
        return iface.cast(delegate);
    }
}
