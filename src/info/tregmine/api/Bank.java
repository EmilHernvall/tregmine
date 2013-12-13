package info.tregmine.api;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Robert Catron
 * @since 12/7/2013
 * @see Account
 */
public class Bank
{
    
    private String name;
    private int id;
    
    private List<Account> accounts;
    private int lot_id;
    
    public Bank(String name)
    {
        this.name = name;
        
        accounts = Lists.newArrayList();
    }
    
    public Bank(String name, int id)
    {
        this(name);
        this.id = id;
    }
    
    public Bank(){}
    
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    
    public int getId(){ return this.id; }
    public void setId(int id){ this.id = id; }
    
    public List<Account> getAccounts(){ return accounts; }
    public void setAccounts(List<Account> accounts){ this.accounts = accounts; }
    
    public int getLotId(){ return lot_id; }
    public void setLotId(int id){ this.lot_id = id; }
}