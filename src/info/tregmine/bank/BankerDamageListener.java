package info.tregmine.bank;

import info.tregmine.Tregmine;
import info.tregmine.api.bank.Bank;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;
import info.tregmine.database.IContext;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BankerDamageListener implements Listener {

    private Tregmine plugin;

    public BankerDamageListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void villagerMurder(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Villager)) {
            return;
        }

        Villager villager = (Villager) event.getEntity();
        ZoneWorld world = plugin.getWorld(villager.getWorld());
        Zone zone = world.findZone(villager.getLocation());

        if(zone == null) {
            return;
        }

        Bank bank;

        if (!(villager.hasMetadata("banker"))) {
            try (IContext ctx = plugin.createContext()) {

                IBankDAO bankDAO = ctx.getBankDAO();
                bank = bankDAO.getBank(zone.getId());

                if (bankDAO.isBanker(bank, villager.getUniqueId())) {
                    villager.setMetadata("banker", new FixedMetadataValue(plugin, true));
                } else {
                    return;
                }
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        if (!(villager.hasMetadata("banker"))) {
            return;
        }

        event.setCancelled(true);
        event.setDamage(0);
    }

}
