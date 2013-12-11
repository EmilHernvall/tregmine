package info.tregmine.christmas.populators;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class SleighPopulator extends BlockPopulator {
	private static final int SLEIGH_SPAWN_CHANCE = 5; // Out of 10000

	public void populate(World world, Random random, Chunk source) {
		if (random.nextInt(10000) <= SLEIGH_SPAWN_CHANCE) {
			int x = (source.getX() << 4) + random.nextInt(16);
			int z = (source.getZ() << 4) + random.nextInt(16);
			int y = world.getHighestBlockYAt(x, z);
			generate(world, random, x, y + 50, z);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean generate(World world, Random rand, int i, int j, int k) {

		world.getBlockAt(i + 0, j + 0, k + 0).setTypeIdAndData(44, (byte) 8, true);
		world.getBlockAt(i + 0, j + 0, k + 3).setTypeIdAndData(44, (byte) 8, true);
		world.getBlockAt(i + 0, j + 1, k + 0).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 0, j + 1, k + 3).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 1, j + 0, k + 0).setTypeIdAndData(43, (byte) 0, true);
		world.getBlockAt(i + 1, j + 0, k + 3).setTypeIdAndData(43, (byte) 0, true);
		world.getBlockAt(i + 1, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 1, j + 2, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 1, j + 2, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 1, j + 2, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 2, j + 0, k + 0).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 2, j + 0, k + 3).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 2, j + 1, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 2, j + 1, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 2, j + 1, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 2, j + 1, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 3, j + 0, k + 0).setTypeIdAndData(43, (byte) 0, true);
		world.getBlockAt(i + 3, j + 0, k + 3).setTypeIdAndData(43, (byte) 0, true);
		world.getBlockAt(i + 3, j + 1, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 3, j + 1, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 3, j + 1, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 3, j + 1, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 4, j + 0, k + 0).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 4, j + 0, k + 3).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 4, j + 1, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 4, j + 1, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 4, j + 1, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 4, j + 1, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 4, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 4, j + 2, k + 1).setTypeIdAndData(53, (byte) 0, true);
		world.getBlockAt(i + 4, j + 2, k + 2).setTypeIdAndData(53, (byte) 0, true);
		world.getBlockAt(i + 4, j + 2, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 5, j + 0, k + 0).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 5, j + 0, k + 3).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 5, j + 1, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 5, j + 1, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 5, j + 1, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 5, j + 1, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 5, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 5, j + 2, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 6, j + 0, k + 0).setTypeIdAndData(43, (byte) 0, true);
		world.getBlockAt(i + 6, j + 0, k + 3).setTypeIdAndData(43, (byte) 0, true);
		world.getBlockAt(i + 6, j + 1, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 6, j + 1, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 6, j + 1, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 6, j + 1, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 6, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 6, j + 2, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 7, j + 0, k + 0).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 7, j + 0, k + 3).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 7, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 7, j + 2, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 7, j + 2, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 7, j + 2, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 8, j + 0, k + 0).setTypeIdAndData(43, (byte) 0, true);
		world.getBlockAt(i + 8, j + 0, k + 3).setTypeIdAndData(43, (byte) 0, true);
		world.getBlockAt(i + 8, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 8, j + 2, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 8, j + 2, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 8, j + 2, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 8, j + 3, k + 0).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 8, j + 3, k + 1).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 8, j + 3, k + 2).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 8, j + 3, k + 3).setTypeIdAndData(35, (byte) 14, true);
		world.getBlockAt(i + 9, j + 0, k + 0).setTypeIdAndData(44, (byte) 8, true);
		world.getBlockAt(i + 9, j + 0, k + 3).setTypeIdAndData(44, (byte) 8, true);
		world.getBlockAt(i + 9, j + 1, k + 0).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 9, j + 1, k + 3).setTypeIdAndData(44, (byte) 0, true);
		world.getBlockAt(i + 1, j + 3, k + 0).setTypeIdAndData(76, (byte) 5, true);
		world.getBlockAt(i + 1, j + 3, k + 3).setTypeIdAndData(76, (byte) 5, true);
		world.getBlockAt(i + 4, j + 3, k + 0).setTypeIdAndData(76, (byte) 5, true);
		world.getBlockAt(i + 4, j + 3, k + 3).setTypeIdAndData(76, (byte) 5, true);
		world.getBlockAt(i + 8, j + 4, k + 0).setTypeIdAndData(76, (byte) 5, true);
		world.getBlockAt(i + 8, j + 4, k + 1).setTypeIdAndData(76, (byte) 5, true);
		world.getBlockAt(i + 8, j + 4, k + 2).setTypeIdAndData(76, (byte) 5, true);
		world.getBlockAt(i + 8, j + 4, k + 3).setTypeIdAndData(76, (byte) 5, true);

		return true;
	}
}