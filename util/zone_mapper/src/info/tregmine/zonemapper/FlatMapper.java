package info.tregmine.zonemapper;

import java.io.*;
import java.util.*;
import java.util.zip.ZipException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.mojang.nbt.*;

import org.bukkit.Material;

public class FlatMapper implements IMapper
{
    private File serverDir;
    private File mapDir;
    private String secret;

    private Zone zone;
    private Rectangle rect;
    private String hashedName;
    private File worldDir;
    private File regionDir;

    private int regionCounter = 0, chunkCounter = 0, errorCounter = 0;

    private int width, height;
    private int minX, maxX, minZ, maxZ;
    private int minRegX, maxRegX, minRegZ, maxRegZ;

    private ColorScheme colorScheme;

    private BufferedImage image;

    public FlatMapper(File serverDir, File mapDir, String secret)
    {
        this.serverDir = serverDir;
        this.mapDir = mapDir;
        this.secret = secret;

        File dataDir = new File("data");
        this.colorScheme = ColorScheme.loadScheme(dataDir, "colorscheme");
    }

    public void map(Zone zone)
    throws IOException
    {
        this.zone = zone;
        this.worldDir = new File(serverDir, zone.world);
        this.regionDir = new File(worldDir, "region");
        this.rect = zone.rect;

        try {
            Digest digest = new Digest("MD5");
            hashedName = digest.hashAsString(zone.id + "," + secret);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.minX = Math.min(rect.x1, rect.x2);
        this.maxX = Math.max(rect.x1, rect.x2);
        this.minZ = Math.min(rect.y1, rect.y2);
        this.maxZ = Math.max(rect.y1, rect.y2);

        this.minRegX = minX / 32 / 16 - 1;
        this.maxRegX = maxX / 32 / 16 + 1;
        this.minRegZ = minZ / 32 / 16 - 1;
        this.maxRegZ = maxZ / 32 / 16 + 1;

        this.width = maxX - minX + 1;
        this.height = maxZ - minZ + 1;

        System.out.printf("Width: %d, Height: %d\n", width, height);

        if (width > 3500 && height > 3500) {
            System.out.println("Zone too big.");
            return;
        }

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

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

        File mapFile = new File(mapDir, hashedName + ".png");
        ImageIO.write(image, "png", mapFile);

        System.out.println("Saved " + mapFile.getName());
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

                int x = blockX - minX;
                int z = blockZ - minZ;
                Color color = null;

                // Iterate in the y direction from up to down
                ListTag<? extends Tag> sections = level.getList("Sections");
                boolean transparent = false;
                int y = 0;
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

                        Color current = new Color(colors[0]);
                        int red = current.getRed();
                        int green = current.getGreen();
                        int blue = current.getBlue();
                        int alpha = current.getAlpha();

                        if (color == null) {
                            color = current;
                        } else {
                            int newRed = (red * color.getAlpha() + (0xFF - color.getAlpha())*color.getRed())/0xFF;
                            int newGreen = (green * color.getAlpha()+ (0xFF - color.getAlpha())*color.getGreen())/0xFF;
                            int newBlue = (blue * color.getAlpha()+ (0xFF - color.getAlpha())*color.getBlue())/0xFF;
                            color.setRGBA(newRed, newGreen, newBlue, alpha);
                        }

                        if (alpha == 0xFF) {
                            solidFound = true;
                            y = blockY;
                            break;
                        } else {
                            transparent = true;
                        }
                    }

                    if (solidFound) {
                        break;
                    }
                }

                if (color == null) {
                    continue;
                }

                if (!transparent) {
                    int worldheight = 256;
                    boolean below = y < 64;

                    // Make height range from 0 - 1 (1 - 0 for below and 0 - 1 above)
                    float height = (below ? (64 - y)/64.0f : (y - 64)/(float)worldheight);

                    // Defines the 'step' in coloring.
                    float step = 10 / (float)(worldheight + 1);

                    // The step applied to height.
                    float scale = ((int)(height/step))*step;

                    // Make the smaller values change the color (slightly) more than the higher values.
                    scale = (float)Math.pow(scale, 1.1f);

                    // Don't let the color go fully white or fully black.
                    scale *= 0.8f;

                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();
                    if (below) {
                        red -= red * scale;
                        green -= green * scale;
                        blue -= blue * scale;
                    } else {
                        red += (255-red) * scale;
                        green += (255-green) * scale;
                        blue += (255-blue) * scale;
                    }

                    color.setRGBA(red, green, blue, 255);
                }

                try {
                    image.setRGB(x, z, color.getARGB());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.printf("%d, %d\n", x, z);
                    throw e;
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
