package info.tregmine.api;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.EnumSet;
import java.util.Date;
//import java.util.List;
import java.net.InetSocketAddress;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Entity;

import info.tregmine.api.encryption.BCrypt;
import info.tregmine.zones.Zone;

public class TregminePlayer extends PlayerDelegate
{
    public enum GuardianState {
        ACTIVE, INACTIVE, QUEUED;
    };

    public enum ChatState {
        SETUP, CHAT, TRADE, SELL;
    };

    // Flags are stored as integers - order must _NOT_ be changed
    public enum Flags {
        CHILD,
        TPSHIELD,
        SOFTWARNED,
        HARDWARNED,
        INVISIBLE,
        HIDDEN_LOCATION;
    };

    // Persistent values
    private int id = 0;
    private String name = null;
    private String password = null;
    private String keyword = null;
    private Rank rank = Rank.UNVERIFIED;
    private ChatColor color = ChatColor.WHITE;
    private String quitMessage = null;
    private int guardianRank = 0;
    private int playTime = 0;
    private Set<Flags> flags;

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

    // Player state for block fill
    private Block fillBlock1 = null;
    private Block fillBlock2 = null;
    private int fillBlockCounter = 0;

    // Player state for zone creation
    private Block zoneBlock1 = null;
    private Block zoneBlock2 = null;
    private int zoneBlockCounter = 0;
    private int targetZoneId = 0;

    public TregminePlayer(Player player)
    {
        super(player);

        this.name = player.getName();
        this.loginTime = new Date();

        this.flags = EnumSet.noneOf(Flags.class);
    }

    public TregminePlayer(String name)
    {
        super(null);

        this.name = name;
    }

    public void setId(int v) { this.id = v; }
    public int getId() { return id; }

    public String getChatName()
    {
        return name;
    }

    public void setTemporaryChatName(String name)
    {
        this.name = name;

        if (getChatName().length() > 16) {
            setPlayerListName(name.substring(0, 15));
        }
        else {
            setPlayerListName(name);
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

    public String getKeyword() { return keyword; }
    public void setKeyword(String v) { this.keyword = v; }

    public Rank getRank() { return rank; }
    public void setRank(Rank v)
    {
        this.rank = v;

        if (getDelegate() != null) {
            setTemporaryChatName(getNameColor() + getName());
        }

    }

    public void setGuardianRank(int v) { this.guardianRank = v; }
    public int getGuardianRank() { return guardianRank; }

    public void setPlayTime(int v) { this.playTime = v; }
    public int getPlayTime() { return playTime; }

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

        setTemporaryChatName(getNameColor() + getName());
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

    public void setCurrentTexture(String url)
    {
        if (url == null) {
            this.texture = "https://dl.dropbox.com/u/5405236/mc/df.zip";
        }

        if (!url.equals(this.texture)) {
            this.texture = url;
            setTexturePack(url);
        }
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
        Entity v = getVehicle();
        if (v != null && v instanceof Horse) {
            Horse horse = (Horse)v;
            horse.eject();
            horse.teleport(loc);
            teleport(loc);
            horse.setPassenger(getDelegate());
        }
        else {
            teleport(loc);
        }
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
}
