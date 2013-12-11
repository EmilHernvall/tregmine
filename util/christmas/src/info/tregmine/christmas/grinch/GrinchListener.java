package info.tregmine.christmas.grinch;

import java.util.ArrayList;
import java.util.Random;

import info.tregmine.christmas.ChristmasMain;
import info.tregmine.christmas.API.HitBlockEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class GrinchListener implements Listener {

	static int spawnedGrinch = 0;
	int hostileGrinch = 0;

	private static ChristmasMain plugin;

	public GrinchListener(ChristmasMain instance)
	{
		this.plugin = instance;
	}

	public static void xmasMsg(String s, Boolean bool){
		for(Player online : Bukkit.getOnlinePlayers()){
			if (online.getWorld().getName() == "Christmas"){
				if (bool.TRUE){
					online.sendMessage(ChatColor.DARK_GRAY + "<" + ChatColor.RED + "The Grinch" + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + s);
				}else{
					online.sendMessage(s);
				}
			}
		}
	}

	@EventHandler
	public void onGrinchArrowHitPlayer(EntityDamageByEntityEvent e){
		Entity e1 = e.getEntity();
		Entity d1 = e.getDamager();

		if(e.getEntity().getWorld().getName() != "Christmas"){
			return;
		}

		if(e1 instanceof Player && d1 instanceof Arrow){
			if(((Arrow)d1).getShooter() instanceof Giant){
				Player hurt = (Player) e1;
				hurt.setFireTicks(hurt.getFireTicks() + 20);
			}
		}
	}

	@EventHandler
	private void onArrowHitBlock(HitBlockEvent e) {
		if(!(e.getArrow().getLocation().getWorld().getName() == "Christmas")){
			return;
		}
		Arrow arrow = e.getArrow();
		Bukkit.getWorld("Christmas").playEffect(arrow.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
		arrow.remove();
	}

	@EventHandler
	public void onGrinchHurt(EntityDamageEvent e) {
		if (!(e.getEntity().getWorld().getName() == "Christmas")){
			return;
		}
		if (!(e.getEntityType() == EntityType.GIANT)){
			return;
		}
		if (e.getCause() != DamageCause.ENTITY_ATTACK){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDamage(final EntityDamageByEntityEvent e) {
		World w = e.getEntity().getWorld();
		if(e.getEntity().getWorld().getName() != "Christmas"){
			return;
		}
		if (e.getEntity() instanceof Giant) {
			if (e.getDamager() instanceof Player) {
				final Player p = (Player) e.getDamager();
				if(p.getLocation().subtract(0,1,0).getBlock().getType() == Material.AIR){
					e.setCancelled(true);
					Location ALoc = new Location(p.getLocation().getWorld(),p.getLocation().getX(), w.getHighestBlockYAt(p.getLocation()), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
					p.teleport(ALoc);
				}else{
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {

							e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 10.0F);
						}
					},20 * 2);
				}
			}else{
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onGrinchSpawn(PlayerInteractEvent e) {

		final Player p = e.getPlayer();

		if(p.getWorld().getName() != "Christmas"){
			return;
		}

		if(p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR){
			return;
		}

		if(p.getItemInHand().getItemMeta().getDisplayName() == null){
			return;
		}

		if(p.getItemInHand().getType() != Material.MONSTER_EGG){
			return;
		}

		if(p.getItemInHand().getItemMeta().getDisplayName().contains("Grinch Spawn Egg")){
			if(spawnedGrinch != 0){
				e.getPlayer().sendMessage(ChatColor.RED + "Sorry, the Grinch is already currently spawned!");

				return;
			}
		}

		if(p.getItemInHand().getItemMeta().getDisplayName() != null){
			if(p.getItemInHand().getItemMeta().getDisplayName().contains("Grinch Spawn Egg")){
				if(plugin.getConfig().getInt("grinch" + ".maxSpawnsPerPlayer") <=
						(plugin.getConfig().getInt("player." + p.getName() + ".grinchSpawns"))){
					p.sendMessage(ChatColor.RED + "Sorry, This server only allows each player to spawn the Grinch " + (plugin.getConfig().getInt("grinch" + ".maxSpawnsPerPlayer")) + " time(s)!");
					p.sendMessage(ChatColor.RED + "If you believe that this is an error, then please contact your server administrator.");
				}else{
					spawnedGrinch = 1;
					plugin.getConfig().set("player." + p.getName() + ".grinchSpawns", plugin.getConfig().getInt("player." + p.getName() + ".grinchSpawns") + 1);
					plugin.saveConfig();

					ItemStack egg = p.getItemInHand();

					if(egg.getAmount() == 1){
						p.getInventory().remove(egg);

					}else{
						egg.setAmount(egg.getAmount() - 1);
					}

					spawnGrinchWitch(p, e.getClickedBlock());
				}
			}
		}
	}
	static int taskID;

	public static void spawnGrinchWitch(final Player p, final Block b){
		final Location bLoc = b.getLocation().add(0,1,0);
		final World w = p.getWorld();

		final Witch witch = (Witch)w.spawnEntity(bLoc, EntityType.WITCH);
		witch.setCanPickupItems(false);
		witch.setCustomName(ChatColor.GREEN + "The Grinch!");
		witch.setRemoveWhenFarAway(false);
		witch.setMaxHealth(200.0);
		witch.setHealth(200.0);
		witch.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 100000));

		GrinchSpawnSpeech.speech(p, witch, bLoc);
	}

	public static void spawnGrinchGiant(final Player p, final Block b){
		final Location bLoc = b.getLocation();
		final World w = p.getWorld();

		final Giant g = (Giant)w.spawnEntity(bLoc, EntityType.GIANT);
		g.setCanPickupItems(false);
		g.setCustomName(ChatColor.GREEN + "The Grinch!");
		g.setRemoveWhenFarAway(false);
		g.setMaxHealth(200.0);
		g.setHealth(200.0);
		g.setTarget((LivingEntity)p);
		g.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1000000));

		final int id = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
			public void run() {
				final Arrow arrow = ((LivingEntity) g).launchProjectile(Arrow.class);
				arrow.setShooter(((LivingEntity) g));

				if(!(arrow.isOnGround()) || arrow.isDead() == false || arrow.isValid() == true){

					arrow.getWorld().playEffect(arrow.getLocation(), Effect.STEP_SOUND, Material.EMERALD_BLOCK);
					arrow.getWorld().playEffect(arrow.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
				} 
			}
		}, 1L, 1L).getTaskId();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				if (spawnedGrinch == 0){
					plugin.getServer().getScheduler().cancelTask(id);
				}
			}
		}, 0, 20L);
	}


	private static FallingBlock randBlock(Location l){

		Location ground = new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ());
		Material block = ground.getBlock().getType();

		Random rand = new Random();
		int item = rand.nextInt(4);
		Material stack = null;
		Byte b = null; 

		if (item == 0){
			stack = Material.WOOL;
			b = 5;
		}else if (item == 1){
			stack = Material.WOOL;
			b = 14;
		}else if (item == 2){
			stack = block;
			b = 0;
		}else if (item == 3){
			stack = block;
			b = 0;
		}
		return(l.getWorld().spawnFallingBlock(l, stack, b));
	}

	public static void createTornado(final Location l, final Player p) {

		class VortexBlock {

			FallingBlock entity;

			private float ticker_vertical  = 0.0f;
			private float ticker_horisontal = (float) (Math.random() *  2 * Math.PI);

			public VortexBlock(Location l) {
				entity = randBlock(l);
			}

			public void setVelocity(Vector v) {
				entity.setVelocity(v);
			}

			public float verticalTicker() {
				if(ticker_vertical < 1.0f) {
					ticker_vertical += 0.05f;
				}

				return ticker_vertical;
			}

			public float horisontalTicker() {
				return (ticker_horisontal += 0.8f);
			}
		}
		final ArrayList<VortexBlock> blocks = new ArrayList<VortexBlock>();

		final int id = new BukkitRunnable() {

			public void run() {

				// Spawns 15 blocks at a the time, with a maximum of 200 blocks at the same time.
				for(int i = 0 ; i < 15 ; i++) {
					if(blocks.size() >= 200) {
						VortexBlock vb = blocks.get(0);
						vb.entity.remove();
						blocks.remove(vb);
					}

					blocks.add(new VortexBlock(l));
				}

				// Makes all of the blocks in the list spin.
				for(VortexBlock vb : blocks) {
					double radius = Math.sin(vb.verticalTicker()) * 2;

					float horisontal = vb.horisontalTicker();

					vb.setVelocity(new Vector(radius * Math.cos(horisontal), 0.5D, radius * Math.sin(horisontal)));
				}
			}
		}.runTaskTimer(plugin, 5L, 5L).getTaskId();

		// Stop the "tornado" after 10 "seconds".
		new BukkitRunnable() {
			public void run() {
				plugin.getServer().getScheduler().cancelTask(id);
				for(VortexBlock vb : blocks) {
					vb.entity.remove();
				}
				BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						spawnGrinchGiant(p, l.getBlock());
					}
				}, 5L);

			}
		}.runTaskLater(plugin, 20L * 10);
	}

	public void dropXmasBoots(Player player){
		ItemStack xmasBoots = new ItemStack( Material.GOLD_BOOTS, 1);
		ItemMeta meta = xmasBoots.getItemMeta();
		meta.setDisplayName("§4§k||§aChristmas Boots§4§k||");
		xmasBoots.setItemMeta(meta);

		player.getInventory().addItem(xmasBoots);
	}

	public void giveCake(Player p){
		ItemStack cake = new ItemStack( Material.CAKE, 1);
		ItemMeta meta = cake.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Christmas Pudding");
		cake.setItemMeta(meta);

		p.getInventory().addItem(cake);
	}
	
	public void dropExp(Location loc){
		((ExperienceOrb)loc.getWorld().spawn(loc, ExperienceOrb.class)).setExperience(100);
	}

	@EventHandler
	public void onGrinchDeath(EntityDeathEvent e) {
		final Location loc = e.getEntity().getLocation();
		final World wLoc = loc.getWorld();

		if(e.getEntity().getCustomName() == null){
			return;
		}

		Player player = e.getEntity().getKiller();

		if(e.getEntity().getCustomName().contains("The Grinch!") && (e.getEntityType() == EntityType.GIANT)){

			spawnedGrinch = 0;
			dropXmasBoots(player);
			GrinchDefeatSpeech.speech(player);
			dropExp(e.getEntity().getLocation());

			for(Player p : Bukkit.getOnlinePlayers()){
				if (p.getWorld().getName() == "Christmas"){
					giveCake(p);
				}
			}

			final Villager v = (Villager)wLoc.spawnEntity(loc.add(0,1,0), EntityType.VILLAGER);
			v.setCanPickupItems(false);
			v.setCustomName(ChatColor.GREEN + "The Grinch!");
			v.setProfession(Profession.PRIEST);			
		}
	}

	//I may come back to this later
	/*
	@EventHandler
	public void onGrinchKillPlayer(PlayerDeathEvent e) {
		final Location loc = e.getEntity().getLocation();
		final World wLoc = loc.getWorld();

		if (wLoc.getName() != "Christmas"){
			return;
		}

		Entity ent = e.getEntity();
		EntityDamageEvent ede = ent.getLastDamageCause();
		DamageCause dc = ede.getCause();

		if (dc == DamageCause.FIRE || dc == DamageCause.FIRE_TICK){
			if (spawnedGrinch == 1){
				Insult((Player) ent);
			}
		}

		if(e.getEntity().getKiller() == null){
			return;
		}

		if(e.getEntity().getKiller().getCustomName() == null){
			return;
		}

		if(e.getEntity().getKiller().getCustomName().contains("The Grinch!")){
			Player p = e.getEntity();
			Insult(p);
		}else if(e.getEntity().getKiller() instanceof Arrow){
			Arrow arrow = (Arrow) e.getEntity().getKiller();
			Player p = (Player) arrow.getShooter();
			Insult(p);
		}
	}

	private String Insult(Player p) {
		int rand = (int)(Math.random() * 6 + 1);
		String prefix = " just got killed by The Grinch. ";

		if(rand == 1){
			Bukkit.broadcastMessage(p.getName() + prefix + "Gotta go pick up the limbs!");
		}else if(rand == 2){
			Bukkit.broadcastMessage(p.getName() + prefix + "Better luck next time!");
		}else if(rand == 3){
			Bukkit.broadcastMessage(p.getName() + prefix + "You're staining my snow!");
		}else if(rand == 4){
			Bukkit.broadcastMessage(p.getName() + prefix + "Yes, arrows do kill.");
		}else if(rand == 5){
			Bukkit.broadcastMessage(p.getName() + prefix + "You had one job...");
		}else if(rand == 6){
			Bukkit.broadcastMessage(p.getName() + prefix + "HEADSHOT!");
		}
		return null;
	}
	 */
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e){
		if(e.getLocation().getWorld().getName() == "Christmas"){
			e.setCancelled(true);
		}
	}
}