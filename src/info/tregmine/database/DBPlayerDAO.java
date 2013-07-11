package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;

import info.tregmine.api.TregminePlayer;

public class DBPlayerDAO
{
    private Connection conn;

    public DBPlayerDAO(Connection conn)
    {
        this.conn = conn;
    }

    public TregminePlayer getPlayer(int id) throws SQLException
    {
        TregminePlayer player;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt =
                    conn.prepareStatement("SELECT * FROM player "
                            + "WHERE player_id = ?");
            stmt.setInt(1, id);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return null;
            }

            player = new TregminePlayer(rs.getString("player_name"));
            player.setId(rs.getInt("player_id"));
            player.setPassword(rs.getString("player_password"));
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

        loadSettings(player);

        return player;
    }

    public TregminePlayer getPlayer(Player player) throws SQLException
    {
        return getPlayer(player.getName(), player);
    }

    public TregminePlayer getPlayer(String name) throws SQLException
    {
        return getPlayer(name, null);
    }

    public TregminePlayer getPlayer(String name, Player wrap)
            throws SQLException
    {
        TregminePlayer player = new TregminePlayer(wrap);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt =
                    conn.prepareStatement("SELECT * FROM player "
                            + "WHERE player_name = ?");
            stmt.setString(1, player.getName());
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return null;
            }

            player.setId(rs.getInt("player_id"));
            player.setPassword(rs.getString("player_password"));
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

        loadSettings(player);

        return player;
    }

    private void loadSettings(TregminePlayer player) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt =
                    conn.prepareStatement("SELECT * FROM player_property "
                            + "WHERE player_id = ?");
            stmt.setInt(1, player.getId());
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                String key = rs.getString("property_key");
                String value = rs.getString("property_value");
                if ("admin".equals(key)) {
                    player.setAdmin(Boolean.valueOf(value));
                }
                else if ("builder".equals(key)) {
                    player.setBuilder(Boolean.valueOf(value));
                }
                else if ("child".equals(key)) {
                    player.setChild(Boolean.valueOf(value));
                }
                else if ("invisible".equals(key)) {
                    player.setInvisible(Boolean.valueOf(value));
                }
                else if ("donator".equals(key)) {
                    player.setDonator(Boolean.valueOf(value));
                }
                else if ("banned".equals(key)) {
                    player.setBanned(Boolean.valueOf(value));
                }
                else if ("trusted".equals(key)) {
                    player.setTrusted(Boolean.valueOf(value));
                }
                else if ("tpblock".equals(key)) {
                    player.setTeleportShield(Boolean.valueOf(value));
                }
                else if ("hiddenloc".equals(key)) {
                    player.setHiddenLocation(Boolean.valueOf(value));
                }
                else if ("guardian".equals(key)) {
                    player.setGuardian(true);
                    player.setGuardianRank(Integer.parseInt(value));
                }
                else if ("password".equals(key)) {
                    player.setPassword(value);
                }
                else if ("keyword".equals(key)) {
                    player.setKeyword(value);
                }
                else if ("countryName".equals(key)) {
                    player.setCountryName(value);
                }
                else if ("city".equals(key)) {
                    player.setCity(value);
                }
                else if ("ip".equals(key)) {
                    player.setIp(value);
                }
                else if ("postalCode".equals(key)) {
                    player.setPostalCode(value);
                }
                else if ("region".equals(key)) {
                    player.setRegion(value);
                }
                else if ("hostName".equals(key)) {
                    player.setHostName(value);
                }
                else if ("color".equals(key)) {
                    player.setNameColor(value);
                }
                else if ("timezone".equals(key)) {
                    player.setTimezone(value);
                }
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

    public TregminePlayer createPlayer(Player wrap) throws SQLException
    {
        TregminePlayer player = new TregminePlayer(wrap);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO player (player_name) VALUE (?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getName());
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            rs = stmt.getResultSet();
            if (!rs.next()) {
                throw new SQLException("Failed to get player id");
            }

            player.setId(rs.getInt(1));
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

        return player;
    }

    public void updatePlayerPermissions(TregminePlayer player)
            throws SQLException
    {
        updateProperty(player, "admin", player.isAdmin());
        updateProperty(player, "builder", player.isBuilder());
        updateProperty(player, "child", player.isChild());
        updateProperty(player, "donator", player.isDonator());
        updateProperty(player, "banned", player.isBanned());
        updateProperty(player, "trusted", player.isTrusted());
        if (player.isGuardian()) {
            updateProperty(player, "guardian",
                    String.valueOf(player.getGuardianRank()));
        }
    }

    public void updatePlayerKeyword(TregminePlayer player) throws SQLException
    {
        updateProperty(player, "keyword", player.getKeyword());
    }

    public void updatePlayerInfo(TregminePlayer player) throws SQLException
    {
        // updateProperty(player, "invisible", player.isInvisible());
        // updateProperty(player, "tpblock", player.hasTeleportShield());
        // updateProperty(player, "hiddenloc", player.hasHiddenLocation());
        updateProperty(player, "countryName", player.getCountryName());
        updateProperty(player, "city", player.getCity());
        updateProperty(player, "ip", player.getIp());
        updateProperty(player, "postalCode", player.getPostalCode());
        updateProperty(player, "region", player.getRegion());
        updateProperty(player, "hostName", player.getHostName());
        updateProperty(player, "color", player.getColor());
        updateProperty(player, "timezone", player.getTimezone());
    }

    private void updateProperty(TregminePlayer player, String key, boolean value)
            throws SQLException
    {
        updateProperty(player, key, String.valueOf(value));
    }

    private void updateProperty(TregminePlayer player, String key, String value)
            throws SQLException
    {
        if (value == null) {
            return;
        }

        PreparedStatement stmt = null;
        try {
            String sqlInsert =
                    "REPLACE INTO player_property (player_id, "
                            + "property_key, property_value) VALUE (?, ?, ?)";
            stmt = conn.prepareStatement(sqlInsert);
            stmt.setInt(1, player.getId());
            stmt.setString(2, key);
            stmt.setString(3, value);
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

    public void updatePassword(TregminePlayer player) throws SQLException
    {
        PreparedStatement stmt = null;
        try {
            String sql =
                    "UPDATE player SET player_password = ? WHERE player_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getPasswordHash());
            stmt.setInt(2, player.getId());
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
