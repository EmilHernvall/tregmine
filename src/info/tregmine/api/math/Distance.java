package info.tregmine.api.math;

import org.bukkit.Location;

/**
 * Basic math function to calculate distances
 */

public class Distance
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
        if (loc1.getWorld().equals(loc2.getWorld())) {
            double x = Math.pow(loc1.getX() - loc2.getX(), 2);
            double z = Math.pow(loc1.getZ() - loc2.getZ(), 2);
            return Math.sqrt(x + z);
        } else {
            return Integer.MAX_VALUE;
        }
    }

}
