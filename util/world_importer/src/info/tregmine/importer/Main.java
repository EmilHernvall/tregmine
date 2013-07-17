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
    private static Set<Material> BLESSED_MATERIALS =
        new HashSet<Material>() {{
            add(Material.CHEST);
            add(Material.FURNACE);
            add(Material.BURNING_FURNACE);
            add(Material.WOOD_DOOR);
            add(Material.WOODEN_DOOR);
            add(Material.LEVER);
            add(Material.STONE_BUTTON);
            add(Material.STONE_PLATE);
            add(Material.WOOD_PLATE);
            add(Material.WORKBENCH);
            add(Material.SIGN_POST);
            add(Material.DIODE);
            add(Material.DIODE_BLOCK_OFF);
            add(Material.TRAP_DOOR);
            add(Material.DIODE_BLOCK_ON);
            add(Material.JUKEBOX);
            add(Material.SIGN);
            add(Material.FENCE_GATE);
            add(Material.DISPENSER);
            add(Material.WOOD_BUTTON);
        }};

    public static int uglyLocationHash(String world, int x, int y, int z)
    {
        int checksum = (x + "," +
                        z + "," +
                        y + "," +
                        world).hashCode();
        return checksum;
    }

    private static byte nibble4(byte[] arr, int index)
    {
        return index % 2 == 0 ? (byte)(arr[index/2] & 0x0F) :
                                (byte)((arr[index/2] >> 4) & 0x0F);
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

        Map<Integer, Integer> chestHashes = new HashMap<Integer, Integer>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM inventory";

            stmt = conn.prepareStatement(sql);
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                if (!worldName.equals(rs.getString("inventory_world"))) {
                    continue;
                }

                chestHashes.put(rs.getInt("inventory_checksum"),
                                rs.getInt("inventory_id"));
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

        int regionCounter = 0,
            chunkCounter = 0,
            invCounter = 0,
            blessableCounter = 0,
            errorCounter = 0;
        for (File regionFile : list) {
            /*if (!"r.-236.-2.mca".equals(regionFile.getName())) {
                continue;
            }*/
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
                    int xIdx = level.getInt("xPos") * 16;
                    int zIdx = level.getInt("zPos") * 16;

                    //System.out.printf("Chunk: pos=(%d, %d) chunk=(%d, %d) file=%s\n", xIdx, zIdx, x, z, regionFile.getName());

                    ListTag<? extends Tag> sections = level.getList("Sections");
                    for (int i = 0; i < sections.size(); i++) {
                        CompoundTag section = (CompoundTag)sections.get(i);
                        byte yIdx = section.getByte("Y");
                        byte[] blocks = section.getByteArray("Blocks");
                        byte[] add = section.getByteArray("Add");
                        byte[] data = section.getByteArray("Data");

                        for (int chunkX = 0; chunkX < 16; chunkX++) {
                            for (int chunkY = 0; chunkY < 16; chunkY++) {
                                for (int chunkZ = 0; chunkZ < 16; chunkZ++) {
                                    int blockPos = chunkY*16*16 +
                                                   chunkZ*16 +
                                                   chunkX;

                                    int blockX = xIdx + chunkX;
                                    int blockY = 16 * yIdx + chunkY;
                                    int blockZ = zIdx + chunkZ;

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

                                    Material material = Material.getMaterial(blockID);
                                    if (!BLESSED_MATERIALS.contains(material)) {
                                        continue;
                                    }

                                    blessableCounter++;

                                    int checksum = uglyLocationHash(worldName,
                                                                    blockX,
                                                                    blockY,
                                                                    blockZ);
                                    if (!chestHashes.containsKey(checksum)) {
                                        continue;
                                    }

                                    int invId = chestHashes.get(checksum);

                                    System.out.printf("(%d, %d, %d) id=%d checksum=%d block=%s\n",
                                                      blockX,
                                                      blockY,
                                                      blockZ,
                                                      invId,
                                                      checksum,
                                                      material.toString());

                                    update(conn, worldName, invId, blockX, blockY, blockZ);
                                }
                            }
                        }
                    }

                    /*ListTag<? extends Tag> entities = level.getList("TileEntities");
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
                        if (!chestHashes.containsKey(checksum)) {
                            continue;
                        }

                        int invId = chestHashes.get(checksum);

                        System.out.printf("(%d, %d, %d) checksum=%d id=%d\n",
                                          entityX,
                                          entityY,
                                          entityZ,
                                          checksum,
                                          invId);

                        update(conn, worldName, invId, entityX, entityY, entityZ);

                        //for (int j = 0; j < items.size(); j++) {
                        //    CompoundTag item = (CompoundTag)items.get(j);
                        //    short itemId = item.getShort("id");
                        //    short data = item.getShort("Damage");
                        //    byte count = item.getByte("Count");
                        //    byte slot = item.getByte("Slot");
                        //    CompoundTag tag = item.getCompound("tag");

                        //    Material material = Material.getMaterial(itemId);
                        //    //System.out.printf("\t%s data=%d count=%d slot=%d\n",
                        //    //                  material.toString(),
                        //    //                  data,
                        //    //                  count,
                        //    //                  slot);

                        //    //if (!tag.isEmpty()) {
                        //    //    tag.print("", System.out);
                        //    //}
                        //}

                        invCounter++;
                    }*/
                }
            }

            region.close();

            regionCounter++;
        }

        System.out.printf("Found %d chunks\n", chunkCounter);
        System.out.printf("Got %d errors\n", errorCounter);
        System.out.printf("Found %d inventories\n", invCounter);
        System.out.printf("Found %d blessable blocks\n", blessableCounter);
    }

    private static void update(Connection conn, String worldName,
                               int id, int x, int y, int z)
    throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE inventory SET inventory_x = ?, inventory_y = ?, " +
                         "inventory_z = ? ";
            sql += "WHERE inventory_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setInt(3, z);
            stmt.setInt(4, id);
            stmt.execute();
        }
        finally {
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }

    private static void printUsageAndExit()
    {
        System.out.println("Map importer for Minecraft");
        System.exit(1);
    }
}
