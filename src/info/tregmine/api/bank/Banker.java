package info.tregmine.api.bank;

import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;
import info.tregmine.Tregmine;
import info.tregmine.bank.BankerReturnRunnable;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;
import info.tregmine.database.IContext;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.bukkit.ChatColor.AQUA;

public class Banker {

    // Variables
    private Location loc;
    private ControllableMob<Villager> banker;
    private Villager villagerCore;
    private BukkitTask returnTask;
    private Tregmine plugin;
    private Bank bank;
    private int banker_id;

    public Banker(Tregmine plugin, int banker_id, Location sLoc, Bank bank, Villager.Profession profession, String bankerName)
    {
        LivingEntity ent = (LivingEntity) sLoc.getWorld().spawnEntity(sLoc, EntityType.VILLAGER);
        Villager villager = (Villager) ent;

        villager.setCustomName(AQUA + bankerName);
        villager.setProfession(profession);
        villager.setAgeLock(true);
        villager.setCustomNameVisible(true);
        villager.setMetadata("banker", new FixedMetadataValue(plugin, true));

        ControllableMob<Villager> cVillager = ControllableMobs.putUnderControl(villager, true);
        cVillager.getAttributes().getMaxHealthAttribute().setBasisValue(100.0);

        this.loc = sLoc;
        this.banker = cVillager;
        this.plugin = plugin;
        this.bank = bank;
        this.villagerCore = villager;
        this.banker_id = banker_id;

        plugin.getBankers().put(sLoc, this);

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        this.returnTask = scheduler.runTaskTimerAsynchronously(plugin, new BankerReturnRunnable(this), 0L, 20L);
    }

    public Banker(Tregmine plugin, int banker_id, Location sLoc, Bank bank, Villager villager)
    {
        ControllableMob<Villager> cVillager = ControllableMobs.putUnderControl(villager, true);
        cVillager.getAttributes().getMaxHealthAttribute().setBasisValue(100.0);

        this.loc = sLoc;
        this.banker = cVillager;
        this.plugin = plugin;
        this.bank = bank;
        this.villagerCore = villager;
        this.banker_id = banker_id;

        plugin.getBankers().put(sLoc, this);

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        this.returnTask = scheduler.runTaskTimerAsynchronously(plugin, new BankerReturnRunnable(this), 0L, 20L);
    }

    public Banker(Tregmine plugin, int id, Location sLoc, Bank bank)
    {
        this(plugin, id, sLoc, bank, Villager.Profession.LIBRARIAN, "Banker " + getRandomBanker());
    }

    public Banker(Tregmine plugin, int id, Location sLoc, Bank bank, String name)
    {
        this(plugin, id, sLoc, bank, Villager.Profession.LIBRARIAN, name);
    }

    // Location methods.
    public Location getLocation() { return loc; }
    public boolean setLocation(Location newLoc)
    {
        // Checks new location is not in a different zone.
        ZoneWorld world  = plugin.getWorld(newLoc.getWorld());
        Zone currentZone = plugin.getZone(bank.getZoneId());
        Zone newZone     = world.findZone(newLoc);

        if (currentZone.getId() != newZone.getId()) {
            return false;
        }

        returnTask.cancel();

        this.loc = newLoc;
        banker.getEntity().teleport(newLoc);

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        this.returnTask = scheduler.runTaskTimerAsynchronously(plugin, new BankerReturnRunnable(this), 0L, 20L);
        return true;
    }

    public void saveBanker()
    {
        try (IContext ctx = plugin.createContext()) {
            IBankDAO bankDAO = ctx.getBankDAO();
            bankDAO.updateBanker(this);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public ControllableMob<Villager> getBanker()
    {
        this.banker = ControllableMobs.getOrPutUnderControl(villagerCore, true);
        return banker;
    }

    public Villager getVillager() { return banker.getEntity(); }

    public int getBankerId() { return banker_id; }
    public void setBankerId(int value) { this.banker_id = value; }

    public Bank getBank() { return bank; }
    public Zone getZone() { return plugin.getZone(bank.getZoneId()); }

    public boolean isSame(Villager villager)
    {
        return villager.getUniqueId().equals(banker.getEntity().getUniqueId());
    }

    private static String getRandomBanker()
    {
        List<String> bankers = new ArrayList<>();
        for (Banker_Names banker : Banker_Names.values()) {
            bankers.add(banker.toString());
        }

        return bankers.get(new Random().nextInt(bankers.size()));
    }

}
