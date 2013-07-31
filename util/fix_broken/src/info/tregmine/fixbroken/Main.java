package info.tregmine.fixbroken;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipException;

import com.mojang.nbt.*;

import org.bukkit.Material;

public class Main
{
    private static byte nibble4(byte[] arr, int index)
    {
        return index % 2 == 0 ? (byte)(arr[index/2] & 0x0F) :
                                (byte)((arr[index/2] >> 4) & 0x0F);
    }

    public static void main(String[] args)
    throws Exception
    {
        if (args.length != 2) {
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

        File outputFolder;
        try {
            outputFolder = new File(args[1]);
            if (!outputFolder.exists()) {
                throw new RuntimeException(args[1] + " doesn't exist");
            } else if (!outputFolder.isDirectory()) {
                throw new RuntimeException(args[1] + " is not a folder");
            }
        } catch (Exception e) {
            System.err.println("Output folder problem: " + e.getMessage());
            System.out.println("");
            printUsageAndExit();
            return;
        }

        String worldName = baseFolder.getName();
        System.out.println("Processing world: " + worldName);

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

        int regionCounter = 0;
        for (File regionFile : list) {
            /*if (System.currentTimeMillis() - regionFile.lastModified() > 3*86400*1000l) {
                regionCounter++;
                continue;
            }*/
            System.out.printf("Processing %s (%d/%d)\n",
                              regionFile.getName(),
                              regionCounter,
                              list.length);

            int chunkCounter = 0,
                errorCounter = 0;

            RegionFile region = new RegionFile(regionFile);
            RegionFile newRegion = new RegionFile(new File(outputFolder, regionFile.getName()));
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
                        //System.out.printf("Zip Error in file %s, chunk %d, %d\n",
                        //                  regionFile.getName(),
                        //                  x, z);
                        continue;
                    }
                    catch (IOException e) {
                        errorCounter++;
                        //System.out.printf("IO Error in file %s, chunk %d, %d\n",
                        //                  regionFile.getName(),
                        //                  x, z);
                        continue;
                    }
                    finally {
                        regionChunkInputStream.close();
                    }

                    DataOutputStream output = newRegion.getChunkDataOutputStream(x, z);
                    NbtIo.write(chunkData, output);
                    output.close();

                    chunkCounter++;
                }
            }

            region.close();
            newRegion.close();

            System.out.printf("Found %d chunks\n", chunkCounter);
            System.out.printf("Got %d errors\n", errorCounter);

            regionCounter++;
        }
    }

    private static void printUsageAndExit()
    {
        System.out.println("Utility to find and fix broken regions");
        System.exit(1);
    }
}
