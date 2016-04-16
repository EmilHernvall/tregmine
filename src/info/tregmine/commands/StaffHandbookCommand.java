package info.tregmine.commands;

import java.util.List;

import org.bukkit.ChatColor;

import info.tregmine.Tregmine;
import info.tregmine.api.TregminePlayer;
import info.tregmine.database.DAOException;
import info.tregmine.database.IContext;
import info.tregmine.database.IHandbookDAO;
import info.tregmine.database.IHomeDAO;

public class StaffHandbookCommand extends AbstractCommand{
	Tregmine t;
	public StaffHandbookCommand(Tregmine instance){
		super(instance, "staffbook");
		this.t = instance;
	}
	public boolean handlePlayer(TregminePlayer a, String[] b){
		if(!a.getIsStaff()){
			a.nopermsMessage(false, "staffbook");
			return true;
		}
		try (IContext ctx = tregmine.createContext()) {
            IHandbookDAO c = ctx.getHandbookDAO();
            List<String[]> handbook = c.getHandbook();
            if(handbook.size() == 0){
            	a.sendMessage(ChatColor.RED + "There are no rules in the handbook.");
            	return true;
            }
            a.sendMessage(ChatColor.GOLD + t.getConfig().getString("general.servername") + ChatColor.AQUA + " Staff Handbook");
            for(String[] rule : handbook){
            	a.sendMessage(ChatColor.translateAlternateColorCodes('#', rule[0]) + ChatColor.RESET + ". " + ChatColor.translateAlternateColorCodes('#', rule[1]));
            }
        } catch (DAOException e) {
        	a.sendMessage(ChatColor.RED + "Something bad happened...");
            throw new RuntimeException(e);
        }
		return true;
	}
}
