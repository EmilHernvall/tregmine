package info.tregmine.christmas.populators;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class CandyCanePopulator extends BlockPopulator {
	private static final int CANDYCANE_SPAWN_CHANCE = 20; // Out of 100

	public void populate(World world, Random random, Chunk source) {
		if (random.nextInt(100) <= CANDYCANE_SPAWN_CHANCE) {
			int x = (source.getX() << 4) + random.nextInt(8);
			int z = (source.getZ() << 4) + random.nextInt(8);
			int y = world.getHighestBlockYAt(x, z);
			generate(world, random, x, y, z);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean generate(World world, Random rand, int i, int j, int k) {

		if (world.getBlockAt(i, j - 1, k).getType() != Material.SNOW_BLOCK) {
			return false;
		}

		int randdir = (int )(Math.random() * 4 + 1);

		if( randdir == 1 ){
			world.getBlockAt(i + 0, j + 0, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 1, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 3, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 4, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 4, k + 1).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 4, k + 2).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 3, k + 2).setTypeIdAndData(35, (byte) 0, true);
		}else if( randdir == 2 ){
			world.getBlockAt(i + 0, j + 0, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 1, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 3, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 4, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 4, k - 1).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 4, k - 2).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 3, k - 2).setTypeIdAndData(35, (byte) 0, true);
		}else if( randdir == 3 ){
			world.getBlockAt(i + 0, j + 0, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 1, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 3, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 4, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 1, j + 4, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 2, j + 4, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 2, j + 3, k + 0).setTypeIdAndData(35, (byte) 0, true);
		}else{
			world.getBlockAt(i + 0, j + 0, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 1, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 2, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i + 0, j + 3, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i + 0, j + 4, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i - 1, j + 4, k + 0).setTypeIdAndData(35, (byte) 0, true);
			world.getBlockAt(i - 2, j + 4, k + 0).setTypeIdAndData(35, (byte) 14, true);
			world.getBlockAt(i - 2, j + 3, k + 0).setTypeIdAndData(35, (byte) 0, true);
		}
		
		return true;
	}
}
