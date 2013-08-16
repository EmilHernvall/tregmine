package info.tregmine.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.quadtree.Point;
import info.tregmine.zones.Lot;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import info.tregmine.api.util.ScoreboardClearTask;

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
        if (player.getRank().canModifyZones()) {
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());

        Block block = event.getBlockClicked();
        Location location = block.getLocation();
        Point pos = new Point(location.getBlockX(), location.getBlockZ());

        Zone currentZone = player.getCurrentZone();
        if (currentZone == null || !currentZone.contains(pos)) {
            currentZone = world.findZone(pos);
            player.setCurrentZone(currentZone);
        }

        if (currentZone != null) {
            Zone.Permission perm = currentZone.getUser(player.getName());

            Lot lot = world.findLot(pos);
            if (lot != null) {
                if (perm == Zone.Permission.Owner && currentZone.isCommunist()) {
                    // Zone owners can modify lots in communist zones
                }
                else if (lot.isOwner(player.getName())) {
                    // Lot owners can always modify lots
                }
                else {
                    player.sendMessage(ChatColor.RED +
                            "[" + currentZone.getName() + "] " +
                            "You are not allowed to break blocks in lot " +
                            lot.getName() + ".");
                    event.setCancelled(true);
                    return;
                }

                return;
            }

            // if everyone is allowed to build in this zone...
            if (currentZone.getDestroyDefault()) {
                // ...the only people that can't build are those that are banned
                if (perm != null && perm == Zone.Permission.Banned) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are banned from " + currentZone.getName()
                            + ".");
                }
            }
            // if this zone has limited building privileges...
            else {
                // ...we only allow builders and owners to make changes.
                if (perm == null
                        || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
                    player.setFireTicks(50);
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are not allowed to break blocks in "
                            + currentZone.getName() + ".");
                }
            }
        }

    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player.getRank().canModifyZones()) {
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());

        Location location = event.getBlock().getLocation();
        Point pos = new Point(location.getBlockX(), location.getBlockZ());

        Zone currentZone = player.getCurrentZone();
        if (currentZone == null || !currentZone.contains(pos)) {
            currentZone = world.findZone(pos);
            player.setCurrentZone(currentZone);
        }

        if (currentZone != null) {
            Zone.Permission perm = currentZone.getUser(player.getName());

            Lot lot = world.findLot(pos);
            if (lot != null) {
                if (perm == Zone.Permission.Owner && currentZone.isCommunist()) {
                    // Zone owners can modify lots in communist zones
                }
                else if (lot.isOwner(player.getName())) {
                    // Lot owners can always modify lots
                }
                else {
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are not allowed to place paintings in lot "
                            + lot.getName() + ".");
                    event.setCancelled(true);
                    return;
                }

                return;
            }

            // if everyone is allowed to build in this zone...
            if (currentZone.getDestroyDefault()) {
                // ...the only people that can't build are those that are banned
                if (perm != null && perm == Zone.Permission.Banned) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are banned from " + currentZone.getName()
                            + ".");
                }
            }
            // if this zone has limited building privileges...
            else {
                // ...we only allow builders and owners to make changes.
                if (perm == null
                        || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
                    player.setFireTicks(50);
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are not allowed to place paintings in "
                            + currentZone.getName() + ".");
                }
            }
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
        if (player.getRank().canModifyZones()) {
            return;
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());

        Location location = player.getLocation();
        Point pos = new Point(location.getBlockX(), location.getBlockZ());

        Zone currentZone = player.getCurrentZone();
        if (currentZone == null || !currentZone.contains(pos)) {
            currentZone = world.findZone(pos);
            player.setCurrentZone(currentZone);
        }

        if (currentZone != null) {
            Zone.Permission perm;

            try {
                perm = currentZone.getUser(player.getName());
            } catch (Exception e) {
                player.sendMessage("ERROR");
                e.printStackTrace();
                return;
            }

            Lot lot = world.findLot(pos);
            if (lot != null) {
                if (perm == Zone.Permission.Owner && currentZone.isCommunist()) {
                    // Zone owners can modify lots in communist zones
                }
                else if (lot.isOwner(player.getName())) {
                    // Lot owners can always modify lots
                }
                else {
                    player.sendMessage(ChatColor.RED +
                            "[" + currentZone.getName() + "] " +
                            "You are not allowed to destroy paintings in lot " +
                            lot.getName() + ".");
                    event.setCancelled(true);
                    return;
                }

                return;
            }

            // if everyone is allowed to build in this zone...
            if (currentZone.getDestroyDefault()) {
                // ...the only people that can't build are those that are banned
                if (perm != null && perm == Zone.Permission.Banned) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are banned from " + currentZone.getName()
                            + ".");
                }
            }
            // if this zone has limited building privileges...
            else {
                // ...we only allow builders and owners to make changes.
                if (perm == null
                        || (perm != Zone.Permission.Maker && perm != Zone.Permission.Owner)) {
                    player.setFireTicks(50);
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "["
                            + currentZone.getName() + "] "
                            + "You are not allowed to destroy paintings in "
                            + currentZone.getName() + ".");
                }
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

        // Handle stick, for zone and lot creation
        if (item.getType() == Material.STICK) {
            // within a zone, lots can be created by zone owners or people with
            // the zones permission.
            String type = null;
            if (zone != null) {
                Zone.Permission perm = zone.getUser(player.getName());
                if (perm != Zone.Permission.Owner && !player.getRank().canModifyZones()) {
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

            if (count == 0) {
                player.setZoneBlock1(block);
                player.setZoneBlock2(null);
                event.getPlayer().sendMessage(
                        "First block set of new " + type + ".");
                player.setZoneBlockCounter(1);
                if (zone != null) {
                    player.setTargetZoneId(zone.getId());
                }
                else {
                    player.setTargetZoneId(0);
                }
            }
            else if (count == 1) {
                int zf = player.getTargetZoneId();
                if (zf != 0 && zf != zone.getId()) {
                    player.sendMessage("The full extent of the lot must be in the same zone.");
                    return;
                }

                player.setZoneBlock2(block);
                player.sendMessage("Second block set of new " + type + ".");
                player.setZoneBlockCounter(0);
            }
        }
        // Check if this is a lot - if so, limit items that can be blessed to
        // lot owner
        else if (BlessedBlockListener.ALLOWED_MATERIALS.contains(block.getType()) &&
                 !player.getRank().canModifyZones()) {
            if (lot == null) {
                return;
            }

            if (!lot.isOwner(player.getName())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Blessed to lot owners.");
            }
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

        player.teleport(newPos);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player == null) {
            event.getPlayer().kickPlayer("Something went wrong");
            Tregmine.LOGGER.info(event.getPlayer().getName() + " was not found " +
                    "in players map.");
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
                Zone.Permission perm = currentZone.getUser(player.getName());

                // if anyone is allowed to enter by default...
                if (currentZone.getEnterDefault()) {
                    // ...we only need to reject banned players
                    if (player.getRank().canModifyZones()) {
                        // never applies to admins
                    }
                    else if (perm != null && perm == Zone.Permission.Banned) {
                        bannedMessage(currentZone, player);
                        player.teleport(player.getWorld().getSpawnLocation());
                        // movePlayerBack(player, movingFrom, movingTo);
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
                        player.teleport(player.getWorld().getSpawnLocation());
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

        Location movingTo = player.getLocation();
        Point currentPos =
                new Point(movingTo.getBlockX(), movingTo.getBlockZ());

        Zone currentZone = null;

        if (currentZone == null || !currentZone.contains(currentPos)) {

            currentZone = world.findZone(currentPos);
            if (currentZone != null) {
                Zone.Permission perm = currentZone.getUser(player.getName());

                if (currentZone.getEnterDefault()) {
                    if (player.getRank().canModifyZones()) {
                        // never applies to admins
                    }
                    else if (perm != null && perm == Zone.Permission.Banned) {
                        bannedMessage(currentZone, player);
                        player.teleport(this.plugin.getServer()
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
                        player.teleport(this.plugin.getServer()
                                .getWorld("world").getSpawnLocation());
                        player.sendMessage(ChatColor.RED
                                + "You are not allowed in this zone");
                        return;
                    }
                    else if (perm == Zone.Permission.Banned) {
                        bannedMessage(currentZone, player);
                        player.teleport(this.plugin.getServer()
                                .getWorld("world").getSpawnLocation());
                        player.sendMessage(ChatColor.RED
                                + "You are not allowed in this zone");
                        return;
                    }
                }

                if (currentZone.isPvp() && !player.getRank().canModifyZones()) {
                    player.teleport(this.plugin.getServer().getWorld("world")
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
                    Zone.Permission perm =
                            currentZone.getUser(player.getName());

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
            player.setScoreboard(board);

            ScoreboardClearTask.start(plugin, player);
        }

        player.sendMessage(ChatColor.RED + "[" + currentZone.getName() + "] "
                + currentZone.getTextEnter());

        if (currentZone.isPvp()) {
            player.sendMessage(ChatColor.RED
                    + "[" + currentZone.getName() + "] "
                    + "Warning! This is a PVP zone! Other players can damage or kill you here.");
        }

        if (perm != null) {
            String permNotification = perm.getPermissionNotification();
            player.sendMessage(ChatColor.RED + "[" + currentZone.getName()
                    + "] " + permNotification);
        }
    }
}
