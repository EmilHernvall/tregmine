package info.tregmine.commands;

import static org.bukkit.ChatColor.*;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;

public class SayCommand extends AbstractCommand
{
    public SayCommand(Tregmine tregmine)
    {
        super(tregmine, "say");
    }

    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        buf.append(args[0]);
        for (int i = 1; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

    @Override
    public boolean handlePlayer(TregminePlayer player, String[] args)
    {
        if(player.isAdmin()){
        Server server = player.getServer();
        String msg = argsToMessage(args);

        server.broadcastMessage("<" + RED + "GOD" + WHITE + "> " + LIGHT_PURPLE
                + msg);

        LOGGER.info(player.getName() + ": <GOD> " + msg);

        Player[] players = server.getOnlinePlayers();
        for (Player p : players) {
            TregminePlayer current = tregmine.getPlayer((p.getName()));
            if (current.isAdmin()) {
                current.sendMessage(DARK_AQUA + "/say used by: "
                        + player.getChatName());
            }
        }
    }
        return true;
    }

    @Override
    public boolean handleOther(Server server, String[] args)
    {
        String msg = argsToMessage(args);

        server.broadcastMessage("<" + BLUE + "GOD" + WHITE + "> "
                + LIGHT_PURPLE + msg);
        LOGGER.info("CONSOLE: <GOD> " + msg);

        return true;
    }
}
