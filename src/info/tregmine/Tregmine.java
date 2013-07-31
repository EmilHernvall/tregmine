package info.tregmine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import org.eclipse.jetty.server.Server;

import info.tregmine.api.PlayerBannedException;
import info.tregmine.api.PlayerReport;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBInventoryDAO;
import info.tregmine.database.DBLogDAO;
import info.tregmine.database.DBPlayerDAO;
import info.tregmine.database.DBPlayerReportDAO;
import info.tregmine.database.DBZonesDAO;
import info.tregmine.quadtree.IntersectionException;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import static info.tregmine.database.DBInventoryDAO.InventoryType;

import info.tregmine.listeners.*;
import info.tregmine.commands.*;
import info.tregmine.web.*;

/**
 * @author Ein Andersson
 * @author Emil Hernvall
 */
public class Tregmine extends JavaPlugin
{
    public final static int VERSION = 0;
    public final static int AMOUNT = 0;

    public final static Logger LOGGER = Logger.getLogger("Minecraft");

    private org.bukkit.Server server;

    private Server webServer;
    private WebHandler webHandler;

    private Map<String, TregminePlayer> players;
    private Map<Integer, TregminePlayer> playersById;
    private Map<Location, Integer> blessedBlocks;

    private Map<String, ZoneWorld> worlds;
    private Map<Integer, Zone> zones;

    private Queue<TregminePlayer> mentors;
    private Queue<TregminePlayer> students;

    @Override
    public void onLoad()
    {
        // Set up all data structures
        players = new HashMap<String, TregminePlayer>();
        playersById = new HashMap<Integer, TregminePlayer>();

        mentors = new LinkedList<TregminePlayer>();
        students = new LinkedList<TregminePlayer>();

        worlds = new TreeMap<String, ZoneWorld>(
            new Comparator<String>() {
                @Override
                public int compare(String a, String b)
                {
                    return a.compareToIgnoreCase(b);
                }
            });

        zones = new HashMap<Integer, Zone>();

        Player[] players = getServer().getOnlinePlayers();
        for (Player player : players) {
            try {
                addPlayer(player);
            } catch (PlayerBannedException e) {
                player.kickPlayer(e.getMessage());
            }
        }
    }

    @Override
    public void onEnable()
    {
        reloadConfig();

        Tregmine.LOGGER.info("Data folder is: " + getDataFolder());

        FileConfiguration config = getConfig();
        String apiKey = config.getString("api.signing-key");

        Tregmine.LOGGER.info("API Key: " + apiKey);

        this.server = getServer();

        // Load blessed blocks
        Connection conn = null;
        PreparedStatement stm = null;
        try {
            conn = ConnectionPool.getConnection();

            DBInventoryDAO inventoryDAO = new DBInventoryDAO(conn);
            this.blessedBlocks = inventoryDAO.loadBlessedBlocks(getServer());

            LOGGER.info("Loaded " + blessedBlocks.size() + " blessed blocks");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (stm != null)
                    stm.close();
            } catch (SQLException e) {
            }
        }

        // Register all listeners
        PluginManager pluginMgm = server.getPluginManager();
        pluginMgm.registerEvents(new BlessedBlockListener(this), this);
        pluginMgm.registerEvents(new BoxFillBlockListener(this), this);
        pluginMgm.registerEvents(new ChatListener(this), this);
        pluginMgm.registerEvents(new CompassListener(this), this);
        pluginMgm.registerEvents(new PlayerLookupListener(this), this);
        pluginMgm.registerEvents(new SetupListener(this), this);
        pluginMgm.registerEvents(new SignColorListener(), this);
        pluginMgm.registerEvents(new TauntListener(this), this);
        pluginMgm.registerEvents(new TregmineBlockListener(this), this);
        pluginMgm.registerEvents(new TregminePlayerListener(this), this);
        pluginMgm.registerEvents(new ZoneBlockListener(this), this);
        pluginMgm.registerEvents(new ZoneEntityListener(this), this);
        pluginMgm.registerEvents(new ZonePlayerListener(this), this);

        // Declaration of all commands
        getCommand("admins").setExecutor(new NotifyCommand(this, "admins") {
            @Override
            public boolean isTarget(TregminePlayer player)
            {
                return player.isAdmin();
            }
        });

        getCommand("guardians").setExecutor(
                new NotifyCommand(this, "guardians") {
                    @Override
                    public boolean isTarget(TregminePlayer player)
                    {
                        return player.isGuardian();
                    }
                });

        getCommand("action").setExecutor(new ActionCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("bless").setExecutor(new BlessCommand(this));
        getCommand("blockhere").setExecutor(new BlockHereCommand(this));
        getCommand("channel").setExecutor(new ChannelCommand(this));
        getCommand("clean").setExecutor(new CleanInventoryCommand(this));
        getCommand("cname").setExecutor(new ChangeNameCommand(this));
        getCommand("createmob").setExecutor(new CreateMobCommand(this));
        getCommand("createwarp").setExecutor(new CreateWarpCommand(this));
        getCommand("creative").setExecutor(
                new GameModeCommand(this, "creative", GameMode.CREATIVE));
        getCommand("fill").setExecutor(new FillCommand(this, "fill"));
        getCommand("force").setExecutor(new ForceCommand(this));
        getCommand("give").setExecutor(new GiveCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("inv").setExecutor(new InventoryCommand(this));
        getCommand("item").setExecutor(new ItemCommand(this));
        getCommand("keyword").setExecutor(new KeywordCommand(this));
        getCommand("kick").setExecutor(new KickCommand(this));
        getCommand("lot").setExecutor(new LotCommand(this));
        getCommand("mentor").setExecutor(new MentorCommand(this));
        getCommand("msg").setExecutor(new MsgCommand(this));
        getCommand("newspawn").setExecutor(new NewSpawnCommand(this));
        getCommand("normal").setExecutor(new NormalCommand(this));
        getCommand("nuke").setExecutor(new NukeCommand(this));
        getCommand("password").setExecutor(new PasswordCommand(this));
        getCommand("pos").setExecutor(new PositionCommand(this));
        getCommand("quitmessage").setExecutor(new QuitMessageCommand(this));
        getCommand("regeneratechunk").setExecutor(
                new RegenerateChunkCommand(this));
        getCommand("report").setExecutor(new ReportCommand(this));
        getCommand("say").setExecutor(new SayCommand(this));
        getCommand("seen").setExecutor(new SeenCommand(this));
        getCommand("sell").setExecutor(new SellCommand(this));
        getCommand("sendto").setExecutor(new SendToCommand(this));
        getCommand("setspawner").setExecutor(new SetSpawnerCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("summon").setExecutor(new SummonCommand(this));
        getCommand("survival").setExecutor(
                new GameModeCommand(this, "survival", GameMode.SURVIVAL));
        getCommand("testfill").setExecutor(new FillCommand(this, "testfill"));
        getCommand("time").setExecutor(new TimeCommand(this));
        getCommand("town").setExecutor(new ZoneCommand(this, "town"));
        getCommand("tp").setExecutor(new TeleportCommand(this));
        getCommand("tpshield").setExecutor(new TeleportShieldCommand(this));
        getCommand("tpto").setExecutor(new TeleportToCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("user").setExecutor(new UserCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("wallet").setExecutor(new WalletCommand(this));
        getCommand("warn").setExecutor(new WarnCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("weather").setExecutor(new WeatherCommand(this));
        getCommand("who").setExecutor(new WhoCommand(this));
        getCommand("zone").setExecutor(new ZoneCommand(this, "zone"));

        try {
            webHandler = new WebHandler(this, apiKey);
            webHandler.addAction(new VersionAction.Factory());
            webHandler.addAction(new PlayerListAction.Factory());
            webHandler.addAction(new PlayerKickAction.Factory());

            webServer = new Server(9192);
            webServer.setHandler(webHandler);
            webServer.start();

            BukkitScheduler scheduler = server.getScheduler();
            scheduler.scheduleSyncRepeatingTask(this, webHandler, 0, 20);
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to start web server!", e);
        }
    }

    // run when plugin is disabled
    @Override
    public void onDisable()
    {
        server.getScheduler().cancelTasks(this);

        // Add a record of logout to db for all players
        for (TregminePlayer player : getOnlinePlayers()) {
            player.sendMessage(ChatColor.AQUA
                    + "Tregmine successfully unloaded. Build "
                    + getDescription().getVersion());

            removePlayer(player);
        }

        try {
            webServer.stop();
            webServer.join();
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to start web server!", e);
        }
    }

    // ============================================================================
    // Data structure accessors
    // ============================================================================

    public Map<Location, Integer> getBlessedBlocks()
    {
        return blessedBlocks;
    }

    public Queue<TregminePlayer> getMentorQueue()
    {
        return mentors;
    }

    public Queue<TregminePlayer> getStudentQueue()
    {
        return students;
    }

    // ============================================================================
    // Player methods
    // ============================================================================

    public void reloadPlayer(TregminePlayer player)
    {
        try {
            addPlayer(player.getDelegate());
        } catch (PlayerBannedException e) {
            player.kickPlayer(e.getMessage());
        }
    }

    public List<TregminePlayer> getOnlinePlayers()
    {
        List<TregminePlayer> players = new ArrayList<TregminePlayer>();
        for (Player player : server.getOnlinePlayers()) {
            players.add(getPlayer(player));
        }

        return players;
    }

    public TregminePlayer addPlayer(Player srcPlayer)
            throws PlayerBannedException
    {
        if (players.containsKey(srcPlayer.getName())) {
            return players.get(srcPlayer.getName());
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            TregminePlayer player = playerDAO.getPlayer(srcPlayer.getPlayer());

            if (player == null) {
                player = playerDAO.createPlayer(srcPlayer);
            }

            if (player.isTrusted()) {
                player.setSetup(true);
            }

            DBPlayerReportDAO reportDAO = new DBPlayerReportDAO(conn);
            List<PlayerReport> reports = reportDAO.getReportsBySubject(player);
            for (PlayerReport report : reports) {
                Date validUntil = report.getValidUntil();
                if (validUntil == null) {
                    continue;
                }
                if (validUntil.getTime() < System.currentTimeMillis()) {
                    continue;
                }

                if (report.getAction() == PlayerReport.Action.SOFTWARN) {
                    player.setTempColor("warned");
                }
                else if (report.getAction() == PlayerReport.Action.HARDWARN) {
                    player.setTempColor("warned");
                    player.setTrusted(false);
                }
                else if (report.getAction() == PlayerReport.Action.BAN) {
                    // event.disallow(Result.KICK_BANNED, report.getMessage());
                    throw new PlayerBannedException(report.getMessage());
                }
            }

            DBLogDAO logDAO = new DBLogDAO(conn);
            logDAO.insertLogin(player, false);

            player.setTemporaryChatName(player.getNameColor()
                    + player.getName());

            players.put(player.getName(), player);
            playersById.put(player.getId(), player);

            return player;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

    }

    public void removePlayer(TregminePlayer player)
    {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBLogDAO logDAO = new DBLogDAO(conn);
            logDAO.insertLogin(player, true);

            PlayerInventory inv = (PlayerInventory) player.getInventory();

            // Insert regular inventory
            DBInventoryDAO invDAO = new DBInventoryDAO(conn);
            int invId = invDAO.getInventoryId(player.getId(), InventoryType.PLAYER);
            if (invId == -1) {
                invId = invDAO.insertInventory(player, null, InventoryType.PLAYER);
            }

            invDAO.insertStacks(invId, inv.getContents());

            // Insert armor inventory
            int armorId = invDAO.getInventoryId(player.getId(),
                                                InventoryType.PLAYER_ARMOR);
            if (armorId == -1) {
                armorId = invDAO.insertInventory(player, null,
                                                 InventoryType.PLAYER_ARMOR);
            }

            invDAO.insertStacks(armorId, inv.getArmorContents());

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            playerDAO.updatePlayTime(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        player.setValid(false);

        players.remove(player.getName());
        playersById.remove(player.getId());
        mentors.remove(player);
        students.remove(player);
    }

    public TregminePlayer getPlayer(String name)
    {
        return players.get(name);
    }

    public TregminePlayer getPlayer(Player player)
    {
        return players.get(player.getName());
    }

    public TregminePlayer getPlayer(int id)
    {
        return playersById.get(id);
    }

    public TregminePlayer getPlayerOffline(String name)
    {
        if (players.containsKey(name)) {
            return players.get(name);
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            return playerDAO.getPlayer(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public TregminePlayer getPlayerOffline(int id)
    {
        if (playersById.containsKey(id)) {
            return playersById.get(id);
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();

            DBPlayerDAO playerDAO = new DBPlayerDAO(conn);
            return playerDAO.getPlayer(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public List<TregminePlayer> matchPlayer(String pattern)
    {
        List<Player> matches = server.matchPlayer(pattern);
        if (matches.size() == 0) {
            return new ArrayList<TregminePlayer>();
        }

        List<TregminePlayer> decoratedMatches = new ArrayList<TregminePlayer>();
        for (Player match : matches) {
            TregminePlayer decoratedMatch = getPlayer(match);
            if (decoratedMatch == null) {
                continue;
            }
            decoratedMatches.add(decoratedMatch);
        }

        return decoratedMatches;
    }

    // ============================================================================
    // Zone methods
    // ============================================================================

    public ZoneWorld getWorld(World world)
    {
        ZoneWorld zoneWorld = worlds.get(world.getName());

        // lazy load zone worlds as required
        if (zoneWorld == null) {
            Connection conn = null;
            try {
                conn = ConnectionPool.getConnection();
                DBZonesDAO dao = new DBZonesDAO(conn);

                zoneWorld = new ZoneWorld(world);
                List<Zone> zones = dao.getZones(world.getName());
                for (Zone zone : zones) {
                    try {
                        zoneWorld.addZone(zone);
                        this.zones.put(zone.getId(), zone);
                    } catch (IntersectionException e) {
                        LOGGER.warning("Failed to load zone " + zone.getName()
                                + " with id " + zone.getId() + ".");
                    }
                }

                List<Lot> lots = dao.getLots(world.getName());
                for (Lot lot : lots) {
                    try {
                        zoneWorld.addLot(lot);
                    } catch (IntersectionException e) {
                        LOGGER.warning("Failed to load lot " + lot.getName()
                                + " with id " + lot.getId() + ".");
                    }
                }

                worlds.put(world.getName(), zoneWorld);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                }
            }
        }

        return zoneWorld;
    }

    public Zone getZone(int zoneId)
    {
        return zones.get(zoneId);
    }
}
