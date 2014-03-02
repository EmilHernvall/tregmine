package info.tregmine.events;

import info.tregmine.api.TregminePlayer;
import info.tregmine.zones.Lot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TregminePortalEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private World from;
    private World to;
    private TregminePlayer player;

    public TregminePortalEvent(World from, World to, TregminePlayer playerInvolved)
    {
        this.from = from;
        this.to = to;
        this.player = playerInvolved;
    }
    
    public HandlerList getHandlers()
    {
        return handlers;
    }
    
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
    public World getFrom()
    {
        return from;
    }
    
    public World getTo()
    {
        return to;
    }
    
    public TregminePlayer getPlayer()
    {
        return player;
    }
    
    public void setFrom(World newFrom)
    {
        this.from = newFrom;
    }
    
    public void setTo(World newTo)
    {
        this.to = newTo;
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
