package info.tregmine.database.db;

import com.google.common.collect.Lists;
import info.tregmine.Tregmine;
import info.tregmine.api.bank.Account;
import info.tregmine.api.bank.Bank;
import info.tregmine.api.bank.Banker;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Villager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        String sql = "SELECT * FROM bank WHERE zone_id = ? LIMIT 1";
        Bank bank = null;
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, lotId);
            stm.execute();

            ResultSet rs = stm.getResultSet();

            if (rs.next()) {
                bank = new Bank(rs.getInt("zone_id"));
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
        String sql = "INSERT INTO bank (zone_id) VALUES (?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, bank.getZoneId());
            stm.execute();

            stm.executeQuery("SELECT LAST_INSERT_ID()");
            try (ResultSet rs = stm.getResultSet()) {
                if (!rs.next()) {
                    return 0;
                }
                return rs.getInt(1);
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
                    acct.setId(rs.getInt("account_id"));
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
                    acct.setId(rs.getInt("account_id"));
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
                    acc.setId(rs.getInt("account_id"));
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

    private int getMaxAccountNr(Bank bank)
    throws DAOException
    {
        String sql = "SELECT coalesce(max(account_number), 0) FROM bank_account " +
            "WHERE bank_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, bank.getId());
            stm.execute();

            try (ResultSet rs = stm.getResultSet()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        return 0;
    }

    public void createAccount(Account acct, int playerId)
    throws DAOException
    {
        acct.setAccountNumber(getMaxAccountNr(acct.getBank()) + 1);

        String s = "";
        for (int i = 0; i < 4; i++) {
            s += String.valueOf(r.nextInt(10));
        }
        acct.setPin(s);

        String sql = "INSERT INTO bank_account (bank_id, player_id, " +
            "account_balance, account_number, account_pin) VALUES (?,?,?,?, ?)";

        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, acct.getBank().getId());
            stm.setInt(2, playerId);
            stm.setLong(3, 0);
            stm.setInt(4, acct.getAccountNumber());
            stm.setString(5, acct.getPin());
            stm.execute();

            stm.executeQuery("SELECT LAST_INSERT_ID()");

            try (ResultSet rs = stm.getResultSet()) {
                if (!rs.next()) {
                    throw new DAOException("Failed to get insert_id!", sql);
                }

                acct.setId(rs.getInt(1));
            }
        } catch(SQLException e) {
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
    public void deposit(Bank bank, Account acct, int playerId, long amount)
    throws DAOException
    {
        String sqlAcc = "UPDATE bank_account SET " +
                        "account_balance = account_balance + ? " +
                        "WHERE bank_id = ? AND player_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sqlAcc)) {
            stm.setLong(1, amount);
            stm.setInt(2, bank.getId());
            stm.setInt(3, acct.getPlayerId());
            stm.execute();
        } catch(SQLException e) {
            throw new DAOException(sqlAcc, e);
        }

        String sqlTransaction = "INSERT INTO bank_transaction(account_id, " +
                                "player_id, transaction_type, " +
                                "transaction_amount, transaction_timestamp) ";
        sqlTransaction += "VALUES (?, ?, ?, ?, unix_timestamp())";
        try (PreparedStatement stm = conn.prepareStatement(sqlTransaction)) {
            stm.setInt(1, acct.getId());
            stm.setInt(2, playerId);
            stm.setString(3, "deposit");
            stm.setLong(4, amount);
            stm.execute();
        } catch(SQLException e) {
            throw new DAOException(sqlTransaction, e);
        }
    }

    @Override
    public boolean withdraw(Bank bank, Account acct, int playerId, long amount)
    throws DAOException
    {
        if (acct.getBalance() - amount < 0) {
            return false;
        }

        String sqlAcc = "UPDATE bank_account SET " +
                        "account_balance = account_balance - ? " +
                        "WHERE bank_id = ? AND player_id = ?";
        try (PreparedStatement stm = conn.prepareStatement(sqlAcc)) {
            stm.setLong(1, amount);
            stm.setInt(2, bank.getId());
            stm.setInt(3, acct.getPlayerId());
            stm.execute();
        } catch (SQLException e) {
            throw new DAOException(sqlAcc, e);
        }

        String sqlTransaction = "INSERT INTO bank_transaction(account_id, " +
                                "player_id, transaction_type, " +
                                "transaction_amount, transaction_timestamp) ";
        sqlTransaction += "VALUES (?, ?, ?, ?, unix_timestamp())";
        try (PreparedStatement stm = conn.prepareStatement(sqlTransaction)) {
            stm.setInt(1, acct.getId());
            stm.setInt(2, playerId);
            stm.setString(3, "withdrawal");
            stm.setLong(4, amount);
            stm.execute();
        } catch(SQLException e) {
            throw new DAOException(sqlTransaction, e);
        }

        return true;
    }

    @Override
    public void addBanker(Banker banker)
    throws DAOException
    {
        String sql = "INSERT INTO bank_bankers (bank_id, banker_id, banker_name, banker_x, banker_y, banker_z, banker_world) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Villager villager = banker.getVillager();
        Bank bank = banker.getBank();

        UUID uuid = villager.getUniqueId();
        String name = villager.getCustomName();

        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, bank.getId());
            stm.setString(2, uuid.toString());
            stm.setString(3, ChatColor.stripColor(name));
            stm.setInt(4, villager.getLocation().getBlockX());
            stm.setInt(5, villager.getLocation().getBlockY());
            stm.setInt(6, villager.getLocation().getBlockZ());
            stm.setString(7, villager.getLocation().getWorld().getName());
            stm.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void deleteBanker(UUID uuid)
            throws DAOException
    {
        String sql = "DELETE FROM bank_bankers WHERE banker_id = ?";

        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, uuid.toString());
            stm.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public int loadBankers(Server server, Tregmine plugin) throws DAOException
    {
            String sql = "SELECT * FROM bank_bankers";

            int counter = 0;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.execute();

                try (ResultSet rs = stmt.getResultSet()) {
                    while (rs.next()) {
                        UUID id = UUID.fromString(rs.getString("banker_id"));

                        String worldName = rs.getString("banker_world");
                        int blockX = rs.getInt("banker_x");
                        int blockY = rs.getInt("banker_y");
                        int blockZ = rs.getInt("banker_z");

                        World world = server.getWorld(worldName);
                        Location bankerLoc = new Location(world, blockX, blockY, blockZ);

                        ZoneWorld zWorld = plugin.getWorld(bankerLoc.getWorld());
                        Zone zone = zWorld.findZone(bankerLoc);

                        // TODO: Change this to pull zoneID from DB (and store zoneID)
                        if (zone == null) {
                            // Should never occur
                            plugin.getLogger().info("BANKER ISSUE: Banker could not load, Zone was not found at location:" + bankerLoc.toString());
                            continue;
                        }

                        Bank bank = this.getBank(zone.getId());
                        boolean found = false;

                        for (Villager villager : world.getEntitiesByClass(Villager.class)) {
                            if (villager.getUniqueId().equals(id)) {
                                Banker b = new Banker(plugin, bankerLoc, bank, villager);
                                b.setLocation(bankerLoc);
                                found = true;
                            }
                        }

                        if (!found) {
                            // TODO: Update database "banker_id" to use the newly spawned bankers UUID
                            Banker b = new Banker(plugin, bankerLoc, bank, rs.getString("banker_name"));
                            b.setLocation(bankerLoc);
                        }

                        counter++;
                    }
                }
            } catch (SQLException e) {
                throw new DAOException(sql, e);
            }

            return counter;
    }

    @Override
    public boolean isBanker(Bank bank, UUID uniqueId)
    throws DAOException
    {
        String sql = "SELECT * FROM bank_bankers WHERE bank_id = ? AND banker_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bank.getId());
            stmt.setString(2, uniqueId.toString());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

}
