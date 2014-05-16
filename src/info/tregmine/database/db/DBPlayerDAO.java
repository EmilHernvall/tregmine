package info.tregmine.database.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.EnumMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import info.tregmine.Tregmine;
import info.tregmine.api.Rank;
import info.tregmine.api.TregminePlayer;
import info.tregmine.api.Badge;
import info.tregmine.database.DAOException;
import info.tregmine.database.IPlayerDAO;

public class DBPlayerDAO implements IPlayerDAO
{
    private Connection conn;
    private Tregmine plugin;

    public DBPlayerDAO(Connection conn, Tregmine instance)
    {
        this.conn = conn;
        this.plugin = instance;
    }

    @Override
    public TregminePlayer getPlayer(int id) throws DAOException
    {
        String sql = "SELECT * FROM player WHERE player_id = ?";

        TregminePlayer player = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return null;
                }

                player = new TregminePlayer(rs.getString("player_name"), plugin);
                player.setId(rs.getInt("player_id"));

                String uniqueIdStr = rs.getString("player_uuid");
                if (uniqueIdStr != null) {
                    player.setStoredUuid(UUID.fromString(uniqueIdStr));
                }
                player.setPasswordHash(rs.getString("player_password"));
                player.setRank(Rank.fromString(rs.getString("player_rank")));

                if (rs.getString("player_inventory") == null) {
                    player.setCurrentInventory("survival");
                } else {
                    player.setCurrentInventory(rs.getString("player_inventory"));
                }

                int flags = rs.getInt("player_flags");
                for (TregminePlayer.Flags flag : TregminePlayer.Flags.values()) {
                    if ((flags & (1 << flag.ordinal())) != 0) {
                        player.setFlag(flag);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DAOException(sql, e);
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
            player = new TregminePlayer(wrap, plugin);
        } else {
            player = new TregminePlayer(name, plugin);
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    return null;
                }

                UUID uniqueId = null;
                if (wrap != null) {
                    uniqueId = wrap.getUniqueId();
                } else {
                    String uniqueIdStr = rs.getString("player_uuid");
                    if (uniqueIdStr != null) {
                        player.setStoredUuid(UUID.fromString(uniqueIdStr));
                    }
                }

                player.setId(rs.getInt("player_id"));
                player.setStoredUuid(uniqueId);
                player.setPasswordHash(rs.getString("player_password"));
                player.setRank(Rank.fromString(rs.getString("player_rank")));

                if (rs.getString("player_inventory") == null) {
                    player.setCurrentInventory("survival");
                } else {
                    player.setCurrentInventory(rs.getString("player_inventory"));
                }

                int flags = rs.getInt("player_flags");
                for (TregminePlayer.Flags flag : TregminePlayer.Flags.values()) {
                    if ((flags & (1 << flag.ordinal())) != 0) {
                        player.setFlag(flag);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        loadSettings(player);

        return player;
    }

    private void loadSettings(TregminePlayer player) throws DAOException
    {
        String sql = "SELECT * FROM player_property WHERE player_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
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
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public TregminePlayer createPlayer(Player wrap) throws DAOException
    {
        String sql = "INSERT INTO player (player_uuid, player_name, player_rank, player_keywords) VALUE (?, ?, ?, ?)";

        TregminePlayer player = new TregminePlayer(wrap, plugin);
        player.setStoredUuid(player.getUniqueId());

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, player.getName());
            stmt.setString(3, player.getRank().toString());
            stmt.setString(4, player.getRealName());
            stmt.execute();

            stmt.executeQuery("SELECT LAST_INSERT_ID()");

            try (ResultSet rs = stmt.getResultSet()) {
                if (!rs.next()) {
                    throw new DAOException("Failed to get player id", sql);
                }

                player.setId(rs.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new DAOException(sql, e);
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

        try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setInt(1, player.getId());
            stmt.setString(2, key);
            stmt.setString(3, value);
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sqlInsert, e);
        }
    }

    @Override
    public void updatePlayer(TregminePlayer player) throws DAOException
    {
        String sql = "UPDATE player SET player_uuid = ?, player_password = ?, " +
            "player_rank = ?, player_flags = ?, player_inventory = ? ";
        sql += "WHERE player_id = ?";

        int flags = 0;
        for (TregminePlayer.Flags flag : TregminePlayer.Flags.values()) {
            flags |= player.hasFlag(flag) ? 1 << flag.ordinal() : 0;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, player.getStoredUuid().toString());
            stmt.setString(2, player.getPasswordHash());
            stmt.setString(3, player.getRank().toString());
            stmt.setInt(4, flags);
            stmt.setString(5, player.getCurrentInventory());
            stmt.setInt(6, player.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public List<String> getKeywords(TregminePlayer to) throws DAOException
    {
        String sql = "SELECT * FROM player " +
                "WHERE player_id = ? ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, to.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if(!rs.next()) return null;

                String stringofkeywords = rs.getString("player_keywords");
                String[] strings = stringofkeywords.split(",");

                List<String> playerkeywords = new ArrayList<String>();
                for (String i : strings){
                    if("".equalsIgnoreCase(i)) continue;
                    playerkeywords.add(i);
                }

                return playerkeywords;
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void updateKeywords(TregminePlayer player, List<String> update) throws DAOException
    {
        String sql = "UPDATE player SET player_keywords = ? " +
                "WHERE player_id = ?";

        StringBuilder buffer = new StringBuilder();
        String delim = "";
        for (String keyword : update) {
            buffer.append(delim);
            buffer.append(keyword);
            delim = ",";
        }
        String keywordsString = buffer.toString();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, keywordsString);
            stmt.setInt(2, player.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public List<String> getIgnored(TregminePlayer to) throws DAOException
    {
        String sql = "SELECT * FROM player " +
                "WHERE player_id = ? ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, to.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if(!rs.next()) return null;

                String stringofignored = rs.getString("player_ignore");
                String[] strings = stringofignored.split(",");

                List<String> playerignore = new ArrayList<String>();
                for (String i : strings){
                    if("".equalsIgnoreCase(i)) continue;
                    playerignore.add(i);
                }

                return playerignore;
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public void updateIgnore(TregminePlayer player, List<String> update) throws DAOException
    {
        String sql = "UPDATE player SET player_ignore = ? " +
                "WHERE player_id = ?";

        StringBuilder buffer = new StringBuilder();
        String delim = "";
        for (String ignored : update) {
            buffer.append(delim);
            buffer.append(ignored);
            delim = ",";
        }
        String updateIgnoreString = buffer.toString();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updateIgnoreString);
            stmt.setInt(2, player.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public boolean doesIgnore(TregminePlayer player, TregminePlayer victim) throws DAOException
    {
        String sql = "SELECT * FROM player " +
                "WHERE player_id = ? ";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                if(!rs.next()) return false;

                String stringofignored = rs.getString("player_ignore");
                String[] strings = stringofignored.split(",");

                List<String> playerignore = new ArrayList<String>();
                for (String i : strings){
                    if("".equalsIgnoreCase(i)) continue;
                    playerignore.add(i);
                }

                if (playerignore.contains(victim.getRealName())) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }
    }

    @Override
    public Map<Badge, Integer> getBadges(TregminePlayer player)
    throws DAOException
    {
        String sql = "SELECT * FROM player_badge " +
                "WHERE player_id = ?";

        Map<Badge, Integer> badges = new EnumMap<Badge, Integer>(Badge.class);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.execute();

            try (ResultSet rs = stmt.getResultSet()) {
                while (rs.next()) {
                    Badge badge = Badge.fromString(rs.getString("badge_name"));
                    int lvl = rs.getInt("badge_level");
                    badges.put(badge, lvl);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(sql, e);
        }

        return badges;
    }

    @Override
    public void updateBadges(TregminePlayer player)
    throws DAOException
    {
        Map<Badge, Integer> dbBadges = player.getBadges();
        Map<Badge, Integer> memBadges = getBadges(player);

        Map<Badge, Integer> added = new EnumMap<Badge, Integer>(Badge.class);
        Map<Badge, Integer> changed = new EnumMap<Badge, Integer>(Badge.class);
        for (Map.Entry<Badge, Integer> entry : memBadges.entrySet()) {
            if (dbBadges.containsKey(entry.getKey()) &&
                dbBadges.get(entry.getKey()) != entry.getValue()) {

                changed.put(entry.getKey(), entry.getValue());
            }
            else if (!dbBadges.containsKey(entry.getKey())) {
                added.put(entry.getKey(), entry.getValue());
            }
        }

        Map<Badge, Integer> deleted = new EnumMap<Badge, Integer>(Badge.class);
        for (Map.Entry<Badge, Integer> entry : dbBadges.entrySet()) {
            if (!memBadges.containsKey(entry.getKey())) {
                deleted.put(entry.getKey(), entry.getValue());
            }
        }

        String sqlInsert = "INSERT INTO player_badge (player_id, badge_name, " +
            "badge_level, badge_timestamp) ";
        sqlInsert += "VALUES (?, ?, ?, unix_timestamp())";
        try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            for (Map.Entry<Badge, Integer> entry : added.entrySet()) {
                stmt.setInt(1, player.getId());
                stmt.setString(2, entry.getKey().toString());
                stmt.setInt(3, entry.getValue());
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new DAOException(sqlInsert, e);
        }

        String sqlUpdate = "UPDATE player_badge SET badge_level = ? " +
            "WHERE player_id = ? AND badge_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
            for (Map.Entry<Badge, Integer> entry : changed.entrySet()) {
                stmt.setInt(1, entry.getValue());
                stmt.setInt(2, player.getId());
                stmt.setString(3, entry.getKey().toString());
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new DAOException(sqlUpdate, e);
        }

        String sqlDelete = "DELETE FROM player_badge " +
            "WHERE player_id = ? AND badge_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
            for (Map.Entry<Badge, Integer> entry : deleted.entrySet()) {
                stmt.setInt(1, player.getId());
                stmt.setString(2, entry.getKey().toString());
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new DAOException(sqlDelete, e);
        }
    }
}
