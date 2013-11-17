package info.tregmine;

import info.tregmine.api.*;
import info.tregmine.commands.*;
import info.tregmine.database.*;
import info.tregmine.database.IInventoryDAO.InventoryType;
import info.tregmine.database.db.DBContextFactory;
import info.tregmine.listeners.*;
import info.tregmine.quadtree.IntersectionException;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.maxmind.geoip.LookupService;

/**
 * @author Ein Andersson
 * @author Emil Hernvall
 */
public class Tregmine extends JavaPlugin
{
    public final static int VERSION = 0;
    public final static int AMOUNT = 0;

    public final static Logger LOGGER = Logger.getLogger("Minecraft");

    private IContextFactory contextFactory;

    private Server server;
    private WebServer webServer;

    private Map<String, TregminePlayer> players;
    private Map<Integer, TregminePlayer> playersById;
    private Map<Location, Integer> blessedBlocks;
    private Map<Location, FishyBlock> fishyBlocks;

    private Map<String, ZoneWorld> worlds;
    private Map<Integer, Zone> zones;

    private Queue<TregminePlayer> mentors;
    private Queue<TregminePlayer> students;

    private LookupService cl = null;

    @Override
    public void onLoad()
    {
        File folder = getDataFolder();
        Tregmine.LOGGER.info("Data folder is: " + folder);

        reloadConfig();

        FileConfiguration config = getConfig();

        contextFactory = new DBContextFactory(config);

        // Set up all data structures
        players = new HashMap<>();
        playersById = new HashMap<>();

        mentors = new LinkedList<>();
        students = new LinkedList<>();

        worlds = new TreeMap<>(
            new Comparator<String>() {
                @Override
                public int compare(String a, String b)
                {
                    return a.compareToIgnoreCase(b);
                }
            });

        zones = new HashMap<>();

        Player[] players = getServer().getOnlinePlayers();
        for (Player player : players) {
            try {
                TregminePlayer tp =
                    addPlayer(player, player.getAddress().getAddress());
                if (tp.getRank() == Rank.TOURIST) {
                    students.offer(tp);
                }
            } catch (PlayerBannedException e) {
                player.kickPlayer(e.getMessage());
            }
        }

        try {
            cl = new LookupService(new File(folder,"GeoIPCity.dat"),
                                   LookupService.GEOIP_MEMORY_CACHE);
        } catch (IOException e) {
            Tregmine.LOGGER.warning("GeoIPCity.dat was not found! " +
                    "Geo location will not function as expected.");
        }
    }

    @Override
    public void onEnable()
    {
        this.server = getServer();

        // Load blessed blocks
        try (IContext ctx = contextFactory.createContext()) {
            IBlessedBlockDAO blessedBlockDAO = ctx.getBlessedBlockDAO();
            this.blessedBlocks = blessedBlockDAO.load(getServer());

            LOGGER.info("Loaded " + blessedBlocks.size() + " blessed blocks");

            IFishyBlockDAO fishyBlockDAO = ctx.getFishyBlockDAO();
            this.fishyBlocks = fishyBlockDAO.loadFishyBlocks(getServer());

            LOGGER.info("Loaded " + fishyBlocks.size() + " fishy blocks");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        // Set up web server
        webServer = new WebServer(this);
        webServer.start();

        // Register all listeners
        PluginManager pluginMgm = server.getPluginManager();
        pluginMgm.registerEvents(new BlessedBlockListener(this), this);
        pluginMgm.registerEvents(new BoxFillBlockListener(this), this);
        pluginMgm.registerEvents(new ChatListener(this), this);
        pluginMgm.registerEvents(new CompassListener(this), this);
        pluginMgm.registerEvents(new PlayerLookupListener(this), this);
        pluginMgm.registerEvents(new SetupListener(this), this);
        pluginMgm.registerEvents(new SignColorListener(), this);
        pluginMgm.registerEvents(new TabListener(this), this);
        pluginMgm.registerEvents(new TauntListener(this), this);
        pluginMgm.registerEvents(new TregmineBlockListener(this), this);
        pluginMgm.registerEvents(new TregminePlayerListener(this), this);
        pluginMgm.registerEvents(new ZoneBlockListener(this), this);
        pluginMgm.registerEvents(new ZoneEntityListener(this), this);
        pluginMgm.registerEvents(new ZonePlayerListener(this), this);
        pluginMgm.registerEvents(new FishyBlockListener(this), this);
        pluginMgm.registerEvents(new InventoryListener(this), this);
        pluginMgm.registerEvents(new DonationSigns(this), this);
        pluginMgm.registerEvents(new ExpListener(this), this);
        pluginMgm.registerEvents(new ItemFrameListener(this), this);
        pluginMgm.registerEvents(new EggListener(this), this);

        // Declaration of all commands
        getCommand("admins").setExecutor(
            new NotifyCommand(this, "admins") {
                @Override
                public boolean isTarget(TregminePlayer player)
                {
                    return player.getRank() == Rank.JUNIOR_ADMIN ||
                           player.getRank() == Rank.SENIOR_ADMIN;
                }
            });

        getCommand("guardians").setExecutor(
            new NotifyCommand(this, "guardians") {
                @Override
                public boolean isTarget(TregminePlayer player)
                {
                    return player.getRank() == Rank.GUARDIAN;
                }
            });

        getCommand("action").setExecutor(new ActionCommand(this));
        getCommand("alert").setExecutor(new AlertCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("bless").setExecutor(new BlessCommand(this));
        getCommand("blockhere").setExecutor(new BlockHereCommand(this));
        getCommand("brush").setExecutor(new BrushCommand(this));
        getCommand("channel").setExecutor(new ChannelCommand(this));
        getCommand("clean").setExecutor(new CleanInventoryCommand(this));
        getCommand("cname").setExecutor(new ChangeNameCommand(this));
        getCommand("createmob").setExecutor(new CreateMobCommand(this));
        getCommand("createwarp").setExecutor(new CreateWarpCommand(this));
        getCommand("creative").setExecutor(new GameModeCommand(this, "creative", GameMode.CREATIVE));
        getCommand("fill").setExecutor(new FillCommand(this, "fill"));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("force").setExecutor(new ForceCommand(this));
        getCommand("forceblock").setExecutor(new ForceShieldCommand(this));
        getCommand("give").setExecutor(new GiveCommand(this));
        getCommand("head").setExecutor(new HeadCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("ignore").setExecutor(new IgnoreCommand(this));
        getCommand("inv").setExecutor(new InventoryCommand(this));
        getCommand("invlog").setExecutor(new InventoryLogCommand(this));
        getCommand("item").setExecutor(new ItemCommand(this));
        getCommand("keyword").setExecutor(new KeywordCommand(this));
        getCommand("kick").setExecutor(new KickCommand(this));
        getCommand("lot").setExecutor(new LotCommand(this));
        getCommand("lottery").setExecutor(new LotteryCommand(this));
        getCommand("mentor").setExecutor(new MentorCommand(this));
        getCommand("msg").setExecutor(new MsgCommand(this));
        getCommand("newspawn").setExecutor(new NewSpawnCommand(this));
        getCommand("normal").setExecutor(new NormalCommand(this));
        getCommand("nuke").setExecutor(new NukeCommand(this));
        getCommand("password").setExecutor(new PasswordCommand(this));
        getCommand("pos").setExecutor(new PositionCommand(this));
        getCommand("quitmessage").setExecutor(new QuitMessageCommand(this));
        getCommand("regeneratechunk").setExecutor(new RegenerateChunkCommand(this));
        getCommand("remitems").setExecutor(new RemItemsCommand(this));
        getCommand("report").setExecutor(new ReportCommand(this));
        getCommand("say").setExecutor(new SayCommand(this));
        getCommand("seen").setExecutor(new SeenCommand(this));
        getCommand("sell").setExecutor(new SellCommand(this));
        getCommand("sendto").setExecutor(new SendToCommand(this));
        getCommand("setbiome").setExecutor(new SetBiomeCommand(this));
        getCommand("setspawner").setExecutor(new SetSpawnerCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("summon").setExecutor(new SummonCommand(this));
        getCommand("support").setExecutor(new SupportCommand(this));
        getCommand("survival").setExecutor(new GameModeCommand(this, "survival", GameMode.SURVIVAL));
        getCommand("testfill").setExecutor(new FillCommand(this, "testfill"));
        getCommand("time").setExecutor(new TimeCommand(this));
        getCommand("town").setExecutor(new ZoneCommand(this, "town"));
        getCommand("tp").setExecutor(new TeleportCommand(this));
        getCommand("tpshield").setExecutor(new TeleportShieldCommand(this));
        getCommand("tpto").setExecutor(new TeleportToCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("update").setExecutor(new UpdateCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("wallet").setExecutor(new WalletCommand(this));
        getCommand("warn").setExecutor(new WarnCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("weather").setExecutor(new WeatherCommand(this));
        getCommand("who").setExecutor(new WhoCommand(this));
        getCommand("zone").setExecutor(new ZoneCommand(this, "zone"));
    }

    // run when plugin is disabled
    @Override
    public void onDisable()
    {
        server.getScheduler().cancelTasks(this);

        // Add a record of logout to db for all players
        for (TregminePlayer player : getOnlinePlayers()) {
            player.sendMessage(ChatColor.AQUA
                    + "Tregmine successfully unloaded. Version "
                    + getDescription().getVersion());

            removePlayer(player);
        }

        try {
            webServer.stop();
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to start web server!", e);
        }
    }

    public WebServer getWebServer()
    {
        return webServer;
    }

    public IContextFactory getContextFactory()
    {
        return contextFactory;
    }

    public IContext createContext()
    throws DAOException
    {
        return contextFactory.createContext();
    }

    // ============================================================================
    // Data structure accessors
    // ============================================================================

    public Map<Location, Integer> getBlessedBlocks()
    {
        return blessedBlocks;
    }

    public Map<Location, FishyBlock> getFishyBlocks()
    {
        return fishyBlocks;
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
            addPlayer(player.getDelegate(), player.getAddress().getAddress());
        } catch (PlayerBannedException e) {
            player.kickPlayer(e.getMessage());
        }
    }

    public List<TregminePlayer> getOnlinePlayers()
    {
        List<TregminePlayer> players = new ArrayList<>();
        for (Player player : server.getOnlinePlayers()) {
            players.add(getPlayer(player));
        }

        return players;
    }

    public TregminePlayer addPlayer(Player srcPlayer, InetAddress addr)
            throws PlayerBannedException
    {
        if (players.containsKey(srcPlayer.getName())) {
            return players.get(srcPlayer.getName());
        }

        try (IContext ctx = contextFactory.createContext()) {
            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            TregminePlayer player = playerDAO.getPlayer(srcPlayer.getPlayer());

            if (player == null) {
                player = playerDAO.createPlayer(srcPlayer);
            }

            player.removeFlag(TregminePlayer.Flags.SOFTWARNED);
            player.removeFlag(TregminePlayer.Flags.HARDWARNED);

            IPlayerReportDAO reportDAO = ctx.getPlayerReportDAO();
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
                    player.setFlag(TregminePlayer.Flags.SOFTWARNED);
                }
                else if (report.getAction() == PlayerReport.Action.HARDWARN) {
                    player.setFlag(TregminePlayer.Flags.HARDWARNED);
                }
                else if (report.getAction() == PlayerReport.Action.BAN) {
                    throw new PlayerBannedException(report.getMessage());
                }
            }

            if (!"95.141.47.226".equals(addr.getHostAddress())) {
                player.setIp(addr.getHostAddress());
                player.setHost(addr.getCanonicalHostName());

                if (cl != null) {
                    com.maxmind.geoip.Location l1 = cl.getLocation(player.getIp());
                    if (l1 != null) {
                        Tregmine.LOGGER.info(player.getName() + ": " + l1.countryName +
                                ", " + l1.city + ", " + player.getIp() + ", " +
                                player.getHost());
                        player.setCountry(l1.countryName);
                        player.setCity(l1.city);
                    } else {
                        Tregmine.LOGGER.info(player.getName() + ": " +
                                player.getIp() + ", " + player.getHost());
                    }
                } else {
                    Tregmine.LOGGER.info(player.getName() + ": " +
                            player.getIp() + ", " + player.getHost());
                }
            }

            int onlinePlayerCount = 0;
            Player[] onlinePlayers = getServer().getOnlinePlayers();
            if (onlinePlayers != null) {
                onlinePlayerCount = onlinePlayers.length;
            }

            ILogDAO logDAO = ctx.getLogDAO();
            logDAO.insertLogin(player, false, onlinePlayerCount);

            player.setTemporaryChatName(player.getNameColor()
                    + player.getName());

            players.put(player.getName(), player);
            playersById.put(player.getId(), player);

            return player;
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayer(TregminePlayer player)
    {
        try (IContext ctx = contextFactory.createContext()) {
            int onlinePlayerCount = 0;
            Player[] onlinePlayers = getServer().getOnlinePlayers();
            if (onlinePlayers != null) {
                onlinePlayerCount = onlinePlayers.length;
            }

            ILogDAO logDAO = ctx.getLogDAO();
            logDAO.insertLogin(player, true, onlinePlayerCount);

            PlayerInventory inv = (PlayerInventory) player.getInventory();

            // Insert regular inventory
            IInventoryDAO invDAO = ctx.getInventoryDAO();
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

            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            playerDAO.updatePlayTime(player);
        } catch (DAOException e) {
            throw new RuntimeException(e);
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

        try (IContext ctx = contextFactory.createContext()) {
            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            return playerDAO.getPlayer(name);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public TregminePlayer getPlayerOffline(int id)
    {
        if (playersById.containsKey(id)) {
            return playersById.get(id);
        }

        try (IContext ctx = contextFactory.createContext()) {
            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            return playerDAO.getPlayer(id);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TregminePlayer> matchPlayer(String pattern)
    {
        List<Player> matches = server.matchPlayer(pattern);
        if (matches.size() == 0) {
            return new ArrayList<>();
        }

        List<TregminePlayer> decoratedMatches = new ArrayList<>();
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
            try (IContext ctx = contextFactory.createContext()) {
                IZonesDAO dao = ctx.getZonesDAO();

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
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        return zoneWorld;
    }

    public Zone getZone(int zoneId)
    {
        return zones.get(zoneId);
    }
    
    // ============================================================================
    // Auto Save Alert
    // ============================================================================
    
    @EventHandler
    public void autoSave(WorldSaveEvent event){
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "Tregmine is saving, You may experience some slowness.");
    }
}
