package info.tregmine.listeners;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import info.tregmine.Tregmine;

public class MiscListener implements Listener
{
    private Tregmine plugin;

    public MiscListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void autoSave(WorldSaveEvent event) 
    {
        if ("world".equals(event.getWorld().getName())) {
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Tregmine is saving, You may experience some slowness.");
        }
    }

    /*@EventHandler
    public void onUnloadChunk(ChunkUnloadEvent event) 
    {
        Chunk chunk = event.getChunk();
        Tregmine.LOGGER.info(String.format("Unloading chunk %d, %d", chunk.getX(), chunk.getZ()));
    }*/
}
