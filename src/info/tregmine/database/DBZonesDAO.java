package info.tregmine.database;

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

public class DBZonesDAO
{
    private Connection conn;

    public DBZonesDAO(Connection conn)
    {
        this.conn = conn;
    }

    public int getUserId(String player) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT player_id FROM player " +
                                         "WHERE player_name = ?");
            stmt.setString(1, player);
            stmt.execute();

            rs = stmt.getResultSet();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        return -1;
    }

    private List<Rectangle> getZoneRectangles(int zoneId) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Rectangle> rects = new ArrayList<Rectangle>();
        try {
            stmt = conn.prepareStatement("SELECT * FROM zone_rect WHERE zone_id = ?");
            stmt.setInt(1, zoneId);
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                int x1 = rs.getInt("rect_x1");
                int y1 = rs.getInt("rect_y1");
                int x2 = rs.getInt("rect_x2");
                int y2 = rs.getInt("rect_y2");

                rects.add(new Rectangle(x1, y1, x2, y2));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        return rects;
    }

    private Map<String, Zone.Permission> getZonePermissions(int zoneId)
            throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Zone.Permission> permissions =
                new HashMap<String, Zone.Permission>();
        try {
            stmt = conn.prepareStatement("SELECT * FROM zone_user " +
                    "INNER JOIN player ON user_id = player_id " +
                    "WHERE zone_id = ?");
            stmt.setInt(1, zoneId);
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                String player = rs.getString("player_name");
                Zone.Permission permission =
                        Zone.Permission.fromString(rs.getString("user_perm"));

                permissions.put(player, permission);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        return permissions;
    }

    public List<Zone> getZones(String world) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Zone> zones = new ArrayList<Zone>();
        try {
            stmt = conn.prepareStatement("SELECT * FROM zone WHERE zone_world = ?");
            stmt.setString(1, world);
            stmt.execute();

            rs = stmt.getResultSet();
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
                zone.setTextEnter(rs.getString("zone_entermessage"));
                zone.setTextExit(rs.getString("zone_exitmessage"));
                zone.setTexture(rs.getString("zone_texture"));
                zone.setMainOwner(rs.getString("zone_owner"));
                zones.add(zone);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        for (Zone zone : zones) {
            zone.setRects(getZoneRectangles(zone.getId()));
            zone.setUsers(getZonePermissions(zone.getId()));
        }

        return zones;
    }

    public int createZone(Zone zone) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String sql = "INSERT INTO zone (zone_world, zone_name, " +
                "zone_enterdefault, zone_placedefault, zone_destroydefault, " +
                "zone_pvp, zone_hostiles, zone_communist, zone_entermessage, " +
                "zone_exitmessage, zone_owner) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, zone.getWorld());
            stmt.setString(2, zone.getName());
            stmt.setString(3, zone.getEnterDefault() ? "1" : "0");
            stmt.setString(4, zone.getPlaceDefault() ? "1" : "0");
            stmt.setString(5, zone.getDestroyDefault() ? "1" : "0");
            stmt.setString(6, zone.isPvp() ? "1" : "0");
            stmt.setString(7, zone.hasHostiles() ? "1" : "0");
            stmt.setString(8, zone.isCommunist() ? "1" : "0");
            stmt.setString(9, zone.getTextEnter());
            stmt.setString(10, zone.getTextExit());
            stmt.setString(11, zone.getMainOwner());
            stmt.execute();

            stmt.execute("SELECT LAST_INSERT_ID()");

            rs = stmt.getResultSet();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        zone.setId(id);

        return id;
    }

    public void updateZone(Zone zone) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE zone SET zone_world = ?, zone_name = ?, " +
                "zone_enterdefault = ?, zone_placedefault = ?, " +
                "zone_destroydefault = ?, zone_pvp = ?, zone_hostiles = ?, " +
                "zone_communist = ?, zone_entermessage = ?, zone_exitmessage = ? " +
                "WHERE zone_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, zone.getWorld());
            stmt.setString(2, zone.getName());
            stmt.setString(3, zone.getEnterDefault() ? "1" : "0");
            stmt.setString(4, zone.getPlaceDefault() ? "1" : "0");
            stmt.setString(5, zone.getDestroyDefault() ? "1" : "0");
            stmt.setString(6, zone.isPvp() ? "1" : "0");
            stmt.setString(7, zone.hasHostiles() ? "1" : "0");
            stmt.setString(8, zone.isCommunist() ? "1" : "0");
            stmt.setString(9, zone.getTextEnter());
            stmt.setString(10, zone.getTextExit());
            stmt.setInt(11, zone.getId());
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteZone(int id) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM zone WHERE zone_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        try {
            String sql = "DELETE FROM zone_rect WHERE zone_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        try {
            String sql = "DELETE FROM zone_user WHERE zone_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void addRectangle(int zoneId, Rectangle rect) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "INSERT INTO zone_rect (zone_id, rect_x1, rect_y1, rect_x2, rect_y2) ";
            sql += "VALUES (?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, zoneId);
            stmt.setInt(2, rect.getLeft());
            stmt.setInt(3, rect.getTop());
            stmt.setInt(4, rect.getRight());
            stmt.setInt(5, rect.getBottom());
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void addUser(int zoneId, int userId, Zone.Permission perm)
            throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO zone_user (zone_id, user_id, user_perm) ";
            sql += "VALUES (?,?,?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, zoneId);
            stmt.setInt(2, userId);
            stmt.setString(3, perm.toString());
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteUser(int zoneId, int userId) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "DELETE FROM zone_user WHERE zone_id = ? AND user_id = ? ";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, zoneId);
            stmt.setInt(2, userId);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public List<Lot> getLots(String world) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Lot> lots = new ArrayList<Lot>();
        try {
            stmt =
                    conn.prepareStatement("SELECT zone_lot.* FROM zone_lot "
                            + "INNER JOIN zone USING (zone_id) "
                            + "WHERE zone_world = ?");

            stmt.setString(1, world);
            stmt.execute();

            rs = stmt.getResultSet();
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

                lots.add(lot);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        for (Lot lot : lots) {
            lot.setOwner(getLotOwners(lot.getId()));
        }

        return lots;
    }

    public List<String> getLotOwners(int lotId) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> owners = new ArrayList<String>();
        try {
            stmt =
                    conn.prepareStatement("SELECT * FROM zone_lotuser "
                            + "INNER JOIN player ON player_id = user_id "
                            + "WHERE lot_id = ?");

            stmt.setInt(1, lotId);
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                owners.add(rs.getString("player_name"));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }

        return owners;
    }

    public void addLot(Lot lot) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "INSERT INTO zone_lot (zone_id, lot_name, lot_x1, lot_y1, lot_x2, lot_y2) ";
            sql += "VALUES (?,?,?,?,?,?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, lot.getZoneId());
            stmt.setString(2, lot.getName());

            Rectangle rect = lot.getRect();
            stmt.setInt(3, rect.getLeft());
            stmt.setInt(4, rect.getTop());
            stmt.setInt(5, rect.getRight());
            stmt.setInt(6, rect.getBottom());

            stmt.execute();

            stmt.execute("SELECT LAST_INSERT_ID()");

            rs = stmt.getResultSet();
            if (rs.next()) {
                lot.setId(rs.getInt(1));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteLot(int lotId) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM zone_lot WHERE lot_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, lotId);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void addLotUser(int lotId, int userId) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO zone_lotuser (lot_id, user_id) ";
            sql += "VALUES (?,?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, lotId);
            stmt.setInt(2, userId);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteLotUsers(int lotId) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql = "DELETE FROM zone_lotuser WHERE lot_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, lotId);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteLotUser(int lotId, int userId) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "DELETE FROM zone_lotuser WHERE lot_id = ? AND user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, lotId);
            stmt.setInt(2, userId);
            stmt.execute();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
