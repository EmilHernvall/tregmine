package info.tregmine.api.bank;

import com.tregmine.chat.TregMessage;
import info.tregmine.api.TregminePlayer;
import info.tregmine.zones.Zone;
import info.tregmine.zones.ZoneWorld;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

import static org.bukkit.ChatColor.*;

public class Interfaces {

    public Interfaces(){}

    private int rightLine = 55;

    // BANKERS
    public void bank_banker_create(TregminePlayer player, Banker banker)
    {
        Villager villager = banker.getVillager();
        ZoneWorld world = player.getPlugin().getWorld(player.getWorld());
        Zone zone = world.findZone(villager.getLocation());
        String bankName = zone.getName();

        player.sendMessage(padString("[ " + AQUA + bankName + DARK_GRAY + " ]", rightLine, "*"));
        player.sendMessage(padString("You are talking to " + villager.getCustomName() + "!", rightLine, " "));
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
    }

    public void bank_banker_main(TregminePlayer player, Account account, Banker banker)
    {
        Villager villager = banker.getVillager();
        Bank bank = account.getBank();
        ZoneWorld world = player.getPlugin().getWorld(player.getWorld());
        Zone zone = world.findZone(villager.getLocation());
        String bankName = zone.getName();
        long balance = account.getBalance();

        player.sendMessage(padString("[ " + AQUA + bankName + DARK_GRAY + " ]", rightLine, "*"));
        player.sendMessage(padString("You are talking to " + villager.getCustomName() + "!", rightLine, " "));
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
                .suggest("/bank change_acc ")
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

    public void bank_banker_commands(TregminePlayer player)
    {
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
    }

    public void bank_banker_owner(TregminePlayer player, Banker banker)
    {

    }

    // OUTPOST INTERFACES



    // PRIVATE FUNCTIONS
    private String getLocationString(Location loc)
    {
        return "X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ() + ".";
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

}
