package info.tregmine.api;

import org.bukkit.Location;

public class Warp
{
    private int id;
    private String name;
    private Location location;

    public Warp()
    {
    }

    public int getId() { return id; }
    public void setId(int v) { this.id = v; }

    public String getName() { return name; }
    public void setName(String v) { this.name = v; }

    public Location getLocation() { return location; }
    public void setLocation(Location v) { this.location = v; }
}
