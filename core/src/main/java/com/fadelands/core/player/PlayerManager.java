package com.fadelands.core.player;

import com.fadelands.core.Core;
import com.fadelands.core.playerdata.PlayerData;
import com.fadelands.core.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.*;
import java.util.Objects;
import java.util.UUID;

public class PlayerManager implements Listener {

    private Core core;

    public PlayerManager(Core core) {
        this.core = core;
    }


    // Check if player have vpn before they log in. If they don't, a playerdata and profile is is created/loaded for them.
    @EventHandler(priority =  EventPriority.HIGH)
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        final UUID uuid = event.getUniqueId();

        if(Core.plugin.getVpnManager().ipBlocked(event.getAddress().getHostAddress())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cWe noticed you have a VPN (Virtual Private Network) running. Please turn it off if you want to join the server.");
        }

        if(new User().hasPlayedBefore(event.getName())) {
            playerExist(event.getName(), event.getAddress().getHostAddress(), uuid);
            loadPlayerData(uuid);
        } else {
            createPlayer(event.getName(), event.getAddress().getHostAddress(), uuid);
            loadPlayerData(uuid);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerData playerData= PlayerData.get(player.getUniqueId());
        if (playerData != null) {
            playerData.getStats().setLoginCount(playerData.getStats().getLoginCount() + 1);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerData(player.getUniqueId());
    }

    private void playerExist(String username, String ip, UUID uuid) {
        core.getDatabaseManager().updateTable(username, "players", "player_username", username);
        core.getDatabaseManager().updateTable(username, "players", "last_login", new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
        core.getDatabaseManager().updateTable(username, "players", "last_ip", ip);
        core.getDatabaseManager().updateTable(username, "players", "last_country", "None");
        core.getDatabaseManager().updateTable(username, "players", "last_server", "None");
    }

    private void createPlayer(String username, String ip, UUID uuid) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = core.getDatabaseManager().getConnection();
            ps = connection.prepareStatement("INSERT INTO " +
                    "players " +
                    "(" +
                    "player_uuid," +
                    "player_username," +
                    "first_join," +
                    "last_login," +
                    "first_ip," +
                    "last_ip," +
                    "first_country," +
                    "last_country," +
                    "last_server," +
                    "times_reported" +
                    ") " +
                    "VALUE (?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, username);
            ps.setTimestamp(3, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
            ps.setString(4, null);
            ps.setString(5, ip);
            ps.setString(6, null);
            ps.setString(7, null);
            ps.setString(8, null);
            ps.setString(9, "None");
            ps.setInt(10, 0);
            ps.executeUpdate();

            core.getDatabaseManager().insertTo("stats_global", "player_uuid", uuid.toString());
            core.getDatabaseManager().insertTo("players_settings", "player_uuid", uuid.toString());
            core.getDatabaseManager().insertTo("players_lobbysettings", "player_uuid", uuid.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            core.getDatabaseManager().closeComponents(ps, connection);
        }
    }

    public void savePlayerData(UUID uuid) {
        Connection connection = null;
        PreparedStatement ps = null;

        String query = "UPDATE stats_global SET network_level=?," +
                "points=?,tokens=?,messages_sent=?,commands_used=?," +
                "login_count=?,blocks_placed=?,blocks_removed=?,playtime=?," +
                "deaths=?,kills=? WHERE player_uuid = ?";

        try{
            connection = core.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);

            PlayerData playerData = PlayerData.get(uuid);

            if (playerData != null) {
                ps.setInt(1, playerData.getStats().getNetworkLevel());
                ps.setInt(2, playerData.getStats().getPoints());
                ps.setInt(3, playerData.getStats().getTokens());
                ps.setInt(4, playerData.getStats().getMessagesSent());
                ps.setInt(5, playerData.getStats().getCommandsUsed());
                ps.setInt(6, playerData.getStats().getLoginCount());
                ps.setInt(7, playerData.getStats().getBlocksPlaced());
                ps.setInt(8, playerData.getStats().getBlocksRemoved());
                ps.setInt(9, playerData.getStats().getPlaytime());
                ps.setInt(10, playerData.getStats().getDeaths());
                ps.setInt(11, playerData.getStats().getKills());
                ps.setString(12, uuid.toString());

                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            core.getDatabaseManager().closeComponents(ps, connection);
        }
    }

    public void loadPlayerData(UUID uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM stats_global WHERE player_uuid = ?";

        try{
            connection = core.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            if(rs.next()) {
                PlayerData playerData = new PlayerData(uuid);
                playerData.getStats().setNetworkLevel(rs.getInt("network_level"));
                playerData.getStats().setPoints(rs.getInt("points"));
                playerData.getStats().setTokens(rs.getInt("tokens"));
                playerData.getStats().setMessagesSent(rs.getInt("messages_sent"));
                playerData.getStats().setCommandsUsed(rs.getInt("commands_used"));
                playerData.getStats().setLoginCount(rs.getInt("login_count"));
                playerData.getStats().setBlocksPlaced(rs.getInt("blocks_placed"));
                playerData.getStats().setBlocksRemoved(rs.getInt("blocks_removed"));
                playerData.getStats().setPlaytime(rs.getInt("playtime"));
                playerData.getStats().setDeaths(rs.getInt("deaths"));
                playerData.getStats().setKills(rs.getInt("kills"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            core.getDatabaseManager().closeComponents(rs, ps, connection);
        }
    }
}