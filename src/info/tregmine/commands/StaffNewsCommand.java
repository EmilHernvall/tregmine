package info.tregmine.commands;

import static org.bukkit.ChatColor.*;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;

import info.tregmine.Tregmine;
import info.tregmine.api.PlayerReport;
import info.tregmine.api.Rank;
import info.tregmine.api.StaffNews;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.TregminePlayer.Flags;
import info.tregmine.commands.NotifyCommand;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IPlayerReportDAO;
import info.tregmine.database.IStaffNewsDAO;

public class StaffNewsCommand extends AbstractCommand{
	public StaffNewsCommand(Tregmine tregmine){
		super(tregmine, "staffnews");
	}

    private String argsToMessage(String[] args)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < args.length; ++i) {
            buf.append(" ");
            buf.append(args[i]);
        }

        return buf.toString();
    }

public boolean handlePlayer(TregminePlayer player, String[] args){
	if(!player.getIsStaff()){
		player.nopermsMessage(false, "staffnews");
		return true;
	}
	
	String message = argsToMessage(args);
	LOGGER.info(player.getRealName() + " has added the following message to the staff news board: " + message);
	try (IContext ctx = tregmine.createContext()) {
        StaffNews news = new StaffNews();
        news.setText(message);
        news.setUsername(player.getRealName());
        IStaffNewsDAO newsDAO = ctx.getNewsByUploader();
        newsDAO.insertNews(news);
    } catch (DAOException e) {
        throw new RuntimeException(e);
    }
	player.sendMessage(BLUE + "I DID IT STFU");
	return true;
}


}