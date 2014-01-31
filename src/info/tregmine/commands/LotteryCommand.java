package info.tregmine.commands;

import java.text.NumberFormat;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IWalletDAO;

/**
 * @author Joe Notaro (notaro1997)
 */
public class LotteryCommand extends AbstractCommand
{
    public List<String> lottery;
    public int jackpot = 0;

    public LotteryCommand(Tregmine tregmine)
    {
        super(tregmine, "lottery");

        lottery = new ArrayList<String>();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String args[])
    {
        NumberFormat format = NumberFormat.getNumberInstance();
        int size = lottery.size();
        int amount = lottery.size() * 2000 + jackpot;
        boolean enough = true;
        boolean joined = true;

        if (size < 2) {
            enough = false;
        }

        if (!lottery.contains(player.getName())) {
            joined = false;
        }

        if (args.length < 1 || args.length > 1) {
            player.sendMessage(ChatColor.DARK_AQUA + "----------------" +
                    ChatColor.DARK_PURPLE + "Lottery Info" +
                    ChatColor.DARK_AQUA + "----------------");
            player.sendMessage(ChatColor.RED + "Amount needed to join: " +
                    ChatColor.YELLOW + "2,000 Tregs");
            player.sendMessage(ChatColor.RED + "Players in lottery: " +
                    ChatColor.YELLOW + size);
            player.sendMessage(ChatColor.RED + "Amount currently in lottery: " +
                    ChatColor.YELLOW + format.format(amount) + " Tregs");
            player.sendMessage(ChatColor.RED + "Prize is including a: " +
                    ChatColor.YELLOW + jackpot + " treg bonus!");
            player.sendMessage(ChatColor.RED + "Enough players for lottery (min 2): " +
                    ChatColor.YELLOW + enough);
            player.sendMessage(ChatColor.RED + "You are in lottery: " +
                    ChatColor.YELLOW + joined);
            player.sendMessage(ChatColor.DARK_AQUA + "----------------" +
                    ChatColor.DARK_PURPLE + "Lottery Command" +
                    ChatColor.DARK_AQUA + "-------------");
            player.sendMessage(ChatColor.RED + "/lottery join - " +
                    ChatColor.YELLOW + "Join the lottery (Takes 2,000 Tregs)");
            player.sendMessage(ChatColor.RED + "/lottery quit - " +
                    ChatColor.YELLOW + "Quit the lottery before a winner is picked");
            player.sendMessage(ChatColor.RED + "/lottery choose - " +
                    ChatColor.YELLOW + "Randomly picks a winner");
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("jackpot")) {
            if (player.getRank().canChangeJackpot()) {
                try {
                    jackpot = Integer.parseInt(args[1]);
                    player.sendMessage(ChatColor.GREEN + "Changed jackpot to " + jackpot + "!");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.AQUA + "Failed to change jackpot due to incorrect parameters!");
                    e.printStackTrace();
                }
            }
        }

        if (args.length == 1) {
            try (IContext ctx = tregmine.createContext()) {
                IWalletDAO wallet = ctx.getWalletDAO();

                if (args[0].equalsIgnoreCase("join")) {
                    if (!lottery.contains(player.getName())) {
                        if (wallet.take(player, 2000)) {
                            lottery.add(player.getName());
                            player.sendMessage(ChatColor.GREEN +
                                    "You've been added to the lottery!");
                            player.sendMessage(ChatColor.GREEN +
                                    "2,000 Tregs have been taken from you.");
                            tregmine.getServer().broadcastMessage(
                                    player.getChatName() + ChatColor.DARK_GREEN +
                                    " joined the lottery!");
                        } else {
                            player.sendMessage(ChatColor.RED +
                                    "You need at least 2,000 Tregs to join!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED +
                                "You've already joined the lottery!");
                    }
                }

                if (args[0].equalsIgnoreCase("quit")) {
                    if (lottery.contains(player.getName())) {
                        lottery.remove(player.getName());
                        wallet.add(player, 2000);
                        player.sendMessage(ChatColor.RED +
                                "You are nolonger in the lottery.");
                        player.sendMessage(ChatColor.RED +
                                "You received your 2,000 Tregs back");
                        tregmine.getServer().broadcastMessage(player.getChatName() + ChatColor.RED + " withdrew themself from the lottery"/* + ChatColor.RED + " - " + ChatColor.GOLD + "Amount in lottery: " + format.format(amount) + " Tregs!" */);
                    }
                }

                if (args[0].equalsIgnoreCase("choose")) {
                    if (player.getRank().canChooseLottery()) {
                        if (size >= 2) {
                            Random random = new Random();
                            String randomPlayer = lottery.get(random.nextInt(size));
                            TregminePlayer winner = tregmine.getPlayer(randomPlayer);
                            if (winner == null) {
                                player.sendMessage(ChatColor.RED + randomPlayer + " won, " +
                                    "but is no longer online. Try again.");
                                return true;
                            }

                            wallet.add(winner, amount);
                            tregmine.getServer().broadcastMessage(
                                    winner.getChatName() + ChatColor.DARK_AQUA +
                                    " won the lottery! " + ChatColor.RED + " - " +
                                    ChatColor.GOLD + format.format(amount) +
                                    " Tregs!");
                            lottery.clear();
                            player.sendMessage(ChatColor.GREEN +
                                    "Lottery has been succesfully cleared.");
                        } else {
                            player.sendMessage(ChatColor.RED +
                                "At least two players must be in the lottery!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED +
                            "Only Admins/Guardians/Coders can use this command!");
                    }
                }
            } catch (DAOException error) {
                throw new RuntimeException(error);
            }
        }
        return true;
    }
}
