package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.tregmine.quadtree.Rectangle;

import info.tregmine.zones.Zone;
import info.tregmine.zones.Lot;
import info.tregmine.database.IZonesDAO;
import info.tregmine.database.DAOException;

public class DBZonesDAO implements IZonesDAO
{
    private Connection conn;

    public DBZonesDAO(Connection conn)
    {
        this.conn = conn;
    }

    private List<Rectangle> getZoneRectangles(int zoneId) throws DAOException
    {
        String sql = "SELECT * FROM zone_rect WHERE zone_id = ?";

        List<Rectangle> rects = new ArrayList<Rectangle>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zoneId);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    int x1 = rs.getInt("rect_x1");
                    int y1 = rs.getInt("rect_y1");
                    int x2 = rs.getInt("rect_x2");
                    int y2 = rs.getInt("rect_y2");

                    rects.add(new Rectangle(x1, y1, x2, y2));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return rects;
    }

    private Map<Integer, Zone.Permission> getZonePermissions(int zoneId)
    throws DAOException
    {
        String sql = "SELECT * FROM zone_user " +
            "WHERE zone_id = ?";

        Map<Integer, Zone.Permission> permissions = new HashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zoneId);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    int playerId = rs.getInt("user_id");
                    Zone.Permission permission =
                            Zone.Permission.fromString(rs.getString("user_perm"));

                    permissions.put(playerId, permission);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return permissions;
    }

    @Override
    public List<Zone> getZones(String world) throws DAOException
    {
        String sql = "SELECT * FROM zone WHERE zone_world = ?";

        List<Zone> zones = new ArrayList<Zone>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, world);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    Zone zone = new Zone();
                    zone.setId(rs.getInt("zone_id"));
                    zone.setWorld(rs.getString("zone_world"));
                    zone.setName(rs.getString("zone_name"));
                    zone.setEnterDefault("1".equals(rs.getString("zone_enterdefault")));
                    zone.setPlaceDefault("1".equals(rs.getString("zone_placedefault")));
                    zone.setDestroyDefault("1".equals(rs.getString("zone_destroydefault")));
                    zone.setPvp("1".equals(rs.getString("zone_pvp")));
                    zone.setHostiles("1".equals(rs.getString("zone_hostiles")));
                    zone.setCommunist("1".equals(rs.getString("zone_communist")));
                    zone.setPublicProfile("1".equals(rs.getString("zone_publicprofile")));
                    zone.setTextEnter(rs.getString("zone_entermessage"));
                    zone.setTextExit(rs.getString("zone_exitmessage"));
                    zone.setTexture(rs.getString("zone_texture"));
                    zone.setMainOwner(rs.getString("zone_owner"));

                    int flags = rs.getInt("zone_flags");
                    for (Zone.Flags flag : Zone.Flags.values()) {
                        if ((flags & (1 << flag.ordinal())) != 0) {
                            zone.setFlag(flag);
                        }
                    }

                    zones.add(zone);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        for (Zone zone : zones) {
            zone.setRects(getZoneRectangles(zone.getId()));
            zone.setUsers(getZonePermissions(zone.getId()));
        }

        return zones;
    }

    @Override
    public int createZone(Zone zone) throws DAOException
    {
        String sql = "INSERT INTO zone (zone_world, zone_name, " +
            "zone_enterdefault, zone_placedefault, zone_destroydefault, " +
            "zone_pvp, zone_hostiles, zone_communist, zone_publicprofile, " +
            "zone_entermessage, zone_exitmessage, zone_owner) ";
        sql += "VALUES (?, ?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, zone.getWorld());
            stmt.setString(2, zone.getName());
            stmt.setString(3, zone.getEnterDefault() ? "1" : "0");
            stmt.setString(4, zone.getPlaceDefault() ? "1" : "0");
            stmt.setString(5, zone.getDestroyDefault() ? "1" : "0");
            stmt.setString(6, zone.isPvp() ? "1" : "0");
            stmt.setString(7, zone.hasHostiles() ? "1" : "0");
            stmt.setString(8, zone.isCommunist() ? "1" : "0");
            stmt.setString(9, zone.hasPublicProfile() ? "1" : "0");
            stmt.setString(10, zone.getTextEnter());
            stmt.setString(11, zone.getTextExit());
            stmt.setString(12, zone.getMainOwner());
            stmt.execute();

            stmt.execute("SELECT LAST_INSERT_ID()");

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return 0;
                }

                zone.setId(rs.getInt(1));
                return zone.getId();
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void updateZone(Zone zone) throws DAOException
    {
        String sql = "UPDATE zone SET zone_world = ?, zone_name = ?, " +
            "zone_enterdefault = ?, zone_placedefault = ?, " +
            "zone_destroydefault = ?, zone_pvp = ?, zone_hostiles = ?, " +
            "zone_communist = ?, zone_publicprofile = ?, " +
            "zone_entermessage = ?, zone_exitmessage = ?, " +
            "zone_flags = ? " +
            "WHERE zone_id = ?";

        int flags = 0;
        for (Zone.Flags flag : Zone.Flags.values()) {
            flags |= zone.hasFlag(flag) ? 1 << flag.ordinal() : 0;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, zone.getWorld());
            stmt.setString(2, zone.getName());
            stmt.setString(3, zone.getEnterDefault() ? "1" : "0");
            stmt.setString(4, zone.getPlaceDefault() ? "1" : "0");
            stmt.setString(5, zone.getDestroyDefault() ? "1" : "0");
            stmt.setString(6, zone.isPvp() ? "1" : "0");
            stmt.setString(7, zone.hasHostiles() ? "1" : "0");
            stmt.setString(8, zone.isCommunist() ? "1" : "0");
            stmt.setString(9, zone.hasPublicProfile() ? "1" : "0");
            stmt.setString(10, zone.getTextEnter());
            stmt.setString(11, zone.getTextExit());
            stmt.setInt(12, flags);
            stmt.setInt(13, zone.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void deleteZone(int id) throws DAOException
    {
        String sqlDeleteZone = "DELETE FROM zone WHERE zone_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteZone)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sqlDeleteZone, e);
        }

        String sqlDeleteRect = "DELETE FROM zone_rect WHERE zone_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteRect)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sqlDeleteRect, e);
        }

        String sqlDeleteUser = "DELETE FROM zone_user WHERE zone_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteUser)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sqlDeleteUser, e);
        }
    }

    @Override
    public void addRectangle(int zoneId, Rectangle rect) throws DAOException
    {
        String sql = "INSERT INTO zone_rect (zone_id, rect_x1, rect_y1, " +
            "rect_x2, rect_y2) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zoneId);
            stmt.setInt(2, rect.getLeft());
            stmt.setInt(3, rect.getTop());
            stmt.setInt(4, rect.getRight());
            stmt.setInt(5, rect.getBottom());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void addUser(int zoneId, int userId, Zone.Permission perm)
    throws DAOException
    {
        String sql = "INSERT INTO zone_user (zone_id, user_id, user_perm) ";
        sql += "VALUES (?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zoneId);
            stmt.setInt(2, userId);
            stmt.setString(3, perm.toString());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void deleteUser(int zoneId, int userId) throws DAOException
    {
        String sql = "DELETE FROM zone_user WHERE zone_id = ? AND user_id = ? ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, zoneId);
            stmt.setInt(2, userId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public List<Lot> getLots(String world) throws DAOException
    {
        String sql = "SELECT zone_lot.* FROM zone_lot " +
            "INNER JOIN zone USING (zone_id) WHERE zone_world = ?";

        List<Lot> lots = new ArrayList<Lot>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, world);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    Lot lot = new Lot();
                    lot.setId(rs.getInt("lot_id"));
                    lot.setName(rs.getString("lot_name"));
                    lot.setZoneId(rs.getInt("zone_id"));

                    int x1 = rs.getInt("lot_x1");
                    int y1 = rs.getInt("lot_y1");
                    int x2 = rs.getInt("lot_x2");
                    int y2 = rs.getInt("lot_y2");

                    lot.setRect(new Rectangle(x1, y1, x2, y2));

                    int flags = rs.getInt("lot_flags");
                    for (Lot.Flags flag : Lot.Flags.values()) {
                        if ((flags & (1 << flag.ordinal())) != 0) {
                            lot.setFlag(flag);
                        }
                    }

                    lots.add(lot);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        for (Lot lot : lots) {
            lot.setOwner(getLotOwners(lot.getId()));
        }

        return lots;
    }

    @Override
    public List<Integer> getLotOwners(int lotId) throws DAOException
    {
        String sql = "SELECT * FROM zone_lotuser " +
            "WHERE lot_id = ?";

        List<Integer> owners = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lotId);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    owners.add(rs.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return owners;
    }

    @Override
    public void addLot(Lot lot) throws DAOException
    {
        String sql = "INSERT INTO zone_lot (zone_id, lot_name, lot_x1, " +
            "lot_y1, lot_x2, lot_y2) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lot.getZoneId());
            stmt.setString(2, lot.getName());

            Rectangle rect = lot.getRect();
            stmt.setInt(3, rect.getLeft());
            stmt.setInt(4, rect.getTop());
            stmt.setInt(5, rect.getRight());
            stmt.setInt(6, rect.getBottom());

            stmt.execute();

            stmt.execute("SELECT LAST_INSERT_ID()");

            try (ResultSet rs = stmt.getResultSet()) {
                if (rs.next()) {
                    lot.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void updateLotFlags(Lot lot) throws DAOException
    {
        String sql = "UPDATE zone_lot SET lot_flags = ? WHERE lot_id = ?";

        int flags = 0;
        for (Lot.Flags flag : Lot.Flags.values()) {
            flags |= lot.hasFlag(flag) ? 1 << flag.ordinal() : 0;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, flags);
            stmt.setInt(2, lot.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void deleteLot(int lotId) throws DAOException
    {
        String sql = "DELETE FROM zone_lot WHERE lot_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lotId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void addLotUser(int lotId, int userId) throws DAOException
    {
        String sql = "INSERT INTO zone_lotuser (lot_id, user_id) ";
        sql += "VALUES (?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lotId);
            stmt.setInt(2, userId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void deleteLotUsers(int lotId) throws DAOException
    {
        String sql = "DELETE FROM zone_lotuser WHERE lot_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lotId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void deleteLotUser(int lotId, int userId) throws DAOException
    {
        String sql = "DELETE FROM zone_lotuser WHERE lot_id = ? AND user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lotId);
            stmt.setInt(2, userId);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }
}
