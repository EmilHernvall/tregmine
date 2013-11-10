package info.tregmine.api;

import java.util.Date;

public class InventoryAccess
{
    private int inventoryId;
    private int playerId;
    private Date timestamp;

    public InventoryAccess()
    {
    }

    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int v) { this.inventoryId = v; }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int v) { this.playerId = v; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date v) { this.timestamp = v; }
}
