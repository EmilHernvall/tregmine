package info.tregmine.database;

import info.tregmine.api.Account;
import info.tregmine.api.Bank;

import java.util.List;
import java.util.UUID;

public interface IBankDAO
{
    public Bank getBank(int bankId) throws DAOException;
    public int createBank(Bank bank) throws DAOException;
    public void deleteBank(Bank bank) throws DAOException;

    public List<Account> getAccounts(Bank bank) throws DAOException;
    public Account getAccountByPlayer(Bank bank, int playerId) throws DAOException;
    public Account getAccount(Bank bank, int accNumber) throws DAOException;
    public void setPin(Account acct, String pin) throws DAOException;

    public void createAccount(Account acct, int playerId) throws DAOException;
    public void deposit(Bank bank, Account acct, int playerId, long amount) throws DAOException;
    public boolean withdraw(Bank bank, Account acct, int playerId, long amount) throws DAOException;

    public void addBanker(Bank bank, UUID uuid, String name) throws DAOException;
    public void deleteBanker(UUID uuid) throws DAOException;
    public boolean isBanker(Bank bank, UUID uniqueId) throws DAOException;
}
