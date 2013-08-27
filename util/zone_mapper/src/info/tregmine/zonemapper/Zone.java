package info.tregmine.zonemapper;

public class Zone
{
    int id;
    String name;
    String world;
    Rectangle rect;

    public Zone(int id, String name, String world, Rectangle rect)
    {
        this.id = id;
        this.name = name;
        this.world = world;
        this.rect = rect;
    }
}

