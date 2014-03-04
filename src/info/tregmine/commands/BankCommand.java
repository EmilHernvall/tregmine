package info.tregmine.commands;

import com.tregmine.chat.TregMessage;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMob;
import de.ntcomputer.minecraft.controllablemobs.api.ControllableMobs;
import info.tregmine.Tregmine;
import info.tregmine.api.Account;
import info.tregmine.api.Bank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.MathUtil;
import info.tregmine.database.DAOException;
import info.tregmine.database.IBankDAO;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;
import info.tregmine.events.PlayerMoveBlockEvent;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

import static org.bukkit.ChatColor.*;

public class BankCommand extends AbstractCommand implements Listener
{
    private static class VillagerReturn implements Runnable
    {
        private ControllableMob<Villager> entity;
        private final Location loc;

        public VillagerReturn(ControllableMob<Villager> entity, Location cLoc)
        {
            this.entity = entity;
            this.loc = cLoc;
        }

        @Override
        public void run()
        {
            entity.getActions().moveTo(loc);
        }
    }

    private static enum BankerNames
    {

        EARL("Earl"),
        BEN("Ben"),
        JOHN("John"),
        PETER("Peter"),
        WALTER("Walter"),
        GUSTAVO("Gustavo"),
        CARL("Carl"),
        JAMES("James"),
        JIMMY("Jimmy"),
        ROBERT("Robert"),
        KELLY("Kelly"),
        DEANNA("Deanna"),
        SUSAN("Susan"),
        EMMA("Emma"),
        BETH("Beth"),
        CHLOE("Chloe"),
        TRACY("Tracy"),
        DIANE("Diane"),
        HANNAH("Hannah"),
        SOPHIE("Sophie");

        private String name;
        private BankerNames(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    private Tregmine plugin;
    private Map<TregminePlayer, Villager> bankersInUse = new HashMap<>();

    // Prices and other configuration variables
    private int creationCost = 1000000; // Price to enable a zone to have a bank with one banker.
    private int bankerCost = 100000; // Price for additional bankers.
    private int outpostCost = 100000; // Price for an outpost (ATM) bank.
    private int rightLine = 55; // Interface configuration
    private int villagerTimeout = 60; // How long it takes for a villager to tell a customer to go

    public BankCommand(Tregmine tregmine)
    {
        super(tregmine, "bank");
        this.plugin = tregmine;

        PluginManager pluginMgm = tregmine.getServer().getPluginManager();
        pluginMgm.registerEvents(this, tregmine);
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String args[])
    {
        if (args.length == 1 && "make".equalsIgnoreCase(args[0])) {
            if (!player.getRank().canBuildBanks()) {
                player.sendMessage(RED + "Please get an administrator to add a banker!");
                return true;
            }

            ZoneWorld world = plugin.getWorld(player.getWorld());
            Zone zone = world.findZone(player.getLocation());

            if (zone == null) {
                player.sendMessage(RED + "You can not make a bank in the wilderness!");
                return true;
            }

            boolean firstTime = false;

            try (IContext ctx = plugin.createContext()) {

                IBankDAO bankDAO = ctx.getBankDAO();
                IWalletDAO walletDAO = ctx.getWalletDAO();

                Bank bank = bankDAO.getBank(zone.getId());

                if (bank == null) {
                    if (walletDAO.take(player, creationCost)) {
                        bank = new Bank(zone.getId());
                        bankDAO.createBank(bank);
                        plugin.getLogger().info("Bank created at zone: " + zone.getName());
                        player.sendMessage(GREEN + "Bank created at zone: " + zone.getName());
                        firstTime = true;
                    } else {
                        player.sendMessage(RED + "No bank already made! Requires " + creationCost + "tregs to make!");
                        player.sendMessage(AQUA + "You do not have enough money to proceed!");
                        return true;
                    }
                }

                if (!walletDAO.take(player, bankerCost) && !firstTime) {
                    player.sendMessage(RED + "Requires " + bankerCost + "tregs to make another banker!");
                    player.sendMessage(AQUA + "You do not have enough money to proceed!");
                    return true;
                }
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }

            LivingEntity ent = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
            Villager villager = (Villager) ent;

            villager.setCustomName(AQUA + "Banker " + getRandomBanker());
            villager.setProfession(Villager.Profession.LIBRARIAN);
            villager.setAgeLock(true);
            villager.setCustomNameVisible(true);
            villager.setMetadata("banker", new FixedMetadataValue(plugin, true));

            ControllableMob<Villager> cVillager = ControllableMobs.putUnderControl(villager, true);

            cVillager.getAttributes().getMaxHealthAttribute().setBasisValue(100.0);

            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.runTaskTimerAsynchronously(plugin, new VillagerReturn(cVillager, player.getLocation()), 0L, 20L);

            player.sendMessage(GREEN + "New banker added at: " + getLocationString(villager.getLocation()));

            try (IContext ctx = plugin.createContext()) {

                IBankDAO bankDAO = ctx.getBankDAO();
                Bank bank = bankDAO.getBank(zone.getId());

                bankDAO.addBanker(bank, villager.getUniqueId(), villager.getCustomName());

            } catch (DAOException e) {
                throw new RuntimeException(e);
            }

            return true;
        } else if (args.length == 2 && "internalCommand".equalsIgnoreCase(args[0]) && "quit".equalsIgnoreCase(args[1])) {
            if (bankersInUse.containsKey(player)) {
                player.setChatState(TregminePlayer.ChatState.CHAT);
                bankersInUse.remove(player);
                player.setVillagerTimer(0);
                player.sendMessage(AQUA + "You are no longer talking to the banker!");
            } else {
                player.sendMessage(AQUA + "You were not talking to a banker...");
            }

            return true;
        } else if (args.length == 2 && "internalCommand".equalsIgnoreCase(args[0]) && "change".equalsIgnoreCase(args[1])) {
            if (bankersInUse.containsKey(player)) {
                player.setVillagerTimer(villagerTimeout);
                accountChanger(player); // STILL MAJOR WIP
            } else {
                player.sendMessage(AQUA + "You are not talking to a banker...");
            }

            return true;
        } else if (args.length == 2 && "internalCommand".equalsIgnoreCase(args[0]) && "create".equalsIgnoreCase(args[1])) {
            if (bankersInUse.containsKey(player)) {
                player.setVillagerTimer(villagerTimeout);
                createAccount(player);
            } else {
                player.sendMessage(AQUA + "You are not talking to a banker...");
            }

            return true;
        } else if ((args.length == 2 && "internalCommand".equalsIgnoreCase(args[0]) && "commands".equalsIgnoreCase(args[1])) ||
                    (args.length == 1 && "help".equalsIgnoreCase(args[0]))) {
            if (bankersInUse.containsKey(player)) {
                player.setVillagerTimer(villagerTimeout);

                player.sendMessage(padString("[ * ]", rightLine, "*"));
                player.sendMessage(padString(" For those who prefer commands, here they are:", rightLine, " "));
                player.sendMessage(padString("[ * ]", rightLine, "*"));

                player.sendMessage(GRAY + " /bank deposit <amount>");
                player.sendMessage(DARK_GRAY + " - Deposits the amount of money.");
                player.sendMessage(GRAY + " /bank withdraw <amount>");
                player.sendMessage(DARK_GRAY + " - Withdraws the amount of money.");
                player.sendMessage(GRAY + " /bank change <account id> <pin>");
                player.sendMessage(DARK_GRAY + " - Changes your current account.");
                player.sendMessage(GRAY + " /bank pin <old pin> <new pin>");
                player.sendMessage(DARK_GRAY + " - Change your current pin.");

                player.sendMessage(padString("[ * ]", rightLine, "*"));
            } else {
                player.sendMessage(AQUA + "You are not talking to a banker...");
            }

            return true;
        } else if (args.length >= 2 && "deposit".equalsIgnoreCase(args[0])) {
            if (bankersInUse.containsKey(player)) {
                if (args.length != 3) {
                    try {
                        long depositAmount = Long.parseLong(args[1]);

                        player.setVillagerTimer(villagerTimeout);
                        depositMoney(player, depositAmount);
                    } catch (NumberFormatException e) {
                        player.sendMessage(RED + "Incorrect value: " + args[1]);
                    }
                } else {
                    player.sendMessage(RED + "Wrong syntax: /bank deposit <amount>");
                }
            } else {
                player.sendMessage(AQUA + "You are not talking to a banker...");
            }

            return true;
        } else if (args.length >= 2 && "withdraw".equalsIgnoreCase(args[0])) {
            if (bankersInUse.containsKey(player)) {
                if (args.length != 3) {
                    try {
                        long withdrawAmount = Long.parseLong(args[1]);

                        player.setVillagerTimer(villagerTimeout);
                        withdrawMoney(player, withdrawAmount);
                    } catch (NumberFormatException e) {
                        player.sendMessage(RED + "Incorrect value: " + args[1]);
                    }
                } else {
                    player.sendMessage(RED + "Wrong syntax: /bank withdraw <amount>");
                }
            } else {
                player.sendMessage(AQUA + "You are not talking to a banker...");
            }

            return true;
        } else if (args.length >= 2 && "change_pin".equalsIgnoreCase(args[0])) {
            if (bankersInUse.containsKey(player)) {
                if (args.length != 4) {
                    try {
                        // This will give the error if they are not both numbers
                        Long.parseLong(args[1]);
                        Long.parseLong(args[2]);

                        player.setVillagerTimer(villagerTimeout);
                        changePin(player, args[1], args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(RED + "Incorrect value: " + args[1] + " or " + args[2]);
                    }
                } else {
                    player.sendMessage(RED + "Wrong syntax: /bank change_pin <old pin> <new pin>");
                }
            } else {
                player.sendMessage(AQUA + "You are not talking to a banker...");
            }

            return true;
        }


        return true;
    }

    @EventHandler
    public void villagerConversation(AsyncPlayerChatEvent event)
    {
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        if (player.getChatState().equals(TregminePlayer.ChatState.BANK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void villagerMurder(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Villager)) {
            return;
        }

        Villager villager = (Villager) event.getEntity();
        ZoneWorld world = plugin.getWorld(villager.getWorld());
        Zone zone = world.findZone(villager.getLocation());

        if(zone == null) {
            return;
        }

        Bank bank;

        if (!(villager.hasMetadata("banker"))) {
            try (IContext ctx = plugin.createContext()) {

                IBankDAO bankDAO = ctx.getBankDAO();
                bank = bankDAO.getBank(zone.getId());

                if (bankDAO.isBanker(bank, villager.getUniqueId())) {
                    villager.setMetadata("banker", new FixedMetadataValue(plugin, true));
                } else {
                    return;
                }
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        if (!(villager.hasMetadata("banker"))) {
            return;
        }

        event.setCancelled(true);
        event.setDamage(0);
    }

    @EventHandler
    public void villagerDistance(PlayerMoveBlockEvent event)
    {
        TregminePlayer player = event.getPlayer();

        if (!(bankersInUse.containsKey(player))) {
            return;
        }

        if (!(MathUtil.calcDistance2d(player.getLocation(), bankersInUse.get(player).getLocation()) > 10)) {
            return;
        }

        bankersInUse.remove(player);
        player.sendMessage(RED + "You are no longer talking to a banker! You moved too far away.");
        player.setChatState(TregminePlayer.ChatState.CHAT);
    }

    @EventHandler
    public void villagerInteract(PlayerInteractEntityEvent event)
    {
        if (!(event.getRightClicked() instanceof Villager)) {
            return;
        }

        Villager villager = (Villager) event.getRightClicked();
        TregminePlayer player = plugin.getPlayer(event.getPlayer());

        for (TregminePlayer entry : bankersInUse.keySet()) {
            if (entry.getVillagerTime() == 0) {
                bankersInUse.get(entry).remove();
            }
        }

        if (bankersInUse.containsValue(villager) && !bankersInUse.containsKey(player)) {
            event.setCancelled(true);
            player.sendMessage(RED + "Someone is already using this banker! Please wait...");
            return;
        }

        if (bankersInUse.containsKey(player)) {
            if (!(bankersInUse.get(player).getUniqueId().equals(villager.getUniqueId()))) {
                event.setCancelled(true);
                String name = bankersInUse.get(player).getCustomName();
                player.sendMessage(RED + "You are already talking with " + name);
                return;
            }
        }

        ZoneWorld world = plugin.getWorld(player.getWorld());
        Zone zone = world.findZone(villager.getLocation());

        if (zone == null) {
            if (villager.hasMetadata("banker")) {
                player.sendMessage(RED + "There shouldn't be a villager in the wilderness!");
                player.sendMessage(AQUA + "Please contact an administrator if this is a mistake...");
                return;
            }
            return;
        }

        Bank bank;
        try (IContext ctx = plugin.createContext()) {

            IBankDAO bankDAO = ctx.getBankDAO();
            bank = bankDAO.getBank(zone.getId());

            if (bank == null && villager.hasMetadata("banker")) {
                player.sendMessage(RED + "This zone is not a registered bank!");
                player.sendMessage(AQUA + "Please contact an administrator if this is a mistake...");
                return;
            }

        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        // Check MetaData for Banker - If not know as a banker
        // Check the database to see if they actually are
        if (!(villager.hasMetadata("banker"))) {
            try (IContext ctx = plugin.createContext()) {

                IBankDAO bankDAO = ctx.getBankDAO();
                if (bankDAO.isBanker(bank, villager.getUniqueId())) {
                    villager.setMetadata("banker", new FixedMetadataValue(plugin, true));
                } else {
                    return;
                }
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        event.setCancelled(true);
        player.setChatState(TregminePlayer.ChatState.BANK);
        player.setVillagerTimer(villagerTimeout);

        String bankName = zone.getName();

        Account account;
        long balance;
        boolean newAccount = false;
        try (IContext ctx = plugin.createContext()) {

            IBankDAO bankDAO = ctx.getBankDAO();
            account = bankDAO.getAccountByPlayer(bank, player.getId());

            if (account == null) {
                newAccount = true;
            }

            balance = ctx.getWalletDAO().balance(player);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (newAccount) {

            player.sendMessage(padString("[ " + AQUA + bankName + DARK_GRAY + " ]", rightLine, "*"));
            player.sendMessage(padString("You are talking to Banker " + getBankerName(villager), rightLine, " "));
            player.sendMessage(padString("[ * ]", rightLine, "*"));

            player.sendMessage(DARK_GRAY + " We have picked you out as a new member at our bank!");

            player.sendMessage(padString("[ * ]", rightLine, "*"));

            new TregMessage(" Yes, I would like to make an account!")
                    .color(DARK_PURPLE)
                    .command("/bank internalCommand create")
                    .tooltip(DARK_PURPLE + "Click to create an account at this bank!")
                    .send(player);

            new TregMessage(" Yes, but I would not like an account!")
                    .color(DARK_PURPLE)
                    .command("/bank internalCommand quit")
                    .tooltip(DARK_PURPLE + "Clicking this will end your session with the banker!")
                    .send(player);

            player.sendMessage(padString("[ * ]", rightLine, "*"));

            player.sendMessage(DARK_GRAY + " If this is a mistake, please contact an administrator!");
            player.sendMessage(DARK_GRAY + " DO NOT make a new account! This will make you lose stuff...");

            player.sendMessage(padString("[ * ]", rightLine, "*"));

        } else {

            player.sendMessage(padString("[ " + AQUA + bankName + DARK_GRAY + " ]", rightLine, "*"));
            player.sendMessage(padString("You are talking to Banker " + getBankerName(villager), rightLine, " "));
            player.sendMessage(padString("[ * ]", rightLine, "*"));

            player.sendMessage(DARK_GRAY + " Bank Balance: " + account.getBalance());
            player.sendMessage(DARK_GRAY + " Wallet Balance: " + balance);
            player.sendMessage(DARK_GRAY + " Account Number: " + account.getAccountNumber());

            player.sendMessage(padString("[ * ]", rightLine, "*"));

            new TregMessage(" Click here to go deposit tregs...")
                    .color(DARK_PURPLE)
                    .suggest("/bank deposit ")
                    .tooltip(DARK_PURPLE + "Syntax: /bank deposit <amount>")
                    .send(player);

            new TregMessage(" Click here to go withdraw tregs...")
                    .color(DARK_PURPLE)
                    .suggest("/bank withdraw ")
                    .tooltip(DARK_PURPLE + "Syntax: /bank withdraw <amount>")
                    .send(player);

            new TregMessage(" Click here to go change bank account...")
                    .color(DARK_PURPLE)
                    .command("/bank change_acc ")
                    .tooltip(DARK_PURPLE + "Syntax: /bank change_acc <account number>!")
                    .send(player);

            new TregMessage(" Click here to go change your pin...")
                    .color(DARK_PURPLE)
                    .suggest("/bank change_pin ")
                    .tooltip(DARK_PURPLE + "Syntax: /bank change_pin <old pin> <new pin>!")
                    .send(player);

            new TregMessage(" Click here to exit the bank!")
                    .color(DARK_PURPLE)
                    .command("/bank internalCommand quit")
                    .tooltip(DARK_PURPLE + "Thanks for visiting the bank of " + bankName + "!")
                    .send(player);

            new TregMessage(" Do you prefer to use commands?")
                    .color(DARK_GRAY)
                    .command("/bank internalCommand commands")
                    .tooltip(DARK_GRAY + "Click here to see the command variations!")
                    .send(player);

            player.sendMessage(padString("[ * ]", rightLine, "*"));

        }

        bankersInUse.put(player, villager);
    }

    private String getRandomBanker()
    {
        List<String> bankers = new ArrayList<>();
        for (BankerNames banker : BankerNames.values()) {
            bankers.add(banker.toString());
        }

        return bankers.get(new Random().nextInt(bankers.size()));
    }

    private String getBankerName(Villager villager)
    {
        String name = villager.getCustomName();
        String[] names = name.split(" ");

        try
        {
            return names[1];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return null;
        }

    }

    private String padString(String str, int len, String character)
    {
        int diff = len - (str.length() + 2);

        if (diff % 2 == 1) {
            str = " " + str + "  ";
            diff--;
        } else {
            str = " " + str + " ";
        }

        int side = diff/2;

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < side; i++) {
            buf.append(character);
        }

        return DARK_GRAY + buf.toString() + str + DARK_GRAY + buf.toString();
    }

    private void accountChanger(TregminePlayer player)
    {
        Inventory accountChangerInv = Bukkit.createInventory(player.getPlayer(), 36, "  Enter your account number!");

        addNumber(3, 1, accountChangerInv);
        addNumber(4, 2, accountChangerInv);
        addNumber(5, 3, accountChangerInv);
        addNumber(12, 4, accountChangerInv);
        addNumber(13, 5, accountChangerInv);
        addNumber(14, 6, accountChangerInv);
        addNumber(21, 7, accountChangerInv);
        addNumber(22, 8, accountChangerInv);
        addNumber(23, 9, accountChangerInv);
        addNumber(31, 0, accountChangerInv);

        player.openInventory(accountChangerInv);
    }

    private void addNumber(int slotN, int number, Inventory inv)
    {
        ItemStack slot = new ItemStack(Material.STAINED_GLASS_PANE);
        slot.setAmount(number);
        slot.getData().setData((byte) 15);

        ItemMeta sMeta = slot.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add("");
        sMeta.setLore(lore);

        sMeta.setDisplayName(YELLOW + "Number " + (number));

        slot.setItemMeta(sMeta);

        inv.setItem(slotN, slot);
    }

    private String getLocationString(Location loc)
    {
        return "X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ() + ".";
    }

    private void depositMoney(TregminePlayer player, long amount)
    {
        try (IContext ctx = plugin.createContext()) {

            IBankDAO bankDAO = ctx.getBankDAO();
            IWalletDAO walletDAO = ctx.getWalletDAO();
            Account account;

            ZoneWorld world = plugin.getWorld(player.getWorld());
            Zone zone = world.findZone(player.getLocation());
            Bank bank = bankDAO.getBank(zone.getId());

            account = bankDAO.getAccountByPlayer(bank, player.getId());

            if (account == null) {
                player.sendMessage(RED + "An error occured... You don't haven account!");
                return;
            }

            if (!(account.isVerified())) {
                player.sendMessage(RED + "Please verify your account first!");
                player.sendMessage(AQUA + "Do this with /bank verify <old pin>");
                return;
            }

            if (walletDAO.take(player, amount)) {
                bankDAO.deposit(bank, account, player.getId(), amount);

                account = bankDAO.getAccountByPlayer(bank, player.getId()); // Reset local variable with new info

                player.sendMessage(GREEN + "You deposited " + amount + " tregs!");
                player.sendMessage(DARK_GREEN + "Wallet balance: " + walletDAO.balance(player) + " tregs.");
                player.sendMessage(DARK_GREEN + "Bank balance: " + account.getBalance() + " tregs.");
            }
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private void withdrawMoney(TregminePlayer player, long amount)
    {
        try (IContext ctx = plugin.createContext()) {

            IBankDAO bankDAO = ctx.getBankDAO();
            IWalletDAO walletDAO = ctx.getWalletDAO();
            Account account;

            ZoneWorld world = plugin.getWorld(player.getWorld());
            Zone zone = world.findZone(player.getLocation());
            Bank bank = bankDAO.getBank(zone.getId());

            account = bankDAO.getAccountByPlayer(bank, player.getId());

            if (account == null) {
                player.sendMessage(RED + "An error occured... You don't haven account!");
                return;
            }

            if (!(account.isVerified())) {
                player.sendMessage(RED + "Please verify your account first!");
                player.sendMessage(AQUA + "Do this with /bank verify <old pin>");
                return;
            }

            if (bankDAO.withdraw(bank, account, player.getId(), amount)) {
                walletDAO.add(player, amount);

                account = bankDAO.getAccountByPlayer(bank, player.getId()); // Reset local variable with new info

                player.sendMessage(GREEN + "You deposited " + amount + " tregs!");
                player.sendMessage(DARK_GREEN + "Wallet balance: " + walletDAO.balance(player) + " tregs.");
                player.sendMessage(DARK_GREEN + "Bank balance: " + account.getBalance() + " tregs.");
            }

        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private void changePin(TregminePlayer player, String oldPin, String newPin)
    {
        try (IContext ctx = plugin.createContext()) {

            IBankDAO bankDAO = ctx.getBankDAO();
            Account account;

            ZoneWorld world = plugin.getWorld(player.getWorld());
            Zone zone = world.findZone(player.getLocation());
            Bank bank = bankDAO.getBank(zone.getId());

            account = bankDAO.getAccountByPlayer(bank, player.getId());

            if (account == null) {
                player.sendMessage(RED + "An error occured... You don't haven account!");
                return;
            }

            // No need to check if account is verified, as they need the old pin to be able to change the pin

            if (!(oldPin.equalsIgnoreCase(account.getPin()))) {
                player.sendMessage(RED + "Your old pin is incorrect!");
                return;
            }

            if (newPin.length() != 4) {
                player.sendMessage(RED + "Your new pin must be atleast 4 characters!");
                return;
            }

            bankDAO.setPin(account, newPin);
            account.setPin(newPin);

            player.sendMessage(GREEN + "You have changed your pin to " + newPin + "!");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createAccount(TregminePlayer player)
    {
        try (IContext ctx = plugin.createContext()) {

            IBankDAO bankDAO = ctx.getBankDAO();
            Account account;

            ZoneWorld world = plugin.getWorld(player.getWorld());
            Zone zone = world.findZone(player.getLocation());
            Bank bank = bankDAO.getBank(zone.getId());

            account = bankDAO.getAccountByPlayer(bank, player.getId());

            if (account != null) {
                player.sendMessage(RED + "You already have an account... What happened!?");
                return;
            }

            account = new Account();
            account.setBank(bank);
            account.setPlayerId(player.getId());
            account.setVerified(true);

            bankDAO.createAccount(account, player.getId());

            player.sendMessage(GREEN + "Successfully created an account!");
            player.sendMessage(AQUA + "Your pin is: " + account.getPin() + "! Keep it safe...");

            player.setChatState(TregminePlayer.ChatState.CHAT);
            bankersInUse.remove(player);

            player.sendMessage(AQUA + "Come back later if you want to deal with real money!");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }
}
