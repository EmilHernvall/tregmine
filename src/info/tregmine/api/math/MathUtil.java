package info.tregmine.api.math;

import info.tregmine.Tregmine;

import java.util.logging.Level;

import org.bukkit.Location;

/**
 * Basic math functions
 */

public class MathUtil
{

    /**
     * Calculates the distance in 2d (x,z)
     * 
     * @param loc1
     *            location of the first entity
     * @param loc2
     *            location of the first entity
     * @return The distant
     */
    public static double calc2d(Location loc1, Location loc2)
    {
        double x = Math.pow(loc1.getX() - loc2.getX(), 2);
        double z = Math.pow(loc1.getZ() - loc2.getZ(), 2);
        return Math.sqrt(x + z);
    }
    
    /**
     * Calculates the value of percentage of a number.
     * 
     * Example: <code> calcPercent(100, 20);</code> would return 20,
     * and <code> calcPercent(200, 10);</code> would yield the same result.
     * 
     * @param start - The base number.
     * @param percentage - The percentage to calculate. Can not be greater than or equal to 100,
     * and can no be 0.
     * @return <code>percentage</code> percent of <code>start</code>.
     */
    public static long percentage(long start, int percentage)
    {
        if(percentage > 100 || percentage < 0){
            Tregmine.LOGGER.log(Level.WARNING, "Illegal percentage value.");
        }
        double perc = (percentage / 100.0);
        return (long) (start * perc);
    }

}
