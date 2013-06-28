package info.tregmine.listeners;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
//import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
//import org.kitteh.tag.PlayerReceiveNameTagEvent;
//import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class TregminePlayerListener implements Listener
{
    private static class RankComparator implements Comparator<TregminePlayer>
    {
        private int order;

        public RankComparator()
        {
            this.order = 1;
        }

        public RankComparator(boolean reverseOrder)
        {
            this.order = reverseOrder ? -1 : 1;
        }

        @Override
        public int compare(TregminePlayer a, TregminePlayer b)
        {
            return order * (a.getGuardianRank() - b.getGuardianRank());
        }
    }

    private final static String[] quitMessages =
        new String[] {
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " deserted from the battlefield with a hearty good bye!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " stole the cookies and ran!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " was eaten by a teenage mutant ninja platypus!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " parachuted of the plane and into the unknown!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " stole the cookies and ran!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " was eaten by a teenage mutant ninja creeper!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " jumped off the plane with a cobble stone parachute!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " built Rome in one day and now deserves a break!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " will come back soon because Tregmine is awesome!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " leaves the light and enter darkness.",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " disconnects from a better life.",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " already miss the best friends in the world!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " will build something epic next time.",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " is not banned yet!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " has left our world!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " went to browse Tregmine's forums instead!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " was scared away by einand! :(",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + "'s" + ChatColor.DARK_GRAY + " CPU was killed by the Rendermen!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " You don't know notaro1997, you don't know his life, you don't know his story.",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " logged out on accident!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " found the IRL warp!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " left the game due to IRL chunk error issues!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " left the Matrix. Say hi to Morpheus!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " <reserved space for ads. Contact a Senior Admin. Only 200k!>",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " disconnected? What is this!? Impossibru!",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " Be sure to visit the rifton general store! Follow the red line at /warp rifton",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " Come to Exon (Near sunspot)",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " his/her mom called.",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " toliet brb",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " found a lose cable and ate it.",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " just had a taste of einands wrath.",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " found the true END of minecraft.",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " sorry was that the kick button?",		
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " was not accidently banned by " + ChatColor.DARK_RED + "BlackX",		
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " found love elswhere",		
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " rage quit this server",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " Tregmine will miss you a LOT, I hope your away time is almost as pleasant as being here",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " NOOOOOO What did i do?",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " Voz just got an eargasm (or is it a eyegasm)",
            ChatColor.DARK_GRAY + "Quit - " + "%s" + ChatColor.DARK_GRAY + " maybe I won't be back tomorrow."
        };

    private final Tregmine plugin;

    public TregminePlayerListener(Tregmine instance) {
        plugin = instance;
        plugin.getServer();
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        if (event.getPlayer().getWorld().getName().matches("alpha")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(InventoryCloseEvent event)
    {
        Player player =  (Player) event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {

            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = new ArrayList<String>();
                    lore.add(info.tregmine.api.lore.Created.CREATIVE.toColorString());
                    TregminePlayer p = this.plugin.getPlayer(player);
                    lore.add(ChatColor.WHITE + "by: " + p.getChatName() );
                    lore.add(ChatColor.WHITE + "Value: " + ChatColor.MAGIC + "0000" + ChatColor.RESET + ChatColor.WHITE + " Treg" );
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        if (event.getBucket() == Material.LAVA_BUCKET) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            Location loc = block.getLocation();

            if (player.getItemInHand().getType() == Material.BOOK) {

                player.sendMessage(ChatColor.DARK_AQUA + "Type: " + ChatColor.AQUA + block.getType().toString() + " ("+ ChatColor.BLUE + block.getType().getId() + ChatColor.DARK_AQUA + ")" );
                player.sendMessage(ChatColor.DARK_AQUA +"Data: " + ChatColor.AQUA + (int) block.getData() );
                player.sendMessage(
                        ChatColor.RED +"X" +
                        ChatColor.WHITE + ", " +
                        ChatColor.GREEN + "Y" +
                        ChatColor.WHITE + ", " +
                        ChatColor.BLUE + "Z" +
                        ChatColor.WHITE + ": " +
                        ChatColor.RED + loc.getBlockX() +
                        ChatColor.WHITE + ", " +
                        ChatColor.GREEN + loc.getBlockY() +
                        ChatColor.WHITE + ", " +
                        ChatColor.BLUE + loc.getBlockZ()
                        );

                try {
                    player.sendMessage(ChatColor.DARK_AQUA + "Biome: " + ChatColor.AQUA + block.getBiome().toString() );
                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_AQUA + "Biome: " + ChatColor.AQUA + "NULL" );
                }

                Tregmine.LOGGER.info("POS: " +  loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
            }
        }
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event)
    {
        Tregmine.LOGGER.info("COMMAND: " + event.getPlayer().getName() + "::" + event.getMessage());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);

        TregminePlayer player = plugin.getPlayer(event.getPlayer());
        if (player == null) {
            event.getPlayer().kickPlayer("error loading profile!");
        }

        activateGuardians();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();

        TregminePlayer tregPlayer = new TregminePlayer(player);

        if (player.getLocation().getWorld().getName().matches("world_the_end")) {
            player.teleport(this.plugin.getServer().getWorld("world").getSpawnLocation());
        }

        if(tregPlayer.exists()) {
            tregPlayer.load();
        } else {
            tregPlayer.create();
            tregPlayer.load();
        }

        if (tregPlayer.isBanned()) {
            event.disallow(Result.KICK_BANNED, "You shall not pass!");
        } else  {
            this.plugin.addPlayer(tregPlayer);
        }

        if (tregPlayer.getMetaString("keyword") != null) {
            String keyword = tregPlayer.getMetaString("keyword") + ".mc.tregmine.info:25565".toLowerCase();
            Tregmine.LOGGER.warning("host: " + event.getHostname() );
            Tregmine.LOGGER.warning("keyword:" + keyword );

            if (keyword.equals(event.getHostname().toLowerCase()) || keyword.matches("mc.tregmine.info")) {
                Tregmine.LOGGER.warning(tregPlayer.getName() +  " keyword :: success" );
            } else {
                Tregmine.LOGGER.warning(tregPlayer.getName() +  " keyword :: faild" );
                event.disallow(Result.KICK_BANNED, "Wrong keyword!");
            }
        } else {
            Tregmine.LOGGER.warning(tregPlayer.getName() +  " keyword :: notset" );
        }

        if (tregPlayer.isGuardian()) {
            tregPlayer.setGuardianState(TregminePlayer.GuardianState.QUEUED);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        event.setQuitMessage(null);
        TregminePlayer tregP = this.plugin.getPlayer(event.getPlayer());

        if (!event.getPlayer().isOp()) {
            Random rand = new Random();
            int msgIndex = rand.nextInt(quitMessages.length);
            String message = String.format(quitMessages[msgIndex], tregP.getChatName());
            this.plugin.getServer().broadcastMessage(message);
        }

        plugin.removePlayer(tregP);
        Tregmine.LOGGER.info("Unloaded settings for " + event.getPlayer().getName() + ".");

        activateGuardians();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        TregminePlayer player = this.plugin.getPlayer(event.getPlayer());
        if (player == null) {
            event.getPlayer().kickPlayer("error loading profile!");
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        TregminePlayer tregminePlayer;

        try {
            tregminePlayer = this.plugin.getPlayer(event.getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
            event.setCancelled(true);
            return;
        }

        if (tregminePlayer.isAdmin()) {
            return;
        }


        if (!tregminePlayer.isTrusted()) {
            event.setCancelled(true);
            return;
        }

        if (event.getPlayer().getWorld().getName().matches("alpha")) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        TregminePlayer tregminePlayer = this.plugin.getPlayer(event.getPlayer());

        if (tregminePlayer.isAdmin()) {
            return;
        }

        if (!tregminePlayer.isTrusted()) {
            event.setCancelled(true);
            return;
        }

        if (event.getPlayer().getWorld().getName().matches("alpha")) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        event.setLeaveMessage(null);
    }

    private void activateGuardians()
    {
        // Identify all guardians and categorize them based on their current state
        Player[] players = plugin.getServer().getOnlinePlayers();
        Set<TregminePlayer> guardians = new HashSet<TregminePlayer>();
        List<TregminePlayer> activeGuardians = new ArrayList<TregminePlayer>();
        List<TregminePlayer> inactiveGuardians = new ArrayList<TregminePlayer>();
        List<TregminePlayer> queuedGuardians = new ArrayList<TregminePlayer>();
        for (Player srvPlayer : players) {
            TregminePlayer guardian = plugin.getPlayer(srvPlayer.getName());
            if (guardian == null || !guardian.isGuardian()) {
                continue;
            }

            TregminePlayer.GuardianState state = guardian.getGuardianState();
            if (state == null) {
                state = TregminePlayer.GuardianState.QUEUED;
            }

            switch (state) {
                case ACTIVE:
                    activeGuardians.add(guardian);
                    break;
                case INACTIVE:
                    inactiveGuardians.add(guardian);
                    break;
                case QUEUED:
                    queuedGuardians.add(guardian);
                    break;
            }

            guardian.setGuardianState(TregminePlayer.GuardianState.QUEUED);
            guardians.add(guardian);
        }

        Collections.sort(activeGuardians, new RankComparator());
        Collections.sort(inactiveGuardians, new RankComparator(true));
        Collections.sort(queuedGuardians, new RankComparator());

        int idealCount = (int)Math.ceil(Math.sqrt(players.length)/2);
        // There are not enough guardians active, we need to activate a few more
        if (activeGuardians.size() <= idealCount) {
            // Make a pool of every "willing" guardian currently online
            List<TregminePlayer> activationList = new ArrayList<TregminePlayer>();
            activationList.addAll(activeGuardians);
            activationList.addAll(queuedGuardians);

            // If the pool isn't large enough to satisfy demand, we add the guardians
            // that have made themselves inactive as well.
            if (activationList.size() < idealCount) {
                int diff = idealCount - activationList.size();
                // If there aren't enough of these to satisfy demand, we add all of them
                if (diff >= inactiveGuardians.size()) {
                    activationList.addAll(inactiveGuardians);
                }
                // Otherwise we just add the lowest ranked of the inactive
                else {
                    activationList.addAll(inactiveGuardians.subList(0, diff));
                }
            }

            // If there are more than necessarry guardians online, only activate
            // the most highly ranked.
            Set<TregminePlayer> activationSet;
            if (activationList.size() > idealCount) {
                Collections.sort(activationList, new RankComparator());
                activationSet = new HashSet<TregminePlayer>(activationList.subList(0, idealCount));
            } else {
                activationSet = new HashSet<TregminePlayer>(activationList);
            }

            // Perform activation
            StringBuffer globalMessage = new StringBuffer();
            String delim = "";
            for (TregminePlayer guardian : activationSet) {
                guardian.setGuardianState(TregminePlayer.GuardianState.ACTIVE);
                globalMessage.append(delim);
                globalMessage.append(guardian.getName());
                delim = ", ";
            }

            Set<TregminePlayer> oldActiveGuardians = new HashSet<TregminePlayer>(activeGuardians);
            if (!activationSet.containsAll(oldActiveGuardians) || activationSet.size() != oldActiveGuardians.size()) {

                plugin.getServer().broadcastMessage(ChatColor.BLUE + "Active guardians are: " + globalMessage + ". Please contact any of them if you need help.");

                // Notify previously active guardian of their state change
                for (TregminePlayer guardian : activeGuardians) {
                    if (!activationSet.contains(guardian)) {
                        guardian.sendMessage(ChatColor.BLUE + "You are no longer on active duty, and should not respond to help requests, unless asked by an admin or active guardian.");
                    }
                }

                // Notify previously inactive guardians of their state change
                for (TregminePlayer guardian : inactiveGuardians) {
                    if (activationSet.contains(guardian)) {
                        guardian.sendMessage(ChatColor.BLUE + "You have been restored to active duty and should respond to help requests.");
                    }
                }

                // Notify previously queued guardians of their state change
                for (TregminePlayer guardian : queuedGuardians) {
                    if (activationSet.contains(guardian)) {
                        guardian.sendMessage(ChatColor.BLUE + "You are now on active duty and should respond to help requests.");
                    }
                }
            }
        }

    }
}
