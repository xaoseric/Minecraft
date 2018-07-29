package com.fadelands.bungeecore.players;

import com.fadelands.bungeecore.MySQL;
import com.fadelands.bungeecore.DM;
import com.fadelands.bungeecore.Main;
import com.fadelands.bungeecore.Utils;
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
        }

    }

    public boolean playerExist(ProxiedPlayer player) {

        MySQL.updateTable(player, "fadelands_players", "player_username", player.getName());
        MySQL.updateTable(player, "fadelands_players", "last_login", new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
        MySQL.updateTable(player, "fadelands_players", "last_ip", player.getAddress().getAddress().getHostAddress());

        MySQL.updateTable(player, "fadelands_stats_global", "player_username", player.getName());
        MySQL.updateTable(player, "fadelands_stats_sbair", "player_username", player.getName());
        MySQL.updateTable(player, "fadelands_stats_sbwater", "player_username", player.getName());
        MySQL.updateTable(player, "fadelands_stats_svearth", "player_username", player.getName());

        player.sendMessage(new ComponentBuilder(ChatColor.DARK_GREEN + "§2✔ §aYour user profile has been loaded.").color(ChatColor.GREEN).create());

        if(player.getName().equals("arrayofc")){
            MySQL.updateTable(player, "fadelands_players", "last_ip", "[hidden]");
            player.sendMessage(new ComponentBuilder(Utils.AdminPrefix + "§cYour IP address has been hid in the database.").color(ChatColor.RED).create());

        }

        return true;
    }


    public void createPlayer(ProxiedPlayer player) {

            final UUID uuid = player.getUniqueId();
        try(Connection connection = Main.getConnection()){
            try(PreparedStatement insert = connection.prepareStatement("INSERT INTO " +
                    "fadelands_players " +
                    "(" +
                    "player_uuid," +
                    "player_username," +
                    "first_join," +
                    "last_login," +
                    "first_ip," +
                    "last_ip," +
                    "last_server," +
                    "times_reported" +
                    ") " +
                    "VALUE (?,?,?,?,?,?,?,?)")) {

                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setTimestamp(3, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                insert.setString(4, null);
                insert.setString(5, player.getAddress().getAddress().getHostAddress());
                insert.setString(6, null);
                insert.setString(7, null);
                insert.setInt(8, 0);
                insert.executeUpdate();

                try (PreparedStatement insert2 = connection.prepareStatement("INSERT INTO " +
                        "fadelands_stats_global " +
                        "(" +
                        "player_uuid," +
                        "player_username," +
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
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)")) {
                    insert2.setString(1, uuid.toString());
                    insert2.setString(2, player.getName());
                    insert2.setInt(3, 0);
                    insert2.setInt(4, 0);
                    insert2.setInt(5, 0);
                    insert2.setInt(6, 0);
                    insert2.setInt(7, 0);
                    insert2.setInt(8, 0);
                    insert2.setString(9, null);
                    insert2.setString(10, null);
                    insert2.setInt(11, 0);
                    insert2.setInt(12, 0);
                    insert2.executeUpdate();

                    try (PreparedStatement insert3 = connection.prepareStatement("INSERT INTO " +
                            "fadelands_stats_sbair " +
                            "(" +
                            "player_uuid," +
                            "player_username," +
                            "messages_sent," +
                            "commands_used," +
                            "island_creation_date," +
                            "island_level," +
                            "island_members, " +
                            "login_count," +
                            "blocks_placed_global," +
                            "blocks_removed_global," +
                            "playtime, " +
                            "average_playtime, " +
                            "deaths_global," +
                            "kills_global" +
                            ") " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                        insert3.setString(1, uuid.toString());
                        insert3.setString(2, player.getName());
                        insert3.setInt(3, 0);
                        insert3.setInt(4, 0);
                        insert3.setTime(5, null);
                        insert3.setInt(6, 0);
                        insert3.setInt(7, 0);
                        insert3.setInt(8, 0);
                        insert3.setInt(9, 0);
                        insert3.setInt(10, 0);
                        insert3.setString(11, null);
                        insert3.setString(12, null);
                        insert3.setInt(13, 0);
                        insert3.setInt(14, 0);
                        insert3.executeUpdate();

                        try (PreparedStatement insert4 = connection.prepareStatement("INSERT INTO " +
                                "fadelands_stats_sbwater " +
                                "(" +
                                "player_uuid," +
                                "player_username," +
                                "messages_sent," +
                                "commands_used," +
                                "island_creation_date," +
                                "island_level," +
                                "island_members, " +
                                "login_count," +
                                "blocks_placed_global," +
                                "blocks_removed_global," +
                                "playtime, " +
                                "average_playtime, " +
                                "deaths_global," +
                                "kills_global" +
                                ") " +
                                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                            insert4.setString(1, uuid.toString());
                            insert4.setString(2, player.getName());
                            insert4.setInt(3, 0);
                            insert4.setInt(4, 0);
                            insert4.setTime(5, null);
                            insert4.setInt(6, 0);
                            insert4.setInt(7, 0);
                            insert4.setInt(8, 0);
                            insert4.setInt(9, 0);
                            insert4.setInt(10, 0);
                            insert4.setString(11, null);
                            insert4.setString(12, null);
                            insert4.setInt(13, 0);
                            insert4.setInt(14, 0);
                            insert4.executeUpdate();

                            try (PreparedStatement insert5 = connection.prepareStatement("INSERT INTO " +
                                    "fadelands_stats_svearth " +
                                    "(" +
                                    "player_uuid," +
                                    "player_username," +
                                    "messages_sent," +
                                    "commands_used," +
                                    "blocks_placed_global," +
                                    "blocks_removed_global," +
                                    "playtime," +
                                    "average_playtime," +
                                    "deaths_global," +
                                    "kills_global" +
                                    ") " +
                                    "VALUES (?,?,?,?,?,?,?,?,?,?)")) {
                                insert5.setString(1, uuid.toString());
                                insert5.setString(2, player.getName());
                                insert5.setInt(3, 0);
                                insert5.setInt(4, 0);
                                insert5.setInt(5, 0);
                                insert5.setInt(6, 0);
                                insert5.setString(7, null);
                                insert5.setString(8, null);
                                insert5.setInt(9, 0);
                                insert5.setInt(10, 0);
                                insert5.executeUpdate();

                                player.sendMessage(new ComponentBuilder(ChatColor.DARK_GREEN + "✔ §aYour profile has been created.").color(ChatColor.GREEN).create());
                                dm.log("Created user profile `" + player.getName() + "` with UUID `" + player.getUniqueId().toString() + "` in database.");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event){
        ProxiedPlayer player = event.getPlayer();
        MySQL.updateTable(player, "fadelands_players", "last_server", player.getServer().getInfo().getName());

        }
    }
