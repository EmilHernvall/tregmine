package info.tregmine.bank;

import info.tregmine.Tregmine;
import info.tregmine.api.bank.Banker;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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

        for (Banker b : plugin.getBankers().values()) {
            if (b.getVillager().getUniqueId().equals(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
                event.setDamage(0);
            }
        }
    }

}
