package info.tregmine.zonemapper;

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
    public static void main(String[] args)
    throws Exception
    {
        if (args.length != 2) {
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

        String secret = System.getenv("TREGMINE_SECRET");
        if (secret == null) {
            System.out.println("Please set environment var TREGMINE_SECRET");
            return;
        }

        String connectionString = System.getenv("TREGMINE_CONNSTR");
        if (connectionString == null) {
            System.out.println("Please set environment var TREGMINE_CONNSTR");
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

        File mapFolder;
        try {
            mapFolder = new File(args[1]);
            if (!mapFolder.exists()) {
                throw new RuntimeException(args[0] + " doesn't exist");
            } else if (!mapFolder.isDirectory()) {
                throw new RuntimeException(args[0] + " is not a folder");
            }
        } catch (Exception e) {
            System.err.println("Map folder problem: " + e.getMessage());
            System.out.println("");
            printUsageAndExit();
            return;
        }

        String sql = "SELECT * FROM zone";
        List<Zone> zones = new ArrayList<Zone>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    int id = rs.getInt("zone_id");
                    String name = rs.getString("zone_name");
                    String world = rs.getString("zone_world");
                    Rectangle rect = getZoneRect(conn, id);

                    zones.add(new Zone(id, name, world, rect));
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        IMapper mapper = new FlatMapper(baseFolder, mapFolder, secret);
        for (Zone zone : zones) {
            System.out.printf("Processing %s (%d)\n", zone.name, zone.id);
            mapper.map(zone);
        }
    }

    private static Rectangle getZoneRect(Connection conn, int zoneId)
    throws SQLException
    {
        String sql = "SELECT * FROM zone_rect ";
        sql += "WHERE zone_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zoneId);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return null;
                }

                int x1 = rs.getInt("rect_x1");
                int y1 = rs.getInt("rect_y1");
                int x2 = rs.getInt("rect_x2");
                int y2 = rs.getInt("rect_y2");

                return new Rectangle(x1, y1, x2, y2);
            }
        }
    }

    private static void printUsageAndExit()
    {
        System.out.println("Zone Mapper for Tregmine");
        System.exit(1);
    }
}
