package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.common.collect.Lists;

import info.tregmine.api.Account;
import info.tregmine.api.Bank;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;

public class DBBankDAO implements IBankDAO
{
    
    private Connection conn;
    
    public DBBankDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public Bank getBank(String name)
    throws DAOException
    {
        String sql = "SELECT * FROM banks WHERE bank_name = ? LIMIT 1";
        Bank bank = null;
        try(PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setString(1, name);
            stm.execute();
            
            ResultSet rs = stm.getResultSet();
            
            if(rs.next()){
                bank = new Bank(name);
                bank.setId(rs.getInt("bank_id"));
                bank.setAccounts(this.getAccounts(bank));
                bank.setLotId(rs.getInt("lot_id"));
                
                return bank;
            }
            
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
        return null;
    }

    @Override
    public int createBank(Bank bank)
    throws DAOException
    {
        String sql = "INSERT INTO banks (bank_name, lot_id) VALUES (?, ?)";
        try(PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setString(1, bank.getName());
            stm.setInt(2, bank.getLotId());
            stm.execute();
            
            try(ResultSet rs = stm.getResultSet()){
                if(!rs.next()){
                    return 0;
                }
                return rs.getInt("bank_id");
            }
            
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
    }

    @Override
    public List<Account> getAccounts(Bank bank)
    throws DAOException
    {
        String sql = "SELECT * FROM bank_accounts WHERE bank_name = ?";
        List<Account> accounts = Lists.newArrayList();
        try(PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setString(1, bank.getName());
            stm.execute();
            
            try(ResultSet rs = stm.getResultSet()){
            
                while(rs.next()){
                    Account acct = new Account();
                    acct.setBank(bank);
                    acct.setPlayer(rs.getString("player_name"));
                    acct.setAmount(rs.getLong("account_balance"));
                    accounts.add(acct);
                }
            
            }
            return accounts;
            
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
    }

    @Override
    public Account getAccount(Bank bank, String player)
    throws DAOException
    {
        String sql = "SELECT * FROM bank_accounts WHERE bank_name = ? AND player_name = ?";
        Account acct = null;
        try(PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setInt(1, bank.getId());
            stm.setString(2, player);
            stm.execute();
            
            ResultSet rs = stm.getResultSet();
            if(rs.next()){
                acct = new Account();
                acct.setBank(bank);
                acct.setPlayer(player);
                acct.setAmount(rs.getLong("account_balance"));
                return acct;
            }
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
        return null;
    }

}
