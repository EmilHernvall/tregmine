package info.tregmine.christmas.populators;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class IcePopulator extends BlockPopulator {
	private static final int ICE_SPAWN_CHANCE = 90; // Out of 100

	public void populate(World world, Random random, Chunk source) {
		if (random.nextInt(100) <= ICE_SPAWN_CHANCE) {
			int x = (source.getX() << 4) + random.nextInt(8);
			int z = (source.getZ() << 4) + random.nextInt(8);
			int y = world.getHighestBlockYAt(x, z);
			generate(world, random, x, y, z);
		}
	}

	public boolean generate(World world, Random rand, int i, int j, int k) {

		if (world.getBlockAt(i, j - 1, k).getType() != Material.SNOW_BLOCK) {
			return false;
		}

		world.getBlockAt(i, j - 1, k).setType(Material.ICE);
		return true;
	}
}
