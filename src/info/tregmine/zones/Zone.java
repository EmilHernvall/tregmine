package info.tregmine.zones;

import java.util.*;

import info.tregmine.quadtree.Point;
import info.tregmine.quadtree.Rectangle;

import info.tregmine.api.TregminePlayer;

public class Zone
{
    private String texture;

    public enum lotStatus {
        dropzone, bank;
    }

    public enum Permission {
        // can modify the zone in any way
        Owner("%s is now an owner of %s.", "You are now an owner of %s.",
                "%s is no longer an owner of %s.",
                "You are no longer an owner of %s.",
                "You are an owner in this zone."),
        // can build in the zone
        Maker("%s is now a maker in %s.", "You are now a maker in %s.",
                "%s is no longer a maker in %s.",
                "You are no longer a maker in %s.",
                "You are a maker in this zone."),
        // is allowed in the zone, if this isn't the default
        Allowed("%s is now allowed in %s.", "You are now allowed in %s.",
                "%s is no longer allowed in %s.",
                "You are no longer allowed in %s.",
                "You are allowed in this zone."),
        // banned from the zone
        Banned("%s is now banned from %s.", "You have been banned from %s.",
                "%s is no longer banned in %s.",
                "You are no longer banned in %s.",
                "You are banned from this zone.");

        private String addedConfirmation;
        private String addedNotification;
        private String delConfirmation;
        private String delNotification;
        private String permNotification;

        private Permission(String addedConfirmation, String addedNotification,
                String delConfirmation, String delNotification,
                String permNotification)
        {
            this.addedConfirmation = addedConfirmation;
            this.addedNotification = addedNotification;
            this.delConfirmation = delConfirmation;
            this.delNotification = delNotification;
            this.permNotification = permNotification;
        }

        public String getAddedConfirmation()
        {
            return addedConfirmation;
        }

        public String getAddedNotification()
        {
            return addedNotification;
        }

        public String getDeletedConfirmation()
        {
            return delConfirmation;
        }

        public String getDeletedNotification()
        {
            return delNotification;
        }

        public String getPermissionNotification()
        {
            return permNotification;
        }

        public static Permission fromString(String type)
        {
            if ("owner".equalsIgnoreCase(type)) {
                return Permission.Owner;
            }
            else if ("maker".equalsIgnoreCase(type)) {
                return Permission.Maker;
            }
            else if ("allowed".equalsIgnoreCase(type)) {
                return Permission.Allowed;
            }
            else if ("banned".equalsIgnoreCase(type)) {
                return Permission.Banned;
            }

            return null;
        }
    }

    // Flags are stored as integers - order must _NOT_ be changed
    public enum Flags {
        BLOCK_WARNED, // default false == 1
        ADMIN_ONLY, // default false == 2
        REQUIRE_RESIDENCY; // default false == 4
    }

    private int id;
    private String world;
    private String name;
    private List<Rectangle> rects;
    private Set<Flags> flags;

    private boolean enterDefault = true;
    private boolean placeDefault = true;
    private boolean destroyDefault = true;
    private boolean pvp = false;
    private boolean hostiles = false;
    private boolean communism = false;
    private boolean publicProfile = false;

    private String textEnter;
    private String textExit;

    private String mainOwner;

    private Map<Integer, Permission> users;

    public Zone()
    {
        rects = new ArrayList<Rectangle>();
        users = new HashMap<Integer, Permission>();

        this.flags = EnumSet.noneOf(Flags.class);
    }

    public int getId()
    {
        return id;
    }

    public String getMainOwner()
    {
        return this.mainOwner;
    }

    public void setMainOwner(String _owner)
    {
        this.mainOwner = _owner;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getWorld()
    {
        return world;
    }

    public void setWorld(String world)
    {
        this.world = world;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Rectangle> getRects()
    {
        return rects;
    }

    public void addRect(Rectangle rect)
    {
        rects.add(rect);
    }

    public void setRects(List<Rectangle> rects)
    {
        this.rects = rects;
    }

    public boolean getEnterDefault()
    {
        return enterDefault;
    }

    public void setEnterDefault(boolean enterDefault)
    {
        this.enterDefault = enterDefault;
    }

    public boolean getPlaceDefault()
    {
        return placeDefault;
    }

    public void setPlaceDefault(boolean placeDefault)
    {
        this.placeDefault = placeDefault;
    }

    public boolean getDestroyDefault()
    {
        return destroyDefault;
    }

    public void setDestroyDefault(boolean destroyDefault)
    {
        this.destroyDefault = destroyDefault;
    }

    public boolean isPvp()
    {
        return pvp;
    }

    public void setPvp(boolean pvp)
    {
        this.pvp = pvp;
    }

    public void setTexture(String texture)
    {
        this.texture = texture;
    }

    public String getTexture()
    {
        if (this.texture == null) {
            return "";
        }

        return this.texture;
    }

    public void setHostiles(boolean hostiles)
    {
        this.hostiles = hostiles;
    }

    public boolean hasHostiles()
    {
        return hostiles;
    }

    public void setCommunist(boolean v)
    {
        this.communism = v;
    }

    public boolean isCommunist()
    {
        return communism;
    }

    public void setPublicProfile(boolean v)
    {
        this.publicProfile = v;
    }

    public boolean hasPublicProfile()
    {
        return publicProfile;
    }

    public String getTextEnter()
    {
        return textEnter;
    }

    public void setTextEnter(String textEnter)
    {
        this.textEnter = textEnter;
    }

    public String getTextExit()
    {
        return textExit;
    }

    public void setTextExit(String textExit)
    {
        this.textExit = textExit;
    }

    public void setUsers(Map<Integer, Permission> v)
    {
        this.users = v;
    }

    public void addUser(TregminePlayer player, Permission perm)
    {
        users.put(player.getId(), perm);
    }

    public void deleteUser(TregminePlayer player)
    {
        users.remove(player.getId());
    }

    public Collection<Integer> getUsers()
    {
        return users.keySet();
    }

    public Permission getUser(TregminePlayer player)
    {
        return users.get(player.getId());
    }

    public boolean contains(Point p)
    {
        for (Rectangle rect : rects) {
            if (rect.contains(p)) {
                return true;
            }
        }

        return false;
    }

    public void setFlag(Flags flag) { flags.add(flag); }
    public void removeFlag(Flags flag) { flags.remove(flag); }
    public boolean hasFlag(Flags flag) { return flags.contains(flag); }
}
