package info.tregmine.listeners;

import java.util.*;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.hanging.*;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import info.tregmine.Tregmine;
import info.tregmine.api.*;
import info.tregmine.api.util.ScoreboardClearTask;
import info.tregmine.events.PlayerLotChangeEvent;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.*;

public class ZonePlayerListener implements Listener
{
    private Tregmine plugin;

    public ZonePlayerListener(Tregmine instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        Location location = event.getBlockClicked().getLocation();

        if (!player.hasBlockPermission(location, true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        Location location = event.getBlock().getLocation();

        if (!player.hasBlockPermission(location, true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event)
    {
        Entity entity = event.getRemover();

        if (!(entity instanceof Player)) {
            return;
        }

        TregminePlayer player = plugin.getPlayer((Player) event.getRemover());

        if (!player.hasBlockPermission(player.getLocation(), true)) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
    	TregminePlayer player = plugin.getPlayer(event.getPlayer());
    	if(player == null){
    		return;
    	}
    	ItemStack item = player.getItemInHand();
    	if (item.getType() == Material.STICK) {
    		event.setCancelled(true);
    	
    	Block block = event.getBlock();
    	Point currentPos = new Point(block.getX(), block.getZ());
    	ZoneWorld world = plugin.getWorld(block.getWorld());
    	Zone zone = world.findZone(currentPos);
    	Lot lot = world.findLot(currentPos);
    	// within a zone, lots can be created by zone owners or people with
        // the zones permission.
        String type = null;
        if (zone != null) {
            Zone.Permission perm = zone.getUser(player);
            if (perm != Zone.Permission.Owner &&
                !player.getRank().canModifyZones()) {

                return;
            }
            if (lot != null) {
                player.sendMessage("This lot is called " + lot.getName() + ".");
                return;
            }
            type = "lot";
        }
        // outside of a zone
        else {
            // outside of any existing zone, this can only be used by people
            // with zones permission.
            if (!player.getRank().canModifyZones()) {
                return;
            }
            type = "zone";
        }
        player.setZoneBlock1(block);
        event.getPlayer().sendMessage(
                "First block set of new " + type + ".");
        if (zone != null) {
            player.setTargetZoneId(zone.getId());
        }
        else {
            player.setTargetZoneId(0);
        }}
    }
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event){
    	TregminePlayer player = plugin.getPlayer(event.getPlayer());
    	if(player == null){
    		return;
    	}
    	ItemStack item = player.getItemInHand();
    	if (item.getType() == Material.STICK) {
    		event.setCancelled(true);
    	Block block = event.getBlock();
    	Point currentPos = new Point(block.getX(), block.getZ());
    	ZoneWorld world = plugin.getWorld(block.getWorld());
    	Zone zone = world.findZone(currentPos);
    	Lot lot = world.findLot(currentPos);
    	// within a zone, lots can be created by zone owners or people with
        // the zones permission.
        String type = null;
        if (zone != null) {
            Zone.Permission perm = zone.getUser(player);
            if (perm != Zone.Permission.Owner &&
                !player.getRank().canModifyZones()) {

                return;
            }
            if (lot != null) {
                player.sendMessage("This lot is called " + lot.getName() + ".");
                return;
            }
            type = "lot";
        }
        // outside of a zone
        else {
            // outside of any existing zone, this can only be used by people
            // with zones permission.
            if (!player.getRank().canModifyZones()) {
                return;
            }
            type = "zone";
        }

        
        player.setZoneBlock1(block);
        event.getPlayer().sendMessage(
                "First block set of new " + type + ".");
        if (zone != null) {
            player.setTargetZoneId(zone.getId());
        }
        else {
            player.setTargetZoneId(0);
        }
    	}
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = player.getItemInHand();
        Block block = event.getClickedBlock();

        Point currentPos = new Point(block.getX(), block.getZ());

        ZoneWorld world = plugin.getWorld(block.getWorld());
        if (world == null) {
            return;
        }

        Zone zone = world.findZone(currentPos);
        Lot lot = world.findLot(currentPos);

        // Check if this is a lot - if so, limit items that can be blessed to
        // lot owner
        if (BlessedBlockListener.ALLOWED_MATERIALS.contains(block.getType()) &&
                !player.getRank().canModifyZones()) {
            if (lot == null) {
                return;
            }

            if (!lot.isOwner(player) && lot.hasFlag(Lot.Flags.AUTOBLESS)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Blessed to lot owners.");
            }
        }

        // Handle stick, for zone and lot creation
        if (item.getType() == Material.STICK) {
            // within a zone, lots can be created by zone owners or people with
            // the zones permission.
            String type = null;
            if (zone != null) {
                Zone.Permission perm = zone.getUser(player);
                if (perm != Zone.Permission.Owner &&
                    !player.getRank().canModifyZones()) {

                    return;
                }
                if (lot != null) {
                    player.sendMessage("This lot is called " + lot.getName() + ".");
                    return;
                }
                type = "lot";
            }
            // outside of a zone
            else {
                // outside of any existing zone, this can only be used by people
                // with zones permission.
                if (!player.getRank().canModifyZones()) {
                    return;
                }
                type = "zone";
            }

            int count;
            try {
                count = player.getZoneBlockCounter();
            } catch (Exception e) {
                count = 0;
            }
            int zf = player.getTargetZoneId();
            if (zf != 0 && zf != zone.getId()) {
                player.sendMessage("The full extent of the lot must be in the same zone.");
                return;
            }

            player.setZoneBlock2(block);
            player.sendMessage("Second block set of new " + type + ".");

        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void boneMealUsage(PlayerInteractEvent event){
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getItem() == null) return;
        if (!event.getItem().getType().equals(Material.INK_SACK)) return;
        if (event.getItem().getData().getData() != (byte) 15) return;

        Block b = event.getClickedBlock();
        if (b == null || !(b.getType().equals(Material.GRASS))) return;
        Location center = b.getLocation();

        event.setCancelled(true); // Stops normal bonemealing
        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            if (event.getItem().getAmount() == 1){
                event.getPlayer().getInventory().remove(event.getItem());
            }else{
                event.getItem().setAmount(event.getItem().getAmount() - 1);
            }
        }

        List<Integer> radius_options = Arrays.asList(3,4,5,6,7);
        Random radius_random = new Random();
        Integer radius = radius_options.get(radius_random.nextInt(radius_options.size()));
        List<Block> areaofeffect = new ArrayList<Block>();
        for (int X = -radius; X < radius; X++) {
            for (int Y = -(radius/2); Y < (radius/2); Y++) {
                for (int Z = -radius; Z < radius; Z++) {
                    if (Math.sqrt((X*X) + (Z*Z)) <= radius &&
                        Y + center.getBlockY() >= 0) {
                        Block block = b.getWorld().getBlockAt(X + center.getBlockX(),
                                                               Y + center.getBlockY(),
                                                               Z + center.getBlockZ());
                        areaofeffect.add(block);
                    }
                }
            }
        }

        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        for ( Block block : areaofeffect ) {
            Block block_under = block.getWorld().getBlockAt(block.getX(),
                                                            block.getY() - 1,
                                                            block.getZ());
            if (block_under.getType() != Material.GRASS) continue;
            if (block.getType() != Material.AIR) continue;

            if (player.hasBlockPermission(block.getLocation(), false)) {
                boneMealPlant(block);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void boneMealPlant(Block block){
        List<Material> bonemeal_produce = Arrays.asList(Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR,
                                                        Material.YELLOW_FLOWER,
                                                        Material.RED_ROSE,
                                                        Material.LONG_GRASS, Material.LONG_GRASS, Material.LONG_GRASS, Material.LONG_GRASS);
        Random bonemeal_random = new Random();
        Material produce = bonemeal_produce.get(bonemeal_random.nextInt(bonemeal_produce.size()));

        block.setType(produce);
        if(block.getType().equals(Material.LONG_GRASS)){
            block.setData((byte) 1);
        }
    }


    private void movePlayerBack(TregminePlayer player, Location movingFrom,
            Location movingTo)
    {
        Vector a = new Vector(movingFrom.getX(),
                movingFrom.getY(),
                movingFrom.getZ());

        Vector b = new Vector(movingTo.getX(),
                movingTo.getY(),
                movingTo.getZ());

        Vector diff = b.subtract(a);
        diff = diff.multiply(-5);

        Vector newPosVector = a.add(diff);

        Location newPos = new Location(player.getWorld(),
                newPosVector.getX(),
                newPosVector.getY(),
                newPosVector.getZ());

        player.teleportWithHorse(newPos);
    }

    // For Lot Flag: Lot.Flags.PRIVATE;
    @EventHandler
    public void onPlayerMoveLot(PlayerLotChangeEvent event)
    {
        if (event.getNew() == null) {
            return;
        }
        
        if (!event.getNew().hasFlag(Lot.Flags.FLIGHT_ALLOWED) &&
            !event.getPlayer().getRank().canModifyZones()) {

            event.getPlayer().setFlying(false);
        }

        // Owner of the lot so bypasses private flag.
        if (event.getNew().isOwner(event.getPlayer())) {
            return;
        }

        // Let admins bypass protection, but warn them just incase.
        if (event.getPlayer().getRank().canModifyZones()) {
            return;
        }

        // If not private flagged lot, then return.
        if (!event.getNew().hasFlag(Lot.Flags.PRIVATE)) {
            return;
        }

        movePlayerBack(event.getPlayer(), event.getFrom(), event.getTo());
        event.getPlayer().sendMessage(ChatColor.RED + "[" + event.getNew().getName() + "] "
                + "This lot is private to it's owners! Please contact the lot owners.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player == null) {
            event.getPlayer().kickPlayer("Something went wrong");
            Tregmine.LOGGER.info(event.getPlayer().getName() + " was not found " +
                    "in players map (PlayerMoveEvent).");
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());

        Location movingFrom = event.getFrom();
        Point oldPos =
                new Point(movingFrom.getBlockX(), movingFrom.getBlockZ());

        Location movingTo = event.getTo();
        Point currentPos =
                new Point(movingTo.getBlockX(), movingTo.getBlockZ());

        Zone currentZone = player.getCurrentZone();
        if (currentZone == null || !currentZone.contains(currentPos)) {

            if (currentZone != null && currentZone.contains(oldPos)) {
                player.setCurrentTexture("https://dl.dropbox.com/u/5405236/mc/df.zip");
                player.sendMessage(ChatColor.RED + "[" + currentZone.getName()
                        + "] " + currentZone.getTextExit());
            }

            currentZone = world.findZone(currentPos);
            if (currentZone != null) {
                Zone.Permission perm = currentZone.getUser(player);

                // if anyone is allowed to enter by default...
                if (currentZone.getEnterDefault()) {
                    // ...we only need to reject banned players
                    if (player.getRank().canModifyZones()) {
                        // never applies to admins
                    }
                    else if (perm != null && perm == Zone.Permission.Banned) {
                        bannedMessage(currentZone, player);
                        player.teleportWithHorse(player.getWorld().getSpawnLocation());
                        // movePlayerBack(player, movingFrom, movingTo);
                        return;
                    }
                    // If blockWarned is true, and they are either hardwarned
                    // or softwarned then get them out of there with an error.
                    else if (currentZone.hasFlag(Zone.Flags.BLOCK_WARNED) &&
                            (player.hasFlag(TregminePlayer.Flags.HARDWARNED) ||
                             player.hasFlag(TregminePlayer.Flags.SOFTWARNED))) {
                        blockedMessage(currentZone, player);
                        movePlayerBack(player, movingFrom, movingTo);
                        return;
                    }
                    else if (currentZone.hasFlag(Zone.Flags.ADMIN_ONLY) &&
                            (player.getRank() != Rank.JUNIOR_ADMIN &&
                             player.getRank() != Rank.SENIOR_ADMIN)) {
                        blockedMessage(currentZone, player);
                        movePlayerBack(player, movingFrom, movingTo);
                        return;
                    }
                    else if (currentZone.hasFlag(Zone.Flags.REQUIRE_RESIDENCY) &&
                            (player.getRank() == Rank.TOURIST ||
                             player.getRank() == Rank.SETTLER ||
                             player.getRank() == Rank.UNVERIFIED)) {
                        blockedMessage(currentZone, player);
                        movePlayerBack(player, movingFrom, movingTo);
                        return;
                    }
                }
                // if this is a whitelist zone...
                else {
                    // ...reject people not in the user list, as well as banned
                    // people
                    if (player.getRank().canModifyZones()) {
                        // never applies to admins
                    }
                    else if (perm == null) {
                        disallowedMessage(currentZone, player);
                        movePlayerBack(player, movingFrom, movingTo);
                        return;
                    }
                    else if (perm == Zone.Permission.Banned) {
                        bannedMessage(currentZone, player);
                        player.teleportWithHorse(player.getWorld().getSpawnLocation());
                        return;
                    }
                }

                welcomeMessage(currentZone, player, perm);

            }
            player.setCurrentZone(currentZone);
        }
    }


    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        ZoneWorld world = plugin.getWorld(player.getWorld());
        World cWorld = player.getWorld();
        
        String[] worldNamePortions = cWorld.getName().split("_");

        if (worldNamePortions[0].equalsIgnoreCase("world")) {
            player.loadInventory("survival", true);
        } else {
            player.loadInventory(worldNamePortions[0], true);
        }
        
        if (cWorld.getName().equalsIgnoreCase(plugin.getRulelessWorld().getName()) &&
                (!player.getRank().canBypassWorld() && player.getGameMode() != GameMode.CREATIVE)) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }

        Location movingTo = player.getLocation();
        Point currentPos =
                new Point(movingTo.getBlockX(), movingTo.getBlockZ());

        Zone currentZone = null;

        if (currentZone == null || !currentZone.contains(currentPos)) {

            currentZone = world.findZone(currentPos);
            if (currentZone != null) {
                Zone.Permission perm = currentZone.getUser(player);

                if (currentZone.getEnterDefault()) {
                    if (player.getRank().canModifyZones()) {
                        // never applies to admins
                    }
                    else if (perm != null && perm == Zone.Permission.Banned) {
                        bannedMessage(currentZone, player);
                        player.teleportWithHorse(this.plugin.getServer()
                                .getWorld("world").getSpawnLocation());
                        player.sendMessage(ChatColor.RED
                                + "You are not allowed in this zone");
                        return;
                    }
                }
                else {
                    if (player.getRank().canModifyZones()) {
                        // never applies to admins
                    }
                    else if (perm == null) {
                        disallowedMessage(currentZone, player);
                        player.teleportWithHorse(this.plugin.getServer()
                                .getWorld("world").getSpawnLocation());
                        player.sendMessage(ChatColor.RED
                                + "You are not allowed in this zone");
                        return;
                    }
                    else if (perm == Zone.Permission.Banned) {
                        bannedMessage(currentZone, player);
                        player.teleportWithHorse(this.plugin.getServer()
                                .getWorld("world").getSpawnLocation());
                        player.sendMessage(ChatColor.RED
                                + "You are not allowed in this zone");
                        return;
                    }
                }

                if (currentZone.isPvp() && !player.getRank().canModifyZones()) {
                    player.teleportWithHorse(this.plugin.getServer().getWorld("world")
                            .getSpawnLocation());
                    player.sendMessage(ChatColor.RED
                            + "You are not allowed in this zone");
                    return;
                }
                welcomeMessage(currentZone, player, perm);
            }
            player.setCurrentZone(currentZone);
        }
    }
    
    public void inventoryOpening(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null || 
                !event.getClickedBlock().getType().equals(Material.CHEST)) return;
        
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (!player.getRank().canForceOpenChests()) return;
        
        if (event.getClickedBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getClickedBlock().getState();
            
            // Check the above block is solid, if it isn't then stop - This will stop forcing the
            // chest when you can open it normally anyway.
            Block blockAbove = player.getWorld().getBlockAt(chest.getLocation().add(new Vector(0, 1, 0)));
            if (blockAbove == null || blockAbove.getType().isTransparent()) return;
            
            player.sendMessage(ChatColor.GREEN + "Force opened inventory!");
            player.openInventory(chest.getBlockInventory());
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player == null) {
            event.setCancelled(true);
            return;
        }

        Location src = event.getFrom();
        World srcWorld = src.getWorld();
        Location dst = event.getTo();
        World dstWorld = dst.getWorld();

        if (event.getCause().equals(TeleportCause.END_PORTAL)) {
            Tregmine.LOGGER.info("Portal by: " + player.getName());

            int nrEnt = event.getTo().getWorld().getLivingEntities().size();
            int max = 1000;
            int newEnt = max - nrEnt;

            if (nrEnt <= max) {
                for (int i = 0; i < newEnt; i++) {
                    dstWorld.spawnEntity(dst, EntityType.GHAST);
                }
            }
        }

        if ("world_the_end".equals(dstWorld.getName()) && !player.isOp()) {
            player.sendMessage(ChatColor.RED
                    + "You can't teleport to someone in The End");
            event.setCancelled(true);
            return;
        }

        if (srcWorld.getName().equals(dstWorld.getName())) {
            ZoneWorld world = plugin.getWorld(player.getWorld());

            Point oldPos = new Point(src.getBlockX(), src.getBlockZ());
            Point currentPos = new Point(dst.getBlockX(), dst.getBlockZ());

            Zone currentZone = player.getCurrentZone();

            if (currentZone == null || !currentZone.contains(currentPos)) {

                if (currentZone != null && currentZone.contains(oldPos)) {
                    player.setCurrentTexture("https://dl.dropbox.com/u/5405236/mc/df.zip");
                    player.sendMessage(currentZone.getTextExit());
                }

                currentZone = world.findZone(currentPos);
                if (currentZone != null) {
                    Zone.Permission perm = currentZone.getUser(player);

                    if (currentZone.getEnterDefault()) {
                        if (player.getRank().canModifyZones()) {
                            // never applies to admins
                        }
                        else if (perm != null && perm == Zone.Permission.Banned) {
                            bannedMessage(currentZone, player);
                            event.setCancelled(true);
                            return;
                        }
                    }
                    else {
                        if (player.getRank().canModifyZones()) {
                            // never applies to admins
                        }
                        else if (perm == null) {
                            disallowedMessage(currentZone, player);
                            event.setCancelled(true);
                            return;
                        }
                        else if (perm == Zone.Permission.Banned) {
                            bannedMessage(currentZone, player);
                            event.setCancelled(true);
                            return;
                        }
                    }

                    if (currentZone.isPvp() && !player.getRank().canModifyZones()) {
                        event.setCancelled(true);
                        return;
                    }

                    welcomeMessage(currentZone, player, perm);
                }
                player.setCurrentZone(currentZone);
            }
        }
    }

    private void disallowedMessage(Zone currentZone, TregminePlayer player)
    {
        player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] "
                + "You are not allowed in this zone. Contact the zone owner.");
    }

    private void bannedMessage(Zone currentZone, TregminePlayer player)
    {
        player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] "
                + "You are banned from " + currentZone.getName() + ".");
    }

    private void blockedMessage(Zone currentZone, TregminePlayer player)
    {
        player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] "
                + "You are blocked from " + currentZone.getName() + ".");
    }

    private void welcomeMessage(Zone currentZone, TregminePlayer player,
            Zone.Permission perm)
    {
        if (player.getChatState() == TregminePlayer.ChatState.SETUP) {
            return;
        }

        if (currentZone.getTexture() == "") {
            if (currentZone.isPvp()) {
                player.setCurrentTexture("https://dl.dropbox.com/u/5405236/mc/pvp.zip");
            }
            else {
                player.setCurrentTexture("https://dl.dropbox.com/u/5405236/mc/df.zip");
            }

        }
        else {
            player.setCurrentTexture(currentZone.getTexture());
        }

        /*
         * String text = ""; if (currentZone.getMainOwner() != null) { text =
         * " MainOwner:" + currentZone.getMainOwner(); }
         */

        if (player.isOnline()) {

            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();

            String zoneName = currentZone.getName();
            int maxLen = 13;
            if (zoneName.length() > maxLen) {
                zoneName = zoneName.substring(1, maxLen);
            }

            Objective objective = board.registerNewObjective(zoneName, "2");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(ChatColor.AQUA + zoneName);

            String mainOwner = currentZone.getMainOwner();
            if (mainOwner == null || "".equals(mainOwner)) {
                mainOwner = "N/A";
            }
            else if (mainOwner.length() > maxLen) {
                mainOwner = mainOwner.substring(1, maxLen);
            }

            OfflinePlayer fakePlayer = null;
            if (currentZone.getMainOwner() != null) {
                fakePlayer = Bukkit.getOfflinePlayer(ChatColor.GOLD + mainOwner);
            }
            else {
                fakePlayer = Bukkit.getOfflinePlayer("Unknown");
            }

            Score score = objective.getScore(fakePlayer);
            score.setScore(currentZone.getId());

            try {
                player.setScoreboard(board);

                ScoreboardClearTask.start(plugin, player);
            } catch (IllegalStateException e) {
                // ignore
            }
        }

        player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] "
                + currentZone.getTextEnter());

        if (currentZone.isPvp()) {
            player.sendMessage(ChatColor.RED
                    + "[" + currentZone.getName() + "] "
                    + "Warning! This is a PVP zone! Other players can damage or kill you here.");
        }

        if (currentZone.hasPublicProfile()){
            player.sendMessage(ChatColor.DARK_RED + currentZone.getName() + " has a public profile! You can view it here:");
            player.sendMessage(ChatColor.GRAY + "http://treg.co/index.php/zone/profile?id=" + currentZone.getId());
        }

        if (perm != null) {
            String permNotification = perm.getPermissionNotification();
            player.sendMessage(ChatColor.RED + "[" + currentZone.getName()
                    + "] " + permNotification);
        }
    }
}
