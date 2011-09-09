package info.tregmine.api;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
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
	public Vehicle getVehicle() {
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
	public Arrow shootArrow() {
		return delegate.shootArrow();
	}

	@Override
	public Egg throwEgg() {
		return delegate.throwEgg();
	}

	@Override
	public Snowball throwSnowball() {
		return delegate.throwSnowball();
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
}
