package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import info.tregmine.api.Bank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;
import info.tregmine.quadtree.Rectangle;

public class DBBankDAO implements IBankDAO
{
    private Connection conn;

    public DBBankDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public Bank getBank(String name) throws DAOException
    {
        String sql = "SELECT * FROM bank WHERE bank_name = ?";
        Bank bank = new Bank(name);
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, name);
            stmt.execute();
            
            try(ResultSet rs = stmt.getResultSet()){
                if(!rs.next()){
                    return null;
                }
                bank.setId(rs.getInt("bank_id"));
                bank.setAccounts(getAccounts(bank));
                bank.setRects(getRectangles(bank.getId()));
                bank.setWorld(rs.getString("bank_world"));
                bank.setOwner(rs.getString("bank_owner"));
            }
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
        return bank;
    }

    @Override
    public Bank getBank(int id) throws DAOException
    {
        String sql = "SELECT * FROM bank WHERE bank_id = ?";
        Bank bank = new Bank(id);
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            stmt.execute();
            try(ResultSet rs = stmt.getResultSet()){
                if(!rs.next()){
                    return null;
                }
                bank.setId(rs.getInt("bank_id"));
                bank.setAccounts(getAccounts(bank));
                bank.setRects(getRectangles(bank.getId()));
                bank.setWorld(rs.getString("bank_world"));
                bank.setOwner(rs.getString("bank_owner"));
            }
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
        return bank;
    }

    @Override
    public List<Rectangle> getRectangles(int bankId) throws DAOException
    {
        String sql = "SELECT * FROM bank WHERE bank_id = ?";
        List<Rectangle> rects = Lists.newArrayList();
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, bankId);
            stmt.execute();
            try(ResultSet rs = stmt.getResultSet()){
                if(!rs.next()){
                    return null;
                }
                int x1 = rs.getInt("rect_x1");
                int y1 = rs.getInt("rect_y1");
                int x2 = rs.getInt("rect_x2");
                int y2 = rs.getInt("rect_y2");
                rects.add(new Rectangle(x1, y1, x2, y2));
            }
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
        return rects;
    }

    @Override
    public HashMap<String, Long> getAccounts(Bank bank) throws DAOException
    {
        String sql = "SELECT * FROM bank_account WHERE bank_id = ?";
        HashMap<String, Long> accounts = Maps.newHashMap();
        
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, bank.getId());
            stmt.execute();
            try(ResultSet rs = stmt.getResultSet()){
                while(rs.next()){
                    accounts.put(rs.getString("player_name"), rs.getLong("account_balance"));
                }
            }
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
        return accounts;
    }

    @Override
    public long getAccountBalance(Bank bank, TregminePlayer player)
            throws DAOException
    {
        updateAccounts(bank);
        if (!getAccounts(bank).containsKey(player.getName()))
            return 0;
        return getAccounts(bank).get(player.getName());
    }

    @Override
    public void createAccount(Bank bank, TregminePlayer player, long amount)
            throws DAOException
    {
        String sql =
                "INSERT INTO bank_account (bank_id, player_name, account_balance) VALUES (?, ?, ?)";
        try(PreparedStatement stmt= conn.prepareStatement(sql)){
            stmt.setInt(1, bank.getId());
            stmt.setString(2, player.getName());
            stmt.setLong(3, amount);
            stmt.execute();
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void updateAccounts(Bank bank) throws DAOException
    {
        bank.setAccounts(getAccounts(bank));
    }

    @Override
    public boolean deposit(Bank bank, TregminePlayer player, long deposit)
            throws DAOException
    {
        String sql =
                "UPDATE bank_account SET account_balance = account_balance + ? WHERE bank_id = ? AND player_name = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, withdrawl);
            stmt.setInt(2, bank.getId());
            stmt.setString(3, player.getName());
            stmt.execute();
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
        return true;
    }

    @Override
    public boolean withdraw(Bank bank, TregminePlayer player, long withdrawl)
            throws DAOException
    {
        String sql =
                "UPDATE bank_account SET account_balance = account_balance - ? WHERE bank_id = ? AND player_name = ?";
        long newBalance = getAccountBalance(bank, player) - withdrawl;
        if(newBalance < 0){
            return false;
        }
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, withdrawl);
            stmt.setInt(2, bank.getId());
            stmt.setString(3, player.getName());
            stmt.execute();
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
        return true;
    }
}
