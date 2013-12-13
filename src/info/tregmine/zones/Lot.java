package info.tregmine.zones;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import info.tregmine.quadtree.Rectangle;

import info.tregmine.api.TregminePlayer;

public class Lot
{
    // Flags are stored as integers - order must _NOT_ be changed
    public enum Flags {
        AUTOBLESS, // default true == 1
        FLIGHT_ALLOWED, // default true == 2
        FISHY_SHARE, // default false == 4
        PRIVATE, // default false == 8
        FREE_BUILD, // default false == 16
        PVP, // default false == 32
        BANK;
    }

    private int id;
    private int zoneId;
    private String name;
    private Rectangle rect;
    private Set<Integer> owners;
    private Set<Flags> flags;

    public Lot()
    {
        this.owners = new HashSet<Integer>();

        this.flags = EnumSet.noneOf(Flags.class);
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

    public Set<Integer> getOwners()
    {
        return owners;
    }

    public void setOwner(List<Integer> owners)
    {
        this.owners.addAll(owners);
    }

    public boolean isOwner(TregminePlayer player)
    {
        return owners.contains(player.getId());
    }

    public void addOwner(TregminePlayer player)
    {
        owners.add(player.getId());
    }

    public void deleteOwner(TregminePlayer player)
    {
        owners.remove(player.getId());
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

    public void setFlag(Flags flag) { flags.add(flag); }
    public void removeFlag(Flags flag) { flags.remove(flag); }
    public boolean hasFlag(Flags flag) { return flags.contains(flag); }
}
