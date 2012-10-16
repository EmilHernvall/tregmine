package info.tregmine.sign;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class Color implements Listener {

	
	@EventHandler
	public void onSignChange(SignChangeEvent e)
	 {
		for(int i=0;i<4;i++) 
		{ 
			String[] splitLine = (e.getLine(i)+" ").split("#"); 
			String newLine = splitLine[0]; 
			for(int j=1;j<splitLine.length;j++) 
			{ 
				if(splitLine[j].length()==0 || "0123456789abcdefABCDEF".indexOf(splitLine[j].charAt(0))==-1) 
				{ 
					newLine+="#"; 
				} 
				else 
				{ 
					newLine+="\u00A7"; 
				} 
				newLine+=splitLine[j]; 
			} 
			e.setLine(i,newLine);
		} 
	 }
}