package info.tregmine.commands;

import org.bukkit.ChatColor;
import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class ChangeNameCommand extends AbstractCommand
{
    public ChangeNameCommand(Tregmine tregmine)
    {
        super(tregmine, "cname");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (args.length != 2) {
            return false;
        }
        if (!player.getRank().canChangeName()) {
        	player.sendMessage(ChatColor.RED + "You can't change your name!");
            return true;
        }
        String colorstring = args[0];

// Example comparisons
//        if(colorstring.equalsIgnoreCase("")){
//        	usecolor = ChatColor.;
//        }
//        if(colorstring.equalsIgnoreCase("") || colorstring.equalsIgnoreCase("")){
//        	usecolor = ChatColor.;
//        }
        ChatColor usecolor = ChatColor.WHITE;
        if(colorstring.equalsIgnoreCase("red")){
        	usecolor = ChatColor.RED;
        }
        else if(colorstring.equalsIgnoreCase("darkred") || colorstring.equalsIgnoreCase("dark_red")){
        	usecolor = ChatColor.DARK_RED;
        }
        else if(colorstring.equalsIgnoreCase("aqua")){
        	usecolor = ChatColor.AQUA;
        }
        else if(colorstring.equalsIgnoreCase("black")){
        	usecolor = ChatColor.BLACK;
        }
        else if(colorstring.equalsIgnoreCase("blue")){
        	usecolor = ChatColor.BLUE;
        }
        else if(colorstring.equalsIgnoreCase("darkaqua") || colorstring.equalsIgnoreCase("dark_aqua")){
        	usecolor = ChatColor.DARK_AQUA;
        }
        else if(colorstring.equalsIgnoreCase("darkblue") || colorstring.equalsIgnoreCase("dark_blue")){
        	usecolor = ChatColor.DARK_BLUE;
        }
        else if(colorstring.equalsIgnoreCase("darkgray") || colorstring.equalsIgnoreCase("dark_gray")){
        	usecolor = ChatColor.DARK_GRAY;
        }
        else if(colorstring.equalsIgnoreCase("darkgreen") || colorstring.equalsIgnoreCase("dark_green")){
        	usecolor = ChatColor.DARK_GREEN;
        }
        else if(colorstring.equalsIgnoreCase("darkpurple") || colorstring.equalsIgnoreCase("dark_purple")){
        	usecolor = ChatColor.DARK_PURPLE;
        }
        else if(colorstring.equalsIgnoreCase("gold")){
        	usecolor = ChatColor.GOLD;
        }
        else if(colorstring.equalsIgnoreCase("gray")){
        	usecolor = ChatColor.GRAY;
        }
        else if(colorstring.equalsIgnoreCase("green")){
        	usecolor = ChatColor.GREEN;
        }
        else if(colorstring.equalsIgnoreCase("lightpurple") || colorstring.equalsIgnoreCase("light_purple") || colorstring.equalsIgnoreCase("purple")){
        	usecolor = ChatColor.LIGHT_PURPLE;
        }
        else if(colorstring.equalsIgnoreCase("white")){
        	usecolor = ChatColor.WHITE;
        }
        else if(colorstring.equalsIgnoreCase("yellow")){
        	usecolor = ChatColor.YELLOW;
        }
        else{
        	player.sendMessage(ChatColor.RED + "You have entered an invalid color. White will be used instead.");
        }
        player.setTemporaryChatName(usecolor + args[1]);
        player.sendMessage("You are now: " + player.getChatName());
        LOGGER.info(player.getName() + " changed name to "
                + player.getChatName());

        return true;
    }
}
