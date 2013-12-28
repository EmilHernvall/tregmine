package info.tregmine.commands;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.*;

public class RemItemsCommand extends AbstractCommand
{
    public RemItemsCommand(Tregmine tregmine)
    {
        super(tregmine, "remitems");
    }

    @SuppressWarnings("deprecation")
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
            p.sendMessage(ChatColor.GRAY + "/remitems item [radius] - Removes all ground items in 50 block radius, or uses specified radius.");
            p.sendMessage(ChatColor.GRAY + "/remitems item all - Remove all ground items in your current world.");
            p.sendMessage(ChatColor.GRAY + "/remitems mob <radius> [type] - Removes all mobs in specified radius, optionally only remove specified animal.");
            p.sendMessage(ChatColor.GRAY + "/remitems mob help - Returns a list of valid monsters.");
        }
        
        else if("item".equalsIgnoreCase(args[0])) {
            int radius;
            boolean error = false;
            if (args.length == 2) {
                if ("all".equalsIgnoreCase(args[1])) {
                    worldItemRemover(p);
                    return true;
                } else {
                    try {
                        radius = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        radius = 50;
                        error = true;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        radius = 50;
                        error = true;
                    }
                }
            } else {
                radius = 50;
                error = true;
            }
            
            if (error) {
                p.sendMessage(ChatColor.GREEN + "No radius detected, or incorrect parameter. Using radius of 50...");
            } else {
                p.sendMessage(ChatColor.GREEN + "Removing all ground items within " + radius + " blocks.");
            }
            
            itemRemover(radius, p);
            return true;
        }
        
        else if("mob".equalsIgnoreCase(args[0])) {
            if (args.length == 2 && "help".equalsIgnoreCase(args[1])) {
                StringBuilder buf = new StringBuilder();

                String delim = "";
                for (EntityType mob : EntityType.values()) {
                    if (mob.isSpawnable() && mob.isAlive()) {
                        buf.append(delim);
                        buf.append(mob.getName());
                        delim = ", ";
                    }
                }

                p.sendMessage("Valid names are: ");
                p.sendMessage(buf.toString());

                return true;
            }
            
            int radius = 0;
            boolean radiusError = false;
            if (args.length == 2 || args.length == 3) {
                try {
                    radius = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    radius = 50;
                    radiusError = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    radius = 50;
                    radiusError = true;
                }
            }
            
            EntityType mobType = null;
            boolean mobError = false;
            if (args.length == 3) {
                try {
                    String mobName = args[2];
                    mobType = EntityType.fromName(mobName);
                } catch (Exception e) {
                    mobError = true;
                }
            }
            
            if (mobError) {
                p.sendMessage(ChatColor.RED + "Incorrect mob type... Stopping...");
                return true;
            }
            
            if (radiusError) {
                p.sendMessage(ChatColor.GREEN + "No radius detected, or incorrect parameter. Using radius of 50...");
            } else {
                p.sendMessage(ChatColor.GREEN + "Removing all mobs within " + radius + " blocks.");
            }
            
            mobRemover(radius, p, mobType);
            return true;
        }


        return true;
    }

    private void mobRemover(int radius, TregminePlayer p, EntityType mobType)
    {
        List<Entity> nearby = p.getNearbyEntities(radius, radius, radius);
        int total = 0;
        
        for (Entity entity : nearby) {
            if (mobType == null) {
                kill(entity);
                total++;
                continue;
            } else {
                if (entity.getType().equals(mobType)) {
                    kill(entity);
                    total++;
                    continue;
                }
            }
        }
        p.sendMessage(ChatColor.GOLD + "" + total + ChatColor.GREEN + " mobs successfully removed!");
    }

    private void kill(Entity entity)
    {
        if (entity instanceof Monster) {
            Monster monster = (Monster) entity;
            monster.setHealth(0);
        }
        else if (entity instanceof Animals) {
            Animals animal = (Animals) entity;
            animal.setHealth(0);
        }
        else if (entity instanceof Slime) {
            Slime slime = (Slime) entity;
            slime.setHealth(0);
        }
        else if (entity instanceof EnderDragon) {
            EnderDragon dragon = (EnderDragon) entity;
            dragon.setHealth(0);
        }
        else if (entity instanceof Wither) {
            Wither wither = (Wither) entity;
            wither.setHealth(0);
        }
        else if (entity instanceof Ghast) {
            Ghast ghast = (Ghast) entity;
            ghast.setHealth(0);
        }
    }

    private void itemRemover(int radius, TregminePlayer p)
    {
        List<Entity> nearby = p.getNearbyEntities(radius, radius, radius);
        int total = 0;
        
        for (Entity entity : nearby) {
            if (entity instanceof Item || entity instanceof Arrow) {
                total++;
                entity.remove();
                continue;
            }
        }
        p.sendMessage(ChatColor.GOLD + "" + total + ChatColor.GREEN + " items successfully removed!");
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
                            if (current instanceof Item || current instanceof Arrow){
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
