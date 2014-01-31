package info.tregmine.listeners;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.world.WorldSaveEvent;

import info.tregmine.Tregmine;

public class MiscListener implements Listener
{
    private Tregmine plugin;
    
    public MiscListener(Tregmine instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void autoSave(WorldSaveEvent event) {
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "Tregmine is saving, You may experience some slowness.");
    }
}
