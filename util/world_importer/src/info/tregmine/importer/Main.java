package info.tregmine.importer;

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
    public static int uglyLocationHash(String world, int x, int y, int z)
    {
        int checksum = (x + "," +
                        z + "," +
                        y + "," +
                        world).hashCode();
        return checksum;
    }

    public static void main(String[] args)
    throws Exception
    {
        if (args.length != 1) {
            printUsageAndExit();
        }

        String driver = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        }

        String connectionString = System.getenv("TREGMINE_IMPORTER_CONNSTR");
        if (connectionString == null) {
            System.out.println("Please set environment var TREGMINE_IMPORTER_CONNSTR");
            return;
        }
        Connection conn = DriverManager.getConnection(connectionString);

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

        String worldName = baseFolder.getName();
        System.out.println("Processing world: " + worldName);

        Set<Integer> chestHashes = new HashSet<Integer>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM chestbless";

            stmt = conn.prepareStatement(sql);
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                if (!worldName.equals(rs.getString("world"))) {
                    continue;
                }

                chestHashes.add(rs.getInt("checksum"));
            }
        }
        finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) {}
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }

        System.out.printf("Loaded %d chests\n", chestHashes.size());

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

        int regionCounter = 0, chunkCounter = 0, invCounter = 0, errorCounter = 0;
        for (File regionFile : list) {
            System.out.printf("Processing %s (%d/%d)\n",
                              regionFile.getName(),
                              regionCounter,
                              list.length);
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

                        //System.out.printf("%s: x=%d, y=%d, z=%d, items=%d\n",
                        //                  id,
                        //                  entityX,
                        //                  entityY,
                        //                  entityZ,
                        //                  items.size());

                        ////entity.print("", System.out);

                        int checksum = uglyLocationHash(worldName,
                                                        entityX,
                                                        entityY,
                                                        entityZ);
                        if (chestHashes.contains(checksum)) {
                            System.out.printf("checksum=%d x=%d y=%d z=%d\n",
                                              checksum,
                                              entityX,
                                              entityY,
                                              entityZ);

                            for (int j = 0; j < items.size(); j++) {
                                CompoundTag item = (CompoundTag)items.get(j);
                                short itemId = item.getShort("id");
                                short data = item.getShort("Damage");
                                byte count = item.getByte("Count");
                                byte slot = item.getByte("Slot");
                                CompoundTag tag = item.getCompound("tag");

                                Material material = Material.getMaterial(itemId);
                                //System.out.printf("\t%s data=%d count=%d slot=%d\n",
                                //                  material.toString(),
                                //                  data,
                                //                  count,
                                //                  slot);

                                //if (!tag.isEmpty()) {
                                //    tag.print("", System.out);
                                //}

                            }
                        }

                        invCounter++;
                    }
                }
            }

            regionCounter++;
        }

        System.out.printf("Found %d chunks\n", chunkCounter);
        System.out.printf("Got %d errors\n", errorCounter);
        System.out.printf("Found %d inventories\n", invCounter);
    }

    private static void printUsageAndExit()
    {
        System.out.println("Map importer for Minecraft");
        System.exit(1);
    }
}
