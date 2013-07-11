package info.tregmine.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignColorListener implements Listener
{
    private final static String CHARS = "0123456789abcdefknomrlABCDEFKLNOMR";

    public SignColorListener()
    {
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e)
    {
        for (int i = 0; i < 4; i++) {
            String[] splitLine = (e.getLine(i) + " ").split("#");
            String newLine = splitLine[0];
            for (int j = 1; j < splitLine.length; j++) {
                if (splitLine[j].length() == 0
                        || CHARS.indexOf(splitLine[j].charAt(0)) == -1) {

                    newLine += "#";
                }
                else {
                    newLine += "\u00A7";
                }
                newLine += splitLine[j];
            }
            e.setLine(i, newLine);
        }
    }
}
