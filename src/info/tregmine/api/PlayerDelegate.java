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
import org.bukkit.WeatherType;
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
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

public abstract class PlayerDelegate implements Player
{
    private Player delegate;

    protected PlayerDelegate(Player player) {
        this.delegate = player;
    }

    public void setDelegate(Player v) { this.delegate = v; }
    public Player getDelegate() { return delegate; }

    private void checkState()
    {
        if (delegate == null) {
            throw new IllegalStateException("Can't be used when delegate isn't set.");
        }
    }

    @Override
    public PlayerInventory getInventory() {
        checkState();
        return delegate.getInventory();
    }

    @Override
    public ItemStack getItemInHand() {
        checkState();
        return delegate.getItemInHand();
    }

    @Override
    public String getName() {
        checkState();
        return delegate.getName();
    }

    @Override
    public int getSleepTicks() {
        checkState();
        return delegate.getSleepTicks();
    }

    @Override
    public boolean isSleeping() {
        checkState();
        return delegate.isSleeping();
    }

    @Override
    public void setItemInHand(ItemStack arg0) {
        checkState();
        delegate.setItemInHand(arg0);
    }

    @Override
    public void damage(int arg0) {
        checkState();
        delegate.damage(arg0);
    }

    @Override
    public void damage(int arg0, Entity arg1) {
        checkState();
        delegate.damage(arg0, arg1);
    }

    @Override
    public double getEyeHeight() {
        checkState();
        return delegate.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean arg0) {
        checkState();
        return delegate.getEyeHeight(arg0);
    }

    @Override
    public Location getEyeLocation() {
        checkState();
        return delegate.getEyeLocation();
    }

    @Override
    public int getHealth() {
        checkState();
        return delegate.getHealth();
    }

    @Override
    public int getLastDamage() {
        checkState();
        return delegate.getLastDamage();
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> arg0, int arg1) {
        checkState();
        return delegate.getLastTwoTargetBlocks(arg0, arg1);
    }

    @Override
    public List<Block> getLineOfSight(HashSet<Byte> arg0, int arg1) {
        checkState();
        return delegate.getLineOfSight(arg0, arg1);
    }

    @Override
    public int getMaximumAir() {
        checkState();
        return delegate.getMaximumAir();
    }

    @Override
    public int getMaximumNoDamageTicks() {
        checkState();
        return delegate.getMaximumNoDamageTicks();
    }

    @Override
    public int getNoDamageTicks() {
        checkState();
        return delegate.getNoDamageTicks();
    }

    @Override
    public int getRemainingAir() {
        checkState();
        return delegate.getRemainingAir();
    }

    @Override
    public Block getTargetBlock(HashSet<Byte> arg0, int arg1) {
        checkState();
        return delegate.getTargetBlock(arg0, arg1);
    }

    @Override
    public Entity getVehicle() {
        checkState();
        return delegate.getVehicle();
    }

    @Override
    public boolean isInsideVehicle() {
        checkState();
        return delegate.isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        checkState();
        return delegate.leaveVehicle();
    }

    @Override
    public void setHealth(int arg0) {
        checkState();
        delegate.setHealth(arg0);
    }

    @Override
    public void setLastDamage(int arg0) {
        checkState();
        delegate.setLastDamage(arg0);
    }

    @Override
    public void setMaximumAir(int arg0) {
        checkState();
        delegate.setMaximumAir(arg0);
    }

    @Override
    public void setMaximumNoDamageTicks(int arg0) {
        checkState();
        delegate.setMaximumNoDamageTicks(arg0);
    }

    @Override
    public void setNoDamageTicks(int arg0) {
        checkState();
        delegate.setNoDamageTicks(arg0);
    }

    @Override
    public void setRemainingAir(int arg0) {
        checkState();
        delegate.setRemainingAir(arg0);
    }

    @Override
    public boolean eject() {
        checkState();
        return delegate.eject();
    }

    @Override
    public int getEntityId() {
        checkState();
        return delegate.getEntityId();
    }

    @Override
    public float getFallDistance() {
        checkState();
        return delegate.getFallDistance();
    }

    @Override
    public int getFireTicks() {
        checkState();
        return delegate.getFireTicks();
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        checkState();
        return delegate.getLastDamageCause();
    }

    @Override
    public Location getLocation() {
        checkState();
        return delegate.getLocation();
    }

    @Override
    public int getMaxFireTicks() {
        checkState();
        return delegate.getMaxFireTicks();
    }

    @Override
    public List<Entity> getNearbyEntities(double arg0, double arg1, double arg2) {
        checkState();
        return delegate.getNearbyEntities(arg0, arg1, arg2);
    }

    @Override
    public Entity getPassenger() {
        checkState();
        return delegate.getPassenger();
    }

    @Override
    public Server getServer() {
        checkState();
        return delegate.getServer();
    }

    @Override
    public UUID getUniqueId() {
        checkState();
        return delegate.getUniqueId();
    }

    @Override
    public Vector getVelocity() {
        checkState();
        return delegate.getVelocity();
    }

    @Override
    public World getWorld() {
        checkState();
        return delegate.getWorld();
    }

    @Override
    public boolean isDead() {
        checkState();
        return delegate.isDead();
    }

    @Override
    public boolean isEmpty() {
        checkState();
        return delegate.isEmpty();
    }

    @Override
    public void remove() {
        checkState();
        delegate.remove();
    }

    @Override
    public void setFallDistance(float arg0) {
        checkState();
        delegate.setFallDistance(arg0);
    }

    @Override
    public void setFireTicks(int arg0) {
        checkState();
        delegate.setFireTicks(arg0);
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent arg0) {
        checkState();
        delegate.setLastDamageCause(arg0);
    }

    @Override
    public boolean setPassenger(Entity arg0) {
        checkState();
        return delegate.setPassenger(arg0);
    }

    @Override
    public void setVelocity(Vector arg0) {
        checkState();
        delegate.setVelocity(arg0);
    }

    @Override
    public boolean teleport(Location arg0) {
        checkState();
        return delegate.teleport(arg0);
    }

    @Override
    public boolean teleport(Entity arg0) {
        checkState();
        return delegate.teleport(arg0);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0) {
        checkState();
        return delegate.addAttachment(arg0);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
        checkState();
        return delegate.addAttachment(arg0, arg1);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
        checkState();
        return delegate.addAttachment(arg0, arg1, arg2);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
        checkState();
        return delegate.addAttachment(arg0, arg1, arg2, arg3);
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        checkState();
        return delegate.getEffectivePermissions();
    }

    @Override
    public boolean hasPermission(String arg0) {
        checkState();
        return delegate.hasPermission(arg0);
    }

    @Override
    public boolean hasPermission(Permission arg0) {
        checkState();
        return delegate.hasPermission(arg0);
    }

    @Override
    public boolean isPermissionSet(String arg0) {
        checkState();
        return delegate.isPermissionSet(arg0);
    }

    @Override
    public boolean isPermissionSet(Permission arg0) {
        checkState();
        return delegate.isPermissionSet(arg0);
    }

    @Override
    public void recalculatePermissions() {
        checkState();
        delegate.recalculatePermissions();
    }

    @Override
    public void removeAttachment(PermissionAttachment arg0) {
        checkState();
        delegate.removeAttachment(arg0);
    }

    @Override
    public boolean isOp() {
        checkState();
        return delegate.isOp();
    }

    @Override
    public void setOp(boolean arg0) {
        checkState();
        delegate.setOp(arg0);
    }

    @Override
    public void sendMessage(String arg0) {
        checkState();
        delegate.sendMessage(arg0);
    }

    @Override
    public void awardAchievement(Achievement arg0) {
        checkState();
        delegate.awardAchievement(arg0);
    }

    @Override
    public void chat(String arg0) {
        checkState();
        delegate.chat(arg0);
    }

    @Override
    public InetSocketAddress getAddress() {
        checkState();
        return delegate.getAddress();
    }

    @Override
    public Location getCompassTarget() {
        checkState();
        return delegate.getCompassTarget();
    }

    @Override
    public String getDisplayName() {
        checkState();
        return delegate.getDisplayName();
    }

    @Override
    public long getPlayerTime() {
        checkState();
        return delegate.getPlayerTime();
    }

    @Override
    public long getPlayerTimeOffset() {
        checkState();
        return delegate.getPlayerTimeOffset();
    }

    @Override
    public void incrementStatistic(Statistic arg0) {
        checkState();
        delegate.incrementStatistic(arg0);
    }

    @Override
    public void incrementStatistic(Statistic arg0, int arg1) {
        checkState();
        delegate.incrementStatistic(arg0, arg1);
    }

    @Override
    public void incrementStatistic(Statistic arg0, Material arg1) {
        checkState();
        delegate.incrementStatistic(arg0, arg1);
    }

    @Override
    public void incrementStatistic(Statistic arg0, Material arg1, int arg2) {
        checkState();
        delegate.incrementStatistic(arg0, arg1, arg2);
    }

    @Override
    public boolean isOnline() {
        if (delegate == null) {
            return false;
        }

        return delegate.isOnline();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        checkState();
        return delegate.isPlayerTimeRelative();
    }

    @Override
    public boolean isSleepingIgnored() {
        checkState();
        return delegate.isSleepingIgnored();
    }

    @Override
    public boolean isSneaking() {
        checkState();
        return delegate.isSneaking();
    }

    @Override
    public void kickPlayer(String arg0) {
        checkState();
        delegate.kickPlayer(arg0);
    }

    @Override
    public void loadData() {
        checkState();
        delegate.loadData();
    }

    @Override
    public boolean performCommand(String arg0) {
        checkState();
        return delegate.performCommand(arg0);
    }

    @Override
    public void playEffect(Location arg0, Effect arg1, int arg2) {
        checkState();
        delegate.playEffect(arg0, arg1, arg2);
    }

    @Override
    public void playNote(Location arg0, byte arg1, byte arg2) {
        checkState();
        delegate.playNote(arg0, arg1, arg2);
    }

    @Override
    public void playNote(Location arg0, Instrument arg1, Note arg2) {
        checkState();
        delegate.playNote(arg0, arg1, arg2);
    }

    @Override
    public void resetPlayerTime() {
        checkState();
        delegate.resetPlayerTime();
    }

    @Override
    public void saveData() {
        checkState();
        delegate.saveData();
    }

    @Override
    public void sendBlockChange(Location arg0, Material arg1, byte arg2) {
        checkState();
        delegate.sendBlockChange(arg0, arg1, arg2);
    }

    @Override
    public void sendBlockChange(Location arg0, int arg1, byte arg2) {
        checkState();
        delegate.sendBlockChange(arg0, arg1, arg2);
    }

    @Override
    public boolean sendChunkChange(Location arg0, int arg1, int arg2, int arg3, byte[] arg4) {
        checkState();
        return delegate.sendChunkChange(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public void sendRawMessage(String arg0) {
        checkState();
        delegate.sendRawMessage(arg0);
    }

    @Override
    public void setCompassTarget(Location arg0) {
        checkState();
        delegate.setCompassTarget(arg0);
    }

    @Override
    public void setDisplayName(String arg0) {
        checkState();
        delegate.setDisplayName(arg0);
    }

    @Override
    public void setPlayerTime(long arg0, boolean arg1) {
        checkState();
        delegate.setPlayerTime(arg0, arg1);
    }

    @Override
    public void setSleepingIgnored(boolean arg0) {
        checkState();
        delegate.setSleepingIgnored(arg0);
    }

    @Override
    public void setSneaking(boolean arg0) {
        checkState();
        delegate.setSneaking(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void updateInventory() {
        checkState();
        delegate.updateInventory();
    }

    @Override
    public void setWhitelisted(boolean arg0)
    {
        checkState();
        delegate.setWhitelisted(arg0);
    }

    @Override
    public void setExhaustion(float arg0)
    {
        checkState();
        delegate.setExhaustion(arg0);
    }

    @Override
    public void setLevel(int arg0)
    {
        checkState();
        delegate.setLevel(arg0);
    }

    @Override
    public int getFoodLevel()
    {
        checkState();
        return delegate.getFoodLevel();
    }

    @Override
    public void sendMap(MapView arg0)
    {
        checkState();
        delegate.sendMap(arg0);
    }

    @Override
    public float getExhaustion() {
        checkState();
        return delegate.getExhaustion();
    }

    @Override
    public int getLevel() {
        checkState();
        return delegate.getLevel();
    }

    @Override
    public float getSaturation() {
        checkState();
        return delegate.getSaturation();
    }

    @Override
    public int getTotalExperience() {
        checkState();
        return delegate.getTotalExperience();
    }

    @Override
    public void setFoodLevel(int arg0) {
        checkState();
        delegate.setFoodLevel(arg0);
    }

    @Override
    public void setSaturation(float arg0) {
        checkState();
        delegate.setSaturation(arg0);
    }

    @Override
    public void setTotalExperience(int arg0) {
        checkState();
        delegate.setTotalExperience(arg0);
    }

    @Override
    public GameMode getGameMode() {
        checkState();
        return delegate.getGameMode();
    }

    @Override
    public void setGameMode(GameMode arg0) {
        checkState();
        delegate.setGameMode(arg0);
    }

    @Override
    public boolean isWhitelisted() {
        checkState();
        return delegate.isWhitelisted();
    }

    @Override
    public void setBanned(boolean arg0) {
        checkState();
        delegate.setBanned(arg0);
    }

    @Override
    public Location getBedSpawnLocation() {
        checkState();
        return delegate.getBedSpawnLocation();
    }

    @Override
    public boolean isSprinting() {
        checkState();
        return delegate.isSprinting();
    }

    @Override
    public void setSprinting(boolean arg0) {
        checkState();
        delegate.setSprinting(arg0);
    }

    @Override
    public String getPlayerListName() {
        checkState();
        return delegate.getPlayerListName();
    }

    @Override
    public void setPlayerListName(String arg0) {
        checkState();
        delegate.setPlayerListName(arg0);
    }

    @Override
    public void setTicksLived(int arg0) {
        checkState();
        delegate.setTicksLived(arg0);
    }

    @Override
    public int getTicksLived() {
        checkState();
        return delegate.getTicksLived();
    }

    @Override
    public Map<String, Object> serialize() {
        checkState();
        return delegate.serialize();
    }

    @Override
    public Player getPlayer() {
        checkState();
        return this.getServer().getPlayer(this.getName());
    }

    @Override
    public int getMaxHealth()
    {
        checkState();
        return delegate.getMaxHealth();
    }

    @Override
    public float getExp() {
        checkState();
        return delegate.getExp();
    }

    @Override
    public void giveExp(int arg0) {
        checkState();
        delegate.giveExp(arg0);
    }

    @Override
    public void setExp(float arg0) {
        checkState();
        delegate.setExp(arg0);
    }

    @Override
    public Player getKiller() {
        checkState();
        return  delegate.getKiller();
    }

    @Override
    public boolean teleport(Location arg0, TeleportCause arg1) {
        checkState();
        return delegate.teleport(arg0, arg1);
    }

    @Override
    public boolean teleport(Entity arg0, TeleportCause arg1) {
        checkState();
        return delegate.teleport(arg0, arg1);
    }

    @Override
    public long getFirstPlayed() {
        checkState();
        return delegate.getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        checkState();
        return delegate.getLastPlayed();
    }

    @Override
    public boolean hasPlayedBefore() {
        checkState();
        return delegate.hasPlayedBefore();
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        checkState();
        return delegate.getListeningPluginChannels();
    }

    @Override
    public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
        checkState();
        delegate.sendPluginMessage(arg0, arg1, arg2);
    }

    @Override
    public void playEffect(EntityEffect arg0) {
        checkState();
        delegate.playEffect(arg0);
    }

    @Override
    public boolean getAllowFlight() {
        checkState();
        return delegate.getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean arg0) {
        checkState();
        delegate.setAllowFlight(arg0);
    }

    @Override
    public void setBedSpawnLocation(Location arg0) {
        checkState();
        delegate.setBedSpawnLocation(arg0);
    }

    @Override
    public boolean canSee(Player arg0) {
        checkState();
        return delegate.canSee(arg0);
    }

    @Override
    public void hidePlayer(Player arg0) {
        checkState();
        delegate.hidePlayer(arg0);
    }

    @Override
    public void showPlayer(Player arg0) {
        checkState();
        delegate.showPlayer(arg0);
    }

    @Override
    public boolean addPotionEffect(PotionEffect arg0) {
        checkState();
        return delegate.addPotionEffect(arg0);
    }

    @Override
    public boolean addPotionEffect(PotionEffect arg0, boolean arg1) {
        checkState();
        return delegate.addPotionEffect(arg0, arg1);
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> arg0) {
        checkState();
        return delegate.addPotionEffects(arg0);
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        checkState();
        return delegate.getActivePotionEffects();
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType arg0) {
        checkState();
        return delegate.hasPotionEffect(arg0);
    }

    @Override
    public void removePotionEffect(PotionEffectType arg0) {
        checkState();
        delegate.removePotionEffect(arg0);
    }

    @Override
    public <T> void playEffect(Location arg0, Effect arg1, T arg2) {
        checkState();
        delegate.playEffect(arg0, arg1, arg2);
    }

    @Override
    public void closeInventory() {
        checkState();
        delegate.closeInventory();
    }

    @Override
    public ItemStack getItemOnCursor() {
        checkState();
        return delegate.getItemOnCursor();
    }

    @Override
    public InventoryView getOpenInventory() {
        checkState();
        return delegate.getOpenInventory();
    }

    @Override
    public InventoryView openEnchanting(Location arg0, boolean arg1) {
        checkState();
        return delegate.openEnchanting(arg0, arg1);
    }

    @Override
    public InventoryView openInventory(Inventory arg0) {
        checkState();
        return delegate.openInventory(arg0);
    }

    @Override
    public void openInventory(InventoryView arg0) {
        checkState();
        delegate.openInventory(arg0);
    }

    @Override
    public InventoryView openWorkbench(Location arg0, boolean arg1) {
        checkState();
        return delegate.openWorkbench(arg0, arg1);
    }

    @Override
    public void setItemOnCursor(ItemStack arg0) {
        checkState();
        delegate.setItemOnCursor(arg0);
    }

    @Override
    public boolean setWindowProperty(Property arg0, int arg1) {
        checkState();
        return delegate.setWindowProperty(arg0, arg1);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> arg0) {
        checkState();
        return delegate.launchProjectile(arg0);
    }

    @Override
    public EntityType getType() {
        checkState();
        return delegate.getType();
    }

    @Override
    public List<MetadataValue> getMetadata(String arg0) {
        checkState();
        return delegate.getMetadata(arg0);
    }

    @Override
    public boolean hasMetadata(String arg0) {
        checkState();
        return delegate.hasMetadata(arg0);
    }

    @Override
    public void removeMetadata(String arg0, Plugin arg1) {
        checkState();
        delegate.removeMetadata(arg0, arg1);
    }

    @Override
    public void setMetadata(String arg0, MetadataValue arg1) {
        checkState();
        delegate.setMetadata(arg0, arg1);
    }

    @Override
    public void abandonConversation(Conversation arg0) {
        checkState();
        delegate.abandonConversation(arg0);
    }

    @Override
    public void acceptConversationInput(String arg0) {
        checkState();
        delegate.acceptConversationInput(arg0);
    }

    @Override
    public boolean beginConversation(Conversation arg0) {
        checkState();
        return delegate.beginConversation(arg0);
    }

    @Override
    public boolean isConversing() {
        checkState();
        return delegate.isConversing();
    }

    @Override
    public void sendMessage(String[] arg0) {
        checkState();
        delegate.sendMessage(arg0);
    }

    @Override
    public Arrow shootArrow() {
        checkState();
        return delegate.shootArrow();
    }

    @Override
    public Egg throwEgg() {
        checkState();
        return delegate.throwEgg();
    }

    @Override
    public Snowball throwSnowball() {
        checkState();
        return delegate.throwSnowball();
    }

    @Override
    public boolean isFlying() {
        checkState();
        return delegate.isFlying();
    }

    @Override
    public void setFlying(boolean arg0) {
        checkState();
        delegate.setFlying(arg0);
    }

    @Override
    public boolean isBlocking() {
        checkState();
        return delegate.isBlocking();
    }

    @Override
    public void abandonConversation(Conversation arg0, ConversationAbandonedEvent arg1) {
        checkState();
        delegate.abandonConversation(arg0, arg1);
    }

    @Override
    public int getExpToLevel() {
        checkState();
        return delegate.getExpToLevel();
    }

    @Override
    public boolean hasLineOfSight(Entity arg0) {
        checkState();
        return delegate.hasLineOfSight(arg0);
    }

    @Override
    public boolean isValid() {
        checkState();
        return delegate.isValid();
    }

    @Override
    public float getFlySpeed() {
        checkState();
        return delegate.getFlySpeed();
    }

    @Override
    public float getWalkSpeed() {
        checkState();
        return delegate.getWalkSpeed();
    }

    @Override
    public void setFlySpeed(float arg0) throws IllegalArgumentException {
        checkState();
        delegate.setFlySpeed(arg0);
    }

    @Override
    public void setWalkSpeed(float arg0) throws IllegalArgumentException {
        checkState();
        delegate.setWalkSpeed(arg0);
    }

    @Override
    public void playSound(Location arg0, Sound arg1, float arg2, float arg3) {
        checkState();
        delegate.playSound(arg0, arg1, arg2, arg3);
    }

    @Override
    public Inventory getEnderChest() {
        checkState();
        return delegate.getEnderChest();
    }

    @Override
    public void giveExpLevels(int arg0) {
        checkState();
        delegate.giveExpLevels(arg0);
    }

    @Override
    public void setBedSpawnLocation(Location arg0, boolean arg1) {
        checkState();
        delegate.setBedSpawnLocation(arg0, arg1);
    }

    @Override
    public void setTexturePack(String arg0) {
        checkState();
        delegate.setTexturePack(arg0);
    }

    @Override
    public boolean getCanPickupItems() {
        checkState();
        return delegate.getCanPickupItems();
    }

    @Override
    public EntityEquipment getEquipment() {
        checkState();
        return delegate.getEquipment();
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        checkState();
        return delegate.getRemoveWhenFarAway();
    }

    @Override
    public void setCanPickupItems(boolean arg0) {
        checkState();
        delegate.setCanPickupItems(arg0);
    }

    @Override
    public void setRemoveWhenFarAway(boolean arg0) {
        checkState();
        delegate.setRemoveWhenFarAway(arg0);
    }

    @Override
    public Location getLocation(Location arg0) {
        checkState();
        return delegate.getLocation(arg0);
    }

    @Override
    public void resetMaxHealth() {
        checkState();
        delegate.resetMaxHealth();
    }

    @Override
    public void setMaxHealth(int arg0) {
        checkState();
        delegate.setMaxHealth(arg0);
    }

    @Override
    public String getCustomName() {
        checkState();
        return delegate.getCustomName();
    }

    @Override
    public boolean isCustomNameVisible() {
        checkState();
        return delegate.isCustomNameVisible();
    }

    @Override
    public void setCustomName(String arg0) {
        checkState();
        delegate.setCustomName(arg0);
    }

    @Override
    public void setCustomNameVisible(boolean arg0) {
        checkState();
        delegate.setCustomNameVisible(arg0);
    }

    @Override
    public WeatherType getPlayerWeather() {
        checkState();
        return delegate.getPlayerWeather();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOnGround() {
        checkState();
        return delegate.isOnGround();
    }

    @Override
    public void resetPlayerWeather() {
        checkState();
        delegate.resetPlayerWeather();
    }

    @Override
    public void setPlayerWeather(WeatherType arg0) {
        checkState();
        delegate.setPlayerWeather(arg0);
    }

    @Override
    public Scoreboard getScoreboard() {
        checkState();
        return delegate.getScoreboard();
    }

    @Override
    public void setScoreboard(Scoreboard arg0) throws IllegalArgumentException,	IllegalStateException {
        checkState();
        delegate.setScoreboard(arg0);
    }
}
