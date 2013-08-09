package info.tregmine.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Random;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.ConnectionPool;
import info.tregmine.database.DBWalletDAO;

public class LotteryCommand extends AbstractCommand{

	public LotteryCommand(Tregmine tregmine) {

		super(tregmine, "lottery");
	}

	Random random = new Random();
	int amount = tregmine.lottery.size() * 2000;
	int size = tregmine.lottery.size();
	String winner = tregmine.lottery.get(random.nextInt(size));
	TregminePlayer lotteryWinner = (TregminePlayer) tregmine.getServer().getPlayer(winner);
	NumberFormat format = NumberFormat.getNumberInstance();
	String enough = "True";
	String joined = "True";

	@Override
	public boolean handlePlayer(TregminePlayer player, String args[]){

		if(size < 2){
			enough = "False (At least 2)";
		}

		if(!tregmine.lottery.contains(player.getName())){
			joined = "False";
		}

		if(args.length < 1 || args.length > 1){
			player.sendMessage(ChatColor.DARK_AQUA + "----------------" + ChatColor.DARK_PURPLE + "Lottery Info" + ChatColor.DARK_AQUA + "----------------");
			player.sendMessage(ChatColor.RED + "Amount needed to join: " + ChatColor.YELLOW + "2,000 Tregs");
			player.sendMessage(ChatColor.RED + "Players in lottery: " + ChatColor.YELLOW + size);
			player.sendMessage(ChatColor.RED + "Amount currently in lottery: " + ChatColor.YELLOW + format.format(amount) + " Tregs");
			player.sendMessage(ChatColor.RED + "Enough players for lottery: " + ChatColor.YELLOW + enough);
			player.sendMessage(ChatColor.RED + "You are in lottery: " + ChatColor.YELLOW + joined);
			player.sendMessage(ChatColor.DARK_AQUA + "----------------" + ChatColor.DARK_PURPLE + "Lottery Command" + ChatColor.DARK_AQUA + "----------------");
			player.sendMessage(ChatColor.RED + "/lottery join - " + ChatColor.YELLOW + "Join the lottery (Takes 2,000 Tregs)");
			player.sendMessage(ChatColor.RED + "/lottery quit - " + ChatColor.YELLOW + "Quit the lottery before a winner is picked.");
			player.sendMessage(ChatColor.RED + "/lottery choose - " + ChatColor.YELLOW + "Randomly picks a winner (Admins/Guardians only)");
		}

		if(args.length == 1){
			Connection connection = null;
			try {
				connection = ConnectionPool.getConnection();
				DBWalletDAO wallet = new DBWalletDAO(connection);
					
					if(args[0].equalsIgnoreCase("join")){
						if(!tregmine.lottery.contains(player.getName())){
							if(wallet.take(player, 2000)){
								tregmine.lottery.add(player.getName());
								player.sendMessage(ChatColor.DARK_GREEN + "You've been added to the lottery!");
								player.sendMessage(ChatColor.DARK_GREEN + "2,000 Tregs have been taken from you.");
								player.sendMessage(ChatColor.RED + "Amount currently in lottery: " + format.format(amount) + " Tregs");
								tregmine.getServer().broadcastMessage(player.getChatName() + ChatColor.DARK_GREEN + " joined the lottery!" + ChatColor.RED + " - " + ChatColor.GOLD + "Amount in lottery: " + format.format(amount) + " Tregs!");
							}else{
								player.sendMessage(ChatColor.RED + "You need at least 2,000 Tregs to join!");
							}
						}else{
							player.sendMessage(ChatColor.RED + "You've already joined the lottery!");
						}
					}
					
					if(args[0].equalsIgnoreCase("quit")){
						if(tregmine.lottery.contains(player.getName())){
							wallet.add(player, 2000);
							player.sendMessage(ChatColor.RED + "You are nolonger in the lottery.");
							player.sendMessage(ChatColor.RED + "You received your 2,000 Tregs back");
							tregmine.getServer().broadcastMessage(player.getChatName() + ChatColor.RED + " withdrew themself from the lottery" + ChatColor.RED + " - " + ChatColor.GOLD + "Amount in lottery: " + format.format(amount) + " Tregs!");
						}
					}
					
					if(args[0].equalsIgnoreCase("choose")){
						if(player.getRank().canChooseLottery()){
							wallet.add(lotteryWinner, amount);
							lotteryWinner.sendMessage(ChatColor.DARK_AQUA + "You won the lottery!" + ChatColor.GOLD + "You've received " + format.format(amount) + " Tregs!");
							player.sendMessage(lotteryWinner.getChatName() + ChatColor.DARK_AQUA + " won the lottery.");
							tregmine.getServer().broadcastMessage(lotteryWinner.getChatName() + ChatColor.DARK_AQUA + " won the lottery! " + ChatColor.RED + " - " + ChatColor.GOLD + format.format(amount) + " Tregs!");
							tregmine.lottery.clear();
							player.sendMessage(ChatColor.GREEN + "Lottery has been succesfully cleared.");
						}else{
							player.sendMessage(ChatColor.RED + "Only Guardians/Admins/Coders can use this command!");
						}
					}
			} catch (SQLException error) {
				throw new RuntimeException(error);
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
					}
				}
			}
		}
		return false;
	}
}
