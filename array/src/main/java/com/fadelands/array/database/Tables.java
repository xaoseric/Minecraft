package com.fadelands.array.database;

import com.fadelands.array.Array;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public class Tables {

    public static void createTables() {

        // DATABASE PLAYER TABLES

        String players = "CREATE TABLE IF NOT EXISTS players " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "player_username VARCHAR(30), " +
                "first_join timestamp, " +
                "last_login timestamp, " +
                "first_ip VARCHAR(30)," +
                "last_ip VARCHAR(50)," +
                "first_country VARCHAR(20)," +
                "last_country VARCHAR(20)," +
                "last_server VARCHAR(20)," +
                "times_reported INT)";

        String statsglobal = "CREATE TABLE IF NOT EXISTS stats_global " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "tokens INT," +
                "messages_sent INT, " +
                "commands_used INT, " +
                "login_count INT, " +
                "blocks_placed_global INT, " +
                "blocks_removed_global INT, " +
                "playtime varchar(50), " +
                "average_playtime varchar(50), " +
                "deaths_global INT, " +
                "kills_global INT)";

        // DATABASE CHAT TABLES

        String chatlogs = "CREATE TABLE IF NOT EXISTS chat_messages " +
                "(player_uuid VARCHAR(64), " +
                "player_username VARCHAR(30), " +
                "server VARCHAR(50), " +
                "date timestamp, " +
                "messages VARCHAR(100))";

        String commandlogs = "CREATE TABLE IF NOT EXISTS chat_commands " +
                "(player_uuid VARCHAR(64), " +
                "player_username VARCHAR(30), " +
                "server VARCHAR(50), " +
                "date timestamp, " +
                "commands VARCHAR(100))";

        String pmlogs = "CREATE TABLE IF NOT EXISTS chat_pms " +
                "(sender_uuid VARCHAR(64), " +
                "sender_name VARCHAR(30), " +
                "receiver_uuid VARCHAR(64), " +
                "receiver_name VARCHAR(30), " +
                "date timestamp, " +
                "message VARCHAR(100))";

        String color = "CREATE TABLE IF NOT EXISTS chat_color " +
                "(player_uuid VARCHAR(64) NOT NULL PRIMARY KEY, " +
                "color VARCHAR(30) NOT NULL)";

        // DATABASE STAFF TABLES

        String reports = "CREATE TABLE IF NOT EXISTS reports " +
                "(id INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "reporter VARCHAR(64), " +
                "reported VARCHAR(64), " +
                "server VARCHAR(30), " +
                "reason TEXT, " +
                "date timestamp, " +
                "status integer, " +
                "staff VARCHAR(64), " +
                "handled timestamp, " +
                "comment TEXT)";

        String staff = "CREATE TABLE IF NOT EXISTS staff_members " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "date_hired timestamp null, " +
                "bans BIGINT null, " +
                "mutes BIGINT null, " +
                "reports_handled BIGINT null, " +
                "resignation_date timestamp null)";

        // DATABASE DISCORD TABLES

        String discordLink = "CREATE TABLE IF NOT EXISTS discord_regs " +
                "(player_uuid VARCHAR(64), " +
                "player_username VARCHAR(30), " +
                "registered boolean, " +
                "reg_to_discordid VARCHAR(100), " +
                "reg_to_discordname VARCHAR(100), " +
                "reg_to_discord_disc VARCHAR(100), " +
                "security_key VARCHAR(16))";

        String settings = "CREATE TABLE IF NOT EXISTS players_settings " +
                "(player_uuid VARCHAR(64), " +
                "friend_requests boolean default true, " +
                "join_leave_messages boolean default true, " +
                "public_chat boolean default true, " +
                "private_messages boolean default true, " +
                "inform_if_muted boolean default true, " +
                "game_tips boolean default true, " +
                "show_scoreboard boolean default true, " +
                "show_announcements boolean default true, " +
                "show_friend_join_alerts boolean default true, " +
                "show_friend_alerts boolean default true, " +
                "party_requests boolean default true" +
                ")";

        String lobbysettings = "CREATE TABLE IF NOT EXISTS players_lobbysettings " +
                "(player_uuid VARCHAR(64), " +
                "double_jump boolean default false, " +
                "player_visibility boolean default true)";

        String settingsStaff = "CREATE TABLE IF NOT EXISTS staff_settings " +
                "(player_uuid VARCHAR(64), " +
                "flight_toggled boolean default false, " +
                "vanish_toggled boolean default false, " +
                "staff_chat boolean default true, " +
                "staff_notifications boolean default true, " +
                "admin_notifications boolean default true)";

        //Punishments
        String banrecords = "CREATE TABLE IF NOT EXISTS ban_records " +
                "(banned_uuid VARCHAR(64) PRIMARY KEY NOT NULL, " +
                "banner_uuid VARCHAR(64) NOT NULL, " +
                "banned_date timestamp NOT NULL, " +
                "unban_date timestamp NOT NULL, " +
                "server VARCHAR(30) NOT NULL, " +
                "reason text NOT NULL)";

        String punishments = "CREATE TABLE IF NOT EXISTS punishments " +
                "(punish_id integer AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "appeal_key varchar(30) NOT NULL, " +
                "punisher_uuid varchar(64) NOT NULL, " +
                "punish_type integer NOT NULL, " +
                "reason text NOT NULL, " +
                "punished_uuid varchar(64) NOT NULL, " +
                "date bigint NOT NULL, " +
                "until bigint NOT NULL, " +
                "active boolean default TRUE, " +
                "removed boolean default FALSE, " +
                "remove_reason text NULL, " +
                "remove_admin varchar(40))";

        //Server
        String lockdown = "CREATE TABLE IF NOT EXISTS server_lockdown " +
                "(lockdowner varchar(64), " +
                "active boolean, " +
                "reason text)";

        Array.plugin.getDatabaseManager().createTable(players);
        Array.plugin.getDatabaseManager().createTable(statsglobal);
        //Chat

        Array.plugin.getDatabaseManager().createTable(chatlogs);
        Array.plugin.getDatabaseManager().createTable(commandlogs);
        Array.plugin.getDatabaseManager().createTable(pmlogs);
        Array.plugin.getDatabaseManager().createTable(color);

        // settings
        Array.plugin.getDatabaseManager().createTable(lobbysettings);
        Array.plugin.getDatabaseManager().createTable(settings);

        //Staff

        Array.plugin.getDatabaseManager().createTable(reports);
        Array.plugin.getDatabaseManager().createTable(staff);
        Array.plugin.getDatabaseManager().createTable(settingsStaff);

        //Discord

        Array.plugin.getDatabaseManager().createTable(discordLink);

        // Punishments
        Array.plugin.getDatabaseManager().createTable(punishments);

        // Server
        Array.plugin.getDatabaseManager().createTable(lockdown);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Array] Created/generated database tables.");
        }
}
