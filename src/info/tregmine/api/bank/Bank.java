package info.tregmine.api.bank;

import com.google.common.collect.Lists;

import java.util.List;

public class Bank
{
    private int id;
    private int zoneId;

    private List<Account> accounts;

    public Bank(int zoneId)
    {
        this.zoneId = zoneId;

        accounts = Lists.newArrayList();
    }

    public Bank(int id, int zoneId)
    {
        this(zoneId);
        this.id = id;
    }

    public Bank(){}

    public int getId(){ return this.id; }
    public void setId(int id){ this.id = id; }

    public int getZoneId(){ return zoneId; }
    public void setZoneId(int id){ this.zoneId = id; }

    public List<Account> getAccounts(){ return accounts; }
    public void setAccounts(List<Account> accounts){ this.accounts = accounts; }
}
