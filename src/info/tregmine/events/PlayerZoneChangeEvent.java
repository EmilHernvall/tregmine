package info.tregmine.events;

import info.tregmine.api.TregminePlayer;
import info.tregmine.zones.Zone;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerZoneChangeEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Location from;
    private Location to;
    private TregminePlayer player;
    private Zone oldZone;
    private Zone newZone;
    
    public PlayerZoneChangeEvent(Location fromLoc, Location toLoc, TregminePlayer playerInvolved, Zone previousZone, Zone currentZone)
    {
        this.from = fromLoc;
        this.to = toLoc;
        this.player = playerInvolved;
        this.oldZone = previousZone;
        this.newZone = currentZone;
    }
    
    public HandlerList getHandlers()
    {
        return handlers;
    }
    
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
    public Location getFrom()
    {
        return from;
    }
    
    public Location getTo()
    {
        return to;
    }
    
    public TregminePlayer getPlayer()
    {
        return player;
    }
    
    public void setFrom(Location newFrom)
    {
        this.from = newFrom;
    }
    
    public void setTo(Location newTo)
    {
        this.to = newTo;
    }
    
    public Zone getOld()
    {
        return oldZone;
    }
    
    public Zone getNew()
    {
        return newZone;
    }
    
    public void setOld(Zone value)
    {
        this.oldZone = value;
    }
    
    public void setNew(Zone value)
    {
        this.newZone = value;
    }
    
    public boolean isCancelled()
    {
        return cancelled;
    }
 
    public void setCancelled(boolean cancel)
    {
        cancelled = cancel;
    }
}
