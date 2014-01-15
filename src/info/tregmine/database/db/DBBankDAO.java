package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import info.tregmine.api.Account;
import info.tregmine.api.Bank;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;

public class DBBankDAO implements IBankDAO
{
    private Connection conn;
    private Random r;

    public DBBankDAO(Connection conn)
    {
        this.conn = conn;
        this.r = new Random();
    }

    @Override
    public Bank getBank(int lotId)
    throws DAOException
    {
        String sql = "SELECT * FROM bank WHERE lot_id = ? LIMIT 1";
        Bank bank = null;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, lotId);
            stm.execute();

            ResultSet rs = stm.getResultSet();

            if (rs.next()) {
                bank = new Bank(rs.getInt("lot_id"));
                bank.setId(rs.getInt("bank_id"));
                bank.setAccounts(this.getAccounts(bank));

                return bank;
            }

        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        return null;
    }

    @Override
    public int createBank(Bank bank) throws DAOException
    {
        String sql = "INSERT INTO bank (lot_id) VALUES (?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, bank.getLotId());
            stm.execute();

            stm.executeQuery("SELECT LAST_INSERT_ID()");
            try (ResultSet rs = stm.getResultSet()) {
                if (!rs.next()) {
                    return 0;
                }
                return rs.getInt(0);
            }

        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    public void deleteBank(Bank bank)
    throws DAOException
    {
        String sql = "DELETE FROM bank WHERE bank_id = ?";

        try (PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setInt(1, bank.getId());
            stm.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public List<Account> getAccounts(Bank bank)
    throws DAOException
    {
        String sql = "SELECT * FROM bank_account WHERE bank_id = ?";
        List<Account> accounts = Lists.newArrayList();
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, bank.getId());
            stm.execute();

            try (ResultSet rs = stm.getResultSet()) {

                while (rs.next()) {
                    Account acct = new Account();
                    acct.setBank(bank);
                    acct.setPlayerId(rs.getInt("player_id"));
                    acct.setBalance(rs.getLong("account_balance"));
                    acct.setAccountNumber(rs.getInt("account_number"));
                    acct.setPin(rs.getString("account_pin"));
                    accounts.add(acct);
                }

            }
            return accounts;

        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public Account getAccountByPlayer(Bank bank, int player)
    throws DAOException
    {
        String sql = "SELECT * FROM bank_account " +
            "WHERE bank_id = ? AND player_id = ?";
        Account acct = null;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, bank.getId());
            stm.setInt(2, player);
            stm.execute();

            try (ResultSet rs = stm.getResultSet()) {
                if (rs.next()) {
                    acct = new Account();
                    acct.setBank(bank);
                    acct.setPlayerId(rs.getInt("player_id"));
                    acct.setBalance(rs.getLong("account_balance"));
                    acct.setAccountNumber(rs.getInt("account_number"));
                    acct.setPin(rs.getString("account_pin"));
                    return acct;
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        return null;
    }

    public Account getAccount(Bank bank, int accNumber)
    throws DAOException
    {
        String sql = "SELECT * FROM bank_account " +
            "WHERE bank_id = ? AND account_number = ?";
        try(PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setInt(1, bank.getId());
            stm.setInt(2, accNumber);

            stm.execute();

            try(ResultSet rs = stm.getResultSet()){
                if (rs.next()) {
                    Account acc = new Account();
                    acc.setAccountNumber(accNumber);
                    acc.setBank(bank);
                    acc.setBalance(rs.getLong("account_balance"));
                    acc.setPlayerId(rs.getInt("player_id"));
                    acc.setPin(rs.getString("account_pin"));
                    return acc;
                }
            }

        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        return null;
    }

    public void createAccount(Account acct, int playerId, long amount)
    throws DAOException
    {
        if (getAccount(acct.getBank(), acct.getAccountNumber()) != null) {
            acct.setAccountNumber(acct.getAccountNumber() + 1);
        }

        String sql = "INSERT INTO bank_account (bank_id, player_id, " +
            "account_balance, account_number, account_pin) VALUES (?,?,?,?, ?)";

        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, acct.getBank().getId());
            stm.setInt(2, playerId);
            stm.setLong(3, amount);
            stm.setInt(4, acct.getAccountNumber());
            String s = "";
            for (int i = 0; i < 4; i++) {
                s += String.valueOf(r.nextInt(10));
            }
            stm.setString(5, s);

            acct.setPin(s);
            stm.execute();
        }catch(SQLException e){
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void setPin(Account acct, String pin)
    throws DAOException
    {
        String sql = "UPDATE bank_account SET account_pin = ? " +
            "WHERE account_number = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, pin);
            stm.setInt(2, acct.getAccountNumber());
            stm.execute();
        } catch(SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void deposit(Bank bank, Account acct, long amount)
    throws DAOException
    {
        String sql = "UPDATE bank_account SET account_balance = account_balance + ? " +
            "WHERE bank_id = ? AND player_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setLong(1, amount);
            stm.setInt(2, bank.getId());
            stm.setInt(3, acct.getPlayerId());
            stm.execute();
        } catch(SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public boolean withdraw(Bank bank, Account acct, long amount)
    throws DAOException
    {
        if (acct.getBalance() - amount < 0) {
            return false;
        }

        String sql = "UPDATE bank_account SET account_balance = account_balance - ? " +
            "WHERE bank_id = ? AND player_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setLong(1, amount);
            stm.setInt(2, bank.getId());
            stm.setInt(3, acct.getPlayerId());
            stm.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return true;
    }

}
