package info.tregmine.events;

import org.bukkit.event.*;

import info.tregmine.api.TregminePlayer;

public class TregmineChatEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private TregminePlayer player;
    private String message;
    private String channel;
    private boolean web;

    public TregmineChatEvent(TregminePlayer player, String message, String channel, boolean web)
    {
        this.player = player;
        this.message = message;
        this.channel = channel;
        this.web = web;
    }

    public TregminePlayer getPlayer()
    {
        return player;
    }

    public String getMessage()
    {
        return message;
    }

    public String getChannel()
    {
        return channel;
    }

    public boolean isWebChat()
    {
        return web;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value)
    {
        this.cancelled = value;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
