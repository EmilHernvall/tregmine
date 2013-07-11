package info.tregmine.zones;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import info.tregmine.quadtree.Rectangle;

public class Lot
{
    private int id;
    private int zoneId;
    private String name;
    private Rectangle rect;
    private Set<String> owners;

    public Lot()
    {
        this.owners = new HashSet<String>();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(int zoneId)
    {
        this.zoneId = zoneId;
    }

    public Set<String> getOwners()
    {
        return owners;
    }

    public void setOwner(List<String> owners)
    {
        this.owners.addAll(owners);
    }

    public boolean isOwner(String player)
    {
        return owners.contains(player);
    }

    public void addOwner(String player)
    {
        owners.add(player);
    }

    public void deleteOwner(String player)
    {
        owners.remove(player);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Rectangle getRect()
    {
        return rect;
    }

    public void setRect(Rectangle rect)
    {
        this.rect = rect;
    }
}
