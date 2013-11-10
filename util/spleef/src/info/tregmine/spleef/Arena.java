package info.tregmine.spleef;

import info.tregmine.spleef.ArenaManager.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Arena {

    private int id;
    private boolean started = false;
    private boolean cd = false;
    private Location redspawn, bluespawn, yellowspawn, greenspawn, blackspawn;
    private Location min, max;
    private Location head;
    private String rotation;
    private ArrayList<PlayerData> players = new ArrayList<PlayerData>();

    private Scoreboard sb;
    private Objective o;
    private Score red, blue, yellow, green;

    private Scoreboard clear;

    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard board = manager.getNewScoreboard();

    public Arena(int id) {
        this.id = id;

        ConfigurationSection conf = SettingsManager.getInstance().get(id + "");

        this.redspawn = getLocation(conf.getConfigurationSection("redspawn"));
        this.bluespawn = getLocation(conf.getConfigurationSection("bluespawn"));
        this.yellowspawn = getLocation(conf.getConfigurationSection("yellowspawn"));
        this.greenspawn = getLocation(conf.getConfigurationSection("greenspawn"));
        this.blackspawn = getLocation(conf.getConfigurationSection("blackspawn"));

        /* 
         * Soon we will change this to a game win counter :D
         */
        sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        o = sb.registerNewObjective("Score", "foo");
        red = o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.RED + "Red"));
        blue = o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.BLUE + "Blue"));
        yellow = o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.YELLOW + "Yellow"));
        green = o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GREEN + "Green"));

        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(ChatColor.BOLD + "Blocks Broken");

        clear = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
    }

    private Location getLocation(ConfigurationSection path) {
        return new Location(
                Bukkit.getServer().getWorld(path.getString("world")),
                path.getDouble("x"),
                path.getDouble("y"),
                path.getDouble("z")
                );
    }

    private String getRotation(ConfigurationSection path) {
        return new String(
                path.getString("rotation")
                );
    }

    public int getID() {
        return id;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Location getSpawn(Team team) {
        switch(team) {
        case RED: return redspawn;
        case BLUE: return bluespawn;
        case YELLOW: return yellowspawn;
        case GREEN: return greenspawn;
        case BLACK: return blackspawn;
        default: return null;
        }
    }

    public Team getTeam(Player p) {
        return getData(p).getTeam();
    }

    public void addPlayer(Player p) {
        players.add(new PlayerData(p.getName(), getTeamWithNoPlayers(), p.getInventory().getContents(), p.getInventory().getArmorContents(), p.getLocation()));

        if (getTeam(p) == Team.BLACK){
            PlayerData pd = getData(p);
            players.remove(pd);
            MessageManager.getInstance().info(p, "This arena is full, sorry.");
            return;
        }

        ItemStack spade = new ItemStack(Material.DIAMOND_SPADE, 1);
        ItemMeta im = spade.getItemMeta();
        im.setDisplayName("§3§lShovel");
        im.setLore(Arrays.asList("§oRight Click on Snow"));
        spade.setItemMeta(im);

        p.getInventory().clear();
        p.getInventory().addItem(spade);
        p.getInventory().setHelmet(new Wool(DyeColor.valueOf(getData(p).getTeam().toString())).toItemStack(1));

        p.teleport(getSpawn(getData(p).getTeam()));

        p.setScoreboard(sb);

        p.setGameMode(GameMode.SURVIVAL);
        p.setFlying(false);

        MessageManager.getInstance().info(p, "You have joined arena " + getID() + " and are on the " + ChatColor.valueOf(getData(p).getTeam().toString()) + getData(p).getTeam().toString().toLowerCase() + ChatColor.YELLOW + " team!");

        if (players.size() >= 2 && !cd) start();
    }

    public void removePlayer(Player p, boolean lost) {
        PlayerData pd = getData(p);

        p.getInventory().clear();
        for (ItemStack i : pd.getContents()) if (i != null) p.getInventory().addItem(i);
        p.getInventory().setArmorContents(pd.getArmorContents());

        p.teleport(pd.getLocation());

        players.remove(pd);

        p.setScoreboard(clear);

        if (lost) {
            msg(p.getName() + " fell out of Spleef.");
            MessageManager.getInstance().good(p, "Better luck next time :)");
        }

        if (players.size() == 1){
            Iterator<PlayerData> it = players.iterator();
            PlayerData data = it.next();
            Player winner = Bukkit.getServer().getPlayer(data.getPlayerName());

            stop(winner);   
        }
    }

    public void start() {
        cd = true;
        msg("Spleef starting in 30 seconds!");
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SettingsManager.getInstance().getPlugin(), new Runnable() {
            public void run() {
                Arena.this.started = true;
                Arena.this.cd = false;
                msg("Spleef has begun!");
            }
        }, 30 * 20);
    }

    public void stop(Player winner) {
        msg(winner != null ? winner.getName() + " has won Spleef!" : "Spleef was ended.");

        if (winner != null){
            winnerFW(winner);
            setWinnerHead(winner);
        }
        
        Arena.this.started = false;
        resetArena();

        Iterator<PlayerData> it = players.iterator();
        while (it.hasNext()) {
            PlayerData pd = it.next();
            Player p = Bukkit.getServer().getPlayer(pd.getPlayerName());
            removePlayer(p, false);
            players.remove(pd);
            it.remove();
        }
    }

    public void winnerFW(Player p){
        Firework fw = (Firework) p.getWorld()
                .spawnEntity(p.getLocation(),EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        FireworkEffect.Type type = FireworkEffect.Type.BURST;
        FireworkEffect effect = (FireworkEffect.builder().trail(false)
                .withColor(Color.GREEN).flicker(false).with(type).build());
        fwm.addEffect(effect);
        fwm.setPower(-0);
        fw.setFireworkMeta(fwm);
    }

    public void clearScore(Player p) {
        Team t = getTeam(p);
        if (t == Team.RED) red.setScore(0);
        else if (t == Team.BLUE) blue.setScore(0);
        else if (t == Team.YELLOW) yellow.setScore(0);
        else green.setScore(0);

    }

    public void addBlockBreak(Player p) {
        Team t = getTeam(p);
        if (t == Team.RED) red.setScore(red.getScore() + 1);
        else if (t == Team.BLUE) blue.setScore(blue.getScore() + 1);
        else if (t == Team.YELLOW) yellow.setScore(yellow.getScore() + 1);
        else green.setScore(green.getScore() + 1);
    }

    private void msg(String msg) {
        for (PlayerData pd : players) {
            Player p = Bukkit.getServer().getPlayer(pd.getPlayerName());
            MessageManager.getInstance().info(p, msg);
        }
    }

    private Team getTeamWithNoPlayers() {
        int red = 0, blue = 0, yellow = 0, green = 0;
        for (PlayerData pd : players) {
            if (pd.getTeam() == Team.RED) red++;
            else if (pd.getTeam() == Team.BLUE) blue++;
            else if (pd.getTeam() == Team.YELLOW) yellow++;
            else if (pd.getTeam() == Team.GREEN) green++;
        }

        if (yellow < 1) return Team.YELLOW;
        else if (blue < 1) return Team.BLUE;
        else if (red < 1) return Team.RED;
        else if (green < 1) return Team.GREEN;
        else return Team.BLACK;
    }

    public boolean containsPlayer(Player p) {
        return getData(p) != null;
    }

    public PlayerData getData(Player p) {
        for (PlayerData pd : players) {
            if (pd.getPlayerName().equalsIgnoreCase(p.getName())) return pd;
        }
        return null;
    }

    private void setWinnerHead(Player p)
    {
        ConfigurationSection conf = SettingsManager.getInstance().get(id + "");

        this.head = getLocation(conf.getConfigurationSection("head" + "." + "latestwinner"));
        this.rotation = getRotation(conf.getConfigurationSection("head" + "." + "latestwinner"));

        BlockState block = head.getBlock().getState();

        Skull skull = (Skull) block;
        skull.setSkullType(SkullType.PLAYER);
        skull.setOwner(p.getName());
        skull.setRotation(BlockFace.valueOf(rotation));
        skull.update();
    }

    public void resetArena() {
        ConfigurationSection conf = SettingsManager.getInstance().get(id + "");

        this.min = getLocation(conf.getConfigurationSection("minArenaLoc"));
        this.max = getLocation(conf.getConfigurationSection("maxArenaLoc"));

        int spleefMinX = min.getBlockX();
        int spleefMaxX = max.getBlockX();
        int spleefMinZ = min.getBlockZ();
        int spleefMaxZ = max.getBlockZ();
        int spleefSnow = min.getBlockY();
        World world = max.getWorld();

        for(int x = spleefMinX; x <= spleefMaxX; x++)
        {
            for(int z = spleefMinZ; z <= spleefMaxZ; z++)
            {
                Location arena = new Location(world, x, spleefSnow, z);
                arena.getBlock().setType(Material.SNOW_BLOCK);
            }
        }
    }
}
