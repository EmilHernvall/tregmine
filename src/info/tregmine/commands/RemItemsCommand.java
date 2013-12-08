package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Monster;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.MathUtil;

public class RemItemsCommand extends AbstractCommand
{
    public RemItemsCommand(Tregmine tregmine)
    {
        super(tregmine, "remitems");
    }

    @Override
    public boolean handlePlayer(final TregminePlayer p, String[] args)
    {
        if (!p.getRank().canRemItems()) {
            return true;
        }

        if (args.length != 1){
            p.sendMessage(ChatColor.GRAY + "Please type /remitems help");
            return true;
        }

        if(args[0].equalsIgnoreCase("help")){
            p.sendMessage(ChatColor.GRAY + "/remitems <radius> - Remove all ground items in the specified radius");
            p.sendMessage(ChatColor.GRAY + "/remitems all - Remove all ground items in your current world.");
        }

        else if(args[0].equalsIgnoreCase("all")){
            worldItemRemover(p);
        }

        else{
            int distance;
            try {
                distance = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                distance = 500;
            } catch (ArrayIndexOutOfBoundsException e) {
                distance = 500;
            }

            p.sendMessage(ChatColor.GREEN + "Removing all ground items within " + distance + " blocks.");

            Location loc = p.getLocation();

            World world = p.getWorld();

            List<Entity> entList = world.getEntities();
            int total = 0;

            for(Entity current : entList){
                if (current instanceof Item){
                    if (MathUtil.calcDistance2d(loc, current.getLocation()) > distance) {
                        continue;
                    }
                    total++;
                    current.remove();

                }
            }
            p.sendMessage(ChatColor.GOLD + "" + total + ChatColor.GREEN + " items successfully removed!");
        }

        return true;
    }

    public int number = 60;
    int taskID;

    void worldItemRemover(final TregminePlayer p){

        final World world = p.getLocation().getWorld();

        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(tregmine, new Runnable() 
        {
            public void run()
            {
                if (number == -1){
                    number = 60;
                    Bukkit.getServer().getScheduler().cancelTask(taskID );
                }
                else{
                    if (number == 60){
                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.sendMessage(p.getChatName() + ChatColor.DARK_RED + " is clearing all ground items in " + ChatColor.GOLD + world.getName());
                            online.sendMessage(ChatColor.AQUA + "Please pick up any items you have on the ground.");
                            online.sendMessage(ChatColor.AQUA + "Item removal commencing in 60 seconds.");
                        }
                        number--;
                    }
                    else if (number == 30 || number == 20 || number == 10){
                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.sendMessage(ChatColor.AQUA + "" + number + " seconds until item removal.");
                        }
                        number--;
                    }
                    else if (number <= 5 && number > 0){
                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.sendMessage(ChatColor.AQUA + "" + number);
                        }
                        number--;
                    }
                    else if(number == 0){

                        number--;

                        List<Entity> entList = world.getEntities();
                        int count = 0;

                        for(Entity current : entList){
                            if (current instanceof Item){
                                count++;
                                current.remove();
                            }
                        }
                        p.sendMessage(ChatColor.GOLD + "" + count +  ChatColor.GREEN + " items successfully removed");

                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.sendMessage(ChatColor.GREEN + "Item removal in " + ChatColor.GOLD 
                                    + world.getName() + ChatColor.GREEN + " completed successfully!");
                            online.sendMessage(ChatColor.GOLD + "" + count +  ChatColor.GREEN + " items were removed.");
                        }
                    }
                    else{
                        number--;
                    }
                }

            }
        }, 20, 20);
    }
}
