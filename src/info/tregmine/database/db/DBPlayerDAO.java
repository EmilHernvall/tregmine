package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;

import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Rank;
import info.tregmine.database.IPlayerDAO;
import info.tregmine.database.DAOException;

public class DBPlayerDAO implements IPlayerDAO
{
    private Connection conn;

    public DBPlayerDAO(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public TregminePlayer getPlayer(int id) throws DAOException
    {
        String sql = "SELECT * FROM player WHERE player_id = ?";

        TregminePlayer player = null;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
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
        }
        catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        finally {
            SQLUtils.close(rs);
            SQLUtils.close(stmt);
        }

        loadSettings(player);

        return player;
    }

    @Override
    public TregminePlayer getPlayer(Player player) throws DAOException
    {
        return getPlayer(player.getName(), player);
    }

    @Override
    public TregminePlayer getPlayer(String name) throws DAOException
    {
        return getPlayer(name, null);
    }

    @Override
    public TregminePlayer getPlayer(String name, Player wrap)
            throws DAOException
    {
        String sql = "SELECT * FROM player WHERE player_name = ?";

        TregminePlayer player;
        if (wrap != null) {
            player = new TregminePlayer(wrap);
        } else {
            player = new TregminePlayer(name);
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
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
        }
        catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        finally {
            SQLUtils.close(rs);
            SQLUtils.close(stmt);
        }

        loadSettings(player);

        return player;
    }

    private void loadSettings(TregminePlayer player) throws DAOException
    {
        String sql = "SELECT * FROM player_property WHERE player_id = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
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
        }
        catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        finally {
            SQLUtils.close(rs);
            SQLUtils.close(stmt);
        }
    }

    @Override
    public TregminePlayer createPlayer(Player wrap) throws DAOException
    {
        String sql = "INSERT INTO player (player_name, player_rank) VALUE (?, ?)";

        TregminePlayer player = new TregminePlayer(wrap);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getRank().toString());
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            rs = stmt.getResultSet();
            if (!rs.next()) {
                throw new DAOException("Failed to get player id", sql);
            }

            player.setId(rs.getInt(1));
        }
        catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        finally {
            SQLUtils.close(rs);
            SQLUtils.close(stmt);
        }

        return player;
    }

    @Override
    public void updatePlayerKeyword(TregminePlayer player) throws DAOException
    {
        updateProperty(player, "keyword", player.getKeyword());
    }

    @Override
    public void updatePlayTime(TregminePlayer player) throws DAOException
    {
        int playTime = player.getPlayTime() + player.getTimeOnline();
        updateProperty(player, "playtime", String.valueOf(playTime));
    }

    @Override
    public void updatePlayerInfo(TregminePlayer player) throws DAOException
    {
        updateProperty(player, "quitmessage", player.getQuitMessage());
    }

    private void updateProperty(TregminePlayer player, String key, boolean value)
            throws DAOException
    {
        updateProperty(player, key, String.valueOf(value));
    }

    private void updateProperty(TregminePlayer player, String key, int value)
            throws DAOException
    {
        updateProperty(player, key, String.valueOf(value));
    }

    private void updateProperty(TregminePlayer player, String key, String value)
            throws DAOException
    {
        String sqlInsert = "REPLACE INTO player_property (player_id, " +
                           "property_key, property_value) VALUE (?, ?, ?)";

        if (value == null) {
            return;
        }

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sqlInsert);
            stmt.setInt(1, player.getId());
            stmt.setString(2, key);
            stmt.setString(3, value);
            stmt.execute();

        }
        catch (SQLException e) {
            throw new DAOException(sqlInsert, e);
        }
        finally {
            SQLUtils.close(stmt);
        }
    }

    @Override
    public void updatePlayer(TregminePlayer player) throws DAOException
    {
        String sql = "UPDATE player SET player_password = ?, player_rank = ?, " +
            "player_flags = ? ";
        sql += "WHERE player_id = ?";

        int flags = 0;
        for (TregminePlayer.Flags flag : TregminePlayer.Flags.values()) {
            flags |= player.hasFlag(flag) ? 1 << flag.ordinal() : 0;
        }

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, player.getPasswordHash());
            stmt.setString(2, player.getRank().toString());
            stmt.setInt(3, flags);
            stmt.setInt(4, player.getId());
            stmt.execute();
        }
        catch (SQLException e) {
            throw new DAOException(sql, e);
        }
        finally {
            SQLUtils.close(stmt);
        }
    }
}
