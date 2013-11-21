
package info.tregmine.christmas.populators;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class PresentPopulator extends BlockPopulator {

	private static final int PRESENT_SPAWN_CHANCE = 8; // Out of 100

	public void populate(World world, Random random, Chunk source) {
		if (random.nextInt(100) <= PRESENT_SPAWN_CHANCE) {
			int x = (source.getX() << 4) + random.nextInt(16);
			int z = (source.getZ() << 4) + random.nextInt(16);
			int y = world.getHighestBlockYAt(x, z);
			generate(world, random, x-1 , y-1, z-1);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean generate(World world, Random rand, int i, int j, int k) {

		if (world.getBlockAt(i, j, k).getType() != (Material.SNOW_BLOCK)){
			return false;
		}

		int color;
		int randColor = (int )(Math.random() * 10 + 1);

		if( randColor == 1 ){
			color = 2;
		}else if( randColor == 2 ){
			color = 3;
		}else if( randColor == 3 ){
			color = 4;
		}else if( randColor == 4 ){
			color = 5;
		}else if( randColor == 5 ){
			color = 6;
		}else if( randColor == 6 ){
			color = 7;
		}else if( randColor == 7 ){
			color = 8;
		}else if( randColor == 8 ){
			color = 9;
		}else if( randColor == 9 ){
			color = 10;
		}else{
			color = 11;
		}

		//Layer 1
		world.getBlockAt(i + 0, j + 0, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 0, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 0, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 0, j + 0, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 0, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 1, j + 0, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 0, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 0, k + 3).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 0, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 0, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 2, j + 0, k + 1).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 2, j + 0, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 2, j + 0, k + 3).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 2, j + 0, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 2, j + 0, k + 5).setTypeIdAndData(35, (byte) 15, true);

		world.getBlockAt(i + 3, j + 0, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 0, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 0, k + 3).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 0, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 0, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 4, j + 0, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 0, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 0, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 4, j + 0, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 0, k + 5).setTypeIdAndData(35, (byte) color, true);

		//Layer 2
		world.getBlockAt(i + 0, j + 1, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 1, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 1, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 0, j + 1, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 1, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 1, j + 1, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 1, k + 2).setTypeId(92);
		world.getBlockAt(i + 1, j + 1, k + 3).setTypeId(92);
		world.getBlockAt(i + 1, j + 1, k + 4).setTypeId(92);
		world.getBlockAt(i + 1, j + 1, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 2, j + 1, k + 1).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 2, j + 1, k + 2).setTypeId(92);
		world.getBlockAt(i + 2, j + 1, k + 3).setTypeId(92);
		world.getBlockAt(i + 2, j + 1, k + 4).setTypeId(92);
		world.getBlockAt(i + 2, j + 1, k + 5).setTypeIdAndData(35, (byte) 15, true);

		world.getBlockAt(i + 3, j + 1, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 1, k + 2).setTypeId(92);
		world.getBlockAt(i + 3, j + 1, k + 3).setTypeId(92);
		world.getBlockAt(i + 3, j + 1, k + 4).setTypeId(92);
		world.getBlockAt(i + 3, j + 1, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 4, j + 1, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 1, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 1, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 4, j + 1, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 1, k + 5).setTypeIdAndData(35, (byte) color, true);

		//Layer 3
		world.getBlockAt(i + 0, j + 2, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 2, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 2, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 0, j + 2, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 2, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 1, j + 2, k + 1).setTypeIdAndData(35, (byte) color, true);
		//air
		//air
		//air
		world.getBlockAt(i + 1, j + 2, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 2, j + 2, k + 1).setTypeIdAndData(35, (byte) 15, true);
		//air
		//air
		//air
		world.getBlockAt(i + 2, j + 2, k + 5).setTypeIdAndData(35, (byte) 15, true);

		world.getBlockAt(i + 3, j + 2, k + 1).setTypeIdAndData(35, (byte) color, true);
		//air
		//air
		//air
		world.getBlockAt(i + 3, j + 2, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 4, j + 2, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 2, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 2, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 4, j + 2, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 2, k + 5).setTypeIdAndData(35, (byte) color, true);

		//Layer 4
		world.getBlockAt(i + 0, j + 3, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 3, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 3, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 0, j + 3, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 3, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 1, j + 3, k + 1).setTypeIdAndData(35, (byte) color, true);
		//air
		//air
		//air
		world.getBlockAt(i + 1, j + 3, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 2, j + 3, k + 1).setTypeIdAndData(35, (byte) 15, true);
		//air
		//air
		//air
		world.getBlockAt(i + 2, j + 3, k + 5).setTypeIdAndData(35, (byte) 15, true);

		world.getBlockAt(i + 3, j + 3, k + 1).setTypeIdAndData(35, (byte) color, true);
		//air
		//air
		//air
		world.getBlockAt(i + 3, j + 3, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 4, j + 3, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 3, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 3, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 4, j + 3, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 3, k + 5).setTypeIdAndData(35, (byte) color, true);

		//Layer 5
		world.getBlockAt(i + 0, j + 4, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 4, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 4, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 0, j + 4, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 0, j + 4, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 1, j + 4, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 4, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 4, k + 3).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 4, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 1, j + 4, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 2, j + 4, k + 1).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 2, j + 4, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 2, j + 4, k + 3).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 2, j + 4, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 2, j + 4, k + 5).setTypeIdAndData(35, (byte) 15, true);

		world.getBlockAt(i + 3, j + 4, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 4, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 4, k + 3).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 4, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 3, j + 4, k + 5).setTypeIdAndData(35, (byte) color, true);

		world.getBlockAt(i + 4, j + 4, k + 1).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 4, k + 2).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 4, k + 3).setTypeIdAndData(35, (byte) 15, true);
		world.getBlockAt(i + 4, j + 4, k + 4).setTypeIdAndData(35, (byte) color, true);
		world.getBlockAt(i + 4, j + 4, k + 5).setTypeIdAndData(35, (byte) color, true);

		//Bow
		world.getBlockAt(i + 1, j + 5, k + 3).setTypeIdAndData(35, (byte) 4, true);

		world.getBlockAt(i + 2, j + 5, k + 2).setTypeIdAndData(35, (byte) 4, true);
		world.getBlockAt(i + 2, j + 5, k + 4).setTypeIdAndData(35, (byte) 4, true);

		world.getBlockAt(i + 3, j + 5, k + 3).setTypeIdAndData(35, (byte) 4, true);

		return true;
	}
}
