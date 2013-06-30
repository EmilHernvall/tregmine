package info.tregmine.importer;

import java.io.*;
import java.util.*;

import com.mojang.nbt.*;

import org.bukkit.Material;

public class Main
{
    public static void main(String[] args)
    throws Exception
    {
        if (args.length != 1) {
            printUsageAndExit();
        }

        File baseFolder;
        try {
            baseFolder = new File(args[0]);
            if (!baseFolder.exists()) {
                throw new RuntimeException(args[0] + " doesn't exist");
            } else if (!baseFolder.isDirectory()) {
                throw new RuntimeException(args[0] + " is not a folder");
            }
        } catch (Exception e) {
            System.err.println("Base folder problem: " + e.getMessage());
            System.out.println("");
            printUsageAndExit();
            return;
        }

        File regionFolder = new File(baseFolder, "region");
        System.out.println("Scanning " + regionFolder.getPath());
        File[] list = regionFolder.listFiles(
            new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(RegionFile.ANVIL_EXTENSION);
                }
            });

        if (list == null) {
            System.out.println("List was empty.");
            return;
        }

        int chunkCounter = 0, invCounter = 0;
        for (File regionFile : list) {
            System.out.printf("Processing %s (%d)\n",
                              regionFile.getName(),
                              chunkCounter);
            RegionFile region = new RegionFile(regionFile);
            for (int x = 0; x < 32; x++) {
                for (int z = 0; z < 32; z++) {
                    if (!region.hasChunk(x, z)) {
                        continue;
                    }

                    chunkCounter++;

                    DataInputStream regionChunkInputStream =
                        region.getChunkDataInputStream(x, z);
                    if (regionChunkInputStream == null) {
                        System.out.println("Failed to fetch input stream");
                        continue;
                    }
                    CompoundTag chunkData = NbtIo.read(regionChunkInputStream);
                    regionChunkInputStream.close();

                    CompoundTag level = chunkData.getCompound("Level");
                    ListTag<? extends Tag> entities = level.getList("TileEntities");
                    if (entities.size() == 0) {
                        continue;
                    }

                    for (int i = 0; i < entities.size(); i++) {
                        CompoundTag entity = (CompoundTag)entities.get(i);
                        String id = entity.getString("id");
                        int entityX = entity.getInt("x");
                        int entityY = entity.getInt("y");
                        int entityZ = entity.getInt("z");
                        ListTag<? extends Tag> items = entity.getList("Items");
                        if (items.size() == 0) {
                            continue;
                        }

                        System.out.printf("%s: x=%d, y=%d, z=%d, items=%d\n", 
                                          id,
                                          entityX,
                                          entityY,
                                          entityZ,
                                          items.size());

                        //entity.print("", System.out);

                        for (int j = 0; j < items.size(); j++) {
                            CompoundTag item = (CompoundTag)items.get(j);
                            short itemId = item.getShort("id");
                            short data = item.getShort("Damage");
                            byte count = item.getByte("Count");
                            byte slot = item.getByte("Slot");
                            CompoundTag tag = item.getCompound("tag");

                            Material material = Material.getMaterial(itemId);
                            System.out.printf("\t%s data=%d count=%d slot=%d\n",
                                              material.toString(),
                                              data,
                                              count,
                                              slot);

                            if (!tag.isEmpty()) {
                                tag.print("", System.out);
                            }

                        }

                        invCounter++;
                    }
                }
            }
        }

        System.out.printf("Found %d chunks\n", chunkCounter);
        System.out.printf("Found %d inventories\n", invCounter);
    }

    private static void printUsageAndExit()
    {
        System.out.println("Map importer for Minecraft");
        System.exit(1);
    }
}
