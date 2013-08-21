package info.tregmine.gamemagic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class Gates implements Listener
{
    private GameMagic plugin;

    public final static Logger LOGGER = Logger.getLogger("Minecraft");

    public Gates(GameMagic instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void onForaButtons(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK) {

            return;
        }

        Location bLoc = event.getClickedBlock().getLocation();
        Player player = event.getPlayer();

        //Camrenn's Mansion Gate
        if ((bLoc.getX() == 12199 && bLoc.getY() == 65 && bLoc.getZ() == -69964) ||
            (bLoc.getX() == 12184 && bLoc.getY() == 65) && (bLoc.getZ() == -69962)) {

            if(player.getLocation().getWorld().getBlockAt(12198, 63, -69964).getType().equals(Material.DIRT)){

                //Open The Gate

                int airX = 12189;
                for (int airY = 64; airY <= 68; airY++){
                    for (int airZ = -69973; airZ <= -69964; airZ++){
                        Location location = new Location(player.getLocation().getWorld(), airX, airY, airZ);
                        location.getBlock().setType(Material.AIR);
                    }
                }

                //Open The Boomgate

                Location black1 = new Location(player.getLocation().getWorld(), 12197, 67, -69964);
                black1.getBlock().setType(Material.WOOL);
                black1.getBlock().setData((byte) 15);

                Location black2 = new Location(player.getLocation().getWorld(), 12197, 69, -69964);
                black2.getBlock().setType(Material.WOOL);
                black2.getBlock().setData((byte) 15);

                Location yellow1 = new Location(player.getLocation().getWorld(), 12197, 66, -69964);
                yellow1.getBlock().setType(Material.WOOL);
                yellow1.getBlock().setData((byte) 4);

                Location yellow2 = new Location(player.getLocation().getWorld(), 12197, 68, -69964);
                yellow2.getBlock().setType(Material.WOOL);
                yellow2.getBlock().setData((byte) 4);

                int boomairX = 12197;
                int boomairY = 65;
                for (int boomairZ = -69968; boomairZ <= -69965; boomairZ++){
                    Location location = new Location(player.getLocation().getWorld(), boomairX, boomairY, boomairZ);
                    location.getBlock().setType(Material.AIR);
                }

                //Set The Check State Block

                Location checkblock = new Location(player.getLocation().getWorld(), 12198, 63, -69964);
                checkblock.getBlock().setType(Material.GLASS);

                player.sendMessage(ChatColor.AQUA + "Opening The Gate");
            }

            else if(player.getLocation().getWorld().getBlockAt(12198, 63, -69964).getType().equals(Material.GLASS)){

                //Close The Gate

                int airX = 12189;
                for (int airY = 64; airY <= 68; airY++){
                    for (int airZ = -69973; airZ <= -69964; airZ++){
                        Location location = new Location(player.getLocation().getWorld(), airX, airY, airZ);
                        location.getBlock().setType(Material.WOOL);
                        location.getBlock().setData((byte) 15);
                    }
                }

                for (int z = -69972; z <= -69970; z++){
                    int x = 12189;
                    int y1 = 67;
                    Location location = new Location(player.getLocation().getWorld(), x, y1, z);
                    location.getBlock().setType(Material.IRON_BLOCK);
                }

                for (int z = -69967; z <= -69965; z++){
                    int x = 12189;
                    int y1 = 67;
                    Location location = new Location(player.getLocation().getWorld(), x, y1, z);
                    location.getBlock().setType(Material.IRON_BLOCK);
                }

                for (int z = -69972; z <= -69970; z++){
                    int x = 12189;
                    int y2 = 65;
                    Location location = new Location(player.getLocation().getWorld(), x, y2, z);
                    location.getBlock().setType(Material.IRON_BLOCK);
                }

                for (int z = -69967; z <= -69965; z++){
                    int x = 12189;
                    int y2 = 65;
                    Location location = new Location(player.getLocation().getWorld(), x, y2, z);
                    location.getBlock().setType(Material.IRON_BLOCK);
                }

                //Close The Boomgate

                Location black1 = new Location(player.getLocation().getWorld(), 12197, 65, -69966);
                black1.getBlock().setType(Material.WOOL);
                black1.getBlock().setData((byte) 15);

                Location black2 = new Location(player.getLocation().getWorld(), 12197, 65, -69968);
                black2.getBlock().setType(Material.WOOL);
                black2.getBlock().setData((byte) 15);

                Location yellow1 = new Location(player.getLocation().getWorld(), 12197, 65, -69965);
                yellow1.getBlock().setType(Material.WOOL);
                yellow1.getBlock().setData((byte) 4);

                Location yellow2 = new Location(player.getLocation().getWorld(), 12197, 65, -69967);
                yellow2.getBlock().setType(Material.WOOL);
                yellow2.getBlock().setData((byte) 4);

                //Remove Old Boomgate State

                int boomairX = 12197;
                int boomairZ = -69964;
                for (int boomairY = 66; boomairY <= 69; boomairY++){
                    Location location = new Location(player.getLocation().getWorld(), boomairX, boomairY, boomairZ);
                    location.getBlock().setType(Material.AIR);
                }

                //Set The Check State Block

                Location checkblock = new Location(player.getLocation().getWorld(), 12198, 63, -69964);
                checkblock.getBlock().setType(Material.DIRT);

                player.sendMessage(ChatColor.AQUA + "Closing The Gate");

            }
        }

        //Blockville Gate
        if ((bLoc.getX() == -101843 && bLoc.getY() == 62) && (bLoc.getZ() == -3445) || (bLoc.getX() == -101839 && bLoc.getY() == 62) && (bLoc.getZ() == -3445)) {
            if(player.getLocation().getWorld().getBlockAt(-101841, 61, -3445).getType().equals(Material.DIRT)){

                int gateairX = -101841;
                for (int gateairY = 63; gateairY <= 67; gateairY++){
                    for (int gateairZ = -3449; gateairZ <= -3441; gateairZ++){
                        Location location = new Location(player.getLocation().getWorld(), gateairX, gateairY, gateairZ);
                        location.getBlock().setType(Material.AIR);
                    }
                }

                int air1Y = 68;
                for (int gateairZ = -3448; gateairZ <= -3442; gateairZ++){
                    Location location = new Location(player.getLocation().getWorld(), gateairX, air1Y, gateairZ);
                    location.getBlock().setType(Material.AIR);
                }

                int air2Y = 69;
                for (int gateairZ = -3447; gateairZ <= -3443; gateairZ++){
                    Location location = new Location(player.getLocation().getWorld(), gateairX, air2Y, gateairZ);
                    location.getBlock().setType(Material.AIR);
                }

                int air3Y = 70;
                for (int gateairZ = -3446; gateairZ <= -3444; gateairZ++){
                    Location location = new Location(player.getLocation().getWorld(), gateairX, air3Y, gateairZ);
                    location.getBlock().setType(Material.AIR);
                }

                //Set The Check State Block

                Location checkblock = new Location(player.getLocation().getWorld(), -101841, 61, -3445);
                checkblock.getBlock().setType(Material.GLASS);

                player.sendMessage(ChatColor.AQUA + "Opening The Gate");
            }

            else if(player.getLocation().getWorld().getBlockAt(-101841, 61, -3445).getType().equals(Material.GLASS)){

                //Close The Gate

                int gateairX = -101841;
                for (int gateairY = 63; gateairY <= 67; gateairY++){
                    for (int gateairZ = -3449; gateairZ <= -3441; gateairZ++){
                        Location location = new Location(player.getLocation().getWorld(), gateairX, gateairY, gateairZ);
                        location.getBlock().setType(Material.WOOL);
                        location.getBlock().setData((byte) 15);
                    }
                }

                int air1Y = 68;
                for (int gateairZ = -3448; gateairZ <= -3442; gateairZ++){
                    Location location = new Location(player.getLocation().getWorld(), gateairX, air1Y, gateairZ);
                    location.getBlock().setType(Material.WOOL);
                    location.getBlock().setData((byte) 15);
                }

                int air2Y = 69;
                for (int gateairZ = -3447; gateairZ <= -3443; gateairZ++){
                    Location location = new Location(player.getLocation().getWorld(), gateairX, air2Y, gateairZ);
                    location.getBlock().setType(Material.WOOL);
                    location.getBlock().setData((byte) 15);
                }

                int air3Y = 70;
                for (int gateairZ = -3446; gateairZ <= -3444; gateairZ++){
                    Location location = new Location(player.getLocation().getWorld(), gateairX, air3Y, gateairZ);
                    location.getBlock().setType(Material.WOOL);
                    location.getBlock().setData((byte) 15);
                }

                //Set The Check State Block

                Location checkblock = new Location(player.getLocation().getWorld(), -101841, 61, -3445);
                checkblock.getBlock().setType(Material.DIRT);

                player.sendMessage(ChatColor.AQUA + "Closing The Gate");
            }
        }
    }
}


/** Deanna's gate Info - Keep for Future Reference

//whole - Black Wool
//12189 64 -69964
//12189 68 -69973
//12189, 68, -69973 to 12189, 64, -69964

//line 1 - Iron Block
//12189 67 -69972
//12189 67 -69970

//line 2 - Iron Block
//12189 67 -69967
//12189 67 -69965

//line 3 - Iron Block
//12189 65 -69972
//12189 65 -69970

//line 4 - Iron Block
//12189 65 -69967
//12189 65 -69965

//Button 1 - Outside Property
//12199 65 -69964

//Button 2 - Inside Property
//12185 65 -69962

//Check State Block
//12198 63 -69964

//Boomgate Closed
//12197 65 -69965 and 12197 65 -69967 - Yellow Wool
//12197 65 -69966 and 12197 65 -69968 - Black Wool

//Boomgate Open
//12197 66 -69964 and 12197 68 -69964 - Yellow Wool
//12197 67 -69964 and 12197 69 -69964 - Black Wool

 */

/** Blockville gate Info - Keep for Future Reference

//main of gate - Black Wool
//-101841, 63, -3441
//-101841, 67, -3449

//3rd from top of gate - Black Wool
//-101841, 68, -3448
//-101841, 68, -3442

//2nd from top of gate - Black Wool
//-101841, 69, -3443
//-101841, 69, -3447

//top of gate - Black Wool
//-101841, 70, -3446
//-101841, 70, -3444

//Switch 1 - Outside Gate
//-101843, 62, -3445

//Switch 2 - Inside Gate
//-101839, 62, -3445

//Check State Block
//-101841, 61, -3445

*/
