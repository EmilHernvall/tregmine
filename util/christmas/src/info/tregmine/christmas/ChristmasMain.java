package info.tregmine.christmas;

import info.tregmine.christmas.API.HitBlockMain;
import info.tregmine.christmas.commands.ChristmasCommand;
import info.tregmine.christmas.grinch.GrinchDefeatSpeech;
import info.tregmine.christmas.grinch.GrinchListener;
import info.tregmine.christmas.grinch.GrinchSpawnSpeech;
import info.tregmine.christmas.listeners.BlockBreakListener;
import info.tregmine.christmas.listeners.PortalListener;
import info.tregmine.christmas.listeners.SparklerListener;
import info.tregmine.christmas.listeners.SpawnListener;
import info.tregmine.christmas.listeners.TargetingListener;
import info.tregmine.christmas.listeners.TraderListener;
import info.tregmine.christmas.listeners.WarpListener;
import info.tregmine.christmas.listeners.XmasBootsListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class ChristmasMain extends JavaPlugin implements Listener {
	private final static String WORLD_NAME = "Christmas";
	private static World map = null;

	public void onDisable() {
		Bukkit.broadcastMessage(ChatColor.RED + "Christmas has been, Unloaded!");
		for(Player p : Bukkit.getOnlinePlayers()){
			getConfig().set("player." + p.getName() + ".tokens", getConfig().getInt("player." + p.getName() + ".tokens"));
			getConfig().set("player." + p.getName() + ".grinchSpawns", getConfig().getInt("player." + p.getName() + ".grinchSpawns"));
		}
		getConfig().set("grinch" + ".maxSpawnsPerPlayer", getConfig().getInt("grinch" + ".maxSpawnsPerPlayer"));
		getConfig().set("portal" + ".returnWorld", getConfig().getString("portal" + ".returnWorld"));
		getConfig().set("merchant" + ".grinchEggCost", getConfig().getInt("merchant" + ".grinchEggCost"));
		getConfig().set("whitelist.warp.message", getConfig().getString("whitelist.warp.message"));
		getConfig().set("whitelist.build.message", getConfig().getString("whitelist.build.message"));

		saveConfig();

		for(Player p : Bukkit.getOnlinePlayers()){
			if(XmasBootsListener.bootwearers.contains(p.getName())){
				p.removePotionEffect(PotionEffectType.SPEED);
				p.removePotionEffect(PotionEffectType.JUMP);
				XmasBootsListener.bootwearers.remove(p.getName());
			}
		}
	}

	//TODO add A NORTH POLE at x = 0, z = 0

	public void onEnable() {

		Bukkit.getPluginManager().registerEvents(this,this);
		Bukkit.getPluginManager().registerEvents(new SpawnListener(), this);
		Bukkit.getPluginManager().registerEvents(new SparklerListener(), this);
		Bukkit.getPluginManager().registerEvents(new TargetingListener(this), this);
		Bukkit.getPluginManager().registerEvents(new GrinchListener(this), this);
		Bukkit.getPluginManager().registerEvents(new PortalListener(this), this);
		Bukkit.getPluginManager().registerEvents(new TraderListener(this), this);
		Bukkit.getPluginManager().registerEvents(new WarpListener(this), this);
		Bukkit.getPluginManager().registerEvents(new LightingFix(this), this);
		Bukkit.getPluginManager().registerEvents(new XmasBootsListener(this), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this), this);
		Bukkit.getPluginManager().registerEvents(new GrinchSpawnSpeech(this), this);
		Bukkit.getPluginManager().registerEvents(new GrinchDefeatSpeech(this), this);
		Bukkit.getPluginManager().registerEvents(new HitBlockMain(this), this);

		getCommand("christmas").setExecutor(new ChristmasCommand(this));

		getConfig().set("Setup" + ".configIsSetup", getConfig().getBoolean("Setup" + ".configIsSetup"));
		if(getConfig().getBoolean("Setup" + ".configIsSetup") == false){
			String s = ChatColor.RED + "=" + ChatColor.GREEN + "=";
			String e = ChatColor.RED + "=";

			Bukkit.broadcastMessage(s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+e);
			Bukkit.broadcastMessage(ChatColor.RED + "Sorry but Christmas hasn't been setup yet!");
			Bukkit.broadcastMessage(ChatColor.RED + "To set Christmas Adventure up please do the following:");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Navigate to '/plugins/Christmas/config.yml'");
			Bukkit.broadcastMessage(ChatColor.GRAY + "   (in your root server directory)");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "When you have finished choosing your settings please set");
			Bukkit.broadcastMessage(ChatColor.GRAY + "   'configIsSetup' to 'true' (under Setup in the config file)");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Lastly restart your server, and your ready to go!");
			Bukkit.broadcastMessage(s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+s+e);

			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}

		for(Player p : Bukkit.getOnlinePlayers()){
			getConfig().set("player." + p.getName() + ".warp", getConfig().getBoolean("player." + p.getName() + ".warp"));
			getConfig().set("player." + p.getName() + ".build", getConfig().getBoolean("player." + p.getName() + ".build"));
		}

		getConfig().set("merchant" + ".grinchEggCost", getConfig().getInt("merchant" + ".grinchEggCost"));
		getConfig().set("portal" + ".returnWorld", getConfig().getString("portal" + ".returnWorld"));
		getConfig().set("grinch" + ".maxSpawnsPerPlayer", getConfig().getInt("grinch" + ".maxSpawnsPerPlayer"));
		getConfig().set("whitelist.message", getConfig().getString("whitelist.message"));
		getConfig().set("smartlighting", getConfig().getString("smartlighting"));
		getConfig().set("whitelist.warp.status", getConfig().getBoolean("whitelist.warp.status"));
		getConfig().set("whitelist.build.status", getConfig().getBoolean("whitelist.build.status"));

		if(getConfig().getString("smartlighting") == null){
			getConfig().set("smartlighting", "true");
		}

		if(getConfig().getString("whitelist.warp.message") == null){
			getConfig().set("whitelist.warp.message", "Looks like someone didnt make santas nice list, You might have to wait until christmas day");
		}

		if(getConfig().getString("whitelist.build.message") == null){
			getConfig().set("whitelist.build.message", "Sorry, You dont have permission to build in the Northpole!");
		}

		if(getConfig().getString("portal" + ".returnWorld") == null){
			getConfig().set("portal" + ".returnWorld", "world");
		}

		if(getConfig().getString("whitelist" + ".warp") == null){
			getConfig().set("whitelist" + ".warp", "PlayerName");
		}

		saveConfig();

		this.saveDefaultConfig();

		World w = Bukkit.getWorld("Christmas");
		if(w == null){
			return;
		}
		for (Entity e : w.getEntities()){
			if(e.getType() == EntityType.GIANT){
				e.remove();
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();

		getConfig().set("player." + p.getName() + ".tokens", getConfig().getInt("player." + p.getName() + ".tokens"));
		getConfig().set("player." + p.getName() + ".grinchSpawns", getConfig().getInt("player." + p.getName() + ".grinchSpawns"));
		getConfig().set("player." + p.getName() + ".warp", getConfig().getBoolean("player." + p.getName() + ".warp"));
		getConfig().set("player." + p.getName() + ".build", getConfig().getBoolean("player." + p.getName() + ".build"));
		saveConfig();
	}

	public static World getMap() {
		WorldCreator wc = new WorldCreator(WORLD_NAME);
		wc.environment(Environment.NORMAL);
		wc.generator(new ChristmasChunkGenerator());
		wc.createWorld();
		World world =  Bukkit.getWorld(WORLD_NAME);
		if (map == null) {
			map = Bukkit.getServer().createWorld(wc);
		}
		return world;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new ChristmasChunkGenerator();
	}

	//TODO tags:
	/*
	 * ride your friends!
	 * grinch
	 * merchants
	 * new structures!
	 * new dimension!
	 * custom terrain generation
	 */
}
