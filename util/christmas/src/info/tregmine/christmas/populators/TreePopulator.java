
package info.tregmine.christmas.populators;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class TreePopulator extends BlockPopulator {
   
    public void populate(World world, Random random, Chunk source) {
        if (random.nextInt(100) <= 95) {
            int x = (source.getX() << 4) + random.nextInt(8);
            int z = (source.getZ() << 4) + random.nextInt(8);
            int y = world.getHighestBlockYAt(x, z);
            generate(world, random, x, y, z);
        }
    }

    public boolean generate(World world, Random rand, int i, int j, int k) {
    	
		if (world.getBlockAt(i, j - 2, k).getType() != (Material.SNOW_BLOCK)){
			return false;
		}
    	
    	Location loc1 = new Location(world, i, j - 1, k );
    	world.getBlockAt(loc1).setType(Material.GRASS);
    	
    	Location loc = new Location(world, i, j, k );
    	world.generateTree(loc, TreeType.REDWOOD);
    	
    	world.getBlockAt(loc1).setType(Material.SNOW_BLOCK);
    	
    	return true;
    }
}
