package info.tregmine.database;

import java.util.HashMap;
import java.util.List;

import info.tregmine.api.Bank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.quadtree.Rectangle;

public interface IBankDAO
{
    public Bank getBank(String name) throws DAOException;
    public Bank getBank(int id) throws DAOException;
    public List<Rectangle> getRectangles(int bankId) throws DAOException;
    public HashMap<String, Long> getAccounts(Bank bank) throws DAOException; 
    public long getAccountBalance(Bank bank, TregminePlayer player) throws DAOException;
    public void createAccount(Bank bank, TregminePlayer player, long amount) throws DAOException;
    public void updateAccounts(Bank bank) throws DAOException;
    public boolean deposit(Bank bank, TregminePlayer player, long deposit) throws DAOException;
    public boolean withdraw(Bank bank, TregminePlayer player, long withdrawl) throws DAOException;
}
