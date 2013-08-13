package info.tregmine.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Rank;

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
            stmt = conn.prepareStatement("SELECT * FROM player " +
                                         "WHERE player_id = ?");
            stmt.setInt(1, id);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return null;
            }

            player = new TregminePlayer(rs.getString("player_name"));
            player.setId(rs.getInt("player_id"));
            player.setPasswordHash(rs.getString("player_password"));
            player.setRank(Rank.fromString(rs.getString("player_rank")));

            int flags = rs.getInt("player_flags");
            for (TregminePlayer.Flags flag : TregminePlayer.Flags.values()) {
                if ((flags & (1 << flag.ordinal())) != 0) {
                    player.setFlag(flag);
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
        TregminePlayer player;
        if (wrap != null) {
            player = new TregminePlayer(wrap);
        } else {
            player = new TregminePlayer(name);
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM player " +
                                         "WHERE player_name = ?");
            stmt.setString(1, name);
            stmt.execute();

            rs = stmt.getResultSet();
            if (!rs.next()) {
                return null;
            }

            player.setId(rs.getInt("player_id"));
            player.setPasswordHash(rs.getString("player_password"));
            player.setRank(Rank.fromString(rs.getString("player_rank")));

            int flags = rs.getInt("player_flags");
            for (TregminePlayer.Flags flag : TregminePlayer.Flags.values()) {
                if ((flags & (1 << flag.ordinal())) != 0) {
                    player.setFlag(flag);
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

        loadSettings(player);

        return player;
    }

    private void loadSettings(TregminePlayer player) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM player_property " +
                                         "WHERE player_id = ?");
            stmt.setInt(1, player.getId());
            stmt.execute();

            rs = stmt.getResultSet();
            while (rs.next()) {
                String key = rs.getString("property_key");
                String value = rs.getString("property_value");
                if ("keyword".equals(key)) {
                    player.setKeyword(value);
                }
                else if ("guardian".equals(key)) {
                    player.setGuardianRank(Integer.parseInt(value));
                }
                else if ("quitmessage".equals(key)) {
                    player.setQuitMessage(value);
                }
                else if ("playtime".equals(key)) {
                    player.setPlayTime(Integer.parseInt(value));
                }
            }
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { }
            }
            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { }
            }
        }
    }

    public TregminePlayer createPlayer(Player wrap) throws SQLException
    {
        TregminePlayer player = new TregminePlayer(wrap);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO player (player_name, player_rank) VALUE (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getRank().toString());
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

    public void updatePlayerKeyword(TregminePlayer player) throws SQLException
    {
        updateProperty(player, "keyword", player.getKeyword());
    }

    public void updatePlayTime(TregminePlayer player) throws SQLException
    {
        int playTime = player.getPlayTime() + player.getTimeOnline();
        updateProperty(player, "playtime", String.valueOf(playTime));
    }

    public void updatePlayerInfo(TregminePlayer player) throws SQLException
    {
        updateProperty(player, "quitmessage", player.getQuitMessage());
    }

    private void updateProperty(TregminePlayer player, String key, boolean value)
            throws SQLException
    {
        updateProperty(player, key, String.valueOf(value));
    }

    private void updateProperty(TregminePlayer player, String key, int value)
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
            String sqlInsert = "REPLACE INTO player_property (player_id, " +
                               "property_key, property_value) VALUE (?, ?, ?)";
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

    public void updatePlayer(TregminePlayer player) throws SQLException
    {
        int flags = 0;
        for (TregminePlayer.Flags flag : TregminePlayer.Flags.values()) {
            flags |= player.hasFlag(flag) ? 1 << flag.ordinal() : 0;
        }

        PreparedStatement stmt = null;
        try {
            String sql = "UPDATE player SET player_password = ?, player_rank = ?, " +
                "player_flags = ? ";
            sql += "WHERE player_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getPasswordHash());
            stmt.setString(2, player.getRank().toString());
            stmt.setInt(3, flags);
            stmt.setInt(4, player.getId());
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
