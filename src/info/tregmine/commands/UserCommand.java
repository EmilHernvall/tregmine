package info.tregmine.commands;

import java.util.List;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Monster;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.math.Distance;

public class UserCommand extends AbstractCommand
{
    public UserCommand(Tregmine tregmine)
    {
        super(tregmine, "user");
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if (!player.isGuardian() && !player.isAdmin()) {
            return false;
        }
        if (args.length == 0) {
            return false;
        }

        if ("make".equalsIgnoreCase(args[0])) {
            return make(player, args);
        }
        else if ("reload".equalsIgnoreCase(args[0])) {
            return reload(player, args);
        }

        return true;
    }

    private boolean reload(TregminePlayer player, String[] args)
    {
        List<TregminePlayer> candidates = tregmine.matchPlayer(args[1]);
        if (candidates.size() != 1) {
            return true;
        }

        Player candidate = candidates.get(0);
        tregmine.getPlayer(candidate).load();
        player.sendMessage("Player reloaded "+ candidate.getDisplayName());
        return true;
   }

    private boolean make(TregminePlayer player, String[] args)
    {
        Server server = tregmine.getServer();

        List<TregminePlayer> candidates = tregmine.matchPlayer(args[2]);
        if (candidates.size() != 1) {
            return true;
        }

        TregminePlayer victim = candidates.get(0);

        if ("settler".equalsIgnoreCase(args[1])) {
            victim.setMetaString("color", "trial");
            victim.setMetaString("trusted", "true");
            victim.setTemporaryChatName(victim.getNameColor() + victim.getName());

            player.sendMessage(AQUA + "You made " + victim.getChatName() + AQUA + " settler of this server." );
            victim.sendMessage("Welcome! You are now made settler.");
            LOGGER.info(victim.getName() + " was given settler rights by " + player.getName() + ".");
            return true;
        }

        if ("warn".equalsIgnoreCase(args[1])) {
            victim.setMetaString("color", "warned");
            player.sendMessage(AQUA + "You warned " + victim.getChatName() + ".");
            victim.sendMessage("You are now warned");
            LOGGER.info(victim.getName() + " was warned by " + player.getName() + ".");
            victim.setTemporaryChatName(victim.getNameColor() + victim.getName());
            return true;
        }

        if ("hardwarn".equalsIgnoreCase(args[1])) {
            victim.setMetaString("color", "warned");
            victim.setMetaString("trusted", "false");
            player.sendMessage(AQUA + "You warned " + victim.getChatName() + " and removed his building rights." );
            victim.sendMessage("You are now warned and bereft of your building rights.");
            LOGGER.info(victim.getName() + " was hardwarned by " + player.getName() + ".");
            victim.setTemporaryChatName(victim.getNameColor() + victim.getName());
            return true;
        }

        if ("trial".equalsIgnoreCase(args[1])) {
            player.sendMessage(RED + "Please use /user make settler name");
        }

        if ("resident".equalsIgnoreCase(args[1]) && player.isOp()) {
            victim.setMetaString("color", "trusted");
            victim.setMetaString("trusted", "true");
            LOGGER.info(victim.getName() + " was given trusted rights by " + player.getChatName() + ".");
            player.sendMessage(AQUA + "You made " + victim.getChatName() + AQUA + " a resident." );
            victim.sendMessage("Welcome! You are now a resident");
            victim.setTemporaryChatName(victim.getNameColor() + victim.getName());
            return true;
        }

        if ("donator".equalsIgnoreCase(args[1])) {
            victim.setMetaString("donator", "true");
            //victim.setMetaString("compass", "true");
            victim.setFlying(true);
            victim.setMetaString("color", "donator");
            player.sendMessage(AQUA + "You made  " + victim.getChatName() + " a donator." );
            LOGGER.info(victim.getName() + " was made donator by" + player.getChatName() + ".");
            victim.sendMessage("Congratulations, you are now a donator!");
            victim.setTemporaryChatName(victim.getNameColor() + victim.getName());
            return true;
        }

        if ("child".equalsIgnoreCase(args[1])) {
            victim.setMetaString("color", "child");
            player.sendMessage(AQUA + "You made  " + victim.getChatName() + " a child." );
            LOGGER.info(victim.getName() + " was made child by" + player.getChatName() + ".");
            victim.setTemporaryChatName(victim.getNameColor() + victim.getName());
            return true;
        }

        if ("guardian".equalsIgnoreCase(args[1]) && player.isOp()) {
            if (victim.isDonator()) {
                victim.setMetaString("police", "true");
                victim.setMetaString("color", "police");

                player.sendMessage(AQUA + "You made  " + victim.getChatName() + " a police." );
                LOGGER.info(victim.getName() + " was made police by" + player.getChatName() + ".");
                victim.sendMessage("Congratulations, you are now a police!");
            } else {
                player.sendMessage(AQUA + "Sorry this person is not a  " + GOLD + " donator." );
            }
            victim.setTemporaryChatName(victim.getNameColor() + victim.getName());
            return true;
        }

        return false;
    }
}
