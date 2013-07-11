package info.tregmine.listeners;

import info.tregmine.Tregmine;

//import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityExplodeEvent;

public class TregmineEntityListener implements Listener
{
    //private Tregmine plugin;

    public TregmineEntityListener(Tregmine instance)
    {
        //this.plugin = instance;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if (event.getSpawnReason() == SpawnReason.SPAWNER_EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event)
    {
        event.setCancelled(true);
    }
}
