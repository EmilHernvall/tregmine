package info.tregmine.api;

import org.bukkit.ChatColor;

public class Coloring {
	public String reverseChatColor(String v, char c){
		String manipulate = v;
		v.replace(ChatColor.WHITE.toString(), c+"f");
		v.replace(ChatColor.YELLOW.toString(), c+"e");
		v.replace(ChatColor.LIGHT_PURPLE.toString(), c+"d");
		v.replace(ChatColor.RED.toString(), c+"c");
		v.replace(ChatColor.AQUA.toString(), c+"b");
		v.replace(ChatColor.GREEN.toString(), c+"a");
		v.replace(ChatColor.BLUE.toString(), c+"9");
		v.replace(ChatColor.DARK_GRAY.toString(), c+"8");
		v.replace(ChatColor.GRAY.toString(), c+"7");
		v.replace(ChatColor.GOLD.toString(), c+"6");
		v.replace(ChatColor.DARK_PURPLE.toString(), c+"5");
		v.replace(ChatColor.DARK_RED.toString(), c+"4");
		v.replace(ChatColor.DARK_AQUA.toString(), c+"3");
		v.replace(ChatColor.DARK_GREEN.toString(), c+"2");
		v.replace(ChatColor.DARK_BLUE.toString(), c+"1");
		v.replace(ChatColor.BLACK.toString(), c+"0");
		v.replace(ChatColor.MAGIC.toString(), c+"k");
		v.replace(ChatColor.BOLD.toString(), c+"l");
		v.replace(ChatColor.STRIKETHROUGH.toString(), c+"m");
		v.replace(ChatColor.UNDERLINE.toString(), c+"n");
		v.replace(ChatColor.ITALIC.toString(), c+"o");
		v.replace(ChatColor.RESET.toString(), c+"r");
		return manipulate;
	}
}
