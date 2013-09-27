package info.tregmine.spleef;

import info.tregmine.spleef.cmds.Create;
import info.tregmine.spleef.cmds.Delete;
import info.tregmine.spleef.cmds.ForceStart;
import info.tregmine.spleef.cmds.ForceStop;
import info.tregmine.spleef.cmds.Join;
import info.tregmine.spleef.cmds.Leave;
import info.tregmine.spleef.cmds.Reload;
import info.tregmine.spleef.cmds.SetLoc;
import info.tregmine.spleef.cmds.SubCommand;

import java.util.ArrayList;
import java.util.Arrays;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

	private ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
	
	public void setup() {
		commands.add(new Create());
		commands.add(new Delete());
		commands.add(new ForceStart());
		commands.add(new ForceStop());
		commands.add(new Join());
		commands.add(new Leave());
		commands.add(new Reload());
		commands.add(new SetLoc());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (!(sender instanceof Player)) {
			MessageManager.getInstance().severe(sender, "Only players can use the Spleef plugin!");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("spleef")) {
			if (args.length == 0) {
				for (SubCommand c : commands) {
					MessageManager.getInstance().info(p, "/spleef " + c.name() + " (" + aliases(c) + ")" + " - " + c.info());
				}
				return true;
			}
			
			SubCommand target = get(args[0]);
			
			if (target == null) {
				MessageManager.getInstance().severe(p, "/spleef " + args[0] + " is not a valid subcommand!");
				return true;
			}
			
			ArrayList<String> a = new ArrayList<String>();
			a.addAll(Arrays.asList(args));
			a.remove(0);
			args = a.toArray(new String[a.size()]);
			
			try {
				target.onCommand(p, args);
			}
			
			catch (Exception e) {
				MessageManager.getInstance().severe(p, "An error has occured: " + e.getCause());
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	private String aliases(SubCommand cmd) {
		String fin = "";
		
		for (String a : cmd.aliases()) {
			fin += a + " | ";
		}
		
		return fin.substring(0, fin.lastIndexOf(" | "));
	}
	
	private SubCommand get(String name) {
		for (SubCommand cmd : commands) {
			if (cmd.name().equalsIgnoreCase(name)) return cmd;
			for (String alias : cmd.aliases()) if (name.equalsIgnoreCase(alias)) return cmd;
		}
		return null;
	}
}