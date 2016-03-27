package info.tregmine;

import java.io.*;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.*;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.maxmind.geoip.LookupService;

import info.tregmine.api.*;
import info.tregmine.commands.*;
import info.tregmine.database.*;
import info.tregmine.database.db.DBContextFactory;
import info.tregmine.events.CallEventListener;
import info.tregmine.listeners.*;
import info.tregmine.quadtree.IntersectionException;
import info.tregmine.tools.*;
import info.tregmine.zones.*;
import org.bukkit.scheduler.BukkitScheduler;

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

    private List<String> insults;
    private List<String> quitMessages;

    private Queue<TregminePlayer> mentors;
    private Queue<TregminePlayer> students;

    private World rulelessWorld;
    private World rulelessWorldNether;
    private World rulelessWorldEnd;

    private LookupService cl = null;
    public Tregmine plugin;

    @Override
    public void onLoad()
    {
        File folder = getDataFolder();
        Tregmine.LOGGER.info("Data folder is: " + folder);

        reloadConfig();

        FileConfiguration config = getConfig();

        contextFactory = new DBContextFactory(config, this);

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

        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        for (Player player01 : players) {
            try {
                TregminePlayer tp =
                    addPlayer(player01, player01.getAddress().getAddress());
                if (tp.getRank() == Rank.TOURIST) {
                    students.offer(tp);
                }
            } catch (PlayerBannedException e) {
                player01.kickPlayer(e.getMessage());
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
        plugin = this;
        FileConfiguration config = getConfig();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this,  new Lag(), 100L, 1L);
        List<?> configWorlds = getConfig().getList("worlds.names");
        if(getConfig().getString("worlds.enabled") == "true"){
        String[] worlds = configWorlds.toArray(new String[configWorlds.size()]);
        for(String worldName : worlds){
        	if(worldName.contains("the_end") || worldName.contains("nether")){
        		//Do nothing
        	}else{
        	WorldCreator addWorld = new WorldCreator(worldName);
        	addWorld.environment(World.Environment.NORMAL);
        	addWorld.generateStructures(false);
        	addWorld.type(WorldType.FLAT);
        	addWorld.createWorld();
        	}
        }
        
        for(String worldName : worlds){
        	if(!worldName.contains("the_end")){
        		//Do nothing
        	}else{
        	WorldCreator addWorld = new WorldCreator(worldName);
        	addWorld.environment(World.Environment.THE_END);
        	addWorld.generateStructures(false);
        	addWorld.type(WorldType.FLAT);
        	addWorld.createWorld();
        	}
        }
        for(String worldName : worlds){
        	if(!worldName.contains("nether")){
        		//Do nothing
        	}else{
        	WorldCreator addWorld = new WorldCreator(worldName);
        	addWorld.environment(World.Environment.NETHER);
        	addWorld.generateStructures(false);
        	addWorld.type(WorldType.FLAT);
        	addWorld.createWorld();
        	}
        }
        LOGGER.info("" + configWorlds.size() + " extra worlds attempted to load.");
        }else{
        	LOGGER.info("Loaded 0 extra world(s).");
        }

        try (IContext ctx = contextFactory.createContext()) {
            IBlessedBlockDAO blessedBlockDAO = ctx.getBlessedBlockDAO();
            this.blessedBlocks = blessedBlockDAO.load(getServer());

            LOGGER.info("Loaded " + blessedBlocks.size() + " blessed blocks");

            IFishyBlockDAO fishyBlockDAO = ctx.getFishyBlockDAO();
            this.fishyBlocks = fishyBlockDAO.loadFishyBlocks(getServer());

            LOGGER.info("Loaded " + fishyBlocks.size() + " fishy blocks");

            IMiscDAO miscDAO = ctx.getMiscDAO();
            this.insults = miscDAO.loadInsults();
            this.quitMessages = miscDAO.loadQuitMessages();
            if(insults.size() == 0){insults.add(0, "NO DEATH MESSAGES IN DATABASE. SEE TREGMINE WIKI FOR INFO");}
            if(quitMessages.size() == 0){quitMessages.add(0, "NO QUIT MESSAGES IN DATABASE. SEE TREGMINE WIKI FOR INFO.");}
            LOGGER.info("Loaded " + insults.size() + " insults and " + quitMessages.size() + " quit messages");
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
        pluginMgm.registerEvents(new PistonListener(this), this);
        pluginMgm.registerEvents(new ToolCraft(this), this);
        pluginMgm.registerEvents(new LumberListener(this), this);
        pluginMgm.registerEvents(new VeinListener(this), this);
        pluginMgm.registerEvents(new CallEventListener(this), this);
        pluginMgm.registerEvents(new WorldPortalListener(this), this);
        pluginMgm.registerEvents(new PortalListener(this), this);
        pluginMgm.registerEvents(new BankListener(this), this);
        pluginMgm.registerEvents(new RareDropListener(this), this);
        pluginMgm.registerEvents(new DamageListener(this), this);
        pluginMgm.registerEvents(new ChunkListener(this), this);
        pluginMgm.registerEvents(new MiscListener(this), this);

        // Declaration of all commands
        getCommand("admins").setExecutor(
            new NotifyCommand(this, "admins") {
                @Override
                public boolean isTarget(TregminePlayer player)
                {
                    return player.getRank() == Rank.JUNIOR_ADMIN ||
                           player.getRank() == Rank.SENIOR_ADMIN;
                }
                @Override
                public ChatColor getColor()
                {
                    return Rank.JUNIOR_ADMIN.getColor();
                }
            });

        getCommand("guardians").setExecutor(
            new NotifyCommand(this, "guardians") {
                @Override
                public boolean isTarget(TregminePlayer player)
                {
                    return player.getRank() == Rank.GUARDIAN ||
                           player.getRank() == Rank.JUNIOR_ADMIN ||
                           player.getRank() == Rank.SENIOR_ADMIN;
                }
                @Override
                public ChatColor getColor()
                {
                    return Rank.GUARDIAN.getColor();
                }
            });

        getCommand("coders").setExecutor(
            new NotifyCommand(this, "coders") {
                @Override
                public boolean isTarget(TregminePlayer player)
                {
                    return player.getRank() == Rank.CODER ||
                           player.getRank() == Rank.JUNIOR_ADMIN ||
                           player.getRank() == Rank.SENIOR_ADMIN;
                }
                @Override
                public ChatColor getColor()
                {
                    return Rank.CODER.getColor();
                }
            });

        getCommand("action").setExecutor(new ActionCommand(this));
        getCommand("afk").setExecutor(new AfkCommand(this));
        getCommand("alert").setExecutor(new AlertCommand(this));
        getCommand("allclear").setExecutor(new CheckBlocksCommand(this));
        getCommand("badge").setExecutor(new BadgeCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("bless").setExecutor(new BlessCommand(this));
        getCommand("blockhere").setExecutor(new BlockHereCommand(this));
        getCommand("brush").setExecutor(new BrushCommand(this));
        getCommand("channel").setExecutor(new ChannelCommand(this));
        getCommand("channelview").setExecutor(new ChannelViewCommand(this));
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
        getCommand("hide").setExecutor(new HideCommand(this));
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
        getCommand("promote").setExecutor(new PromoteCommand(this));
        getCommand("quitmessage").setExecutor(new QuitMessageCommand(this));
        getCommand("regeneratechunk").setExecutor(new RegenerateChunkCommand(this));
        getCommand("remitems").setExecutor(new RemItemsCommand(this));
        getCommand("repair").setExecutor(new ToolRepairCommand(this));
        getCommand("reply").setExecutor(new ReplyCommand(this));
        getCommand("report").setExecutor(new ReportCommand(this));
        getCommand("rname").setExecutor(new ResetNameCommand(this));
        getCommand("say").setExecutor(new SayCommand(this));
        getCommand("seen").setExecutor(new SeenCommand(this));
        getCommand("sell").setExecutor(new SellCommand(this));
        getCommand("sendto").setExecutor(new SendToCommand(this));
        getCommand("setbiome").setExecutor(new SetBiomeCommand(this));
        getCommand("setspawner").setExecutor(new SetSpawnerCommand(this));
        getCommand("skipmentor").setExecutor(new SkipMentorCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("summon").setExecutor(new SummonCommand(this));
        getCommand("support").setExecutor(new SupportCommand(this));
        getCommand("survival").setExecutor(new GameModeCommand(this, "survival", GameMode.SURVIVAL));
        getCommand("staffnews").setExecutor(new StaffNewsCommand(this));
        getCommand("testfill").setExecutor(new FillCommand(this, "testfill"));
        getCommand("time").setExecutor(new TimeCommand(this));
        getCommand("tool").setExecutor(new ToolSpawnCommand(this));
        getCommand("town").setExecutor(new ZoneCommand(this, "town"));
        getCommand("tp").setExecutor(new TeleportCommand(this));
        getCommand("tps").setExecutor(new TpsCommand(this));
        getCommand("tpshield").setExecutor(new TeleportShieldCommand(this));
        getCommand("tpto").setExecutor(new TeleportToCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("update").setExecutor(new UpdateCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("wallet").setExecutor(new WalletCommand(this));
        getCommand("watchchunks").setExecutor(new WatchChunksCommand(this));
        getCommand("warn").setExecutor(new WarnCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("weather").setExecutor(new WeatherCommand(this));
        getCommand("webkick").setExecutor(new WebKickCommand(this));
        getCommand("who").setExecutor(new WhoCommand(this));
        getCommand("zone").setExecutor(new ZoneCommand(this, "zone"));
        getCommand("chunkcount").setExecutor(new ChunkCountCommand(this));

        ToolCraftRegistry.RegisterRecipes(getServer()); // Registers all tool recipes

        for (TregminePlayer player : getOnlinePlayers()) {
            player.sendMessage(ChatColor.AQUA + "Tregmine successfully loaded. Version " + getDescription().getVersion());
        }

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this,
            new Runnable() {
                public void run() {
                    for (TregminePlayer player : getOnlinePlayers()) {
                        if (player.getCombatLog() > 0) {
                            player.setCombatLog(player.getCombatLog() - 1);

                            if (player.getCombatLog() == 0) {
                                player.sendMessage(ChatColor.GREEN +
                                    "Combat log has warn off... Safe to log off!");
                            }
                        }
                    }
                }
            }, 20L, 20L);
    }


    // run when plugin is disabled
    @Override
    public void onDisable()
    {
        server.getScheduler().cancelTasks(this);

        // Add a record of logout to db for all players
        for (TregminePlayer player : getOnlinePlayers()) {
            player.saveInventory(player.getCurrentInventory());
            removePlayer(player);
        }

        try {
            webServer.stop();
        }
        catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to start web server!", e);
        }
    }
    
    public String parseColors(String rawText){
    	//Deprecated because I realized ChatColor.translateAlternative exists...
    	String text = rawText;
    		if (text.contains("#r") || text.contains("#R")) {
        		text = text.replaceAll("#R", ChatColor.RESET + "");
                text = text.replaceAll("#r", ChatColor.RESET + "");
            }
        	if (text.contains("#0")) {

                text = text.replaceAll("#0", ChatColor.BLACK + "");
            }
            if (text.contains("#1")) {

                text = text.replaceAll("#1", ChatColor.DARK_BLUE + "");
            }
            if (text.contains("#2")) {

                text = text.replaceAll("#2", ChatColor.DARK_GREEN + "");
            }
            if (text.contains("#3")) {

                text = text.replaceAll("#3", ChatColor.DARK_AQUA + "");
            }
            if (text.contains("#4")) {

                text = text.replaceAll("#4", ChatColor.DARK_RED + "");
            }
            if (text.contains("#5")) {

                text = text.replaceAll("#5", ChatColor.DARK_PURPLE + "");
            }
            if (text.contains("#6")) {

                text = text.replaceAll("#6", ChatColor.GOLD + "");
            }
            if (text.contains("#7")) {

                text = text.replaceAll("#7", ChatColor.GRAY + "");
            }
            if (text.contains("#8")) {

                text = text.replaceAll("#8", ChatColor.DARK_GRAY + "");
            }
            if (text.contains("#9")) {

                text = text.replaceAll("#9", ChatColor.BLUE + "");
            }
            if (text.contains("#a") || text.contains("#A")) {
            	text = text.replaceAll("#A", ChatColor.GREEN + "");
                text = text.replaceAll("#a", ChatColor.GREEN + "");
            }
            if (text.contains("#b") || text.contains("#B")) {
                text = text.replaceAll("#B", ChatColor.AQUA + "");
                text = text.replaceAll("#b", ChatColor.AQUA + "");
            }
            if (text.contains("#c") || text.contains("#C")) {
                text = text.replaceAll("#C", ChatColor.RED + "");
                text = text.replaceAll("#c", ChatColor.RED + "");
            }
            if (text.contains("#d") || text.contains("#D")) {
                text = text.replaceAll("#D", ChatColor.LIGHT_PURPLE + "");
                text = text.replaceAll("#d", ChatColor.LIGHT_PURPLE + "");
            }
            if (text.contains("#e") || text.contains("#E")) {
                text = text.replaceAll("#E", ChatColor.YELLOW + "");
                text = text.replaceAll("#e", ChatColor.YELLOW + "");
            }
            if (text.contains("#f") || text.contains("#F")) {
                text = text.replaceAll("#F", ChatColor.WHITE + "");
                text = text.replaceAll("#f", ChatColor.WHITE + "");
            }
            if (text.contains("#k") || text.contains("#K")) {
                text = text.replaceAll("#K", ChatColor.MAGIC + "");
                text = text.replaceAll("#k", ChatColor.MAGIC + "");
            }
            if (text.contains("#l") || text.contains("#L")) {
                text = text.replaceAll("#L", ChatColor.BOLD + "");
                text = text.replaceAll("#l", ChatColor.BOLD + "");
            }
            if (text.contains("#m") || text.contains("#M")) {
                text = text.replaceAll("#M", ChatColor.STRIKETHROUGH + "");
                text = text.replaceAll("#m", ChatColor.STRIKETHROUGH + "");
            }
            if (text.contains("#n") || text.contains("#N")) {
                text = text.replaceAll("#N", ChatColor.UNDERLINE + "");
                text = text.replaceAll("#n", ChatColor.UNDERLINE + "");
            }
            if (text.contains("#o") || text.contains("#O")) {
                text = text.replaceAll("#O", ChatColor.ITALIC + "");
                text = text.replaceAll("#o", ChatColor.ITALIC + "");
            }
    	return text;
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

    public List<String> getInsults()
    {
        return insults;
    }

    public List<String> getQuitMessages()
    {
        return quitMessages;
    }

    public World getRulelessWorld() { return rulelessWorld; }
    public World getRulelessNether() { return rulelessWorldNether; }
    public World getRulelessEnd() { return rulelessWorldEnd; }

    // ============================================================================
    // Player methods
    // ============================================================================

    public void reloadPlayer(TregminePlayer player)
    {
        try {
            addPlayer(player.getDelegate(), player.getAddress().getAddress());
        } catch (PlayerBannedException e) {
            player.kickPlayer(plugin, e.getMessage());
        }
    }
    public Tregmine getTregmine(){
    	return plugin;
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

            int onlinePlayerCount = 0;
            Collection<? extends Player> onlinePlayers = getServer().getOnlinePlayers();
            if (onlinePlayers != null) {
                onlinePlayerCount = onlinePlayers.size();
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
            Collection<?extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
            if (onlinePlayers != null) {
                onlinePlayerCount = onlinePlayers.size();
            }

            ILogDAO logDAO = ctx.getLogDAO();
            logDAO.insertLogin(player, true, onlinePlayerCount);

            IPlayerDAO playerDAO = ctx.getPlayerDAO();
            playerDAO.updatePlayTime(player);
            playerDAO.updateBadges(player);
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
    public String getPluginFolder(){
    	return this.getDataFolder().getAbsolutePath();
    }
}
