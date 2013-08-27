package info.tregmine.zonemapper;

import java.io.*;
import java.util.*;
import java.util.zip.ZipException;
import java.awt.image.BufferedImage;

import com.mojang.nbt.*;

import org.bukkit.Material;

public class Mapper
{
    private Zone zone;
    private Rectangle rect;
    private File serverDir;
    private File worldDir;
    private File regionDir;

    private int regionCounter = 0, chunkCounter = 0, errorCounter = 0;

    private int minX, maxX, minZ, maxZ;
    private int minRegX, maxRegX, minRegZ, maxRegZ;

    private ColorScheme colorScheme;

    private BufferedImage image;

    public Mapper(Zone zone, File serverDir)
    {
        this.zone = zone;
        this.serverDir = serverDir;
        this.worldDir = new File(serverDir, zone.world);
        this.regionDir = new File(worldDir, "region");
        this.rect = zone.rect;

        this.minX = Math.min(rect.x1, rect.x2);
        this.maxX = Math.max(rect.x1, rect.x2);
        this.minZ = Math.min(rect.y1, rect.y2);
        this.maxZ = Math.max(rect.y1, rect.y2);

        this.minRegX = minX / 32 / 16 - 1;
        this.maxRegX = maxX / 32 / 16 + 1;
        this.minRegZ = minZ / 32 / 16 - 1;
        this.maxRegZ = maxZ / 32 / 16 + 1;

        int width = maxX - minX + 1;
        int height = maxZ - minZ + 1;

        System.out.printf("Width: %d, Height: %d\n", width, height);

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        File dir = new File(".");
        //System.out.println("Dir: " + dir.getAbsolutePath());

        this.colorScheme = ColorScheme.loadScheme(dir, "colorscheme");

        //System.out.printf("colors.length=%d\n", colorScheme.colors.length);
        //System.out.printf("datacolors.length=%d\n", colorScheme.datacolors.length);
    }

    public BufferedImage getImage() { return image; }

    public void map()
    throws IOException
    {
        int regionFiles = Math.abs((maxRegX - minRegX) * (minRegZ - maxRegZ));

        System.out.printf("Exporting zone %s at (%d, %d) - (%d, %d), covering %d regions\n",
                          zone.name, rect.x1, rect.y1, rect.x2, rect.y2, regionFiles);

        for (int regionX = minRegX; regionX <= maxRegX; regionX++) {
            for (int regionZ = minRegZ; regionZ <= maxRegZ; regionZ++) {
                processRegion(regionX, regionZ);
            }
        }

        System.out.printf("Found %d chunks\n", chunkCounter);
        System.out.printf("Got %d errors\n", errorCounter);

        image.flush();
    }

    private void processRegion(int regionX, int regionZ)
    throws IOException
    {
        String regionName = String.format("r.%d.%d.mca", regionX, regionZ);
        File regionFile = new File(regionDir, regionName);
        RegionFile region = new RegionFile(regionFile);
        for (int x = 0; x < 32; x++) {
            for (int z = 0; z < 32; z++) {
                if (!region.hasChunk(x, z)) {
                    continue;
                }

                DataInputStream regionChunkInputStream =
                    region.getChunkDataInputStream(x, z);
                if (regionChunkInputStream == null) {
                    System.out.println("Failed to fetch input stream");
                    continue;
                }

                CompoundTag chunkData;
                try {
                    chunkData = NbtIo.read(regionChunkInputStream);
                }
                catch (ZipException e) {
                    errorCounter++;
                    System.out.printf("Zip Error in file %s, chunk %d, %d\n",
                                      regionFile.getName(),
                                      x, z);
                    continue;
                }
                catch (IOException e) {
                    errorCounter++;
                    System.out.printf("IO Error in file %s, chunk %d, %d\n",
                                      regionFile.getName(),
                                      x, z);
                    continue;
                }

                regionChunkInputStream.close();

                processChunk(chunkData);
            }
        }
    }

    private void processChunk(CompoundTag chunkData)
    {
        CompoundTag level = chunkData.getCompound("Level");
        int xIdx = level.getInt("xPos") * 16;
        int zIdx = level.getInt("zPos") * 16;

        if (xIdx < minX || xIdx > maxX) {
            return;
        }
        if (zIdx < minZ || zIdx > maxZ) {
            return;
        }

        chunkCounter++;

        //System.out.printf("Chunk: pos=(%d, %d) chunk=(%d, %d) file=%s\n", xIdx, zIdx, x, z, regionFile.getName());

        for (int chunkX = 0; chunkX < 16; chunkX++) {
            int blockX = xIdx + chunkX;
            if (blockX < minX || blockX > maxX) {
                continue;
            }
            for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
                int blockZ = zIdx + chunkZ;
                if (blockZ < minZ || blockZ > maxZ) {
                    continue;
                }

                ListTag<? extends Tag> sections = level.getList("Sections");
                for (int i = sections.size()-1; i >= 0; i--) {
                    CompoundTag section = (CompoundTag)sections.get(i);
                    byte yIdx = section.getByte("Y");
                    byte[] blocks = section.getByteArray("Blocks");
                    byte[] add = section.getByteArray("Add");
                    byte[] data = section.getByteArray("Data");

                    boolean solidFound = false;
                    for (int chunkY = 16-1; chunkY >= 0; chunkY--) {
                        int blockPos = chunkY*16*16 +
                                       chunkZ*16 +
                                       chunkX;

                        int blockY = 16 * yIdx + chunkY;

                        byte blockID_a = blocks[blockPos];
                        byte blockID_b = 0;
                        if (add != null && add.length > 0) {
                            blockID_b = nibble4(add, blockPos);
                        }
                        short blockID =
                            (short)(blockID_a + (blockID_b << 8));
                        byte blockData = 0;
                        if (data != null && data.length > 0) {
                            blockData = nibble4(data, blockPos);
                        }

                        if (blockID == 0) {
                            continue;
                        }

                        //Material material = Material.getMaterial(blockID);
                        //System.out.printf("\t%d: %s\n", blockY, material.toString());

                        Color[] colors;
                        try {
                            if(colorScheme.datacolors[blockID] != null) {
                                colors = colorScheme.datacolors[blockID][blockData];
                            }
                            else {
                                colors = colorScheme.colors[blockID];
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //System.out.printf("skipping %d\n", blockID);
                            colorScheme.resizeColorArray(blockID);
                            colors = null;
                        }

                        if (colors == null) {
                            //System.out.printf("not found %d %d\n", blockID, blockData);
                            continue;
                        }

                        int x = blockX - minX;
                        int z = blockZ - minZ;

                        Color color = colors[0];
                        try {
                            image.setRGB(x, z, color.getARGB());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.printf("%d, %d\n", x, z);
                            throw e;

                        }
                        solidFound = true;
                        break;
                    }

                    if (solidFound) {
                        break;
                    }
                }
            }
        }
    }

    private static byte nibble4(byte[] arr, int index)
    {
        return index % 2 == 0 ? (byte)(arr[index/2] & 0x0F) :
                                (byte)((arr[index/2] >> 4) & 0x0F);
    }
}