package com.fadelands.core.manager;

import com.fadelands.core.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.*;
import java.util.UUID;

public class PlayerManager implements Listener {

    private Core core;

    public PlayerManager(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try (Connection connection = core.getDatabaseManager().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE player_uuid = ?")) {
                statement.setString(1, player.getUniqueId().toString());
                try (ResultSet rs = statement.executeQuery()) {
                    boolean exists = rs.next();
                    if (!exists) createPlayer(player);
                    else playerExist(player);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            event.getPlayer().kickPlayer("§cCouldn't load your profile. Please contact a staff member if this issue persists.");
        }
    }

    private void playerExist(Player player) {
        core.getDatabaseManager().updateTable(player, "players", "player_username", player.getName());
        core.getDatabaseManager().updateTable(player, "players", "last_login", new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
        core.getDatabaseManager().updateTable(player, "players", "last_ip", player.getAddress().getAddress().getHostAddress());
        core.getDatabaseManager().updateTable(player, "players", "last_country", "none");
        core.getDatabaseManager().updateTable(player, "players", "last_server", core.getPluginMessage().getServerName(player));
    }

    private void createPlayer(Player player) {
        final UUID uuid = player.getUniqueId();
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

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
            ps.setString(2, player.getName());
            ps.setTimestamp(3, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
            ps.setString(4, null);
            ps.setString(5, player.getAddress().getAddress().getHostAddress());
            ps.setString(6, null);
            ps.setString(7, null);
            ps.setString(8, null);
            ps.setString(9, core.getPluginMessage().getServerName(player));
            ps.setInt(10, 0);
            ps.executeUpdate();

            ps2 = connection.prepareStatement("INSERT INTO " +
                    "stats_global " +
                    "(" +
                    "player_uuid," +
                    "tokens," +
                    "messages_sent," +
                    "commands_used," +
                    "login_count," +
                    "blocks_placed_global," +
                    "blocks_removed_global," +
                    "playtime, " +
                    "average_playtime, " +
                    "deaths_global," +
                    "kills_global" +
                    ") " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            ps2.setString(1, uuid.toString());
            ps2.setInt(2, 0);
            ps2.setInt(3, 0);
            ps2.setInt(4, 0);
            ps2.setInt(5, 0);
            ps2.setInt(6, 0);
            ps2.setInt(7, 0);
            ps2.setString(8, null);
            ps2.setString(9, null);
            ps2.setInt(10, 0);
            ps2.setInt(11, 0);
            ps2.executeUpdate();

            core.getDatabaseManager().insertTo("players_settings", "player_uuid", player.getUniqueId().toString());
            core.getDatabaseManager().insertTo("players_lobbysettings", "player_uuid", player.getUniqueId().toString());

            player.sendMessage("§2✔ §aYour profile has been created.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            core.getDatabaseManager().closeComponents(ps, connection);
            core.getDatabaseManager().closeComponents(ps2);
        }
    }
}