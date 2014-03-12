package info.tregmine.api.bank;

public class Account
{

    private Bank bank;
    private int id;
    private int playerId;
    private long balance;
    private String pin;

    private int account_number;

    private boolean verified;

    public Account(){}

    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }

    public int getId() { return id; }
    public void setId(int v) { this.id = v; }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int v) { this.playerId = v; }

    public long getBalance() { return balance; }
    public void setBalance(long v) { this.balance = v; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public int getAccountNumber() { return account_number; }
    public void setAccountNumber(int v) { this.account_number = v; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean v){ this.verified = v; }
}
