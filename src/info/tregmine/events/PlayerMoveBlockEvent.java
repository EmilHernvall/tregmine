package info.tregmine.events;

import info.tregmine.api.TregminePlayer;

import org.bukkit.Location;
import org.bukkit.event.*;

public final class PlayerMoveBlockEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Location from;
    private Location to;
    private TregminePlayer player;
    
    public PlayerMoveBlockEvent(Location fromLoc, Location toLoc, TregminePlayer playerInvolved)
    {
        this.from = fromLoc;
        this.to = toLoc;
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
    
    public boolean isCancelled()
    {
        return cancelled;
    }
 
    public void setCancelled(boolean cancel)
    {
        cancelled = cancel;
    }
}
