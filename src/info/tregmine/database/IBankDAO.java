package info.tregmine.database;

import java.util.List;

import info.tregmine.api.Account;
import info.tregmine.api.Bank;

public interface IBankDAO
{
    
    public Bank getBank(String name) throws DAOException;
    public int createBank(Bank bank) throws DAOException;
    public void deleteBank(Bank bank) throws DAOException;
    
    public List<Account> getAccounts(Bank bank) throws DAOException;
    public Account getAccount(Bank bank, String player) throws DAOException;
    public Account getAccount(Bank bank, int accNumber) throws DAOException;
    public void setPin(Account acct, String pin) throws DAOException;
    
    public void createAccount(Account acct, String player, long amount) throws DAOException;
    public void deposit(Bank bank, Account acct, long amount) throws DAOException;
    public boolean withdraw(Bank bank, Account acct, long amount) throws DAOException;
    
    
}
