package info.tregmine.debug;

import java.util.*;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.world.*;
import org.bukkit.plugin.java.JavaPlugin;

public class EventDebug extends JavaPlugin implements Listener
{
    List<Player> debugPlayers = new ArrayList<Player>();
    private Set<Class> allEvents = new HashSet<Class>();
    private Set<Class> disabledEvents = new HashSet<Class>();
    Logger logger = Logger.getLogger("Minecraft");

    public EventDebug()
    {
        allEvents.add(BlockPistonExtendEvent.class);
        allEvents.add(LeavesDecayEvent.class);
        allEvents.add(BlockRedstoneEvent.class);
        allEvents.add(NotePlayEvent.class);
        allEvents.add(BlockExpEvent.class);
        allEvents.add(BlockFromToEvent.class);
        allEvents.add(BlockGrowEvent.class);
        allEvents.add(BlockPhysicsEvent.class);
        allEvents.add(BlockPistonRetractEvent.class);
        allEvents.add(BlockBreakEvent.class);
        allEvents.add(BlockCanBuildEvent.class);
        allEvents.add(BlockPlaceEvent.class);
        allEvents.add(BlockSpreadEvent.class);
        allEvents.add(BlockDispenseEvent.class);
        allEvents.add(BlockDamageEvent.class);
        allEvents.add(BlockBurnEvent.class);
        allEvents.add(BlockFormEvent.class);
        allEvents.add(BlockIgniteEvent.class);
        allEvents.add(SignChangeEvent.class);
        allEvents.add(BlockFadeEvent.class);
        allEvents.add(EntityBlockFormEvent.class);
        allEvents.add(ChunkUnloadEvent.class);
        allEvents.add(WorldSaveEvent.class);
        allEvents.add(StructureGrowEvent.class);
        allEvents.add(SpawnChangeEvent.class);
        allEvents.add(WorldUnloadEvent.class);
        allEvents.add(ChunkPopulateEvent.class);
        allEvents.add(WorldInitEvent.class);
        allEvents.add(PortalCreateEvent.class);
        allEvents.add(ChunkLoadEvent.class);
        allEvents.add(WorldLoadEvent.class);
        allEvents.add(HangingBreakEvent.class);
        allEvents.add(HangingBreakByEntityEvent.class);
        allEvents.add(HangingPlaceEvent.class);
        allEvents.add(AsyncPlayerPreLoginEvent.class);
        allEvents.add(PlayerUnleashEntityEvent.class);
        allEvents.add(InventoryClickEvent.class);
        allEvents.add(InventoryPickupItemEvent.class);
        allEvents.add(CraftItemEvent.class);
        allEvents.add(InventoryMoveItemEvent.class);
        allEvents.add(FurnaceBurnEvent.class);
        allEvents.add(InventoryDragEvent.class);
        allEvents.add(InventoryCloseEvent.class);
        allEvents.add(FurnaceExtractEvent.class);
        allEvents.add(InventoryOpenEvent.class);
        allEvents.add(InventoryEvent.class);
        allEvents.add(PrepareItemCraftEvent.class);
        allEvents.add(BrewEvent.class);
        allEvents.add(InventoryCreativeEvent.class);
        allEvents.add(FurnaceSmeltEvent.class);
        allEvents.add(VehicleEntityCollisionEvent.class);
        allEvents.add(VehicleDestroyEvent.class);
        allEvents.add(VehicleBlockCollisionEvent.class);
        allEvents.add(VehicleExitEvent.class);
        allEvents.add(VehicleCreateEvent.class);
        allEvents.add(VehicleEnterEvent.class);
        allEvents.add(VehicleDamageEvent.class);
        allEvents.add(VehicleUpdateEvent.class);
        allEvents.add(VehicleMoveEvent.class);
        allEvents.add(PluginDisableEvent.class);
        allEvents.add(ServerListPingEvent.class);
        allEvents.add(RemoteServerCommandEvent.class);
        allEvents.add(MapInitializeEvent.class);
        allEvents.add(ServerCommandEvent.class);
        allEvents.add(ServiceUnregisterEvent.class);
        allEvents.add(ServiceRegisterEvent.class);
        allEvents.add(PluginEnableEvent.class);
        allEvents.add(WeatherChangeEvent.class);
        allEvents.add(ThunderChangeEvent.class);
        allEvents.add(LightningStrikeEvent.class);
        allEvents.add(PrepareItemEnchantEvent.class);
        allEvents.add(EnchantItemEvent.class);
        allEvents.add(EntityDamageByBlockEvent.class);
        allEvents.add(PigZapEvent.class);
        allEvents.add(EntityExplodeEvent.class);
        allEvents.add(EntityPortalExitEvent.class);
        allEvents.add(SheepDyeWoolEvent.class);
        allEvents.add(ExpBottleEvent.class);
        allEvents.add(ProjectileLaunchEvent.class);
        allEvents.add(EntityDamageByEntityEvent.class);
        allEvents.add(EntityCreatePortalEvent.class);
        allEvents.add(EntityTeleportEvent.class);
        allEvents.add(EntityPortalEvent.class);
        allEvents.add(EntityCombustByEntityEvent.class);
        allEvents.add(PotionSplashEvent.class);
        allEvents.add(PlayerDeathEvent.class);
        allEvents.add(EntityDamageEvent.class);
        allEvents.add(EntityTargetEvent.class);
        allEvents.add(SheepRegrowWoolEvent.class);
        allEvents.add(CreatureSpawnEvent.class);
        allEvents.add(EntityShootBowEvent.class);
        allEvents.add(EntityBreakDoorEvent.class);
        allEvents.add(EntityCombustByBlockEvent.class);
        allEvents.add(EntityTargetLivingEntityEvent.class);
        allEvents.add(EntityDeathEvent.class);
        allEvents.add(ProjectileHitEvent.class);
        allEvents.add(ItemSpawnEvent.class);
        allEvents.add(CreeperPowerEvent.class);
        allEvents.add(EntityTameEvent.class);
        allEvents.add(ExplosionPrimeEvent.class);
        allEvents.add(HorseJumpEvent.class);
        allEvents.add(ItemDespawnEvent.class);
        allEvents.add(EntityRegainHealthEvent.class);
        allEvents.add(EntityUnleashEvent.class);
        allEvents.add(FoodLevelChangeEvent.class);
        allEvents.add(EntityCombustEvent.class);
        allEvents.add(EntityPortalEnterEvent.class);
        allEvents.add(EntityChangeBlockEvent.class);
        allEvents.add(SlimeSplitEvent.class);
        allEvents.add(EntityInteractEvent.class);
        allEvents.add(PlayerLeashEntityEvent.class);
        allEvents.add(PlayerItemHeldEvent.class);
        allEvents.add(PlayerBedLeaveEvent.class);
        allEvents.add(PlayerPortalEvent.class);
        allEvents.add(PlayerChangedWorldEvent.class);
        allEvents.add(PlayerInteractEntityEvent.class);
        allEvents.add(PlayerShearEntityEvent.class);
        allEvents.add(PlayerItemConsumeEvent.class);
        allEvents.add(PlayerTeleportEvent.class);
        allEvents.add(PlayerCommandPreprocessEvent.class);
        allEvents.add(PlayerMoveEvent.class);
        allEvents.add(PlayerQuitEvent.class);
        allEvents.add(PlayerItemBreakEvent.class);
        allEvents.add(AsyncPlayerChatEvent.class);
        allEvents.add(PlayerEditBookEvent.class);
        allEvents.add(PlayerBucketFillEvent.class);
        allEvents.add(PlayerEggThrowEvent.class);
        allEvents.add(PlayerVelocityEvent.class);
        allEvents.add(PlayerLoginEvent.class);
        allEvents.add(PlayerRespawnEvent.class);
        allEvents.add(PlayerBucketEmptyEvent.class);
        allEvents.add(PlayerToggleFlightEvent.class);
        allEvents.add(PlayerExpChangeEvent.class);
        allEvents.add(PlayerBedEnterEvent.class);
        allEvents.add(PlayerInteractEvent.class);
        allEvents.add(PlayerToggleSneakEvent.class);
        allEvents.add(PlayerPickupItemEvent.class);
        allEvents.add(PlayerDropItemEvent.class);
        allEvents.add(PlayerGameModeChangeEvent.class);
        allEvents.add(PlayerAnimationEvent.class);
        allEvents.add(PlayerKickEvent.class);
        allEvents.add(PlayerJoinEvent.class);
        allEvents.add(PlayerToggleSprintEvent.class);
        allEvents.add(PlayerLevelChangeEvent.class);
        allEvents.add(PlayerFishEvent.class);
        allEvents.add(PlayerChatTabCompleteEvent.class);
        allEvents.add(PlayerUnregisterChannelEvent.class);
        allEvents.add(PlayerRegisterChannelEvent.class);
    }
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        
        getCommand("debug").setExecutor(this);
        
        disabledEvents.add(VehicleUpdateEvent.class);
        disabledEvents.add(PlayerMoveEvent.class);
        disabledEvents.add(BlockPhysicsEvent.class);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) {
            return false;
        }

        if ("debug".equalsIgnoreCase(command.getName())) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (debugPlayers.contains(player)) {
                    debugPlayers.remove(player);
                    player.sendMessage(ChatColor.GREEN + "[DEBUG] Stopped debugging");
                    logger.info(player.getDisplayName() + " is no longer debugging");
                } else {
                    debugPlayers.add(player);
                    player.sendMessage(ChatColor.GREEN + "[DEBUG] Now debugging");
                    logger.info(player.getDisplayName() + " is now debugging");
                }
                return true;
            } else if (args.length == 2 && "add".equalsIgnoreCase(args[0])) {
                enableEvent(args[1]);
                player.sendMessage(ChatColor.DARK_AQUA + "Enabled: " + args[1]);
                return true;
            } else if (args.length == 2 && "remove".equalsIgnoreCase(args[0])) {
                disableEvent(args[1]);
                player.sendMessage(ChatColor.DARK_AQUA + "Disabled: " + args[1]);
                return true;
            } else if (args.length == 1 && "list".equalsIgnoreCase(args[0])) {
                if (disabledEvents.size() > 0) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Listing disabled events:");
                    for (Class value : disabledEvents) {
                        player.sendMessage(ChatColor.AQUA + value.getSimpleName());
                    }
                } else {
                    player.sendMessage(ChatColor.DARK_AQUA + "No disabled events!");
                }
                return true;
            }
        }
        return false;
    }

    public Class findEvent(String name)
    {
        for (Class event : allEvents) {
            if (name.equalsIgnoreCase(event.getSimpleName())) {
                return event;
            }
        }
        return null;
    }

    public void enableEvent(String name)
    {
        Class event = findEvent(name);
        disabledEvents.remove(event);
    }

    public void disableEvent(String name)
    {
        Class event = findEvent(name);
        disabledEvents.add(event);
    }

    @EventHandler
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockPistonExtendEvent has been triggered!");
        }
    }

    @EventHandler
    public void onLeavesDecayEvent(LeavesDecayEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("LeavesDecayEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockRedstoneEvent(BlockRedstoneEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockRedstoneEvent has been triggered!");
        }
    }

    @EventHandler
    public void onNotePlayEvent(NotePlayEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("NotePlayEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockExpEvent(BlockExpEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockExpEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockFromToEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockGrowEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockPhysicsEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockPistonRetractEvent(BlockPistonRetractEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockPistonRetractEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockBreakEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockCanBuildEvent(BlockCanBuildEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockCanBuildEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockPlaceEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockSpreadEvent(BlockSpreadEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockSpreadEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockDispenseEvent(BlockDispenseEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockDispenseEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockDamageEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockBurnEvent(BlockBurnEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockBurnEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockFormEvent(BlockFormEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockFormEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockIgniteEvent has been triggered!");
        }
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("SignChangeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBlockFadeEvent(BlockFadeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BlockFadeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityBlockFormEvent(EntityBlockFormEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityBlockFormEvent has been triggered!");
        }
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ChunkUnloadEvent has been triggered!");
        }
    }

    @EventHandler
    public void onWorldSaveEvent(WorldSaveEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("WorldSaveEvent has been triggered!");
        }
    }

    @EventHandler
    public void onStructureGrowEvent(StructureGrowEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("StructureGrowEvent has been triggered!");
        }
    }

    @EventHandler
    public void onSpawnChangeEvent(SpawnChangeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("SpawnChangeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onWorldUnloadEvent(WorldUnloadEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("WorldUnloadEvent has been triggered!");
        }
    }

    @EventHandler
    public void onChunkPopulateEvent(ChunkPopulateEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ChunkPopulateEvent has been triggered!");
        }
    }

    @EventHandler
    public void onWorldInitEvent(WorldInitEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("WorldInitEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPortalCreateEvent(PortalCreateEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PortalCreateEvent has been triggered!");
        }
    }

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ChunkLoadEvent has been triggered!");
        }
    }

    @EventHandler
    public void onWorldLoadEvent(WorldLoadEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("WorldLoadEvent has been triggered!");
        }
    }

    @EventHandler
    public void onHangingBreakEvent(HangingBreakEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("HangingBreakEvent has been triggered!");
        }
    }

    @EventHandler
    public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("HangingBreakByEntityEvent has been triggered!");
        }
    }

    @EventHandler
    public void onHangingPlaceEvent(HangingPlaceEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("HangingPlaceEvent has been triggered!");
        }
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("AsyncPlayerPreLoginEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPlayerUnleashEntityEvent(PlayerUnleashEntityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PlayerUnleashEntityEvent has been triggered!");
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("InventoryClickEvent has been triggered!");
        }
    }

    @EventHandler
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("InventoryPickupItemEvent has been triggered!");
        }
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("CraftItemEvent has been triggered!");
        }
    }

    @EventHandler
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("InventoryMoveItemEvent has been triggered!");
        }
    }

    @EventHandler
    public void onFurnaceBurnEvent(FurnaceBurnEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("FurnaceBurnEvent has been triggered!");
        }
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("InventoryDragEvent has been triggered!");
        }
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("InventoryCloseEvent has been triggered!");
        }
    }

    @EventHandler
    public void onFurnaceExtractEvent(FurnaceExtractEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("FurnaceExtractEvent has been triggered!");
        }
    }

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("InventoryOpenEvent has been triggered!");
        }
    }

    @EventHandler
    public void onInventoryEvent(InventoryEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("InventoryEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPrepareItemCraftEvent(PrepareItemCraftEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PrepareItemCraftEvent has been triggered!");
        }
    }

    @EventHandler
    public void onBrewEvent(BrewEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("BrewEvent has been triggered!");
        }
    }

    @EventHandler
    public void onInventoryCreativeEvent(InventoryCreativeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("InventoryCreativeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onFurnaceSmeltEvent(FurnaceSmeltEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("FurnaceSmeltEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleEntityCollisionEvent(VehicleEntityCollisionEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleEntityCollisionEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleDestroyEvent(VehicleDestroyEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleDestroyEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleBlockCollisionEvent(VehicleBlockCollisionEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleBlockCollisionEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleExitEvent(VehicleExitEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleExitEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleCreateEvent(VehicleCreateEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleCreateEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleEnterEvent(VehicleEnterEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleEnterEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleDamageEvent(VehicleDamageEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleDamageEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleUpdateEvent(VehicleUpdateEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleUpdateEvent has been triggered!");
        }
    }

    @EventHandler
    public void onVehicleMoveEvent(VehicleMoveEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("VehicleMoveEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPluginDisableEvent(PluginDisableEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PluginDisableEvent has been triggered!");
        }
    }

    @EventHandler
    public void onServerListPingEvent(ServerListPingEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ServerListPingEvent has been triggered!");
        }
    }

    @EventHandler
    public void onRemoteServerCommandEvent(RemoteServerCommandEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("RemoteServerCommandEvent has been triggered!");
        }
    }

    @EventHandler
    public void onMapInitializeEvent(MapInitializeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("MapInitializeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onServerCommandEvent(ServerCommandEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ServerCommandEvent has been triggered!");
        }
    }

    @EventHandler
    public void onServiceUnregisterEvent(ServiceUnregisterEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ServiceUnregisterEvent has been triggered!");
        }
    }

    @EventHandler
    public void onServiceRegisterEvent(ServiceRegisterEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ServiceRegisterEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPluginEnableEvent(PluginEnableEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PluginEnableEvent has been triggered!");
        }
    }

    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("WeatherChangeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onThunderChangeEvent(ThunderChangeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ThunderChangeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onLightningStrikeEvent(LightningStrikeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("LightningStrikeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PrepareItemEnchantEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EnchantItemEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityDamageByBlockEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPigZapEvent(PigZapEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PigZapEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityExplodeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityPortalExitEvent(EntityPortalExitEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityPortalExitEvent has been triggered!");
        }
    }

    @EventHandler
    public void onSheepDyeWoolEvent(SheepDyeWoolEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("SheepDyeWoolEvent has been triggered!");
        }
    }

    @EventHandler
    public void onExpBottleEvent(ExpBottleEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ExpBottleEvent has been triggered!");
        }
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ProjectileLaunchEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityDamageByEntityEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityCreatePortalEvent(EntityCreatePortalEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityCreatePortalEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityTeleportEvent(EntityTeleportEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityTeleportEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityPortalEvent(EntityPortalEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityPortalEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityCombustByEntityEvent(EntityCombustByEntityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityCombustByEntityEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PotionSplashEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PlayerDeathEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityDamageEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityTargetEvent(EntityTargetEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityTargetEvent has been triggered!");
        }
    }

    @EventHandler
    public void onSheepRegrowWoolEvent(SheepRegrowWoolEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("SheepRegrowWoolEvent has been triggered!");
        }
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("CreatureSpawnEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityShootBowEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityBreakDoorEvent(EntityBreakDoorEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityBreakDoorEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityCombustByBlockEvent(EntityCombustByBlockEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityCombustByBlockEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityTargetLivingEntityEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityDeathEvent has been triggered!");
        }
    }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ProjectileHitEvent has been triggered!");
        }
    }

    @EventHandler
    public void onItemSpawnEvent(ItemSpawnEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ItemSpawnEvent has been triggered!");
        }
    }

    @EventHandler
    public void onCreeperPowerEvent(CreeperPowerEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("CreeperPowerEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityTameEvent(EntityTameEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityTameEvent has been triggered!");
        }
    }

    @EventHandler
    public void onExplosionPrimeEvent(ExplosionPrimeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ExplosionPrimeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onHorseJumpEvent(HorseJumpEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("HorseJumpEvent has been triggered!");
        }
    }

    @EventHandler
    public void onItemDespawnEvent(ItemDespawnEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("ItemDespawnEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityRegainHealthEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityUnleashEvent(EntityUnleashEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityUnleashEvent has been triggered!");
        }
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("FoodLevelChangeEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityCombustEvent(EntityCombustEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityCombustEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityPortalEnterEvent(EntityPortalEnterEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityPortalEnterEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityChangeBlockEvent has been triggered!");
        }
    }

    @EventHandler
    public void onSlimeSplitEvent(SlimeSplitEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("SlimeSplitEvent has been triggered!");
        }
    }

    @EventHandler
    public void onEntityInteractEvent(EntityInteractEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("EntityInteractEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        for (Player player : debugPlayers) {
            player.sendMessage("PlayerLeashEntityEvent has been triggered!");
        }
    }

    @EventHandler
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerItemHeldEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerBedLeaveEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerPortalEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerChangedWorldEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerInteractEntityEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerShearEntityEvent(PlayerShearEntityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerShearEntityEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerItemConsumeEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerTeleportEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerCommandPreprocessEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerMoveEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerQuitEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerItemBreakEvent(PlayerItemBreakEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerItemBreakEvent has been triggered!");
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("AsyncPlayerChatEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerEditBookEvent(PlayerEditBookEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerEditBookEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerBucketFillEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerEggThrowEvent(PlayerEggThrowEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerEggThrowEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerVelocityEvent(PlayerVelocityEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerVelocityEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerLoginEvent(PlayerLoginEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerLoginEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerRespawnEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerBucketEmptyEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerToggleFlightEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerExpChangeEvent(PlayerExpChangeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerExpChangeEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerBedEnterEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerInteractEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerToggleSneakEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerPickupItemEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerDropItemEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerGameModeChangeEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerAnimationEvent(PlayerAnimationEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerAnimationEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerKickEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerJoinEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerToggleSprintEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerLevelChangeEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerFishEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerChatTabCompleteEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerUnregisterChannelEvent(PlayerUnregisterChannelEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerUnregisterChannelEvent has been triggered!");
    }

    @EventHandler
    public void onPlayerRegisterChannelEvent(PlayerRegisterChannelEvent event)
    {
        if (disabledEvents.contains(event.getClass())) {
            return;
        }
        if (!debugPlayers.contains(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage("PlayerRegisterChannelEvent has been triggered!");
    }
}
