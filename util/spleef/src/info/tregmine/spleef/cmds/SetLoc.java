package info.tregmine.spleef.cmds;

import info.tregmine.api.TregminePlayer;
import info.tregmine.spleef.MessageManager;
import info.tregmine.spleef.SettingsManager;
import info.tregmine.spleef.Spleef;
import info.tregmine.spleef.ArenaManager.Team;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SetLoc extends SubCommand {
    
    Block pos1;
    Block pos2;
    int minBlockX;
    int minBlockY;
    int minBlockZ;
    int maxBlockX;
    int maxBlockY;
    int maxBlockZ;
    World world;

    public void onCommand(Player p, String[] args) {

        //TODO Remove when development finishes and add Tregmine Ranks
        if (!p.isOp() && !p.getName().equals("ice374")) {
            MessageManager.getInstance().severe(p, "Sorry, You dont have permission to perform this command.");
            return;
        }

        Block b = p.getTargetBlock(null, 5);
        Location selected = b.getLocation();

        if (args.length < 2) {
            MessageManager.getInstance().severe(p, "You did not specify enough arguments.");
            return;
        }

        int id = -1;

        try { id = Integer.parseInt(args[0]); }
        catch (Exception e) {
            MessageManager.getInstance().severe(p, args[0] + " is not a valid number!");
            return;
        }

        if (SettingsManager.getInstance().<ConfigurationSection>get(id + "") == null) {
            MessageManager.getInstance().severe(p, "There is no arena with ID " + id + "!");
            return;
        }

        /**
         * The exit argument was for testing, un-comment it out if you need to test something
         */

        /*
        if (args[1].equalsIgnoreCase("exit")){
            if (args.length < 2){
                MessageManager.getInstance().severe(p, "You did not specify enough arguments.");
                return;
            }
            if (args[2].equalsIgnoreCase("win")){
                //Set win location
                ConfigurationSection s = SettingsManager.getInstance().createConfigurationSection(id + "." + "win");

                s.set("world", p.getWorld().getName());
                s.set("x", p.getLocation().getX());
                s.set("y", p.getLocation().getY());
                s.set("z", p.getLocation().getZ());

                SettingsManager.getInstance().set(id + "." + "win", s);

                MessageManager.getInstance().good(p, "Set " + "winner exit!");
            }
            else if (args[2].equalsIgnoreCase("lose")){
                //Set lose location
                ConfigurationSection s = SettingsManager.getInstance().createConfigurationSection(id + "." + "lose");

                s.set("world", p.getWorld().getName());
                s.set("x", p.getLocation().getX());
                s.set("y", p.getLocation().getY());
                s.set("z", p.getLocation().getZ());

                SettingsManager.getInstance().set(id + "." + "lose", s);

                MessageManager.getInstance().good(p, "Set " + "loser exit!");
            }
            else{
                MessageManager.getInstance().good(p, "Correct Syntax: /spleef setloc <id> exit <win|lose>");
            }
        }
        else 
         */

        if (args[1].equalsIgnoreCase("head")){
            if (args[2].equalsIgnoreCase("winner")){

                BlockState block = selected.getBlock().getState();
                if (!(block instanceof Skull)) {                        
                    MessageManager.getInstance().severe(p, "You must be looking at a player head!");
                    return;
                }
                Skull skull = (Skull) block;

                ConfigurationSection s = SettingsManager.getInstance().createConfigurationSection(id + "." + "head" + "." + "latestwinner");

                s.set("world", p.getWorld().getName());
                s.set("x", selected.getBlockX());
                s.set("y", selected.getBlockY());
                s.set("z", selected.getBlockZ());
                s.set("rotation", skull.getRotation().toString());

                SettingsManager.getInstance().set(id + "." + "head" + "." + "latestwinner", s);

                MessageManager.getInstance().good(p, "Latest winner head setup");
            }
            else if (args[2].equalsIgnoreCase("score")){
                //For when we add the leaderboard :)
            }
            else{
                MessageManager.getInstance().good(p, "Correct Syntax: /spleef setloc <id> head winner");
                MessageManager.getInstance().good(p, "Or              /spleef setloc <id> head score <1|2|3>");
            }
        }


        else if (args[1].equalsIgnoreCase("corner")){
            if (args.length < 2){
                MessageManager.getInstance().severe(p, "You did not specify enough arguments.");
                return;
            }
            if (args[2].equalsIgnoreCase("1")){

                MessageManager.getInstance().good(p, "Selected corner " + args[2]);

                pos1 = selected.getBlock();

                calculateCoordinates(p, id);
            }
            else if (args[2].equalsIgnoreCase("2")){

                MessageManager.getInstance().good(p, "Selected corner " + args[2]);

                pos2 = selected.getBlock();

                calculateCoordinates(p, id);
            }
            else{
                MessageManager.getInstance().good(p, "Correct Syntax: /spleef setloc <id> corner <1|2>");
            }
        }

        else{

            Team team = null;

            try { team = Team.valueOf(args[1].toUpperCase()); }
            catch (Exception e) { 
                MessageManager.getInstance().severe(p, args[1] + " is not a valid team!");
                MessageManager.getInstance().severe(p, "Valid teams: Red, Blue, Yellow, Green, Black");
                return;
            }

            ConfigurationSection s = SettingsManager.getInstance().createConfigurationSection(id + "." + team.toString().toLowerCase() + "spawn");

            s.set("world", p.getWorld().getName());
            s.set("x", p.getLocation().getX());
            s.set("y", p.getLocation().getY());
            s.set("z", p.getLocation().getZ());

            SettingsManager.getInstance().set(id + "." + team.toString().toLowerCase() + "spawn", s);

            MessageManager.getInstance().good(p, "Set " + team.toString().toLowerCase() + " spawn!");
        }
    }

    private void calculateCoordinates(Player p, Integer id)
    {
        Location pos1Location = this.pos1 != null ? this.pos1.getLocation() : null;
        Location pos2Location = this.pos2 != null ? this.pos2.getLocation() : null;

        if ((pos1Location == null) && (pos2Location == null)) {
            MessageManager.getInstance().severe(p, "Umm... how did you get this message to appear?");
            return;
        }

        if (pos1Location == null){
            MessageManager.getInstance().good(p, "Now please choose the first corner.");
            return;
        }
        if (pos2Location == null) {
            MessageManager.getInstance().good(p, "Now please choose the second corner.");
            return;
        }

        this.minBlockX = Math.min(pos1Location.getBlockX(), pos2Location.getBlockX());
        this.minBlockY = Math.min(pos1Location.getBlockY(), pos2Location.getBlockY());
        this.minBlockZ = Math.min(pos1Location.getBlockZ(), pos2Location.getBlockZ());
        this.maxBlockX = Math.max(pos1Location.getBlockX(), pos2Location.getBlockX());
        this.maxBlockY = Math.max(pos1Location.getBlockY(), pos2Location.getBlockY());
        this.maxBlockZ = Math.max(pos1Location.getBlockZ(), pos2Location.getBlockZ());

        //Set min arena location
        ConfigurationSection s = SettingsManager.getInstance().createConfigurationSection(id + "." + "minArenaLoc");

        s.set("world", p.getWorld().getName());
        s.set("x", minBlockX);
        s.set("y", minBlockY);
        s.set("z", minBlockZ);

        SettingsManager.getInstance().set(id + "." + "minArenaLoc", s);

        ConfigurationSection b = SettingsManager.getInstance().createConfigurationSection(id + "." + "maxArenaLoc");

        b.set("world", p.getWorld().getName());
        b.set("x", maxBlockX);
        b.set("y", maxBlockY);
        b.set("z", maxBlockZ);

        SettingsManager.getInstance().set(id + "." + "maxArenaLoc", b);

        MessageManager.getInstance().good(p, "Successfully setup arena area! ");
        MessageManager.getInstance().info(p, "Min: " + minBlockX + ", " + minBlockY + ", " + minBlockZ + ".");
        MessageManager.getInstance().info(p, "Max: " + maxBlockX + ", " + maxBlockY + ", " + maxBlockZ + ".");
    }

    public String name() {
        return "setloc";
    }

    public String info() {
        return "Set a location";
    }

    public String[] aliases() {
        return new String[] { "s" };
    }
}
