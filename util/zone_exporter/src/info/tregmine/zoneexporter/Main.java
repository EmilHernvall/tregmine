package info.tregmine.zoneexporter;

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
    public static class Rectangle
    {
        int x1, y1;
        int x2, y2;

        public Rectangle(int x1, int y1, int x2, int y2)
        {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    public static class Point
    {
        int x, y, z;

        public Point(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static void main(String[] args)
    throws Exception
    {
        if (args.length != 3 && args.length != 4) {
            printUsageAndExit();
            return;
        }

        // Set up db connection
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

        String connectionString = System.getenv("TREGMINE_CONNSTR");
        if (connectionString == null) {
            System.out.println("Please set environment var TREGMINE_CONNSTR");
            return;
        }
        Connection conn = DriverManager.getConnection(connectionString);

        // Find source world
        File srcDir;
        try {
            srcDir = new File(args[0]);
            if (!srcDir.exists()) {
                throw new RuntimeException(args[0] + " doesn't exist");
            } else if (!srcDir.isDirectory()) {
                throw new RuntimeException(args[0] + " is not a folder");
            }
        } catch (Exception e) {
            System.err.println("Base folder problem: " + e.getMessage());
            System.out.println("");
            printUsageAndExit();
            return;
        }

        String worldName = srcDir.getName();
        System.out.println("Processing world: " + worldName);

        // Find source zone
        String zoneName = args[1];
        Rectangle rect = getZoneRect(conn, zoneName);
        if (rect == null) {
            System.out.println(zoneName + " was not found.");
            return;
        }

        // Find a warp to use as spawn
        String warpName = null;
        if (args.length == 4) {
            warpName = args[3];
        }

        Map<String, Point> warps = getWarps(conn, rect);
        Point spawn = null;
        if (warps.size() == 0) {
            System.out.println("No warps found!");
            return;
        }
        else if (warps.size() > 1 && warpName != null) {
            spawn = warps.get(warpName);
            if (spawn == null) {
                System.out.println(warpName + " not found.");
                return;
            }
            System.out.printf("Found warp %s\n", warpName);
        }
        else if (warps.size() > 1) {
            System.out.println("Multiple warps found:");
            for (Map.Entry<String, Point> warp : warps.entrySet()) {
                System.out.println(warp.getKey());
            }
            return;
        }
        else {
            for (Map.Entry<String, Point> warp : warps.entrySet()) {
                System.out.printf("Found warp %s\n", warp.getKey());
                spawn = warp.getValue();
            }
        }

        System.out.printf("Using %d, %d, %d as spawn\n", spawn.x, spawn.y, spawn.z);

        // Find target directory
        File dstDir = new File(args[2], zoneName);
        if (!dstDir.exists()) {
            dstDir.mkdir();
        }

        // Copy level.dat
        copyLevel(zoneName, srcDir, dstDir, spawn);

        // Copy region files
        copyRegions(zoneName, rect, srcDir, dstDir);
    }

    private static void copyLevel(String zoneName, File srcDir, File dstDir, Point newSpawn)
    throws Exception
    {
        File srcLevel = new File(srcDir, "level.dat");
        File dstLevel = new File(dstDir, "level.dat");

        DataInputStream levelInput = new DataInputStream(new FileInputStream(srcLevel));
        CompoundTag levelData = NbtIo.readCompressed(levelInput);
        levelInput.close();

        if (levelData == null) {
            throw new RuntimeException("Failed to open level for processing.");
        }

        CompoundTag data = levelData.getCompound("Data");
        data.putString("LevelName", zoneName);
        data.putInt("SpawnX", newSpawn.x);
        data.putInt("SpawnY", newSpawn.y);
        data.putInt("SpawnZ", newSpawn.z);
        data.putInt("GameType", 1);

        DataOutputStream levelOutput = new DataOutputStream(new FileOutputStream(dstLevel));
        NbtIo.writeCompressed(levelData, levelOutput);
        levelOutput.close();
    }

    private static void copyRegions(String zoneName, Rectangle rect, File srcDir, File dstDir)
    throws Exception
    {
        int minX = Math.min(rect.x1, rect.x2);
        int maxX = Math.max(rect.x1, rect.x2);
        int minY = Math.min(rect.y1, rect.y2);
        int maxY = Math.max(rect.y1, rect.y2);

        int minRegX = minX / 32 / 16 - 1;
        int maxRegX = maxX / 32 / 16 + 1;
        int minRegY = minY / 32 / 16 - 1;
        int maxRegY = maxY / 32 / 16 + 1;

        int regionFiles = Math.abs((maxRegX - minRegX) * (minRegY - maxRegY));

        System.out.printf("Exporting zone %s at (%d, %d) - (%d, %d), covering %d regions\n",
                          zoneName, rect.x1, rect.y1, rect.x2, rect.y2, regionFiles);

        File srcRegionDir = new File(srcDir, "region");
        File dstRegionDir = new File(dstDir, "region");
        if (!dstRegionDir.exists()) {
            dstRegionDir.mkdir();
        }

        int regionCounter = 0,
            chunkCounter = 0,
            errorCounter = 0;
        for (int regionX = minRegX; regionX <= maxRegX; regionX++) {
            for (int regionY = minRegY; regionY <= maxRegY; regionY++) {
                String regionName = String.format("r.%d.%d.mca", regionX, regionY);

                File srcRegionFile = new File(srcRegionDir, regionName);
                File dstRegionFile = new File(dstRegionDir, regionName);

                if (!srcRegionFile.exists()) {
                    continue;
                }

                RegionFile srcRegion = new RegionFile(srcRegionFile);
                RegionFile dstRegion = new RegionFile(dstRegionFile);
                for (int x = 0; x < 32; x++) {
                    for (int z = 0; z < 32; z++) {
                        if (!srcRegion.hasChunk(x, z)) {
                            continue;
                        }

                        DataInputStream chunkInput = srcRegion.getChunkDataInputStream(x, z);
                        if (chunkInput == null) {
                            System.out.println("Failed to fetch input stream");
                            continue;
                        }

                        CompoundTag chunkData;
                        try {
                            chunkData = NbtIo.read(chunkInput);
                        }
                        catch (ZipException e) {
                            errorCounter++;
                            System.out.printf("Zip Error in file %s, chunk %d, %d\n",
                                              regionName, x, z);
                            continue;
                        }
                        catch (IOException e) {
                            errorCounter++;
                            System.out.printf("IO Error in file %s, chunk %d, %d\n",
                                              regionName,
                                              x, z);
                            continue;
                        }

                        chunkInput.close();

                        CompoundTag level = chunkData.getCompound("Level");
                        int xIdx = level.getInt("xPos") * 16;
                        int zIdx = level.getInt("zPos") * 16;

                        //System.out.printf("xIdx=%d minX=%d maxX=%d\n", xIdx, minX, maxX);
                        //System.out.printf("xIdx=%d minX=%d maxX=%d\n", zIdx, minY, maxY);

                        if (xIdx < minX || xIdx > maxX) {
                            continue;
                        }
                        if (zIdx < minY || zIdx > maxY) {
                            continue;
                        }

                        chunkCounter++;

                        DataOutputStream chunkOutput = dstRegion.getChunkDataOutputStream(x, z);
                        if (chunkOutput == null) {
                            System.out.println("Failed to fetch input stream");
                            continue;
                        }

                        NbtIo.write(chunkData, chunkOutput);

                        chunkOutput.close();
                    }
                }

                srcRegion.close();

                regionCounter++;
            }
        }

        System.out.printf("Found %d chunks\n", chunkCounter);
        System.out.printf("Got %d errors\n", errorCounter);
    }

    private static Rectangle getZoneRect(Connection conn, String zoneName)
    throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM zone ";
            sql += "INNER JOIN zone_rect USING (zone_id) ";
            sql += "WHERE zone_name = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, zoneName);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return null;
            }

            int x1 = rs.getInt("rect_x1");
            int y1 = rs.getInt("rect_y1");
            int x2 = rs.getInt("rect_x2");
            int y2 = rs.getInt("rect_y2");

            return new Rectangle(x1, y1, x2, y2);
        }
        finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) {}
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }

    private static Map<String, Point> getWarps(Connection conn, Rectangle rect)
    throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM warp ";
            sql += "WHERE (warp_x BETWEEN ? AND ?) ";
            sql += "AND (warp_z BETWEEN ? AND ?) ";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Math.min(rect.x1, rect.x2));
            stmt.setInt(2, Math.max(rect.x1, rect.x2));
            stmt.setInt(3, Math.min(rect.y1, rect.y2));
            stmt.setInt(4, Math.max(rect.y1, rect.y2));
            stmt.execute();

            rs = stmt.getResultSet();

            Map<String, Point> points = new HashMap<String, Point>();
            while (rs.next()) {
                String name = rs.getString("warp_name");
                int x = rs.getInt("warp_x");
                int y = rs.getInt("warp_y");
                int z = rs.getInt("warp_z");

                points.put(name, new Point(x, y, z));
            }

            return points;
        }
        finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) {}
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) {}
            }
        }
    }

    private static void printUsageAndExit()
    {
        System.out.println("Zone Exporter for Tregmine");
        System.out.println("usage: java -jar zone_exporter.jar src_world zone_name target_dir [warp]");
        System.exit(1);
    }
}
