package info.tregmine.christmas.API;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HitBlockEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Arrow arrow;
    private Block block;

    public HitBlockEvent(Arrow arrow, Block block) {
        this.arrow = arrow;
        this.block = block;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public Block getBlock() {
        return block;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}