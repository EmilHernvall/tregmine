package info.tregmine.api;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

import info.tregmine.Tregmine;
import info.tregmine.api.encryption.BCrypt;
import info.tregmine.api.returns.BooleanStringReturn;
import info.tregmine.database.*;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.*;

public class TregminePlayer extends PlayerDelegate
{
    public enum GuardianState {
        ACTIVE, INACTIVE, QUEUED;
    };

    public enum ChatState {
        SETUP, CHAT, TRADE, SELL, FISHY_SETUP, FISHY_WITHDRAW, FISHY_BUY, BANK;
    };

    // Flags are stored as integers - order must _NOT_ be changed
    public enum Flags {
        CHILD,
        TPSHIELD,
        SOFTWARNED,
        HARDWARNED,
        INVISIBLE,
        HIDDEN_LOCATION,
        FLY_ENABLED,
        FORCESHIELD,
        CHEST_LOG,
        HIDDEN_ANNOUNCEMENT,
        CHANNEL_VIEW,
        WATCHING_CHUNKS;
    };

    // Persistent values
    private int id = 0;
    private UUID storedUuid = null;
    private String name = null;
    private String realName = null;
    private String password = null;
    private String keyword = null;
    private Rank rank = Rank.UNVERIFIED;
    private ChatColor color = ChatColor.WHITE;
    private String quitMessage = null;
    private int guardianRank = 0;
    private int playTime = 0;
    private Set<Flags> flags;
    private Map<Badge, Integer> badges;

    // One-time state
    private String chatChannel = "GLOBAL";
    private String texture = "https://dl.dropbox.com/u/5405236/mc/df.zip";
    private Zone currentZone = null;
    private GuardianState guardianState = GuardianState.QUEUED;
    private int blessTarget = 0;
    private ChatState chatState = ChatState.CHAT;
    private Date loginTime = null;
    private boolean valid = true;
    private String ip;
    private String host;
    private String city;
    private String country;
    private TregminePlayer mentor;
    private TregminePlayer student;
    private String currentInventory;
	private int combatLog;

    // Player state for block fill
    private Block fillBlock1 = null;
    private Block fillBlock2 = null;
    private int fillBlockCounter = 0;

    // Player state for zone creation
    private Block zoneBlock1 = null;
    private Block zoneBlock2 = null;
    private int zoneBlockCounter = 0;
    private int targetZoneId = 0;

    // Fishy Block state
    private FishyBlock newFishyBlock;
    private FishyBlock currentFishyBlock;
    private int fishyBuyCount;

    // Chunk Watcher
    private boolean newChunk = false;

    private Tregmine plugin;

    public TregminePlayer(Player player, Tregmine instance)
    {
        super(player);

        this.name = player.getName();
        this.realName = player.getName();
        this.loginTime = new Date();

        this.flags = EnumSet.noneOf(Flags.class);
        this.badges = new EnumMap<Badge, Integer>(Badge.class);
        this.plugin = instance;
    }

    public TregminePlayer(String name, Tregmine instance)
    {
        super(null);

        this.name = name;
        this.realName = name;
        this.flags = EnumSet.noneOf(Flags.class);
        this.badges = new EnumMap<Badge, Integer>(Badge.class);
        this.plugin = instance;
    }

    public Tregmine getPlugin() { return plugin; }

    public void setId(int v) { this.id = v; }
    public int getId() { return id; }

    public void setStoredUuid(UUID v) { this.storedUuid = v; }
    public UUID getStoredUuid() { return storedUuid; }

    public String getChatName()
    {
        return name;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setTemporaryChatName(String name)
    {
        this.name = name;

        if (getDelegate() != null) {
            if (getChatName().length() > 16) {
                setPlayerListName(name.substring(0, 15));
            }
            else {
                setPlayerListName(name);
            }
        }
    }

    public void setPassword(String newPassword)
    {
        password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    public void setPasswordHash(String v) { password = v; }
    public String getPasswordHash() { return password; }

    public boolean verifyPassword(String attempt)
    {
        return BCrypt.checkpw(attempt, password);
    }

    public void setFlag(Flags flag) { flags.add(flag); }
    public void removeFlag(Flags flag) { flags.remove(flag); }
    public boolean hasFlag(Flags flag) { return flags.contains(flag); }

    public boolean hasBadge(Badge badge) { return badges.containsKey(badge); }

    public void setBadges(Map<Badge, Integer> v) { this.badges = v; }
    public Map<Badge, Integer> getBadges() { return badges; }

    public int getBadgeLevel(Badge badge)
    {
        if (!hasBadge(badge)) {
            return 0;
        } else {
            return badges.get(badge);
        }
    }

    public void awardBadgeLevel(Badge badge, String message)
    {
        int badgeLevel = getBadgeLevel(badge) + 1;
        badges.put(badge, badgeLevel);

        if (badgeLevel == 1) {
            sendMessage(ChatColor.GOLD + "Congratulations! You've been awarded " +
                    "the " + badge.getName() + " badge of honor: " + message);
        } else {
            sendMessage(ChatColor.GOLD + "Congratulations! You've been awarded " +
                    "the level " + ChatColor.GREEN + badgeLevel + " " +
                    ChatColor.GOLD + badge.getName() + "badge of honor: " + message);
        }
    }

    public String getKeyword() { return keyword; }
    public void setKeyword(String v) { this.keyword = v; }

    public Rank getRank() { return rank; }
    public void setRank(Rank v)
    {
        this.rank = v;

        setTemporaryChatName(getNameColor() + getRealName());
    }

    public void setGuardianRank(int v) { this.guardianRank = v; }
    public int getGuardianRank() { return guardianRank; }

    public void setPlayTime(int v) { this.playTime = v; }
    public int getPlayTime() { return playTime; }

    public boolean getNewChunk() { return newChunk; }
    public void setNewChunk(boolean value) { this.newChunk = value; }

    // non-persistent state methods

    public boolean canMentor()
    {
        if (hasFlag(TregminePlayer.Flags.SOFTWARNED) ||
            hasFlag(TregminePlayer.Flags.HARDWARNED)) {

            return false;
        }

        return getRank().canMentor();
    }

    public GuardianState getGuardianState()
    {
        return guardianState;
    }

    public void setGuardianState(GuardianState v)
    {
        this.guardianState = v;

        setTemporaryChatName(getNameColor() + getRealName());
    }

    public void setQuitMessage(String v) { this.quitMessage = v; }
    public String getQuitMessage() { return quitMessage; }

    public ChatColor getNameColor()
    {
        if (hasFlag(Flags.SOFTWARNED)) {
            return ChatColor.GRAY;
        }
        else if (hasFlag(Flags.HARDWARNED)) {
            return ChatColor.GRAY;
        }
        else if (hasFlag(Flags.CHILD)) {
            return ChatColor.AQUA;
        }

        if (rank == null) {
            return ChatColor.WHITE;
        }
        else if (rank == Rank.GUARDIAN) {
            switch (guardianState) {
            case ACTIVE:
                return ChatColor.BLUE;
            case INACTIVE:
            case QUEUED:
                return ChatColor.GOLD;
            default:
            }
        }

        return rank.getColor();
    }

    public void setCurrentZone(Zone zone) { this.currentZone = zone; }
    public Zone getCurrentZone() { return currentZone; }
    public Zone updateCurrentZone()
    {
        Point pos = new Point(this.getLocation().getBlockX(), this.getLocation().getBlockZ());
        Zone localZone = this.getCurrentZone();

        if (localZone == null || !localZone.contains(pos)) {
                ZoneWorld world = plugin.getWorld(this.getLocation().getWorld());
                localZone = world.findZone(pos);
                this.setCurrentZone(localZone);
        }

        return currentZone;
    }

    public void setCurrentTexture(String url)
    {
        /*if (url == null) {
            this.texture = "https://dl.dropbox.com/u/5405236/mc/df.zip";
        }

        if (!url.equals(this.texture)) {
            this.texture = url;
            setTexturePack(url);
        }*/
    }

    public void setChatChannel(String v) { this.chatChannel = v; }
    public String getChatChannel() { return chatChannel; }

    public void setBlessTarget(int v) { this.blessTarget = v; }
    public int getBlessTarget() { return blessTarget; }

    public void setChatState(ChatState v) { this.chatState = v; }
    public ChatState getChatState() { return chatState; }

    public void resetTimeOnline()
    {
        loginTime = new Date();
    }

    public int getTimeOnline()
    {
        return (int)((new Date().getTime() - loginTime.getTime())/1000L);
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean v) { this.valid = v; }

    public void setIp(String v) { this.ip = v; }
    public String getIp() { return ip; }

    public void setHost(String v) { this.host = v; }
    public String getHost() { return host; }

    public void setCity(String v) { this.city = v; }
    public String getCity() { return city; }

    public void setCountry(String v) { this.country = v; }
    public String getCountry() { return country; }

    public void setMentor(TregminePlayer v) { this.mentor = v; }
    public TregminePlayer getMentor() { return mentor; }

    public void setStudent(TregminePlayer v) { this.student = v; }
    public TregminePlayer getStudent() { return student; }

    // block fill state
    public void setFillBlock1(Block v) { this.fillBlock1 = v; }
    public Block getFillBlock1() { return fillBlock1; }

    public void setFillBlock2(Block v) { this.fillBlock2 = v; }
    public Block getFillBlock2() { return fillBlock2; }

    public void setFillBlockCounter(int v) { this.fillBlockCounter = v; }
    public int getFillBlockCounter() { return fillBlockCounter; }

    // zones state
    public void setZoneBlock1(Block v) { this.zoneBlock1 = v; }
    public Block getZoneBlock1() { return zoneBlock1; }

    public void setZoneBlock2(Block v) { this.zoneBlock2 = v; }
    public Block getZoneBlock2() { return zoneBlock2; }

    public void setZoneBlockCounter(int v) { this.zoneBlockCounter = v; }
    public int getZoneBlockCounter() { return zoneBlockCounter; }

    public void setTargetZoneId(int v) { this.targetZoneId = v; }
    public int getTargetZoneId() { return targetZoneId; }

    // Fishy block state
    public void setNewFishyBlock(FishyBlock v) { this.newFishyBlock = v; }
    public FishyBlock getNewFishyBlock() { return newFishyBlock; }

    public void setCurrentFishyBlock(FishyBlock v) { this.currentFishyBlock = v; }
    public FishyBlock getCurrentFishyBlock() { return currentFishyBlock; }

    public void setFishyBuyCount(int v) { this.fishyBuyCount = v; }
    public int getFishyBuyCount() { return fishyBuyCount; }

	public void setCombatLog(int value) { this.combatLog = value; }
	public int getCombatLog() { return combatLog; }
	public boolean isCombatLogged() {
		if (combatLog > 0) {
			return true;
		} else {
			return false;
		}
	}

    // convenience methods
    public void hidePlayer(TregminePlayer player)
    {
        hidePlayer(player.getDelegate());
    }

    public void showPlayer(TregminePlayer player)
    {
        showPlayer(player.getDelegate());
    }

    /**
     * Sends the player a notification along with an associated message.
     * <p>
     *
     *  If the message is <b>null</b> or equal to "", the message won't send,
     *  however the notification will still play.
     *
     *  If the notification is <b>null</b>, and the message is not if will send
     *  the player the message.
     * @param notif - The notification to send to the player
     * @param message - The message to send the player with the notification
     * @throws IllegalArgumentException if both notif and message are null
     */
    public void sendNotification(Notification notif, String message)
    {
        if (notif != null && notif != Notification.NONE) {
            if (!message.equalsIgnoreCase("") && message != null) {
                playSound(getLocation(), notif.getSound(), 2F, 1F);
                sendMessage(message);
            }
        } else {
            if (!message.equalsIgnoreCase("") && message != null) {
                sendMessage(message);
            } else {
                throw new IllegalArgumentException("Parameters can not both be null");
            }
        }
    }

    public void teleportWithHorse(Location loc)
    {
        World cWorld = loc.getWorld();
        String[] worldNamePortions = cWorld.getName().split("_");

        Entity v = getVehicle();
        if (v != null && v instanceof Horse) {
            if (!worldNamePortions[0].equalsIgnoreCase("world")) {
                this.sendMessage(ChatColor.RED + "Can not teleport with horse! Sorry!");
                return;
            }

            Horse horse = (Horse)v;
            horse.eject();
            horse.teleport(loc);
            teleport(loc);
            horse.setPassenger(getDelegate());
        }
        else {
            teleport(loc);
        }

        if (worldNamePortions[0].equalsIgnoreCase("world")) {
            this.loadInventory("survival", true);
        } else {
            this.loadInventory(worldNamePortions[0], true);
        }
    }

    public BooleanStringReturn canBeHere(Location loc)
    {
        ZoneWorld world = plugin.getWorld(loc.getWorld());
        Zone zone = world.findZone(loc);
        Lot lot = world.findLot(loc);

        if (zone == null) { // Wilderness - Can be there
            return new BooleanStringReturn(true, null);
        }

        if (this.getRank().canModifyZones()) { // Admins can be there
            return new BooleanStringReturn(true, null);
        }

        Zone.Permission permission = zone.getUser(this);

        if (zone.getEnterDefault()) {
            // Banned - Can not be there
            if (permission != null && permission == Zone.Permission.Banned) {
                return new BooleanStringReturn(false, ChatColor.RED + "[" +
                        zone.getName() + "] You are banned from " + zone.getName());
            }

            // If zone has BlockWarned and user is warned
            if (zone.hasFlag(Zone.Flags.BLOCK_WARNED) &&
                    (this.hasFlag(TregminePlayer.Flags.HARDWARNED) ||
                    this.hasFlag(TregminePlayer.Flags.SOFTWARNED))) {
                return new BooleanStringReturn(false, ChatColor.RED + "[" +
                        zone.getName() + "] You must not be warned to be here!");
            }

            // If zone has Admin Only and user is not admin
            if (zone.hasFlag(Zone.Flags.ADMIN_ONLY) &&
                    (this.getRank() != Rank.JUNIOR_ADMIN ||
                    this.getRank() != Rank.SENIOR_ADMIN)) {
                return new BooleanStringReturn(false, ChatColor.RED + "[" +
                        zone.getName() + "] You must be an admin to enter " +
                        zone.getName());
            }

            // If zone has Require Residency and user is not resident yet
            if (zone.hasFlag(Zone.Flags.REQUIRE_RESIDENCY) &&
                    (this.getRank() == Rank.UNVERIFIED ||
                    this.getRank() == Rank.TOURIST ||
                    this.getRank() == Rank.SETTLER)) {
                return new BooleanStringReturn(false, ChatColor.RED + "[" +
                        zone.getName() + "] You must be atleast " +
                        Rank.RESIDENT.getColor() + "Resident" + ChatColor.RED +
                        zone.getName());
            }
        } else {
            // If no permission (Allowed, Maker, Owner, Banned) then stop
            if (permission == null) {
                return new BooleanStringReturn(false, ChatColor.RED + "[" +
                        zone.getName() + "] You are not allowed to enter " +
                        zone.getName());
            }

            // If the permission is banned then stop
            if (permission == Zone.Permission.Banned) {
                return new BooleanStringReturn(false, ChatColor.RED + "[" +
                        zone.getName() + "] You are banned from " + zone.getName());
            }
        }

        // If private lot
        if (lot != null && lot.hasFlag(Lot.Flags.PRIVATE)) {
            // If not owner - then stop
            if (!lot.isOwner(this)) {
                return new BooleanStringReturn(false, ChatColor.RED + "[" +
                        zone.getName() + "] This lot is private!");
            }
        }

        return new BooleanStringReturn(true, null);
    }

    /**
     * Returns true or false if the player has permission for that block
     * @param loc - Location of the block in question
     * @param punish - Should it return an error message and set fire ticks
     * @return true or false
     */
    public boolean hasBlockPermission(Location loc, boolean punish)
    {
        ZoneWorld world = plugin.getWorld(loc.getWorld());
        Point point = new Point(loc.getBlockX(), loc.getBlockZ());

        Zone zone = world.findZone(point);
        Lot lot = world.findLot(point);

        Zone currentZone = this.getCurrentZone();
        if (currentZone == null || !currentZone.contains(point)) {
            currentZone = world.findZone(point);
            this.setCurrentZone(currentZone);
        }

        if (this.hasFlag(TregminePlayer.Flags.HARDWARNED)) {
            if (punish == true) {
                this.setFireTicks(100);
                this.sendMessage(ChatColor.RED + "["
                        + zone.getName() + "] "
                        + "You are hardwarned!");
            }
            return false;
        }

        if (this.getRank() == Rank.TOURIST) {
            return false;
            // Don't punish as that's just cruel ;p
        }

        if (zone == null) { // Is in the wilderness - So return true
            return true;
        }

        if (this.getRank().canModifyZones()) { // Lets people with canModifyZones have block permission
            return true;
        }

        Zone.Permission perm = zone.getUser(this);

        if (perm == Zone.Permission.Banned) { // If banned then return false
            if (punish == true) {
                this.setFireTicks(100);
                this.sendMessage(ChatColor.RED + "["
                        + zone.getName() + "] "
                        + "You are banned from this zone!");
            }
            return false;
        }

        if (lot == null &&
                (perm == Zone.Permission.Maker ||
                 perm == Zone.Permission.Owner)) { // If allowed/maker/owner and not in a lot : return true
            return true;
        }

        if (lot == null &&
                zone.getPlaceDefault()) { // If placeDefault and not in a lot : return true
            return true;
        }

        if (lot != null &&
                perm == Zone.Permission.Owner &&
                zone.isCommunist()) { // If communist zone return true
            return true;
        }

        if (lot != null &&
                lot.isOwner(this)) { // If is lot owner
            return true;
        }

        if (lot != null &&
                lot.hasFlag(Lot.Flags.FREE_BUILD)) {
            return true;
        }

        if (punish == true) {
            if (lot != null && zone != null) { // Lot Error Message

                this.setFireTicks(100);
                this.sendMessage(ChatColor.RED + "["
                        + currentZone.getName() + "] "
                        + "You do not have sufficient permissions in "
                        + lot.getName() + ".");

            } else { // Zone Error Message

                this.setFireTicks(100);
                this.sendMessage(ChatColor.RED + "["
                        + currentZone.getName() + "] "
                        + "You do not have sufficient permissions in "
                        + zone.getName() + ".");

            }
        }

        return false; // If they don't fit into any of that. Return false
    }

    // java.lang.Object overrides
    @Override
    public boolean equals(Object obj)
    {
        return ((TregminePlayer) obj).getId() == getId();
    }

    @Override
    public int hashCode()
    {
        return getId();
    }

    //-----------------------------//
    // Tregmine Inventory Handling //
    //-----------------------------//

    public String getCurrentInventory() { return currentInventory; }
    public void setCurrentInventory(String inv) { this.currentInventory = inv; }

    /*
     * Load an already existing inventory
     * @param name - Name of the inventory
     * @param save - Same current inventory
     */
    public void loadInventory(String name, boolean save)
    {
        try (IContext ctx = plugin.createContext()) {
            IInventoryDAO dao = ctx.getInventoryDAO();

            if (save) {
                this.saveInventory(currentInventory);
            }

            boolean firstTime = false;

            int id3;
            id3 = dao.fetchInventory(this, name, "main");
            while (id3 == -1) {
                dao.createInventory(this, name, "main");
                plugin.getLogger().info("INVENTORY: Creating");
                id3 = dao.fetchInventory(this, name, "main");
                firstTime = true;
            }

            int id4;
            id4 = dao.fetchInventory(this, name, "armour");
            while (id4 == -1) {
                dao.createInventory(this, name, "armour");
                plugin.getLogger().info("INVENTORY: Creating");
                id4 = dao.fetchInventory(this, name, "armour");
                firstTime = true;
            }

            int id5;
            id5 = dao.fetchInventory(this, name, "ender");
            while (id5 == -1) {
                dao.createInventory(this, name, "ender");
                plugin.getLogger().info("INVENTORY: Creating");
                id5 = dao.fetchInventory(this, name, "ender");
                firstTime = true;
            }

            if (firstTime && this.getWorld() != plugin.getRulelessWorld()) {
                this.saveInventory(name);
            }

            this.getInventory().clear();
            this.getInventory().setHelmet(null);
            this.getInventory().setChestplate(null);
            this.getInventory().setLeggings(null);
            this.getInventory().setBoots(null);
            this.getEnderChest().clear();

            dao.loadInventory(this, id3, "main");
            dao.loadInventory(this, id4, "armour");
            dao.loadInventory(this, id5, "ender");

            this.currentInventory = name;

            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            playerDAO.updatePlayer(this);

        } catch (DAOException e) {
            plugin.getLogger().info("INVENTORY ERROR: Trying to load " + this.getName() + " inventory named: " + name);
            throw new RuntimeException(e);
        }
    }

    /*
     * Save the inventory specified, if null - saves current inventory.
     * @param name - Name of the new inventory
     */
    public void saveInventory(String name)
    {
        String inventory = name;
        if (name == null) {
            inventory = this.currentInventory;
        }

        try (IContext ctx = plugin.createContext()) {
            IInventoryDAO dao = ctx.getInventoryDAO();

            int id;
            id = dao.fetchInventory(this, inventory, "main");
            while (id == -1) {
                dao.createInventory(this, inventory, "main");
                plugin.getLogger().info("INVENTORY: Creating");
                id = dao.fetchInventory(this, inventory, "main");
            }

            dao.saveInventory(this, id, "main");

            int id2;
            id2 = dao.fetchInventory(this, inventory, "armour");
            while (id2 == -1) {
                dao.createInventory(this, inventory, "armour");
                plugin.getLogger().info("INVENTORY: Creating");
                id2 = dao.fetchInventory(this, inventory, "armour");
            }

            dao.saveInventory(this, id2, "armour");

            int id3;
            id3 = dao.fetchInventory(this, inventory, "ender");
            while (id3 == -1) {
                dao.createInventory(this, inventory, "ender");
                plugin.getLogger().info("INVENTORY: Creating");
                id3 = dao.fetchInventory(this, inventory, "ender");
            }

            dao.saveInventory(this, id3, "ender");
        } catch (DAOException e) {
            plugin.getLogger().info("INVENTORY ERROR: Trying to save " + this.getName() + " inventory named: " + name);
            throw new RuntimeException(e);
        }
    }

}
