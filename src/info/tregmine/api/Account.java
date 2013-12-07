package info.tregmine.api;

/**
 * Represents a bank account holding information as to
 * how much a player currently has in a certain bank.
 * @author Robert Catron
 * @since 12/7/2013
 */
public class Account
{
    
    private Bank bank;
    private int id;
    private String player;
    private long amount;

    public Account(){}
    
    public Bank getBank(){ return bank; }
    public void setBank(Bank bank){ this.bank = bank; }
    
    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }
    
    public String getPlayer(){ return player; }
    public void setPlayer(String player){ this.player = player; }
    
    public long getAmount(){ return amount; }
    public void setAmount(long amount){ this.amount = amount; }
}
