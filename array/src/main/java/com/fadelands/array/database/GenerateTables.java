package com.fadelands.array.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public class GenerateTables {

    private static Logger log = Logger.getLogger("Minecraft");

    public static void createTables() {

        // DATABASE PLAYER TABLES

        String players = "CREATE TABLE IF NOT EXISTS fadelands_players " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "player_username VARCHAR(30), " +
                "first_join timestamp, " +
                "last_login timestamp, " +
                "first_ip VARCHAR(30)," +
                "last_ip VARCHAR(50)," +
                "last_server VARCHAR(50)," +
                "times_reported INT)";

        String statsglobal = "CREATE TABLE IF NOT EXISTS fadelands_stats_global " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "player_username VARCHAR(30)," +
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

        String SBrealmAir = "CREATE TABLE IF NOT EXISTS fadelands_stats_sbair " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "player_username VARCHAR(30)," +
                "messages_sent INT, " +
                "commands_used INT, " +
                "island_creation_date timestamp, " +
                "island_level INT, " +
                "island_members INT, " +
                "login_count INT, " +
                "blocks_placed_global INT, " +
                "blocks_removed_global INT, " +
                "playtime varchar(50), " +
                "average_playtime varchar(50), " +
                "deaths_global INT, " +
                "kills_global INT)";

        String SBrealmWater = "CREATE TABLE IF NOT EXISTS fadelands_stats_sbwater " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "player_username VARCHAR(30)," +
                "messages_sent INT, " +
                "commands_used INT, " +
                "island_creation_date timestamp, " +
                "island_level INT, " +
                "island_members INT, " +
                "login_count INT, " +
                "blocks_placed_global INT, " +
                "blocks_removed_global INT, " +
                "playtime varchar(50), " +
                "average_playtime varchar(50), " +
                "deaths_global INT, " +
                "kills_global INT)";

        String SVrealmEarth = "CREATE TABLE IF NOT EXISTS fadelands_stats_svearth " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "player_username VARCHAR(30)," +
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

        String chatlogs = "CREATE TABLE IF NOT EXISTS fadelands_chat_messages " +
                "(player_uuid VARCHAR(64), " +
                "player_username VARCHAR(30), " +
                "server VARCHAR(50), " +
                "date timestamp, " +
                "messages VARCHAR(100))";

        String commandlogs = "CREATE TABLE IF NOT EXISTS fadelands_chat_commands " +
                "(player_uuid VARCHAR(64), " +
                "player_username VARCHAR(30), " +
                "server VARCHAR(50), " +
                "date timestamp, " +
                "commands VARCHAR(100))";

        String pmlogs = "CREATE TABLE IF NOT EXISTS fadelands_chat_pms " +
                "(sender_uuid VARCHAR(64), " +
                "sender_name VARCHAR(30), " +
                "receiver_uuid VARCHAR(64), " +
                "receiver_name VARCHAR(30), " +
                "date timestamp, " +
                "message VARCHAR(100))";

        String color = "CREATE TABLE IF NOT EXISTS fadelands_chat_color " +
                "(player_uuid VARCHAR(64) NOT NULL PRIMARY KEY, " +
                "color VARCHAR(30) NOT NULL)";

        // DATABASE STAFF TABLES

        String reports = "CREATE TABLE IF NOT EXISTS fadelands_ingamereports " +
                "(report_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                "player_uuid VARCHAR(64), " +
                "player_username VARCHAR(30), " +
                "reported_uuid VARCHAR(64), " +
                "reported_username VARCHAR(30), " +
                "server VARCHAR(30), " +
                "date timestamp, " +
                "reason VARCHAR(100), " +
                "status VARCHAR(50), " +
                "handled_by VARCHAR(30), " +
                "handled_uuid VARCHAR(64), " +
                "handled_date VARCHAR(30), " +
                "handled_text VARCHAR(100))";

        String staff = "CREATE TABLE IF NOT EXISTS fadelands_staff_members " +
                "(player_uuid VARCHAR(64) PRIMARY KEY, " +
                "player_username VARCHAR(30), " +
                "date_hired timestamp, " +
                "bans BIGINT, " +
                "kicks BIGINT, " +
                "mutes BIGINT, " +
                "reports_handled BIGINT)";

        // DATABASE DISCORD TABLES

        String discordLink = "CREATE TABLE IF NOT EXISTS fadelands_discord_regs " +
                "(player_uuid VARCHAR(64), " +
                "player_username VARCHAR(30), " +
                "registered boolean, " +
                "reg_to_discordid VARCHAR(100), " +
                "reg_to_discordname VARCHAR(100), " +
                "reg_to_discord_disc VARCHAR(100), " +
                "security_key VARCHAR(16))";

        String settings = "CREATE TABLE IF NOT EXISTS fadelands_players_settings "; //todo: WORK ON THIS LATER!

        String settingsStaff = "CREATE TABLE IF NOT EXISTS fadelands_staff_settings "; //todo: WORK ON THIS LATER!

        // LOGGING

        String serverlogs = "CREATE TABLE IF NOT EXISTS fadelands_server_logs " +
                "(time datetime, " +
                "server VARCHAR(30), " +
                "type VARCHAR(50), " +
                "action VARCHAR(30), " +
                "logs TEXT)";

        String playerlogs = "CREATE TABLE IF NOT EXISTS fadelands_players_logs " +
                "(time datetime, " +
                "server VARCHAR(30), " +
                "type VARCHAR(50), " +
                "action VARCHAR(30), " +
                "logs TEXT)";

        //Players

        SQLUtils.createTable(players);
        SQLUtils.createTable(statsglobal);
        SQLUtils.createTable(SBrealmAir);
        SQLUtils.createTable(SBrealmWater);
        SQLUtils.createTable(SVrealmEarth);

        //Chat

        SQLUtils.createTable(chatlogs);
        SQLUtils.createTable(commandlogs);
        SQLUtils.createTable(pmlogs);
        SQLUtils.createTable(color);

        //Staff

        SQLUtils.createTable(reports);
        SQLUtils.createTable(staff);

        //Discord

        SQLUtils.createTable(discordLink);

        // Logs

        SQLUtils.createTable(serverlogs);
        SQLUtils.createTable(playerlogs);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Array] Created/generated database tables.");
        }
}
