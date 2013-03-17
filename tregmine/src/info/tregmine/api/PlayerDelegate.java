package info.tregmine.api;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.Map;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
//import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public abstract class PlayerDelegate implements Player 
{
	private Player delegate;
	
	protected PlayerDelegate(Player player) {
		this.delegate = player;
	}
	
	@Override
	public PlayerInventory getInventory() {
		return delegate.getInventory();
	}

	@Override
	public ItemStack getItemInHand() {
		return delegate.getItemInHand();
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public int getSleepTicks() {
		return delegate.getSleepTicks();
	}

	@Override
	public boolean isSleeping() {
		return delegate.isSleeping();
	}

	@Override
	public void setItemInHand(ItemStack arg0) {
		delegate.setItemInHand(arg0);
	}

	@Override
	public void damage(int arg0) {
		delegate.damage(arg0);
	}

	@Override
	public void damage(int arg0, Entity arg1) {
		delegate.damage(arg0, arg1);
	}

	@Override
	public double getEyeHeight() {
		return delegate.getEyeHeight();
	}

	@Override
	public double getEyeHeight(boolean arg0) {
		return delegate.getEyeHeight(arg0);
	}

	@Override
	public Location getEyeLocation() {
		return delegate.getEyeLocation();
	}

	@Override
	public int getHealth() {
		return delegate.getHealth();
	}

	@Override
	public int getLastDamage() {
		return delegate.getLastDamage();
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> arg0, int arg1) {
		return delegate.getLastTwoTargetBlocks(arg0, arg1);
	}

	@Override
	public List<Block> getLineOfSight(HashSet<Byte> arg0, int arg1) {
		return delegate.getLineOfSight(arg0, arg1);
	}

	@Override
	public int getMaximumAir() {
		return delegate.getMaximumAir();
	}

	@Override
	public int getMaximumNoDamageTicks() {
		return delegate.getMaximumNoDamageTicks();
	}

	@Override
	public int getNoDamageTicks() {
		return delegate.getNoDamageTicks();
	}

	@Override
	public int getRemainingAir() {
		return delegate.getRemainingAir();
	}

	@Override
	public Block getTargetBlock(HashSet<Byte> arg0, int arg1) {
		return delegate.getTargetBlock(arg0, arg1);
	}

	@Override
	public Entity getVehicle() {
		return delegate.getVehicle();
	}

	@Override
	public boolean isInsideVehicle() {
		return delegate.isInsideVehicle();
	}

	@Override
	public boolean leaveVehicle() {
		return delegate.leaveVehicle();
	}

	@Override
	public void setHealth(int arg0) {
		delegate.setHealth(arg0);
	}

	@Override
	public void setLastDamage(int arg0) {
		delegate.setLastDamage(arg0);
	}

	@Override
	public void setMaximumAir(int arg0) {
		delegate.setMaximumAir(arg0);
	}

	@Override
	public void setMaximumNoDamageTicks(int arg0) {
		delegate.setMaximumNoDamageTicks(arg0);
	}

	@Override
	public void setNoDamageTicks(int arg0) {
		delegate.setNoDamageTicks(arg0);
	}

	@Override
	public void setRemainingAir(int arg0) {
		delegate.setRemainingAir(arg0);
	}

	@Override
	public boolean eject() {
		return delegate.eject();
	}

	@Override
	public int getEntityId() {
		return delegate.getEntityId();
	}

	@Override
	public float getFallDistance() {
		return delegate.getFallDistance();
	}

	@Override
	public int getFireTicks() {
		return delegate.getFireTicks();
	}

	@Override
	public EntityDamageEvent getLastDamageCause() {
		return delegate.getLastDamageCause();
	}

	@Override
	public Location getLocation() {
		return delegate.getLocation();
	}

	@Override
	public int getMaxFireTicks() {
		return delegate.getMaxFireTicks();
	}

	@Override
	public List<Entity> getNearbyEntities(double arg0, double arg1, double arg2) {
		return delegate.getNearbyEntities(arg0, arg1, arg2);
	}

	@Override
	public Entity getPassenger() {
		return delegate.getPassenger();
	}

	@Override
	public Server getServer() {
		return delegate.getServer();
	}

	@Override
	public UUID getUniqueId() {
		return delegate.getUniqueId();
	}

	@Override
	public Vector getVelocity() {
		return delegate.getVelocity();
	}

	@Override
	public World getWorld() {
		return delegate.getWorld();
	}

	@Override
	public boolean isDead() {
		return delegate.isDead();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public void remove() {
		delegate.remove();
	}

	@Override
	public void setFallDistance(float arg0) {
		delegate.setFallDistance(arg0);
	}

	@Override
	public void setFireTicks(int arg0) {
		delegate.setFireTicks(arg0);
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent arg0) {
		delegate.setLastDamageCause(arg0);
	}

	@Override
	public boolean setPassenger(Entity arg0) {
		return delegate.setPassenger(arg0);
	}

	@Override
	public void setVelocity(Vector arg0) {
		delegate.setVelocity(arg0);
	}

	@Override
	public boolean teleport(Location arg0) {
		return delegate.teleport(arg0);
	}

	@Override
	public boolean teleport(Entity arg0) {
		return delegate.teleport(arg0);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0) {
		return delegate.addAttachment(arg0);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
		return delegate.addAttachment(arg0, arg1);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
		return delegate.addAttachment(arg0, arg1, arg2);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
		return delegate.addAttachment(arg0, arg1, arg2, arg3);
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return delegate.getEffectivePermissions();
	}

	@Override
	public boolean hasPermission(String arg0) {
		return delegate.hasPermission(arg0);
	}

	@Override
	public boolean hasPermission(Permission arg0) {
		return delegate.hasPermission(arg0);
	}

	@Override
	public boolean isPermissionSet(String arg0) {
		return delegate.isPermissionSet(arg0);
	}

	@Override
	public boolean isPermissionSet(Permission arg0) {
		return delegate.isPermissionSet(arg0);
	}

	@Override
	public void recalculatePermissions() {
		delegate.recalculatePermissions();
	}

	@Override
	public void removeAttachment(PermissionAttachment arg0) {
		delegate.removeAttachment(arg0);
	}

	@Override
	public boolean isOp() {
		return delegate.isOp();
	}

	@Override
	public void setOp(boolean arg0) {
		delegate.setOp(arg0);
	}

	@Override
	public void sendMessage(String arg0) {
		delegate.sendMessage(arg0);
	}

	@Override
	public void awardAchievement(Achievement arg0) {
		delegate.awardAchievement(arg0);
	}

	@Override
	public void chat(String arg0) {
		delegate.chat(arg0);
	}

	@Override
	public InetSocketAddress getAddress() {
		return delegate.getAddress();
	}

	@Override
	public Location getCompassTarget() {
		return delegate.getCompassTarget();
	}

	@Override
	public String getDisplayName() {
		return delegate.getDisplayName();
	}

	@Override
	public long getPlayerTime() {
		return delegate.getPlayerTime();
	}

	@Override
	public long getPlayerTimeOffset() {
		return delegate.getPlayerTimeOffset();
	}

	@Override
	public void incrementStatistic(Statistic arg0) {
		delegate.incrementStatistic(arg0);
	}

	@Override
	public void incrementStatistic(Statistic arg0, int arg1) {
		delegate.incrementStatistic(arg0, arg1);
	}

	@Override
	public void incrementStatistic(Statistic arg0, Material arg1) {
		delegate.incrementStatistic(arg0, arg1);
	}

	@Override
	public void incrementStatistic(Statistic arg0, Material arg1, int arg2) {
		delegate.incrementStatistic(arg0, arg1, arg2);
	}

	@Override
	public boolean isOnline() {
		return delegate.isOnline();
	}

	@Override
	public boolean isPlayerTimeRelative() {
		return delegate.isPlayerTimeRelative();
	}

	@Override
	public boolean isSleepingIgnored() {
		return delegate.isSleepingIgnored();
	}

	@Override
	public boolean isSneaking() {
		return delegate.isSneaking();
	}

	@Override
	public void kickPlayer(String arg0) {
		delegate.kickPlayer(arg0);
	}

	@Override
	public void loadData() {
		delegate.loadData();
	}

	@Override
	public boolean performCommand(String arg0) {
		return delegate.performCommand(arg0);
	}

	@Override
	public void playEffect(Location arg0, Effect arg1, int arg2) {
		delegate.playEffect(arg0, arg1, arg2);
	}

	@Override
	public void playNote(Location arg0, byte arg1, byte arg2) {
		delegate.playNote(arg0, arg1, arg2);
	}

	@Override
	public void playNote(Location arg0, Instrument arg1, Note arg2) {
		delegate.playNote(arg0, arg1, arg2);
	}

	@Override
	public void resetPlayerTime() {
		delegate.resetPlayerTime();
	}

	@Override
	public void saveData() {
		delegate.saveData();
	}

	@Override
	public void sendBlockChange(Location arg0, Material arg1, byte arg2) {
		delegate.sendBlockChange(arg0, arg1, arg2);
	}

	@Override
	public void sendBlockChange(Location arg0, int arg1, byte arg2) {
		delegate.sendBlockChange(arg0, arg1, arg2);
	}

	@Override
	public boolean sendChunkChange(Location arg0, int arg1, int arg2, int arg3, byte[] arg4) {
		return delegate.sendChunkChange(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void sendRawMessage(String arg0) {
		delegate.sendRawMessage(arg0);
	}

	@Override
	public void setCompassTarget(Location arg0) {
		delegate.setCompassTarget(arg0);
	}

	@Override
	public void setDisplayName(String arg0) {
		delegate.setDisplayName(arg0);
	}

	@Override
	public void setPlayerTime(long arg0, boolean arg1) {
		delegate.setPlayerTime(arg0, arg1);
	}

	@Override
	public void setSleepingIgnored(boolean arg0) {
		delegate.setSleepingIgnored(arg0);
	}

	@Override
	public void setSneaking(boolean arg0) {
		delegate.setSneaking(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateInventory() {
		delegate.updateInventory();
	}
	
	@Override
	public void setWhitelisted(boolean arg0)
	{
		delegate.setWhitelisted(arg0);
	}
	
	@Override
	public void setExhaustion(float arg0)
	{
		delegate.setExhaustion(arg0);
	}
		
	@Override
	public void setLevel(int arg0)
	{
		delegate.setLevel(arg0);
	}
	
	@Override
	public int getFoodLevel()
	{
		return delegate.getFoodLevel();
	}
	
	@Override
	public void sendMap(MapView arg0)
	{
		delegate.sendMap(arg0);
	}
	
	@Override
	public float getExhaustion() {
		return delegate.getExhaustion();
	}

	@Override
	public int getLevel() {
		return delegate.getLevel();
	}

	@Override
	public float getSaturation() {
		return delegate.getSaturation();
	}

	@Override
	public int getTotalExperience() {
		return delegate.getTotalExperience();
	}

	@Override
	public void setFoodLevel(int arg0) {
		delegate.setFoodLevel(arg0);
	}

	@Override
	public void setSaturation(float arg0) {
		delegate.setSaturation(arg0);
	}

	@Override
	public void setTotalExperience(int arg0) {
		delegate.setTotalExperience(arg0);
	}

	@Override
	public GameMode getGameMode() {
		return delegate.getGameMode();
	}

	@Override
	public void setGameMode(GameMode arg0) {
		delegate.setGameMode(arg0);
	}

	@Override
	public boolean isWhitelisted() {
		return delegate.isWhitelisted();
	}

	@Override
	public void setBanned(boolean arg0) {
		delegate.setBanned(arg0);
	}
	
	@Override
	public Location getBedSpawnLocation() {
		return delegate.getBedSpawnLocation();
	}

	@Override
	public boolean isSprinting() {
		return delegate.isSprinting();
	}

	@Override
	public void setSprinting(boolean arg0) {
		delegate.setSprinting(arg0);
	}
	
	@Override
	public String getPlayerListName() {
		return delegate.getPlayerListName();
	}

	@Override
	public void setPlayerListName(String arg0) {
		delegate.setPlayerListName(arg0);
	}

	@Override
	public void setTicksLived(int arg0) {
		delegate.setTicksLived(arg0);
	}

	@Override
	public int getTicksLived() {
		return delegate.getTicksLived();
	}

	@Override
	public Map<String, Object> serialize() {
		return delegate.serialize();
	}
	
	@Override
	public Player getPlayer() {
		return this.getServer().getPlayer(this.getName());
	}
	
	@Override
	public int getMaxHealth()
	{
		return delegate.getMaxHealth();
	}
	@Override
	public float getExp() {
		return delegate.getExp();
	}

	@Override
	public void giveExp(int arg0) {
		delegate.giveExp(arg0);	
	}

	@Override
	public void setExp(float arg0) {
		delegate.setExp(arg0);	
	}

	@Override
	public Player getKiller() {
		return  delegate.getKiller();
	}

	@Override
	public boolean teleport(Location arg0, TeleportCause arg1) {
		return delegate.teleport(arg0, arg1);
	}

	@Override
	public boolean teleport(Entity arg0, TeleportCause arg1) {
		return delegate.teleport(arg0, arg1);
	}

	@Override
	public long getFirstPlayed() {
		return delegate.getFirstPlayed();
	}

	@Override
	public long getLastPlayed() {
		return delegate.getLastPlayed();
	}

	@Override
	public boolean hasPlayedBefore() {
		return delegate.hasPlayedBefore();
	}

	@Override
	public Set<String> getListeningPluginChannels() {
		return delegate.getListeningPluginChannels();
	}

	@Override
	public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
		delegate.sendPluginMessage(arg0, arg1, arg2);	
	}
	
	@Override
	public void playEffect(EntityEffect arg0) {
		delegate.playEffect(arg0);
	}

	@Override
	public boolean getAllowFlight() {
		return delegate.getAllowFlight();
	}

	@Override
	public void setAllowFlight(boolean arg0) {
		delegate.setAllowFlight(arg0);		
	}
	
	@Override
	public void setBedSpawnLocation(Location arg0) {
		delegate.setBedSpawnLocation(arg0);
	}
	
	@Override
	public boolean canSee(Player arg0) {
		return delegate.canSee(arg0);
	}

	@Override
	public void hidePlayer(Player arg0) {
		delegate.hidePlayer(arg0);
	}

	public void hidePlayer(TregminePlayer arg0) {
		Player player = arg0.getPlayer();
		delegate.hidePlayer(player);
	}
	
	
	@Override
	public void showPlayer(Player arg0) {
		delegate.showPlayer(arg0);
	}

	public void showPlayer(TregminePlayer arg0) {
		Player player = arg0.getPlayer();
		delegate.hidePlayer(player);
	}
	
	
	@Override
	public boolean addPotionEffect(PotionEffect arg0) {
		return delegate.addPotionEffect(arg0);
	}

	@Override
	public boolean addPotionEffect(PotionEffect arg0, boolean arg1) {
		return delegate.addPotionEffect(arg0, arg1);
	}

	@Override
	public boolean addPotionEffects(Collection<PotionEffect> arg0) {
//		return delegate.addPotionEffect(arg0);
		return false;
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		return delegate.getActivePotionEffects();
	}

	@Override
	public boolean hasPotionEffect(PotionEffectType arg0) {
		return delegate.hasPotionEffect(arg0);
	}

	@Override
	public void removePotionEffect(PotionEffectType arg0) {
		delegate.removePotionEffect(arg0);		
	}

	@Override
	public <T> void playEffect(Location arg0, Effect arg1, T arg2) {
		delegate.playEffect(arg0, arg1, arg2);
		}

	@Override
	public void closeInventory() {
		delegate.closeInventory();		
	}

	@Override
	public ItemStack getItemOnCursor() {
		return delegate.getItemOnCursor();
	}

	@Override
	public InventoryView getOpenInventory() {
		return delegate.getOpenInventory();
	}

	@Override
	public InventoryView openEnchanting(Location arg0, boolean arg1) {
		return delegate.openEnchanting(arg0, arg1);
	}

	@Override
	public InventoryView openInventory(Inventory arg0) {
		return delegate.openInventory(arg0);
	}

	@Override
	public void openInventory(InventoryView arg0) {
		delegate.openInventory(arg0);
	}

	@Override
	public InventoryView openWorkbench(Location arg0, boolean arg1) {
		return delegate.openWorkbench(arg0, arg1);
	}

	@Override
	public void setItemOnCursor(ItemStack arg0) {
		delegate.setItemOnCursor(arg0);
	}

	@Override
	public boolean setWindowProperty(Property arg0, int arg1) {
		return delegate.setWindowProperty(arg0, arg1);
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> arg0) {
		return delegate.launchProjectile(arg0);
	}

	@Override
	public EntityType getType() {
		return delegate.getType();
	}

	@Override
	public List<MetadataValue> getMetadata(String arg0) {
		return delegate.getMetadata(arg0);
	}

	@Override
	public boolean hasMetadata(String arg0) {
		return delegate.hasMetadata(arg0);
	}

	@Override
	public void removeMetadata(String arg0, Plugin arg1) {
		delegate.removeMetadata(arg0, arg1);
	}

	@Override
	public void setMetadata(String arg0, MetadataValue arg1) {
		delegate.setMetadata(arg0, arg1);
	}

	@Override
	public void abandonConversation(Conversation arg0) {
		delegate.abandonConversation(arg0);		
	}

	@Override
	public void acceptConversationInput(String arg0) {
		delegate.acceptConversationInput(arg0);		
	}

	@Override
	public boolean beginConversation(Conversation arg0) {
		return delegate.beginConversation(arg0);
	}

	@Override
	public boolean isConversing() {
		return delegate.isConversing();
	}

	@Override
	public void sendMessage(String[] arg0) {
		delegate.sendMessage(arg0);
	}

	@Override
	public Arrow shootArrow() {
		return null;
	}

	@Override
	public Egg throwEgg() {
		return null;
	}

	@Override
	public Snowball throwSnowball() {
		return null;
	}
	
	@Override
	public boolean isFlying() {
		return delegate.isFlying();
	}

	@Override
	public void setFlying(boolean arg0) {
		delegate.setFlying(arg0);
	}

	@Override
	public boolean isBlocking() {
		return delegate.isBlocking();		
	}

	@Override
	public void abandonConversation(Conversation arg0, ConversationAbandonedEvent arg1) {
		delegate.abandonConversation(arg0, arg1);		
	}

	@Override
	public int getExpToLevel() {
		return delegate.getExpToLevel();
		}

	@Override
	public boolean hasLineOfSight(Entity arg0) {
		return delegate.hasLineOfSight(arg0);
	}

	@Override
	public boolean isValid() {
		return delegate.isValid();
	}

	@Override
	public float getFlySpeed() {
		return delegate.getFlySpeed();
	}

	@Override
	public float getWalkSpeed() {
		return delegate.getWalkSpeed();
	}

	@Override
	public void setFlySpeed(float arg0) throws IllegalArgumentException {
		delegate.setFlySpeed(arg0);
		}

	@Override
	public void setWalkSpeed(float arg0) throws IllegalArgumentException {
		delegate.setWalkSpeed(arg0);
	}
	
	@Override
	public void playSound(Location arg0, Sound arg1, float arg2, float arg3) {
		delegate.playSound(arg0, arg1, arg2, arg3);		
	}

	@Override
	public Inventory getEnderChest() {
		return delegate.getEnderChest();
	}

	@Override
	public void giveExpLevels(int arg0) {
		delegate.giveExpLevels(arg0);
		
	}

	@Override
	public void setBedSpawnLocation(Location arg0, boolean arg1) {
		delegate.setBedSpawnLocation(arg0, arg1);
	}

	@Override
	public void setTexturePack(String arg0) {
		delegate.setTexturePack(arg0);
	}

	@Override
	public boolean getCanPickupItems() {
		return delegate.getCanPickupItems();
	}

	@Override
	public EntityEquipment getEquipment() {
		return delegate.getEquipment();
	}

	@Override
	public boolean getRemoveWhenFarAway() {
		return delegate.getRemoveWhenFarAway();
	}

	@Override
	public void setCanPickupItems(boolean arg0) {
		delegate.setCanPickupItems(arg0);
	}

	@Override
	public void setRemoveWhenFarAway(boolean arg0) {
		delegate.setRemoveWhenFarAway(arg0);
	}

	@Override
	public Location getLocation(Location arg0) {
		return delegate.getLocation(arg0);
	}

	@Override
	public void resetMaxHealth() {
		delegate.resetMaxHealth();
	}

	@Override
	public void setMaxHealth(int arg0) {
		delegate.setMaxHealth(arg0);
	}
	@Override
	public String getCustomName() {
		return delegate.getCustomName();
	}

	@Override
	public boolean isCustomNameVisible() {
		return delegate.isCustomNameVisible();
	}

	@Override
	public void setCustomName(String arg0) {
		delegate.setCustomName(arg0);
	}

	@Override
	public void setCustomNameVisible(boolean arg0) {
		delegate.setCustomNameVisible(arg0);
	}

	
}
