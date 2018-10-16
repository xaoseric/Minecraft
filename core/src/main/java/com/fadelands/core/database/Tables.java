package com.fadelands.core.database;

import com.fadelands.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

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
                "network_level int default 0, " +
                "points int default 0, " +
                "tokens int default 0," +
                "messages_sent int default 0, " +
                "commands_used int default 0, " +
                "login_count int default 0, " +
                "blocks_placed int default 0, " +
                "blocks_removed int default 0, " +
                "playtime int default 0, " +
                "deaths int default 0, " +
                "kills int default 0)";

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
                "permanent boolean NOT NULL, " +
                "removed boolean default FALSE, " +
                "remove_reason text NULL, " +
                "remove_admin varchar(64))";

        //Server
        String lockdown = "CREATE TABLE IF NOT EXISTS server_lockdown " +
                "(lockdowner varchar(64), " +
                "active boolean, " +
                "reason text)";

        String vpn_ips = "CREATE TABLE IF NOT EXISTS blocked_ips " +
                "(ips varchar(15))";

        Core.plugin.getDatabaseManager().createTable(players);
        Core.plugin.getDatabaseManager().createTable(statsglobal);
        //Chat

        Core.plugin.getDatabaseManager().createTable(chatlogs);
        Core.plugin.getDatabaseManager().createTable(commandlogs);
        Core.plugin.getDatabaseManager().createTable(pmlogs);
        Core.plugin.getDatabaseManager().createTable(color);

        // settings
        Core.plugin.getDatabaseManager().createTable(lobbysettings);
        Core.plugin.getDatabaseManager().createTable(settings);

        //Staff

        Core.plugin.getDatabaseManager().createTable(reports);
        Core.plugin.getDatabaseManager().createTable(staff);
        Core.plugin.getDatabaseManager().createTable(settingsStaff);

        //Discord

        Core.plugin.getDatabaseManager().createTable(discordLink);

        // Punishments
        Core.plugin.getDatabaseManager().createTable(punishments);

        // Server
        Core.plugin.getDatabaseManager().createTable(lockdown);
        Core.plugin.getDatabaseManager().createTable(vpn_ips);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Core] Created/generated database tables.");
        }
}
