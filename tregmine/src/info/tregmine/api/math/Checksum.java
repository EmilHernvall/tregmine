package info.tregmine.api.math;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
* Basic functions for checksums
*/
public class Checksum {
	/**
	* Calculates the checksum of a blocks location
	* @param block 
	* @return unique value of the block checksum
	*/
	public static int block(Block block){
		Location loc = block.getLocation();
		int checksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
		return checksum;
	}

	/**
	* Calculates the checksum of a blocks location
	* @param Location 
	* @return unique value of the block checksum
	*/
	public static int block(Location loc){
		int checksum = (loc.getBlockX() + "," + loc.getBlockZ() + "," + loc.getBlockY() + "," + loc.getWorld().getName()).hashCode();
		return checksum;
	}

	
	/**
	* Calculates the checksum of a 2d(not height) location
	* @param x integer of some things x location 
	* @param z integer of some things x location
	* @param world world
	* @return unique value of the block checksum
	*/
	public static int flat(double x, double z, World world){
		int checksum = (x + "," + z + "," + world.getName()).hashCode();
		return checksum;
	}

	/**
	* Calculates the checksum of a 2d(not height) location
	* @param block 
	* @return unique value of the block checksum
	*/
	public static int flat(Block block){
		int x = block.getLocation().getBlockX();
		int z = block.getLocation().getBlockZ();
		World world = block.getWorld();
		int checksum = (x + "," + z + "," + world.getName()).hashCode();
		return checksum;
	}

	/**
	* Calculates the checksum of a 2d(not height) location
	* @see #flat(double x, double z, World world)
	* @param x integer of some things x location 
	* @param z integer of some things x location
	* @param world world
	* @return unique value of the block checksum
	* @deprecated
	*/
	public static long OLDflat(double x, double z, World world){
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = x + "," + "," + z + ":" + world.getName();
		crc32.update(pos.getBytes());
//		System.out.print(pos);
		return crc32.getValue();
	}
	
	/**
	* Calculates the checksum of a 2d(not height) location
	* @see #flat(Block block)
	* @param block 
	* @return unique value of the block checksum
	* @deprecated
	*/
	public static long OLDflat(Block block){
		Location loc = block.getLocation();
		java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
		String pos = loc.getX() + "," + "," + loc.getZ() + ":" + loc.getWorld().getName();
		crc32.update(pos.getBytes());
		return crc32.getValue();
	}
}
