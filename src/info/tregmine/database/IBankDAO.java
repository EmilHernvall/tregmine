package info.tregmine.database;

import java.util.List;

import info.tregmine.api.Account;
import info.tregmine.api.Bank;

public interface IBankDAO
{
    
    public Bank getBank(String name) throws DAOException;
    public int createBank(Bank bank) throws DAOException;
    
    public List<Account> getAccounts(Bank bank) throws DAOException;
    public Account getAccount(Bank bank, String player) throws DAOException;
    
    
    
}
