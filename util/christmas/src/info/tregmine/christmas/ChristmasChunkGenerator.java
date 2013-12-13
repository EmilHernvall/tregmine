package info.tregmine.christmas;

import info.tregmine.christmas.populators.CandyCanePopulator;
import info.tregmine.christmas.populators.ChristmasTreePopulator;
import info.tregmine.christmas.populators.PresentPopulator;
import info.tregmine.christmas.populators.SantaPopulator;
import info.tregmine.christmas.populators.SkySignPopulator;
import info.tregmine.christmas.populators.SleighPopulator;
import info.tregmine.christmas.populators.SnowmanPopulator;
import info.tregmine.christmas.populators.TreePopulator;
import info.tregmine.christmas.populators.WreathPopulator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class ChristmasChunkGenerator extends ChunkGenerator {

	@SuppressWarnings("deprecation")
	public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
		byte[] b = new byte[16*16*128];
		Random seed = new Random(world.getSeed());
		SimplexOctaveGenerator gen =  new SimplexOctaveGenerator(seed, 8);
		gen.setScale(1/40.0);

		for (int x=0; x<16; x++){
			for (int z=0; z<16; z++){
				int multiplier = 5;
				double noise = gen.noise(x+chunkX*16, z+chunkZ*16, 0.5, 0.5)*multiplier;
				for(int y=0; y<32+noise; y++){
					if(b[xyzToByte(x,y,z)] ==0)
						if(y == 0)
							b[xyzToByte(x,y,z)] = (byte)  (Material.BEDROCK).getId();
						else
							b[xyzToByte(x,y,z)] = (byte)  (Material.SNOW_BLOCK).getId();
				}
				for(int y=0; y<20; y++)
				{
					if(b[xyzToByte(x,y,z)] == 0)
						b[xyzToByte(x,y,z)] = (byte)  (Material.STATIONARY_WATER).getId();
				}

				b[xyzToByte(x,0,z)] = (byte)  (Material.BEDROCK).getId();
			}
		}

		return b;
	}

	//This converts relative chunk locations to bytes that can be written to the chunk
	public int xyzToByte(int x, int y, int z) {
		return (x * 16 + z) * 128 + y;
	}


	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator)
				new ChristmasTreePopulator(),
				new TreePopulator(),
				new CandyCanePopulator(),
				new WreathPopulator(),
				new SantaPopulator(),
				new SkySignPopulator(),
				new SnowmanPopulator(),
				new SleighPopulator(),
				new PresentPopulator());
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		int x = random.nextInt(200) - 100;
		int z = random.nextInt(200) - 100;
		int y = world.getHighestBlockYAt(x, z);
		return new Location(world, x, y, z);
	}
}