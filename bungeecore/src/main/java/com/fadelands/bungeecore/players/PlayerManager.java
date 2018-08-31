package com.fadelands.bungeecore.players;

import com.fadelands.bungeecore.MySQL;
import com.fadelands.bungeecore.utils.DM;
import com.fadelands.bungeecore.Main;
import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.*;
import java.util.UUID;

public class PlayerManager implements Listener {

    DM dm = new DM();

    public Main plugin;

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        try (Connection connection = Main.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_uuid = ?")) {
                statement.setString(1, player.getUniqueId().toString());
                try (ResultSet rs = statement.executeQuery()) {
                    boolean exists = rs.next();
                    if (!exists) createPlayer(player);
                    else playerExist(player);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(new ComponentBuilder("§cSomething went wrong. Contact a staff member asap.").create());
        }
    }

    void playerExist(ProxiedPlayer player) {
        MySQL.updateTable(player, "fadelands_players", "player_username", player.getName());
        MySQL.updateTable(player, "fadelands_players", "last_login", new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
        MySQL.updateTable(player, "fadelands_players", "last_ip", player.getAddress().getAddress().getHostAddress());
        MySQL.updateTable(player, "fadelands_players", "last_country", "yes");

        if(player.getName().equals("arrayofc")){
            MySQL.updateTable(player, "fadelands_players", "last_ip", "[hidden]");
            player.sendMessage(new ComponentBuilder(Utils.AdminPrefix + "§cYour IP address has been hid in the database.").color(ChatColor.RED).create());
        }
    }

    void createPlayer(ProxiedPlayer player) {
        final UUID uuid = player.getUniqueId();
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        try{
            connection = Main.getConnection();
            ps = connection.prepareStatement("INSERT INTO " +
                    "fadelands_players " +
                    "(" +
                    "player_uuid," +
                    "player_username," +
                    "first_join," +
                    "last_login," +
                    "first_ip," +
                    "last_ip," +
                    "first_country," +
                    "last_country," +
                    "times_reported" +
                    ") " +
                    "VALUE (?,?,?,?,?,?,?,?,?)");
                ps.setString(1, uuid.toString());
                ps.setString(2, player.getName());
                ps.setTimestamp(3, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                ps.setString(4, null);
                ps.setString(5, player.getAddress().getAddress().getHostAddress());
                ps.setString(6, null);
                ps.setString(7, null);
                ps.setString(8, null);
                ps.setInt(9, 0);
                ps.executeUpdate();

               ps2 = connection.prepareStatement("INSERT INTO " +
                        "fadelands_stats_global " +
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

                    MySQL.insertTo("fadelands_players_settings", "player_uuid", player.getUniqueId().toString());
                    MySQL.insertTo("fadelands_players_lobbysettings", "player_uuid", player.getUniqueId().toString());

            player.sendMessage(new ComponentBuilder(ChatColor.DARK_GREEN + "✔ §aYour profile has been created.").color(ChatColor.GREEN).create());
                dm.log("Created user profile `" + player.getName() + "` with UUID `" + player.getUniqueId().toString() + "` in database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Main.closeComponents(ps, connection);
            Main.closeComponents(ps2);
        }

    }
}
